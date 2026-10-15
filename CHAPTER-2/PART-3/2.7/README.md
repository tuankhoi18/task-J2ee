# 2.7

> Chapter 2 - Part 3

## Notes

---
# **Bài này nói về cái gì?**
>  Tóm tắt nhanh thì toàn bộ bài này hướng dẫn về cách tạo URL trong header cho các HTTP Requests
## Đặt header làm gì?
Trong các dự án to/thực tế thì đội phát triển của một dự án được chia thành:
- **Frontend**
- **Backend**

Tất nhiên là do dự án nó to nên hai team này không phải lúc này cũng trao đổi những gì đã làm được, nên là đôi khi những thay đổi về code ở Backend có thể khiến Frontend ngừng hoạt động.

Header được đặt trên ở HTTP Response là để giải quyết vấn đề này khi mà mỗi Response được đính kèm với Header có giá trị là URL mới liên quan tới vị trí hoặc các thao tác mà Client có thể sử dụng, đội ngũ Frontend cũng dựa vào thông tin này mà tiếp tục làm phần việc của mình

### Tóm lại
> **Đặt cái header để thằng backend nó có đổi gì thì code Frontend nó không bị lỗi (Nếu Frontend phát triển đúng), thằng Frontend nó cũng dựa vào cái header này để biết là cái URL mới nó mang tên gì, nằm ở đâu**

## Code
### Events
```java
public class SingleResourceRetrieved extends ApplicationEvent {
    private HttpServletResponse response;

    public SingleResourceRetrieved(Object source, HttpServletResponse response) {
        super(source);

        this.response = response;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
```
```java
public class ResourceCreated extends ApplicationEvent {
    private HttpServletResponse response;
    private long idOfNewResource;

    public ResourceCreated(Object source, 
      HttpServletResponse response, long idOfNewResource) {
        super(source);

        this.response = response;
        this.idOfNewResource = idOfNewResource;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
    public long getIdOfNewResource() {
        return idOfNewResource;
    }
}
```
Hai class kế thừa từ ApplicationEvent, "bộ phát tín hiệu" cho hệ thống, tương ứng với hai thao tác POST và GET trong project
### Controller
```java
@RestController
@RequestMapping(value = "/foos")
public class FooController {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private IFooService service;

    @GetMapping(value = "foos/{id}")
    public Foo findById(@PathVariable("id") Long id, HttpServletResponse response) {
        Foo resourceById = Preconditions.checkNotNull(service.findOne(id));

        eventPublisher.publishEvent(new SingleResourceRetrieved(this, response));
        return resourceById;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Foo resource, HttpServletResponse response) {
        Preconditions.checkNotNull(resource);
        Long newId = service.create(resource).getId();

        eventPublisher.publishEvent(new ResourceCreated(this, response, newId));
    }

    @GetMapping("/")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void adminRoot(final HttpServletRequest request, final HttpServletResponse response) {
        String rootUri = request.getRequestURL().toString();

        URI fooUri = new UriTemplate("{rootUri}{resource}").expand(rootUri, "foos");
        String linkToFoos = LinkUtil.createLinkHeader(fooUri.toASCIIString(), "collection");
        response.addHeader("Link", linkToFoos);
    }
}
```
Controller đơn giản, thường thấy ở các bài khác.
Trong các phương thức Request giờ có thêm `eventPublisher.publishEvent()`, dùng để kích hoạt "bộ phát tín hệu" được nêu ở phần trước
Dùng để khám phá các url xuất phát từ root url
```java
@GetMapping("/")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void adminRoot(final HttpServletRequest request, final HttpServletResponse response) {
        String rootUri = request.getRequestURL().toString();

        URI fooUri = new UriTemplate("{rootUri}{resource}").expand(rootUri, "foos");
        String linkToFoos = LinkUtil.createLinkHeader(fooUri.toASCIIString(), "collection");
        response.addHeader("Link", linkToFoos);
    }  
```
### Listeners
```java
@Component
class ResourceCreatedDiscoverabilityListener
        implements ApplicationListener<ResourceCreated>{

    @Override
    public void onApplicationEvent(ResourceCreated resourceCreatedEvent){
        Preconditions.checkNotNull(resourceCreatedEvent);

        HttpServletResponse response = resourceCreatedEvent.getResponse();
        long idOfNewResource = resourceCreatedEvent.getIdOfNewResource();

        addLinkHeaderOnResourceCreation(response, idOfNewResource);
    }
    void addLinkHeaderOnResourceCreation
            (HttpServletResponse response, long idOfNewResource){
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().
                path("/{idOfNewResource}").buildAndExpand(idOfNewResource).toUri();
        response.setHeader("Location", uri.toASCIIString());
    }
}
```
```java
@Component
class SingleResourceRetrievedDiscoverabilityListener
 implements ApplicationListener<SingleResourceRetrieved>{

    @Override
    public void onApplicationEvent(SingleResourceRetrieved resourceRetrievedEvent){
        Preconditions.checkNotNull(resourceRetrievedEvent);

        HttpServletResponse response = resourceRetrievedEvent.getResponse();
        addLinkHeaderOnSingleResourceRetrieval(request, response);
    }
    void addLinkHeaderOnSingleResourceRetrieval(HttpServletResponse response){
        String requestURL = ServletUriComponentsBuilder.fromCurrentRequestUri().
          build().toUri().toASCIIString();
        int positionOfLastSlash = requestURL.lastIndexOf("/");
        String uriForResourceCreation = requestURL.substring(0, positionOfLastSlash);

        String linkHeaderValue = LinkUtil
          .createLinkHeader(uriForResourceCreation, "collection");
        response.addHeader(LINK_HEADER, linkHeaderValue);
    }
}
```
"Bộ thu tín hiệu", đảm nhận việc tạo String và thêm header vào response của các HTTP Request 
- `ResourceCreatedDiscoverabilityListener`

Tạo Header khi Client gửi Request POST, nhận response và id mới của object đã tạo
```java
 URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().
                path("/{idOfNewResource}").buildAndExpand(idOfNewResource).toUri();
```
Sử dụng idOfNewResource làm tham số để tạo URL mới từ URL hiện có
###### kết quả
> curl -i -X POST http://localhost:8080/foos -H 'Content-Type: application/json' -d '{ "id": 2, "name": "HATEOAS" }'
```shell
HTTP/1.1 201
Location: http://localhost:8080/foos/2
Content-Length: 0
Date: Thu, 15 Oct 2026 05:41:24 GMT
```
- `SingleResourceRetrievedDiscoverabilityListener`

Lấy URL hiện tại
```java
 String requestURL = ServletUriComponentsBuilder.fromCurrentRequestUri().
          build().toUri().toASCIIString();
```
Cắt URL tới kí tự / đầu tiên
```java
int positionOfLastSlash = requestURL.lastIndexOf("/");
String uriForResourceCreation = requestURL.substring(0, positionOfLastSlash);
```
###### ví dụ
> http://localhost:8080/foos/2 -> http://localhost:8080/foos/

Sử dụng phương thức `createLinkHeader()` trong LinkUtil để tạo URL mới
```java
String linkHeaderValue = LinkUtil
  .createLinkHeader(uriForResourceCreation, "collection");
response.addHeader(LINK_HEADER, linkHeaderValue);
```
###### kết quả
> curl -i http://localhost:8080/foos/2
```shell
HTTP/1.1 200
HATEOAS: <http://localhost:8080/foos>; rel="collection"
Content-Type: application/json
Content-Length: 25
Date: Thu, 15 Oct 2026 05:41:45 GMT
```
### Util
Lớp tiện ích đơn giản để tạo LinkHeader
```java
public class LinkUtil {
    public static String createLinkHeader(String uri, String rel) {
    return "<" + uri + ">; rel=\"" + rel + "\"";
    }
}
```
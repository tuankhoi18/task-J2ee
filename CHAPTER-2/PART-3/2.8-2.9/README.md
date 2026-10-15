# 2.8

> Chapter 2 - Part 3

## Notes

---
## Các lớp đối tượng
- #### BlogPost (Entity cho Spring JPA)
- #### BlogPostModel (RepresntationModel cho HATEOAS)
- #### BlogPostRequest (DTO)
Vì HATEOAS không thể sử dụng Entity vì đường dẫn hypermedia không phụ thuộc duy nhất vào dữ liệu database thuần túy nên ta tách nó thành 2 lớp như trên

Vì chia BlostPost thành 2 lớp khác nhau nên khi tích hợp HATEOAS, ta phải có một lớp chuyển đổi từ Entity qua Models và gắn link
- #### BlogPostModelAssembler
```java
model.add(linkTo(methodOn(BlogPostController.class)
        .getPost(post.getId()))
        .withSelfRel());
```
- `linkTo` lấy requestmapping của phương thức tham số truyền vaào
- `methodOn` phương thức được truyền vào và tham số của phương thức đó, ví dụ `post.getId()`
- `withSelfRel` Đặt kết quả trong HATEOAS Link và đặt tện là "self", đồng nghĩa với url hiện tại

> **Muốn add link phương thức nào thì cứ methodOn phương thức đó là được**
## Controller
- #### Đối với 1 object, sử dụng phương thức trả về EntityModel<>
- #### Đối với 1 object, sử dụng phương thức trả về CollectionModel<>
- @PathVariable lấy tham số từ giá trị trong { } ở URL
### EntityModel (1 Object)
```java
@GetMapping("/{id}")
public EntityModel<BlogPostModel> getPost(@PathVariable Long id) {
    BlogPost post = postService.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

    return EntityModel.of(assembler.toModel(post));
}
```
###### Kết quả (`GET /api/posts/1`)
```json
{
  "id": 1,
  "title": "Getting Started with HATEOAS",
  "content": "HATEOAS is a constraint of REST...",
  "author": "john.doe",
  "status": "DRAFT",
  "createdAt": "2025-01-01T10:00:00",
  "publishedAt": null,
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/posts/1"
    },
    "publish": {
      "href": "http://localhost:8080/api/posts/1/publish"
    },
    "update": {
      "href": "http://localhost:8080/api/posts/1"
    },
    "delete": {
      "href": "http://localhost:8080/api/posts/1"
    },
    "author": {
      "href": "http://localhost:8080/api/authors/john.doe"
    },
    "comments": {
      "href": "http://localhost:8080/api/posts/1/comments"
    }
  }
}
```
### CollectionModel (Nhiều Object)
```java
 public CollectionModel<BlogPostModel> getAllPosts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        Page<BlogPost> postsPage = postService.findAll(
            PageRequest.of(page, size));

        List<BlogPostModel> posts = postsPage.getContent().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        CollectionModel<BlogPostModel> collection =
            CollectionModel.of(posts);

        // Add pagination links
        collection.add(linkTo(methodOn(BlogPostController.class)
            .getAllPosts(page, size))
            .withSelfRel());

        if (page > 0) {
            collection.add(linkTo(methodOn(BlogPostController.class)
                .getAllPosts(page - 1, size))
                .withRel("prev"));
        }

        if (page < postsPage.getTotalPages() - 1) {
            collection.add(linkTo(methodOn(BlogPostController.class)
                .getAllPosts(page + 1, size))
                .withRel("next"));
        }

        collection.add(linkTo(methodOn(BlogPostController.class)
            .getAllPosts(0, size))
            .withRel("first"));

        collection.add(linkTo(methodOn(BlogPostController.class)
            .getAllPosts(postsPage.getTotalPages() - 1, size))
            .withRel("last"));

        return collection;
    }
```
Sử dụng PageRequest để phân trang, `@RequestParam page` tương ướng với `?page=10` nếu muốn đi tới trang thứ 10, nối 2 param bằng kí tự `&`, cùng với đó là đánh thêm đường dẫn trang hiện tại, trước, sau, đầu, cuối
###### Kết quả (`GET /api/posts?page=1&size=10`)
```json
{
  "_embedded": {
    "blogPostModelList": [
      {
        "id": 11,
        "title": "Post Title 11",
        "status": "PUBLISHED",
        "_links": {
          "self": {"href": "http://localhost:8080/api/posts/11"},
          "archive": {"href": "http://localhost:8080/api/posts/11/archive"}
        }
      },
      {
        "id": 12,
        "title": "Post Title 12",
        "status": "DRAFT",
        "_links": {
          "self": {"href": "http://localhost:8080/api/posts/12"},
          "publish": {"href": "http://localhost:8080/api/posts/12/publish"}
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/posts?page=1&size=10"
    },
    "first": {
      "href": "http://localhost:8080/api/posts?page=0&size=10"
    },
    "prev": {
      "href": "http://localhost:8080/api/posts?page=0&size=10"
    },
    "next": {
      "href": "http://localhost:8080/api/posts?page=2&size=10"
    },
    "last": {
      "href": "http://localhost:8080/api/posts?page=5&size=10"
    }
  },
  "page": {
    "size": 10,
    "totalElements": 57,
    "totalPages": 6,
    "number": 1
  }
}
```
## ClientImplementation (Khung mẫu áp dụng cho các microservices)
```java
public class BlogClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8080/";


    public void publishDraftPosts() {
        // Start from the root - no hardcoded URLs needed
        String postsUrl = baseUrl + "/api/posts";

        // Get all posts
        CollectionModel<BlogPostModel> posts =
                restTemplate.exchange(
                        postsUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<CollectionModel<BlogPostModel>>() {}
                ).getBody();

        // Process each post
        for (BlogPostModel post : posts.getContent()) {
            // Check if the post CAN be published by looking for the "publish" link
            Optional<Link> publishLink = post.getLink("publish");

            if (publishLink.isPresent()) {
                // The link exists, so publishing is allowed
                restTemplate.postForObject(
                        publishLink.get().getHref(),
                        null,
                        BlogPostModel.class);

                System.out.println("Published post: " + post.getTitle());
            } else {
                // No publish link means this action isn't available
                System.out.println("Cannot publish post: " + post.getTitle()
                        + " (status: " + post.getStatus() + ")");
            }
        }
    }
}
```
Kiểm tra nếu như trong URL có trong Object có đường dẫn publish thì có nghĩa là bài báo đó có thể publish đươc
```java
Optional<Link> publishLink = post.getLink("publish");
```
Nếu publish được thì gọi POST Request ngay từ backend (yêu cầu RestTemplate để hoạt động)
```java
restTemplate.postForObject(
                        publishLink.get().getHref(),
                        null,
                        BlogPostModel.class);
```

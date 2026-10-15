# 2.6

> Chapter 2 - Part 2

## Notes

---

## Adding missing dependencies (pom.xml modification)
### JAXB (OpenAPI Dependency)
```java
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.1</version>
</dependency>

<!-- JAXB implementation -->
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>2.3.1</version>
</dependency>
```
### OpenAPI (đã bao gồm swagger)
```java
<dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-ui</artifactId>
        <version>1.6.15</version>
    </dependency>
</dependencies>
```
>  Project được cấp sử dụng Spring 2.x nên sử dụng bản OpenAPI < 2.0 để đảm bảo độ tương thích
---
## Chỉnh sửa/Khai báo thông tin của dư án
### Mô tả chung
```java
@Bean
public OpenAPI restaurantVotingSystem() {
    List<Tag> tags = new ArrayList<>();
    Tag tag = new Tag();
    tag.setName("Restaurants");
    tags.add(tag);
    return new OpenAPI()
            .info(new Info().title("Restaurant API Document")
                    .description("Bài tập 2.6").version("v1")
                    .contact(new Contact()
                            .name("Nguyễn Phạm Tuấn Khôi")
                            .email("9tuankhoi6@gmail.com"))
                    .license(new License()
                            .name("Apache 2.0")
                            .url("http://springdoc.org")))
            .externalDocs(new ExternalDocumentation()
                    .description("Source of the system").url("https://github.com/JolaPsh/voting-system")).tags(tags);
}
```
> - **Yêu cầu các dependency được đề cập trong phần trước được cài đặt**
> - **Yêu cầu khai báo `@Bean` trên lớp `OpenAPI`**
> - **Tên lớp đặt tùy ý, lớp được đặt trong file `SpringBootApp.java`**
> - **`List<Tag>` được khai báo để lưu các thao tác HTTP có trong các controller**
> - **`tag.setName(...)` phải được đặt đúng với tên Tag đã đặt ở controller (Được nhắc đến ở phần sau)**
> - **Các tag được thêm vào List và ghi nhận vào object OpenAPI ở `return new OpenAPI().(...).tags(tags)`**
> - **Tham khảo các thông tin có thể khai báo được trong object `OpenAPI` [ở đây](https://spec.openapis.org/oas/latest#fixed-fields) và object Info [ở đây](https://spec.openapis.org/oas/latest#info-object)**
> - **Trong đó info là thông tin bắt buộc phải có trong object OpenAPI, đọc tài liệu trên nếu có thắc mắc**
### Mô tả cho các thao tác HTTPS trong Controller
#### Đặt tag cho controller (Để ghi nhận vào object OpenAPI ở phần trên)
```java
@Tag(name="Restaurants", description = "Managing the restaurants details for the admins")
public class RestaurantAdminController {
    ...
}
```
> - **Đặt trên tên lớp Controller, gồm hai thông tin cơ bản name(bắt buộc), description và externalDocs (optional)**
#### Annotations của phương thức trong controller
```java
@Operation(summary = "Get all restaurants details")
@Parameter(required = false, description = "No parameters needed", schema = @Schema(implementation = User.class))
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retrieval succeeded",  content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = Restaurant.class)) }),
        @ApiResponse(responseCode = "403", description = "Not authorized",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class))),
        @ApiResponse(responseCode = "409", description = "Server conflict",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class))),
        @ApiResponse(responseCode = "422", description = "Cannot process entity",content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class))),
        @ApiResponse(responseCode = "500", description = "Interal server conflict", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)))
})
public ResponseEntity<List<Restaurant>> getAll(){
    log.info("get all restaurants {}");
    return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
}
```
> - **Annotations được đặt trên tên phương thức, định nghĩa thủ công chức năng, tham số truyền và nội dung trả về của API**
> - **`@Operation`(optional) định nghĩa chức năng phương thức**
> - **`@Parameter` (optional) khai báo thông tin tham số, yêu cầu, schema (Cách mà thông tin truyền vào được lưu trữ), mô tả, ví dụ,...**
> - **`@ApiResponses/@ApiResponse` (optional) định nghĩa mã trả về, mô tả, headers, nội dung (bao gồm mediaType, và schema)** 
> - **Tham khảo thêm về các Annotation khác trong swagger [tại đây](https://docs.swagger.io/swagger-core/v2.0.0-RC3/apidocs/allclasses-noframe.html)**
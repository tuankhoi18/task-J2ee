package HATEOAS.Controllers;

import HATEOAS.Components.BlogPostModelAssembler;
import HATEOAS.DTOs.BlogPostRequest;
import HATEOAS.Entities.BlogPost;
import HATEOAS.Models.BlogPostModel;
import HATEOAS.Services.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/posts")
public class BlogPostController {

    @Autowired
    private BlogPostService postService;

    @Autowired
    private BlogPostModelAssembler assembler;

    @GetMapping("/{id}")
    public EntityModel<BlogPostModel> getPost(@PathVariable Long id) {
        BlogPost post = postService.findById(id);

        return EntityModel.of(assembler.toModel(post));
    }

    @GetMapping
    public CollectionModel<BlogPostModel> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

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

    @PostMapping
    public ResponseEntity<BlogPostModel> createPost(
            @RequestBody BlogPostRequest request) {

        BlogPost post = postService.create(request);
        BlogPostModel model = assembler.toModel(post);

        return ResponseEntity
                .created(model.getRequiredLink("self").toUri())
                .body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPostModel> updatePost(
            @PathVariable Long id,
            @RequestBody BlogPostRequest request) {

        BlogPost post = postService.update(id, request);
        BlogPostModel model = assembler.toModel(post);

        return ResponseEntity.ok(model);
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<BlogPostModel> publishPost(@PathVariable Long id) {
        BlogPost post = postService.publish(id);
        BlogPostModel model = assembler.toModel(post);

        return ResponseEntity.ok(model);
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<BlogPostModel> archivePost(@PathVariable Long id) {
        BlogPost post = postService.archive(id);
        BlogPostModel model = assembler.toModel(post);

        return ResponseEntity.ok(model);
    }

    @PostMapping("/{id}/republish")
    public ResponseEntity<BlogPostModel> republishPost(@PathVariable Long id) {
        BlogPost post = postService.republish(id);
        BlogPostModel model = assembler.toModel(post);

        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

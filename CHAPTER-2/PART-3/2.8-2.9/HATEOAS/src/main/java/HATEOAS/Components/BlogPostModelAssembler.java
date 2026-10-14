package HATEOAS.Components;

import HATEOAS.Controllers.BlogPostController;
import HATEOAS.Entities.BlogPost;
import HATEOAS.Entities.PostStatus;
import HATEOAS.Models.BlogPostModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BlogPostModelAssembler
        implements RepresentationModelAssembler<BlogPost, BlogPostModel> {

    @Override
    public BlogPostModel toModel(BlogPost post) {
        BlogPostModel model = new BlogPostModel();
        // Copy properties from entity
        model.setId(post.getId());
        model.setTitle(post.getTitle());
        model.setContent(post.getContent());
        model.setAuthor(post.getAuthor());
        model.setStatus(post.getStatus());
        model.setCreatedAt(post.getCreatedAt());
        model.setPublishedAt(post.getPublishedAt());

        // Add self link
        model.add(linkTo(methodOn(BlogPostController.class)
                .getPost(post.getId()))
                .withSelfRel());

        // Add conditional links based on state
        if (post.getStatus() == PostStatus.DRAFT) {
            model.add(linkTo(methodOn(BlogPostController.class)
                    .publishPost(post.getId()))
                    .withRel("publish"));

            model.add(linkTo(methodOn(BlogPostController.class)
                    .updatePost(post.getId(), null))
                    .withRel("update"));

            model.add(linkTo(methodOn(BlogPostController.class)
                    .deletePost(post.getId()))
                    .withRel("delete"));
        }

        if (post.getStatus() == PostStatus.PUBLISHED) {
            model.add(linkTo(methodOn(BlogPostController.class)
                    .archivePost(post.getId()))
                    .withRel("archive"));

            // Published posts can only be updated, not deleted
            model.add(linkTo(methodOn(BlogPostController.class)
                    .updatePost(post.getId(), null))
                    .withRel("update"));
        }

        if (post.getStatus() == PostStatus.ARCHIVED) {
            model.add(linkTo(methodOn(BlogPostController.class)
                    .republishPost(post.getId()))
                    .withRel("republish"));
        }
/*

        // Add link to author
        model.add(linkTo(methodOn(AuthorController.class)
                .getAuthor(post.getAuthor()))
                .withRel("author"));

        // Add link to comments
        model.add(linkTo(methodOn(CommentController.class)
                .getComments(post.getId()))
                .withRel("comments"));
*/

        return model;
    }
}
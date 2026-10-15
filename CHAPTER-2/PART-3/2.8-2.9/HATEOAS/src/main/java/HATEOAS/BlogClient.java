package HATEOAS;

import HATEOAS.Models.BlogPostModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

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
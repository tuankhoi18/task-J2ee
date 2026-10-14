package HATEOAS.Models;

import HATEOAS.Entities.PostStatus;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

public class BlogPostModel extends RepresentationModel<BlogPostModel> {
    private Long id;
    private String title;
    private String content;
    private String author;
    private PostStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;

    public BlogPostModel() {}

    public BlogPostModel(Long id, String title, String content, String author, PostStatus status, LocalDateTime createdAt, LocalDateTime publishedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.status = status;
        this.createdAt = createdAt;
        this.publishedAt = publishedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }
}

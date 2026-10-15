package HATEOAS.Services;

import HATEOAS.DTOs.BlogPostRequest;
import HATEOAS.Entities.BlogPost;
import HATEOAS.Entities.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class BlogPostService {

    Map<Long, BlogPost> blogPostMap = Map.ofEntries(
            Map.entry(1L, new BlogPost(
                    1L,
                    "Sonair",
                    "Initial lesions of yaws",
                    "Bondy Bewley",
                    PostStatus.PUBLISHED,
                    LocalDateTime.parse("2025-12-01T00:00:00"),
                    LocalDateTime.parse("2026-04-30T00:00:00")
            )),
            Map.entry(2L, new BlogPost(
                    2L,
                    "Pannier",
                    "Other complications specific to multiple gestation, second trimester, not applicable or unspecified",
                    "Joanna Servis",
                    PostStatus.DRAFT,
                    LocalDateTime.parse("2025-12-03T00:00:00"),
                    LocalDateTime.parse("2026-04-04T00:00:00")
            )),
            Map.entry(3L, new BlogPost(
                    3L,
                    "Trippledex",
                    "Breakdown (mechanical) of aortic (bifurcation) graft (replacement), sequela",
                    "Rycca Rihanek",
                    PostStatus.ARCHIVED,
                    LocalDateTime.parse("2025-11-14T00:00:00"),
                    LocalDateTime.parse("2026-02-27T00:00:00")
            )),
            Map.entry(4L, new BlogPost(
                    4L,
                    "Toughjoyfax",
                    "Strain of flexor muscle, fascia and tendon of left index finger at forearm level, subsequent encounter",
                    "Cyril Ashwin",
                    PostStatus.ARCHIVED,
                    LocalDateTime.parse("2026-06-23T00:00:00"),
                    LocalDateTime.parse("2026-09-21T00:00:00")
            )),
            Map.entry(5L, new BlogPost(
                    5L,
                    "Bamity",
                    "Western equine encephalitis",
                    "Horst Chesterton",
                    PostStatus.DRAFT,
                    LocalDateTime.parse("2025-12-08T00:00:00"),
                    LocalDateTime.parse("2026-01-19T00:00:00")
            )),
            Map.entry(6L, new BlogPost(
                    6L,
                    "Y-Solowarm",
                    "Other Escherichia coli [E. coli] as the cause of diseases classified elsewhere",
                    "Edd Husselbee",
                    PostStatus.ARCHIVED,
                    LocalDateTime.parse("2025-11-26T00:00:00"),
                    LocalDateTime.parse("2026-03-25T00:00:00")
            )),
            Map.entry(7L, new BlogPost(
                    7L,
                    "Kanlam",
                    "Pathological fracture in neoplastic disease, right ankle, subsequent encounter for fracture with delayed healing",
                    "Renato Cranch",
                    PostStatus.PUBLISHED,
                    LocalDateTime.parse("2026-03-10T00:00:00"),
                    LocalDateTime.parse("2026-01-13T00:00:00")
            )),
            Map.entry(8L, new BlogPost(
                    8L,
                    "Fintone",
                    "Coxa magna, left hip",
                    "Rochester Kroin",
                    PostStatus.DRAFT,
                    LocalDateTime.parse("2026-09-27T00:00:00"),
                    LocalDateTime.parse("2026-03-21T00:00:00")
            )),
            Map.entry(9L, new BlogPost(
                    9L,
                    "Viva",
                    "Type III traumatic spondylolisthesis of second cervical vertebra, subsequent encounter for fracture with delayed healing",
                    "Lynde Smitheram",
                    PostStatus.PUBLISHED,
                    LocalDateTime.parse("2026-08-04T00:00:00"),
                    LocalDateTime.parse("2026-02-13T00:00:00")
            )),
            Map.entry(10L, new BlogPost(
                    10L,
                    "Zathin",
                    "Laceration of other blood vessels at forearm level, left arm, subsequent encounter",
                    "Riordan Bentley",
                    PostStatus.DRAFT,
                    LocalDateTime.parse("2026-07-07T00:00:00"),
                    LocalDateTime.parse("2026-01-02T00:00:00")
            )),
            Map.entry(11L, new BlogPost(
                    11L,
                    "Solarbreeze",
                    "Other injury of ovary, unspecified, subsequent encounter",
                    "Corissa Ambrodi",
                    PostStatus.ARCHIVED,
                    LocalDateTime.parse("2026-08-22T00:00:00"),
                    LocalDateTime.parse("2026-08-11T00:00:00")
            )),
            Map.entry(12L, new BlogPost(
                    12L,
                    "Ventosanzap",
                    "Displaced Maisonneuve's fracture of left leg, subsequent encounter for closed fracture with malunion",
                    "Derwin Muttitt",
                    PostStatus.DRAFT,
                    LocalDateTime.parse("2025-11-28T00:00:00"),
                    LocalDateTime.parse("2025-11-28T00:00:00")
            )),
            Map.entry(13L, new BlogPost(
                    13L,
                    "Tin",
                    "3-part fracture of surgical neck of unspecified humerus, subsequent encounter for fracture with malunion",
                    "Jess Wigelsworth",
                    PostStatus.ARCHIVED,
                    LocalDateTime.parse("2026-05-25T00:00:00"),
                    LocalDateTime.parse("2026-10-03T00:00:00")
            )),
            Map.entry(14L, new BlogPost(
                    14L,
                    "Zathin",
                    "Other bursitis, not elsewhere classified, left knee",
                    "Dell Pashbee",
                    PostStatus.ARCHIVED,
                    LocalDateTime.parse("2026-07-23T00:00:00"),
                    LocalDateTime.parse("2026-03-06T00:00:00")
            )),
            Map.entry(15L, new BlogPost(
                    15L,
                    "Ventosanzap",
                    "Visual discomfort, bilateral",
                    "Kipper Tippings",
                    PostStatus.ARCHIVED,
                    LocalDateTime.parse("2026-10-04T00:00:00"),
                    LocalDateTime.parse("2026-06-01T00:00:00")
            ))
    );

    public BlogPost findById(Long id) {
        return blogPostMap.get(id);
    }

    public Page<BlogPost> findAll(PageRequest request) {
        List<BlogPost> allPosts = new ArrayList<>(blogPostMap.values());

        allPosts.sort(Comparator.comparing(BlogPost::getId));
        int pageSize = request.getPageSize();
        int pageNumber = request.getPageNumber();
        int startItem = pageNumber * pageSize;

        List<BlogPost> pagedList;
        if (allPosts.size() <= startItem) {
            pagedList = List.of();
        } else {
            int toIndex = Math.min(startItem + pageSize, allPosts.size());
            pagedList = allPosts.subList(startItem, toIndex);
        }

        return new PageImpl<>(pagedList, request, allPosts.size());
    }

    public BlogPost create(BlogPostRequest request) {
        long id = blogPostMap.size() + 1;
        LocalDateTime now = LocalDateTime.now();
        PostStatus status = request.getStatus() != null ? request.getStatus() : PostStatus.DRAFT;

        BlogPost newPost = new BlogPost(
                id,
                request.getTitle(),
                request.getContent(),
                request.getAuthor(),
                status,
                now,
                status == PostStatus.PUBLISHED ? now : null
        );

        blogPostMap.put(id, newPost);
        return newPost;
    }

    public BlogPost update(Long id, BlogPostRequest request) {
        BlogPost existing = blogPostMap.get(id);
        if (existing == null) {
            throw new IllegalArgumentException("BlogPost not found with id: " + id);
        }

        BlogPost updatedPost = new BlogPost(
                existing.getId(),
                request.getTitle() != null ? request.getTitle() : existing.getTitle(),
                request.getContent() != null ? request.getContent() : existing.getContent(),
                request.getAuthor() != null ? request.getAuthor() : existing.getAuthor(),
                existing.getStatus(),
                existing.getCreatedAt(),
                existing.getPublishedAt()
        );

        blogPostMap.put(id, updatedPost);
        return updatedPost;
    }

    public BlogPost publish(Long id) {
        BlogPost blogPost = blogPostMap.get(id);
        if (blogPost == null) {
            throw new IllegalArgumentException("BlogPost not found with id: " + id);
        }
        blogPost.setStatus(PostStatus.PUBLISHED);
        blogPost.setPublishedAt(LocalDateTime.now());
        blogPostMap.put(id, blogPost);
        return blogPost;
    }

    public BlogPost archive(Long id) {
        BlogPost blogPost = blogPostMap.get(id);
        if (blogPost == null) {
            throw new IllegalArgumentException("BlogPost not found with id: " + id);
        }
        blogPost.setStatus(PostStatus.ARCHIVED);
        blogPost.setPublishedAt(LocalDateTime.now());
        blogPostMap.put(id, blogPost);
        return blogPost;
    }

    public BlogPost republish(Long id) {
        BlogPost blogPost = blogPostMap.get(id);
        if (blogPost == null) {
            throw new IllegalArgumentException("BlogPost not found with id: " + id);
        }
        blogPost.setStatus(PostStatus.PUBLISHED);
        blogPost.setPublishedAt(LocalDateTime.now());
        blogPostMap.put(id, blogPost);
        return blogPost;    }

    public void delete(Long id) {
        blogPostMap.remove(id);
    }
}
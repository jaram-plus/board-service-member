package jaram.jaramplus.mopp_service.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(nullable = false, length = 50)
    private String author;

    @Column(nullable = false)
    private LocalDateTime time;

    public static Post createPost(String title, String content, String author) {
        Post post = new Post();
        post.title = title;
        post.content = content;
        post.author = (author == null || author.isBlank()) ? "익명" : author;
        post.time = LocalDateTime.now().withSecond(0).withNano(0);
        return post;
    }
}

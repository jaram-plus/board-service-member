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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member author;

	@Column(nullable = false)
	private boolean anonymous;

    @Column(nullable = false)
    private LocalDateTime time;

	@Column(nullable = false)
	private int views;

    public static Post createPost(String title, String content, Member author, boolean anonymous) {
        Post post = new Post();
        post.title = title;
        post.content = content;
        post.author = author;
		post.anonymous = anonymous;
        post.time = LocalDateTime.now().withSecond(0).withNano(0);
		post.views = 0;
        return post;
    }
}

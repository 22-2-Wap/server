package com.wap.codingtimer.post;

import com.wap.codingtimer.member.domain.Member;
import com.wap.codingtimer.member.repository.CommentRepository;
import com.wap.codingtimer.member.repository.MemberRepository;
import com.wap.codingtimer.post.domain.Comment;
import com.wap.codingtimer.post.domain.Likes;
import com.wap.codingtimer.post.domain.Post;
import com.wap.codingtimer.post.dto.PageWithCommentsDto;
import com.wap.codingtimer.post.repository.LikesRepository;
import com.wap.codingtimer.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final static int AMOUNT_OF_A_PAGE = 10;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;

    /**
     * 페이지가 1이면 0번 튜플부터 9번 튜플까지
     * 페이지가 2이면 10번 튜플부터 19번 튜플까지
     */
    private Pageable getPagingByDateTime(int page) {
        return PageRequest.of(page, AMOUNT_OF_A_PAGE, Sort.Direction.DESC, "dateTime");
    }

    public List<Post> getAllPostsInPage(int page) {
        Pageable paging = getPagingByDateTime(page);
        List<Post> posts = postRepository.findAll(paging).getContent();

        setLikes(posts.stream().map(Post::getId).toList());

        return posts;
    }

    public List<Post> getAllPostsInCategoryAndPage(String category, int page) {
        Pageable paging = getPagingByDateTime(page);
        List<Post> posts = postRepository.findAllByCategory(category, paging).getContent();
        setLikes(posts.stream().map(Post::getId).toList());

        return posts;
    }

    public PageWithCommentsDto getPageWithCommentsInPage(Long postId, int page) throws RuntimeException {
        Post post = postRepository.findById(postId).orElseThrow();
        setLikes(List.of(post.getId()));

        Pageable paging = getPagingByDateTime(page);
        List<Comment> comments = commentRepository.findAllByPostId(postId, paging).getContent();
        return new PageWithCommentsDto(post, comments);
    }

    @Transactional
    public Long save(String category, Long memberId, String topic, String content) {
        Member member = memberRepository.findById(memberId).get();

        Post post = new Post();
        post.setCategory(category);
        post.setTopic(topic);
        post.setContent(content);
        post.setMember(member);
        post.setDateTime();

        post = postRepository.save(post);
        return post.getId();
    }

    @Transactional
    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }

    @Transactional
    public Long update(String category, Long postId, String topic, String content) {
        Post post = postRepository.findById(postId).get();

        if (!topic.isBlank())
            post.setTopic(topic);
        if (!category.isBlank())
            post.setCategory(category);
        if (!content.isBlank())
            post.setContent(content);

        setLikes(List.of(postId));

        return post.getId();
    }

    @Transactional
    public void addLikes(Long memberId, Long postId) {
        Optional<Likes> likes = likesRepository.findLikesByMemberIdAndPostId(memberId, postId);
        Post post = postRepository.findById(postId).get();

        if (likes.isPresent()) {
            likesRepository.delete(likes.get());
            post.setLikes(post.getLikes() - 1);
            return;
        }

        Member member = memberRepository.findById(memberId).get();

        Likes saveLikes = new Likes();
        saveLikes.setPost(post);
        saveLikes.setMember(member);
        likesRepository.save(saveLikes);

        post.setLikes(post.getLikes() + 1);
    }

    @Transactional
    public void setLikes(List<Long> postIds) {
        for (Long postId : postIds) {
            Post post = postRepository.findById(postId).get();
            int likes = likesRepository.countLikesByPostId(postId);
            post.setLikes(likes);
        }
    }

    @Transactional
    public Long addComment(Long memberId, Long postId, String content) {
        Member member = memberRepository.findById(memberId).get();
        Post post = postRepository.findById(postId).get();

        Comment comment = new Comment();
        comment.setDateTime();
        comment.setPost(post);
        comment.setMember(member);
        comment.setContent(content);

        Comment save = commentRepository.save(comment);
        return save.getId();
    }

    @Transactional
    public void removeComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}

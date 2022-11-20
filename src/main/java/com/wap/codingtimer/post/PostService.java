package com.wap.codingtimer.post;

import com.wap.codingtimer.member.domain.Member;
import com.wap.codingtimer.member.repository.MemberRepository;
import com.wap.codingtimer.post.domain.Comment;
import com.wap.codingtimer.post.domain.Likes;
import com.wap.codingtimer.post.domain.Post;
import com.wap.codingtimer.post.dto.PageWithCommentsDto;
import com.wap.codingtimer.post.repository.CommentRepository;
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

    public List<Post> getAllPostsInPage(int page) {
        Pageable paging = getPagingByDateTime(page);

        return postRepository.findAll(paging).getContent();
    }

    public List<Post> getAllPostsInCategoryAndPage(String category, int page) {
        Pageable paging = getPagingByDateTime(page);

        return postRepository.findAllByCategory(category, paging).getContent();
    }

    public PageWithCommentsDto getPageWithCommentsInPage(Long postId, int page) throws RuntimeException {
        Post post = postRepository.findById(postId).orElseThrow();

        Pageable paging = getPagingByDateTime(page);
        List<Comment> comments = commentRepository.findAllByPostId(postId, paging).getContent();

        return new PageWithCommentsDto(post, comments);
    }

    @Transactional
    public Long save(String category, String memberId, String topic, String content) {
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
        commentRepository.findAllByPostId(postId).stream()
                .mapToLong(Comment::getId)
                .forEach(commentRepository::deleteById);

        postRepository.deleteById(postId);
    }

    @Transactional
    public Long update(String category, Long postId, String topic, String content) {
        Post post = postRepository.findById(postId).get();

        if (topic!=null)
            post.setTopic(topic);
        if (category!=null)
            post.setCategory(category);
        if (content!=null)
            post.setContent(content);

        return post.getId();
    }

    @Transactional
    public void addLikes(String memberId, Long postId) {
        Post post = postRepository.findById(postId).get();

        Optional<Likes> likes = likesRepository.findLikesByMemberIdAndPostId(memberId, postId);
        likes.ifPresentOrElse(o-> {
            likesRepository.delete(o);
            post.getLikes().remove(o);
            }, ()->{
                    Member member = memberRepository.findById(memberId).get();

                    Likes like = new Likes();
                    like.setMember(member);
                    like.setPost(post);
                    likesRepository.save(like);
                }
        );
    }

    @Transactional
    public Long addComment(String memberId, Long postId, String content) {
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

    public List<Post> getMemberPosts(String memberId, int page) {
        Pageable paging = getPagingByDateTime(page);

        return postRepository.findAllByMemberId(memberId, paging).getContent();
    }

    /**
     * 페이지가 1이면 0번 튜플부터 9번 튜플까지
     * 페이지가 2이면 10번 튜플부터 19번 튜플까지
     */
    private Pageable getPagingByDateTime(int page) {
        return PageRequest.of(page, AMOUNT_OF_A_PAGE, Sort.Direction.DESC, "dateTime");
    }
}

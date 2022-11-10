package com.wap.codingtimer.service;

import com.wap.codingtimer.domain.Comment;
import com.wap.codingtimer.domain.Member;
import com.wap.codingtimer.domain.Post;
import com.wap.codingtimer.repository.CommentRepository;
import com.wap.codingtimer.repository.MemberRepository;
import com.wap.codingtimer.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long register(Long memberId, Long postId, String content) {
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
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}

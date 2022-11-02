package com.wap.codingtimer.repository;

import com.wap.codingtimer.domain.Comment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;

@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;


    @Test
    public void 댓글을최신순으로받기() throws Exception{

        Comment comment[] = new Comment[11];
        //given
        for (int i = 0; i < comment.length; i++) {
            comment[i] =new Comment();
            comment[i].setDateTime();
            commentRepository.save(comment[i]);
        }

        //when
        Sort sort = Sort.by("dateTime").descending();
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<Comment> commentPage = commentRepository.findAll(pageable);

        //then
        List<Comment> commentList = commentPage.getContent();
        for (Comment cm:commentList) {
            System.out.println("cm.getId() = " + cm.getId());
        }


    }

}

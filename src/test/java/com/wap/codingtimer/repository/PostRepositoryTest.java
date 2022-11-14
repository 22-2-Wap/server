package com.wap.codingtimer.repository;

import com.wap.codingtimer.post.domain.Post;
import com.wap.codingtimer.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;


    @Test
    public void 시간순으로포스트들고오기() throws Exception{

        //given
        Post post1 = new Post();
        Post post2 = new Post();
        Post post3 = new Post();
        post1.setDateTime();
        post2.setDateTime();
        post3.setDateTime();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);


        //when
        Sort sort = Sort.by("dateTime").descending();
        Pageable pageable = PageRequest.of(0, 4, sort);
        Page<Post> postPage = postRepository.findAll(pageable);


        //then
        List<Post> postList = postPage.getContent();
        System.out.println("post1 = " + post1.getId());
        postPage.get().forEach(post -> {
            System.out.println(post.getId());
        });
        System.out.println("commentList = " + postList);

    }
}
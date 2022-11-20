package com.wap.codingtimer.service;

import com.wap.codingtimer.member.domain.Member;
import com.wap.codingtimer.member.repository.MemberRepository;
import com.wap.codingtimer.post.PostService;
import com.wap.codingtimer.post.domain.Post;
import com.wap.codingtimer.post.dto.PageWithCommentsDto;
import com.wap.codingtimer.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void addPosts() {
        Member member = new Member("idfsadfsad", "pw", "nicknameaefasef");
        member.setNickname("재현ㅎㅎ");
        member = memberRepository.saveAndFlush(member);

        for (int i = 0; i < 15; i++) {
            postService.save("Algorithm", member.getId(), "제목", "내용 ㅎㅎ");
        }
    }

    @Test
    void 페이지_다_가져오기() throws Exception {
        //given
        int page = 1;

        //when
        List<Post> allPostsInPage = postService.getAllPostsInPage(page);
        allPostsInPage.forEach(System.out::println);

        //then
        assertThat(allPostsInPage.size()).isEqualTo(5);
    }

    @Test
    void 카테고리별_페이지_다_가져오기() throws Exception {
        //given
        int page = 1;
        String category = "noSuchCategory";

        //when
        List<Post> allPostsInPage = postService.getAllPostsInCategoryAndPage(category, page);
        allPostsInPage.forEach(System.out::println);

        //then
        assertThat(allPostsInPage.size()).isEqualTo(0);
    }

    @Test
    void 댓글과_같이_포스트_가져오기() throws Exception {
        //given
        Member member = new Member("idqwe", "pw", "nickfnamade");
        member = memberRepository.save(member);

        //when
        Long postId = postService.save("티라미수", member.getId(), "마시땅", "후후");

        //then
        PageWithCommentsDto post = postService.getPageWithCommentsInPage(postId, 0);
        System.out.println("post.getPost() = " + post.getPost());

        assertThat(post.getPost().getId()).isEqualTo(postId);
        assertThat(post.getComments().size()).isZero();
    }

    @Test
    void 삭제() throws Exception {
        //given
        List<Post> allPostsInPage = postService.getAllPostsInPage(0);

        //when
        allPostsInPage.stream()
                .map(Post::getId)
                .forEach(postService::delete);

        //then
        List<Post> result = postService.getAllPostsInPage(0);
        assertThat(result.size()).isEqualTo(5);
    }

    @Test
    void 존재하지_않는_포스트_조회() throws Exception {
        //given

        //when
//        PageWithCommentsDto post = postService.getPageWithCommentsInPage(100L, 0);

        //then
        assertThrows(NoSuchElementException.class,
                () -> postService.getPageWithCommentsInPage(100L, 0));
    }

    @Test
    void 좋아요_누르기_테스트() throws Exception {
        //given
        int page = 0;
        Member member = memberRepository.save(new Member("id", "pw", "nickname"));
        List<Post> allPostsInPage = postService.getAllPostsInPage(page);

        //when
        allPostsInPage.forEach(o -> postService.addLikes(member.getId(), o.getId()));
        long likes = postService.getAllPostsInPage(page).stream()
                .map(Post::getLikes)
                .mapToInt(List::size)
                .sum();
        //then
        assertThat(likes).isEqualTo(10);
    }

    @Test
    void 좋아요_취소_테스트() throws Exception {
        //given
        int page = 0;
        Member member = memberRepository.save(new Member("id", "pw", "nickname"));

        //when
        postService.getAllPostsInPage(page)
                .forEach(o -> postService.addLikes(member.getId(), o.getId()));

        postService.getAllPostsInPage(page)
                .forEach(o -> postService.addLikes(member.getId(), o.getId()));

        //then
        int sum = postService.getAllPostsInPage(page).stream()
                .map(Post::getLikes)
                .mapToInt(List::size)
                .sum();

        assertThat(sum).isZero();
    }
}
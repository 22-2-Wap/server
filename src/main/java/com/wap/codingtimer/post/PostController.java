package com.wap.codingtimer.post;

import com.wap.codingtimer.auth.service.OauthService;
import com.wap.codingtimer.post.dto.PageWithCommentsDto;
import com.wap.codingtimer.post.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("post")
public class PostController {

    private final OauthService oauthService;
    private final PostService postService;

    @GetMapping("all/{page}")
    public List<PostDto> getAllPosts(@PathVariable("page") int page) {

        return postService.getAllPostsInPage(page);
    }

    @GetMapping("{category}/{page}")
    public List<PostDto> getAllPostsByCategory(@PathVariable("category") String category,
                                            @PathVariable("page") int page) {
        return postService.getAllPostsInCategoryAndPage(category, page);
    }

    @GetMapping("/view/{id}/{reply_page}")
    public PageWithCommentsDto getPost(@PathVariable("id") Long postId,
                                       @PathVariable("reply_page") int page,
                                       HttpServletRequest request) {
        String userId = oauthService.getUserId(request);

        PageWithCommentsDto pageWithCommentsInPage = postService.getPageWithCommentsInPage(postId, page);
        pageWithCommentsInPage.setLikePressed(
                postService.isMemberPressedLike(userId, pageWithCommentsInPage.getPost().getId()));

        return pageWithCommentsInPage;
    }

    @PostMapping("{category}")
    public String savePost(@PathVariable("category") String category,
                           @RequestBody PostDto post,
                           HttpServletRequest request) {
        String userId = oauthService.getUserId(request);
        postService.save(category, userId, post.getTopic(), post.getContent());

        return "OK";
    }

    @DeleteMapping("{id}")
    public String deletePost(@PathVariable("id") Long id) {
        postService.delete(id);

        return "OK";
    }

    @PostMapping("{category}/{id}")
    public String updatePost(@PathVariable("id") Long postId,
                             @PathVariable("category") String category,
                             @RequestBody PostDto post) {
        postService.update(category, postId, post.getTopic(), post.getContent());

        return "OK";
    }

    @PostMapping("likes/{id}")
    public String addLikes(@PathVariable("id") Long postId,
                           HttpServletRequest request) {
        String userId = oauthService.getUserId(request);
        postService.addLikes(userId, postId);

        return "OK";
    }

    @PostMapping("reply/{id}")
    public String addComment(@PathVariable("id") Long postId,
                             @RequestBody Map<String, String> content,
                             HttpServletRequest request) {
        String userId = oauthService.getUserId(request);
        postService.addComment(userId, postId, content.get("content"));

        return "OK";
    }

    @DeleteMapping("reply/{id}")
    public String deleteComment(@PathVariable("id") Long commentId) {
        postService.removeComment(commentId);

        return "OK";
    }

    @GetMapping("my-content/{page}")
    public List<PostDto> getUserPosts(@PathVariable("page") int page,
                                   HttpServletRequest request) {
        String userId = oauthService.getUserId(request);

        return postService.getPostsByMemberId(userId, page);
    }
}

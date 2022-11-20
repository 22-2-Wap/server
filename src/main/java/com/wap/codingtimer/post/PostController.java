package com.wap.codingtimer.post;

import com.wap.codingtimer.auth.service.OauthService;
import com.wap.codingtimer.post.domain.Post;
import com.wap.codingtimer.post.dto.PageWithCommentsDto;
import com.wap.codingtimer.post.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("post")
public class PostController {

    private final OauthService oauthService;
    private final PostService postService;

    @GetMapping("all/{page}")
    public List<Post> getAllPosts(@PathVariable("page") int page) {
        return postService.getAllPostsInPage(page);
    }

    @GetMapping("{category}/{page}")
    public List<Post> getAllPostsByCategory(@PathVariable("category") String category,
                                            @PathVariable("page") int page) {
        return postService.getAllPostsInCategoryAndPage(category, page);
    }

    @GetMapping("{id}/{reply_page}")
    public PageWithCommentsDto getPost(@PathVariable("id") Long postId,
                                       @PathVariable("reply_page") int page) {
        return postService.getPageWithCommentsInPage(postId, page);
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
                             @RequestBody String content,
                             HttpServletRequest request) {
        String userId = oauthService.getUserId(request);
        postService.addComment(userId, postId, content);

        return "OK";
    }

    @DeleteMapping("reply/{id}")
    public String deleteComment(@PathVariable("id") Long commentId) {
        postService.removeComment(commentId);

        return "OK";
    }

    @GetMapping("my-content/{page}")
    public List<Post> getUserPosts(@PathVariable("page") int page,
                                   HttpServletRequest request) {
        String userId = oauthService.getUserId(request);
        return postService.getMemberPosts(userId, page);
    }
}

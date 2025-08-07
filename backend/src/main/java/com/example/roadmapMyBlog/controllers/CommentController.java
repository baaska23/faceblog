package com.example.roadmapMyBlog.controllers;

import com.example.roadmapMyBlog.dao.Comment;
import com.example.roadmapMyBlog.services.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;
    
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    
    @GetMapping("/comments")
    public List<Comment> getComments() {
        return commentService.getComments();
    }
    
    @GetMapping("/comments-per-article/{id}")
    public List<Comment> getCommentsPerArticle(@PathVariable int id) {
        return commentService.getCommentsPerArticle(id);
    }
    
    @PostMapping("/comments/{articleId}")
    public Comment addComment(@RequestBody Comment comment, @PathVariable int articleId) {
        return commentService.addComment(comment, articleId);
    }
}

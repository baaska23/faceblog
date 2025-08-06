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
    @PostMapping("/comments")
    public Comment addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }
}

package com.example.roadmapMyBlog.services;

import com.example.roadmapMyBlog.dao.Comment;
import com.example.roadmapMyBlog.repositories.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    
    public List<Comment> getComments() {
        return commentRepository.findAll();
    }
    
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }
}

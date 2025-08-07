package com.example.roadmapMyBlog.services;

import com.example.roadmapMyBlog.dao.Comment;
import com.example.roadmapMyBlog.dao.Article;
import com.example.roadmapMyBlog.repositories.CommentRepository;
import com.example.roadmapMyBlog.repositories.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    
    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }
    
    public List<Comment> getComments() {
        return commentRepository.findAll();
    }
    
    public List<Comment> getCommentsPerArticle(int articleId) {
        return commentRepository.findByArticleId(articleId);
    }
    
    public Comment addComment(Comment comment, int articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        comment.setArticle(article);
        return commentRepository.save(comment);
    }
}
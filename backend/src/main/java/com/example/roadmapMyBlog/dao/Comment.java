package com.example.roadmapMyBlog.dao;

import jakarta.persistence.*;

@Entity
@Table(name="comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String commentingUser;
    private String comment;
    
    
    @ManyToOne
    @JoinColumn(name = "article_id", nullable = true)
    private Article article;
    
    public Comment() {}
    
    public Comment(Integer id, String commentingUser, String comment, Article article) {
        this.id = id;
        this.commentingUser = commentingUser;
        this.comment = comment;
        this.article = article;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getCommentingUser() {
        return commentingUser;
    }
    
    public void setCommentingUser(String commentingUser) {
        this.commentingUser = commentingUser;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public Article getArticle() { return article; }
    
    public void setArticle(Article article) { this.article = article; }
}

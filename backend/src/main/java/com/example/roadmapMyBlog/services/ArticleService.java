package com.example.roadmapMyBlog.services;

import com.example.roadmapMyBlog.dao.Article;
import com.example.roadmapMyBlog.repositories.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(int id) {
        return articleRepository.findById(id);
    }

    public Article addArticle(Article article) {
        Date now = new Date();
        article.setPublished_at(now);
        return articleRepository.save(article);
    }

    public Article updateArticleById(int id, Article updatingArticle) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            Article existingArticle = optionalArticle.get();
            existingArticle.setTitle(updatingArticle.getTitle());
            existingArticle.setContent(updatingArticle.getContent());
            existingArticle.setPublished_at(updatingArticle.getPublished_at());
            existingArticle.setUpdated_at(updatingArticle.getUpdated_at());
            return articleRepository.save(updatingArticle);
        }
        return null;
    }

    public Article patchArticleById(int id, Article partialArticle) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            Article patchingArticle = optionalArticle.get();
            if (partialArticle.getTitle() != null) {
                patchingArticle.setTitle(partialArticle.getTitle());
            }
            if (partialArticle.getContent() != null) {
                patchingArticle.setContent(partialArticle.getContent());
            }
            if (partialArticle.getPublished_at() != null) {
                patchingArticle.setPublished_at(partialArticle.getPublished_at());
            }
            if (partialArticle.getUpdated_at() != null) {
                patchingArticle.setUpdated_at(partialArticle.getUpdated_at());
            }
            return articleRepository.save(patchingArticle);
        }
        return null;
    }

    public Article deleteArticleById(int id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()){
            Article deletingArticle = optionalArticle.get();
            articleRepository.delete(deletingArticle);
            return deletingArticle;
        }
        return null;
    }
}

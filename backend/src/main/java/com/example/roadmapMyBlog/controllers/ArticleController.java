package com.example.roadmapMyBlog.controllers;

import com.example.roadmapMyBlog.dao.Article;
import com.example.roadmapMyBlog.services.ArticleSearchService;
import org.springframework.web.bind.annotation.*;
import com.example.roadmapMyBlog.services.ArticleService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleSearchService articleSearchService;

    public ArticleController(ArticleService articleService, ArticleSearchService articleSearchService) {
        this.articleService = articleService;
        this.articleSearchService = articleSearchService;
    }

    @GetMapping("/articles")
    public List<Article> getArticles() {
        return articleService.getArticles();
    }

    @GetMapping("/articles/{id}")
    public Optional<Article> getArticleById(@PathVariable int id) {
        return articleService.getArticleById(id);
    }

    @PostMapping("/articles")
    public Article addArticle(@RequestBody Article article){
        return articleService.addArticle(article);
    }

    @PutMapping("/articles/{id}")
    public Article updateArticleById(@PathVariable int id, @RequestBody Article article) {
        article.setId(id);
        return articleService.updateArticleById(id, article);
    }

    @PatchMapping("/articles/{id}")
    public Article patchArticleById(@PathVariable int id, @RequestBody Article article) {
        article.setId(id);
        return articleService.patchArticleById(id, article);
    }

    @DeleteMapping("/articles/{id}")
    public Article deleteArticleById(@PathVariable int id) {
        return articleService.deleteArticleById(id);
    }
    
    @GetMapping("/search")
    public List<Article> searchArticleByTrie(@RequestParam String prefix) {
        return articleSearchService.searchByTitlePrefix(prefix);
    }
}
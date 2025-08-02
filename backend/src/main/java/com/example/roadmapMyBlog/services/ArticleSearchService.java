package com.example.roadmapMyBlog.services;

import com.example.roadmapMyBlog.dao.Article;
import com.example.roadmapMyBlog.dsa.Trie;
import com.example.roadmapMyBlog.repositories.ArticleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ArticleSearchService {
    private final ArticleRepository articleRepository;
    private final Trie trie = new Trie();
    
    public ArticleSearchService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
    
    @PostConstruct
    public void buildTrie() {
        List<Article> articles = articleRepository.findAll();
        for (Article article : articles) {
            trie.insert(article.getTitle(), article.getId());
        }
    }
    
    public List<Article> searchByTitlePrefix(String prefix) {
        List<Integer> ids = trie.searchPrefix(prefix);
        return articleRepository.findAllById(ids);
    }
}

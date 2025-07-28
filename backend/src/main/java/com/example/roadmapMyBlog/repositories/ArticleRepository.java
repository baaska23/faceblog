package com.example.roadmapMyBlog.repositories;

import com.example.roadmapMyBlog.dao.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
}
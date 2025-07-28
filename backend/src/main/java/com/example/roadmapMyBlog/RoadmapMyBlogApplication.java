package com.example.roadmapMyBlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.roadmapMyBlog.repositories")
public class RoadmapMyBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoadmapMyBlogApplication.class, args);
	}

}
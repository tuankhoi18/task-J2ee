package com.javaguides.springboot_restful_webservoce.Repository;

import com.javaguides.springboot_restful_webservoce.Entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article,String> {
}

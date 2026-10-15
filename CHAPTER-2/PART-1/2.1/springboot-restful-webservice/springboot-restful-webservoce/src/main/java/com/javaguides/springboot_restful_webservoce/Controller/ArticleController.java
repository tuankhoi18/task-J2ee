package com.javaguides.springboot_restful_webservoce.Controller;

import com.javaguides.springboot_restful_webservoce.Entity.Article;
import com.javaguides.springboot_restful_webservoce.Service.ArticleService;
import com.javaguides.springboot_restful_webservoce.dto.ArticleCreateRequest;
import com.javaguides.springboot_restful_webservoce.dto.ArticleUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")

public class ArticleController {
    @Autowired
    ArticleService articleService;
    @PostMapping
     Article createArticle(@RequestBody ArticleCreateRequest request){
        return articleService.createArticle(request);
    }
    @GetMapping
     List<Article> GetArticle(){
        return articleService.getArticle();
    }
    @GetMapping("/{articleid}")
     Article getArticle(@PathVariable("articleid") String articleid)
    {
        return articleService.getArticle(articleid);
    }
    @PutMapping("/{articleid}")
    Article updateArticle(@RequestBody ArticleUpdateRequest request,@PathVariable("articleid") String articleid)
    {
        return articleService.updateArticle(request,articleid);
    }
    @DeleteMapping("/{articleid}")
    String deleteArticle(@PathVariable("articleid") String articleid)
    {
        articleService.deleteArticle(articleid);
        return "Article has been deleted";
    }

}

package com.javaguides.springboot_restful_webservoce.Service;

import com.javaguides.springboot_restful_webservoce.Entity.Article;
import com.javaguides.springboot_restful_webservoce.Repository.ArticleRepository;
import com.javaguides.springboot_restful_webservoce.dto.ArticleCreateRequest;
import com.javaguides.springboot_restful_webservoce.dto.ArticleUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Autowired
    ArticleRepository articleRepository;
    public Article createArticle(ArticleCreateRequest request)
    {
        Article ar = new Article();
        ar.setTitle(request.getTitle());
        ar.setAuthors(request.getAuthors());
        ar.setPublicationYear(request.getPublicationYear());
        ar.setJournal(request.getJournal());
        ar.setPublisher(request.getPublisher());
        ar.setCitations(request.getCitations());
        ar.setUrl(request.getUrl());

        return articleRepository.save(ar);
    }
    public List<Article> getArticle(){
        return articleRepository.findAll();
    }
    public Article getArticle(String articleid){
        return articleRepository.findById(articleid).orElseThrow(() -> new RuntimeException("Không tìm thấy Article nào"));
    }
    public Article updateArticle(ArticleUpdateRequest request,String articleid)
    {
        Article ar = getArticle(articleid);
        ar.setTitle(request.getTitle());
        ar.setAuthors(request.getAuthors());
        ar.setPublicationYear(request.getPublicationYear());
        ar.setJournal(request.getJournal());
        ar.setPublisher(request.getPublisher());
        ar.setCitations(request.getCitations());
        ar.setUrl(request.getUrl());
        return articleRepository.save(ar);
    }
    public void deleteArticle(String articleid){
        articleRepository.deleteById(articleid);
    }


}

package com.javaguides.springboot_restful_webservoce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleUpdateRequest {
    private String title;

    private String authors;

    private Integer publicationYear;

    private String journal;

    private String publisher;

    private Integer citations;

    private String url;
}

package top.graduation.rs;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joanna Pakosh on Авг., 2018
 */

@SpringBootApplication
@EnableCaching
public class SpringBootApp {

    public static void main(String[] args) {
      SpringApplication.run(SpringBootApp.class, args);
    }

    @Bean
    public OpenAPI restaurantVotingSystem() {
        List<Tag> tags = new ArrayList<>();
        Tag tag = new Tag();
        tag.setName("Restaurants");
        tags.add(tag);
        return new OpenAPI()
                .info(new Info().title("Restaurant API Document")
                        .description("Bài tập 2.6").version("v1")
                        .contact(new Contact()
                                .name("Nguyễn Phạm Tuấn Khôi")
                                .email("9tuankhoi6@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Source of the system").url("https://github.com/JolaPsh/voting-system")).tags(tags);
    }
}
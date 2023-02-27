package com.sparta.springnew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ServletComponentScan // @WebServlet 어노테이션이 동작하게 함
@SpringBootApplication
@EnableJpaAuditing
public class SpringnewApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringnewApplication.class, args);
    }

}

package br.com.amaro.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The main class of application
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@EnableScheduling
@SpringBootApplication
@EntityScan("br.com.amaro.demo.entities")
@EnableJpaRepositories("br.com.amaro.demo.repositories")
public class DemoApplication {

    /**
     * The application start method
     * @param args parameter for configuration input of Spring
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
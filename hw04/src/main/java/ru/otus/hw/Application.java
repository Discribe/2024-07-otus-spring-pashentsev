package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Application {
    public static void main(String[] args) {

        //Создать контекст Spring Boot приложения
        ApplicationContext context = SpringApplication.run(Application.class);
//        var testRunnerService = context.getBean(TestRunnerService.class);
//        testRunnerService.run();

    }
}
package ru.otus.hw;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.service.TestRunnerService;

@ComponentScan(basePackages = "ru.otus.hw")
@PropertySource("classpath:application.properties")
@Configuration
@Import(AppProperties.class)
public class Application {
    public static void main(String[] args) {

        //Создать контекст на основе Annotation/Java конфигурирования
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();

    }
}
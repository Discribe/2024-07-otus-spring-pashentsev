package ru.otus.hw.config;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.StreamsIOService;

@Configuration
public class TestServiceConfiguration {
    @Bean
    QuestionDao csvQuestionDao(){
        return Mockito.mock(CsvQuestionDao.class);
    }
    @Bean
    IOService streamsIOService(){
        return Mockito.mock(StreamsIOService.class);
    }
}

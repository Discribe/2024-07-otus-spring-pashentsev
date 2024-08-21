package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Student;

import javax.swing.*;

@SpringJUnitConfig
@ContextConfiguration( classes = {
        StreamsIOService.class,
        CsvQuestionDao.class,
        TestServiceImpl.class,
        AppProperties.class })
class TestServiceTest {

    @Autowired
    private TestService testService;

    @BeforeEach
    void before(){
      Mockito.mock(StreamsIOService.class).printLine("");
      Mockito.mock(StreamsIOService.class).readIntForRange(1,1,"");
    }
    @Test
    void executeTestFor() {
        testService.executeTestFor(new Student("ddd", "ddd"));

    }
}
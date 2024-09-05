package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.otus.hw.Application;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.config.TestServiceConfiguration;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import javax.swing.*;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {TestServiceConfiguration.class,
        AppProperties.class, Application.class,
        TestServiceImpl.class})
class TestServiceTest {

    @Autowired
    private QuestionDao csvQuestionDao;

    @Autowired
    private TestConfig testConfig;

    @Autowired
    private TestFileNameProvider testFileNameProvider;

    @Autowired
    private TestService testService;

    @Autowired
    private IOService streamsIOService;

    private List questionList;

    private Student student;

    @BeforeEach
    void before() {
        questionList = List.of(new Question("QuestionOne",
                        List.of(
                                new Answer("AnswerTrue", true),
                                new Answer("AnswerFalse", false)
                        )),
                new Question("QuestionTwo",
                        List.of(
                                new Answer("AnswerFalse", false),
                                new Answer("AnswerTrue", true)
                        )),
                new Question("QuestionThree",
                        List.of(
                                new Answer("AnswerFalse", true),
                                new Answer("AnswerTrue", false),
                                new Answer("AnswerTrue", false)
                        ))

        );

        Mockito.doNothing().when(streamsIOService).printLine(any());
        Mockito.doNothing().when(streamsIOService).printFormattedLine(any());
        Mockito.when(csvQuestionDao.findAll())
                .thenReturn(questionList);

        student = new Student("Farionov", "dimitry");
    }

    @Test
    void correctReadProperties() {
        Assertions.assertEquals(1, testConfig.getRightAnswersCountToPass());
        Assertions.assertEquals("questions.csv", testFileNameProvider.getTestFileName());
    }

    @Test
    void executeTestForTwoCorrectAnswers() {
        Mockito.when(streamsIOService.readIntForRange(0, questionList.size() - 1,
                "Некорректно указан номер ответа")).thenReturn(0);

        var testResult = testService.executeTestFor(student);

        Mockito.verify(csvQuestionDao, Mockito.atLeast(1) ).findAll();

        Assertions.assertEquals(student, testResult.getStudent());
        Assertions.assertEquals(2, testResult.getRightAnswersCount());
    }

    @Test
    void executeTestForOneCorrectAnswers() {
        Mockito.when(streamsIOService.readIntForRange(0, questionList.size() - 1,
                "Некорректно указан номер ответа")).thenReturn(1);

        var testResult = testService.executeTestFor(student);

        Mockito.verify(csvQuestionDao, Mockito.atLeast(1) ).findAll();

        Assertions.assertEquals(student, testResult.getStudent());
        Assertions.assertEquals(1, testResult.getRightAnswersCount());

    }
}
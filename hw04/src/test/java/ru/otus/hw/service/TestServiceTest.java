package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@TestPropertySource( properties = {
        "test.locale=en-US",
        "test.rightAnswersCountToPass=1"
})
class TestServiceTest {

    @MockBean
    private QuestionDao csvQuestionDao;

    @Autowired
    private TestConfig testConfig;

    @Autowired
    private TestFileNameProvider testFileNameProvider;

    @Autowired
    private TestService testService;

    @MockBean
    @Qualifier("streamsIOService")
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
        Mockito.when(streamsIOService.readIntForRange(anyInt(), anyInt(), anyString())).thenReturn(0);

        var testResult = testService.executeTestFor(student);

        Mockito.verify(csvQuestionDao, Mockito.atLeast(1) ).findAll();

        Assertions.assertEquals(student, testResult.getStudent());
        Assertions.assertEquals(2, testResult.getRightAnswersCount());
    }

    @Test
    void executeTestForOneCorrectAnswers() {
        Mockito.when(streamsIOService.readIntForRange(anyInt(), anyInt(), anyString())).thenReturn(1);

        var testResult = testService.executeTestFor(student);

        Mockito.verify(csvQuestionDao, Mockito.atLeast(1) ).findAll();

        Assertions.assertEquals(student, testResult.getStudent());
        Assertions.assertEquals(1, testResult.getRightAnswersCount());
    }
}
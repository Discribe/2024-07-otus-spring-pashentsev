package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.LocaleConfig;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.TestService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource( properties = {
        "test.locale=en-US",
        "test.rightAnswersCountToPass=1",
        "test.fileNameByLocaleTag.en-US=questionsInit.csv",
        "app.run.useCommandLineRunner=true",
        "spring.shell.interactive.enabled=false"

})
class CsvQuestionDaoTest {

    @Autowired
    private QuestionDao csvQuestionDao;
    @MockBean
    @Qualifier("streamsIOService")
    private IOService streamsIOService;

    @Autowired
    @SpyBean
    private TestFileNameProvider fileNameProvider;

    @BeforeEach
    void beforeAll(){

        Mockito.doNothing().when(streamsIOService).printLine(any());
        Mockito.doNothing().when(streamsIOService).printFormattedLine(any());
    }

    @Test
    @DisplayName("При отсутствии файла csv корректное отлавливание QuestionReadException")

    void fileCsvNotFound() {
        Assertions.assertEquals(1,1);
        Mockito.when(fileNameProvider.getTestFileName()).thenAnswer(invocationOnMock -> "sdf");

        assertThrows(QuestionReadException.class, () -> {
               csvQuestionDao.findAll();
        });
    }

    @Test
    @DisplayName("Корректное считывание файла и преобразования в POJO обьекты")
    void fileCsvCorrectionRead() {
        Mockito.when(fileNameProvider.getTestFileName()).thenAnswer(invocationOnMock -> "questionsSuccess.csv");
        var result = csvQuestionDao.findAll();

        assertEquals(2, result.size());
    }
}
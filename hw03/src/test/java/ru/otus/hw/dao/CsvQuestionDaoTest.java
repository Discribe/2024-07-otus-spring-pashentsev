package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvQuestionDaoTest {

    private TestFileNameProvider fileNameProvider;

    @BeforeEach
    void beforeAll(){
        fileNameProvider = Mockito.mock(AppProperties.class);
    }

    @Test
    @DisplayName("При отсутствии файла csv корректное отлавливание QuestionReadException")
    void fileCsvNotFound() {
        Mockito.when(fileNameProvider.getTestFileName()).thenAnswer(invocationOnMock -> "sdf");
        assertThrows(QuestionReadException.class, () -> {
            QuestionDao questionDao = new CsvQuestionDao(fileNameProvider);
            questionDao.findAll();
        });
    }

    @Test
    @DisplayName("Корректное считывание файла и преобразования в POJO обьекты")
    void fileCsvCorrectionRead() {
        Mockito.when(fileNameProvider.getTestFileName()).thenAnswer(invocationOnMock -> "questionsSuccess.csv");
        QuestionDao questionDao = new CsvQuestionDao(fileNameProvider);
        var result = questionDao.findAll();

        assertEquals(2, result.size());
    }
}
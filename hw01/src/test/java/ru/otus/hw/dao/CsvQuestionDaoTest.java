package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvQuestionDaoTest {

    @Test
    @DisplayName("При отсутствии файла csv корректное отлавливание QuestionReadException")
    void fileCsvNotFound() {
        TestFileNameProvider fileNameProvider = new AppProperties("questionsNotFound.xml");
        assertThrows(QuestionReadException.class, () -> {
            QuestionDao questionDao = new CsvQuestionDao(fileNameProvider);
            questionDao.findAll();
        });
    }

    @Test
    @DisplayName("Корректное считывание файла и преобразования в POJO обьекты")
    void fileCsvCorrectionRead() {
        TestFileNameProvider fileNameProvider = new AppProperties("questionsSuccess.csv");
        QuestionDao questionDao = new CsvQuestionDao(fileNameProvider);
        var result = questionDao.findAll();

        assertEquals(2, result.size());
    }
}
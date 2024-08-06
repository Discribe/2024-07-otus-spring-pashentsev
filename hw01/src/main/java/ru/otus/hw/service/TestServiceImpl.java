package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final IOService ioService;

    private final TestFileNameProvider fileNameProvider;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        // Получить вопросы из дао и вывести их с вариантами ответов
        QuestionDao questionDao = new CsvQuestionDao(fileNameProvider);
        questionDao.findAll().forEach(
                question -> {
                    System.out.println(question.text());
                    question.answers().forEach(System.out::println);
                    System.out.println();
                }
        );
    }
}

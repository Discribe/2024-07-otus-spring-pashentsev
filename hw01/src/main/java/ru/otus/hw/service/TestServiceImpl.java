package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;

import java.util.concurrent.atomic.AtomicInteger;

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
        var indexQuestion = new AtomicInteger();
        questionDao.findAll().forEach(
                question -> {
                    System.out.println(String.format("%s. %s", indexQuestion.incrementAndGet(), question.text()));
                    question.answers().forEach(answer -> System.out.println(String.format("   %s", answer.text())));
                    System.out.println();
                }
        );
    }
}

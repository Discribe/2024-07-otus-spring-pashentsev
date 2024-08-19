package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final TestFileNameProvider fileNameProvider;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var isAnswerValid = false; // Задать вопрос, получить ответ
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }


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

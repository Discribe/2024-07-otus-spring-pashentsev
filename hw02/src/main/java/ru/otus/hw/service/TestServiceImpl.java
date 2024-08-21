package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.concurrent.atomic.AtomicInteger;

@Component
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
            var countAnswer = new AtomicInteger();
            var isAnswerValid = false; // Задать вопрос, получить ответ
            ioService.printFormattedLine("%s", question.text());
            question.answers().forEach(answer -> ioService.printFormattedLine("%s. %s",
                    countAnswer.getAndIncrement(), answer.text()));

            var indexAnswer = ioService.readIntForRange(0, question.answers().size() - 1,
                    "Некорректно указан номер ответа");

            isAnswerValid = question.answers().get(indexAnswer).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}

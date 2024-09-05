package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService localizedIOService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        localizedIOService.printLine("");
        localizedIOService.printLineLocalized("TestService.answer.the.questions");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        for (var question: questions) {
            var countAnswer = new AtomicInteger();
            var isAnswerValid = false; // Задать вопрос, получить ответ
            localizedIOService.printFormattedLine("%s", question.text());
            question.answers().forEach(answer -> localizedIOService.printFormattedLine("%s. %s",
                    countAnswer.getAndIncrement(), answer.text()));

            var indexAnswer = localizedIOService.readIntForRangeLocalized(0, question.answers().size() - 1,
                    "TestService.answer.number.incorrect.input");

            isAnswerValid = question.answers().get(indexAnswer).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}

package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.Objects;

@ShellComponent
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.run.useCommandLineRunner", havingValue = "false")
public class TestRunnerServiceShellImpl {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private Student student;

    private TestResult testResult;

    @ShellMethod(value = "DefineStudent", key = {"l", "login"})
    public String determineCurrentStudent() {
        student = studentService.determineCurrentStudent();
        return String.format("Hello %s  %s. For start test enter shell command: test or t",
                student.firstName(), student.lastName());
    }

    @ShellMethod(value = "RunTest", key = {"t", "test"})
    public String runTest() {
        if (Objects.isNull(student)) {
            return "Need logins";
        }
        testResult = testService.executeTestFor(student);
        return String.format("For view result test enter shell command: view");
    }

    @ShellMethod(value = "ShowResult", key = {"v", "view"})
    public String showResultTest() {

        if (Objects.isNull(testResult)) {
            return "You are not run test.";
        }

        resultService.showResult(testResult);
        return "Thanks!";
    }

}

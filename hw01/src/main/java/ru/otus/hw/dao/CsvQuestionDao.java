package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {

        List<QuestionDto> questions;
        try (var inputStream = Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(fileNameProvider.getTestFileName()))) {

            var readerCsv = new BufferedReader(new InputStreamReader(inputStream));

            questions = new CsvToBeanBuilder<QuestionDto>(readerCsv)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .withOrderedResults(false)
                    .build()
                    .parse();

            return questions.stream().map(QuestionDto::toDomainObject).collect(Collectors.toList());

        } catch (NullPointerException | IOException e) {
            throw new QuestionReadException(e.getMessage(), e);
        }
    }
}

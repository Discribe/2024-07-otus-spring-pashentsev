package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {

        List<QuestionDto> beans;
        beans = new CsvToBeanBuilder<QuestionDto>(readCvsFile(fileNameProvider.getTestFileName()))
                .withType(QuestionDto.class).withSeparator(';')
                .withSkipLines(1).withOrderedResults(false).build().parse();

        return beans.stream().map(QuestionDto::toDomainObject).collect(Collectors.toList());

    }

    private Reader readCvsFile(String fileName) throws QuestionReadException {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(fileName);

            if (inputStream == null) {
                throw new QuestionReadException("file not found! " + fileName);
            } else {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                Reader reader = new BufferedReader(inputStreamReader);
                return reader;
            }

        } catch (Exception e) {
            throw new QuestionReadException(e.getMessage(), e);
        }
    }


}

package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        FileReader fileReader = null;

            List<QuestionDto> beans;
        beans = new CsvToBeanBuilder(readCvsFile(fileNameProvider.getTestFileName()))
                .withType(QuestionDto.class).withSeparator(';').withSkipLines(1).withOrderedResults(false).build().parse();

        // Использовать CsvToBean
            // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
            // Использовать QuestionReadException
            // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/

            return beans.stream().map(QuestionDto::toDomainObject).collect(Collectors.toList());

    }

    private Reader readCvsFile(String path) throws QuestionReadException {
        try {
            return getFileFromResourceAsStream(path);
        } catch (Exception e) {
            throw new QuestionReadException(e.getMessage(), e);
        }
    }

    private Reader getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
          Reader reader = new BufferedReader(inputStreamReader);
            return reader;
        }

    }

}

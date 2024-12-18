package ru.otus.hw.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Репозиторий на основе Jdbc для работы с книгами ")
@JdbcTest
@Import(JdbcAuthorRepository.class)
class JdbcAuthorRepositoryTest {

    @Autowired
    private JdbcAuthorRepository jdbcAuthorRepository;

    private List<Author> dbAuthors;


    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed().map(id -> new Author(id, "Author_" + id)).toList();
    }

    @Test
    @DisplayName("должен загрузиться список всех авторов")
    void testFindAll() {
        var actualAuthor = jdbcAuthorRepository.findAll();
        var expectedAuthor = getDbAuthors();
        assertThat(actualAuthor).containsExactlyElementsOf(expectedAuthor);
    }

    @DisplayName("должен загрузиться автор по id")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void testFindById(Author author) {

        var actualAuthor = jdbcAuthorRepository.findById(author.getId());
        assertThat(actualAuthor).isPresent().get().isEqualTo(author);
    }

}

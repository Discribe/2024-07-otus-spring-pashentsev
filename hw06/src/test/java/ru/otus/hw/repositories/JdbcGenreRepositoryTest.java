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
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@JdbcTest
@Import(JdbcGenreRepository.class)
class JdbcGenreRepositoryTest {

    @Autowired
    JdbcGenreRepository jdbcGenreRepository;
    private List<Genre> dbGenres;

    private static List<Genre> getDbGenres() {
        return LongStream.range(1, 4).boxed().map(id -> new Genre(id, "Genre_" + id)).toList();
    }

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @Test
    @DisplayName("должен загрузить список всех жанров")
    void testFindAll() {

        var actualGenres = jdbcGenreRepository.findAll();
        var expectedGenres = getDbGenres();
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    @ParameterizedTest
    @MethodSource("getDbGenres")
    @DisplayName("должен загрузить жанр по его id")
    void testFindById(Genre expectedGenre) {
        var actualGenre = jdbcGenreRepository.findById(expectedGenre.getId());
        assertThat(actualGenre).isPresent().get().isEqualTo(expectedGenre);
    }
}
package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {

    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    public JdbcBookRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public Optional<Book> findById(long id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        return namedParameterJdbcOperations.query("select " +
                        "bk.author_id as \"author_id\", " +
                        "bk.genre_id as \"genre_id\", " +
                        "bk.id as \"id\", " +
                        "bk.title as \"title\", " +
                        "auth.full_name as \"author\", " +
                        "gn.name as \"genre\" " +
                        "from books bk " +
                        " inner join authors auth on bk.author_id = auth.id  " +
                        "inner join genres gn on bk.genre_id = gn.id where bk.id = :id", param,
                new BookRowMapper()).stream().findFirst();
    }

    @Override
    public List<Book> findAll() {

        return namedParameterJdbcOperations.query("select " +
                        "bk.author_id as \"author_id\", " +
                        "bk.genre_id as \"genre_id\", " +
                        "bk.id as \"id\", " +
                        "bk.title as \"title\", " +
                        "auth.full_name as \"author\", " +
                        "gn.name as \"genre\" " +
                        "from books bk " +
                        " inner join authors auth on bk.author_id = auth.id  " +
                        "inner join genres gn on bk.genre_id = gn.id",
                new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        namedParameterJdbcOperations.update("delete from books where id =:id", param);

    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("author_id", book.getAuthor().getId())
                .addValue("genre_id", book.getGenre().getId());

        namedParameterJdbcOperations.update("insert into books (title, author_id, genre_id)" +
                " values (:title, :author_id, :genre_id)", param, keyHolder);
        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        findById(book.getId())
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(book.getId())));

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", book.getId())
                .addValue("title", book.getTitle())
                .addValue("author_id", book.getAuthor().getId())
                .addValue("genre_id", book.getGenre().getId());

        namedParameterJdbcOperations.update("update books set title = :title, author_id = :author_id, " +
                "genre_id = :genre_id where id = :id", param, keyHolder);
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long id = rs.getLong("id");
            String title = rs.getString("title");
            String author = rs.getString("author");
            Long authorId = rs.getLong("author_id");
            String genre = rs.getString("genre");
            Long genreId = rs.getLong("genre_id");

            return new Book(id, title,
                    (Objects.nonNull(authorId) && Objects.nonNull(author)) ? new Author(authorId, author) : null,
                    (Objects.nonNull(genreId) && Objects.nonNull(genre)) ? new Genre(genreId, genre) : null);

        }
    }
}

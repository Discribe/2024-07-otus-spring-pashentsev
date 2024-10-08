package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcGenreRepository implements GenreRepository {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public JdbcGenreRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public List<Genre> findAll() {
        return namedParameterJdbcOperations.query("select id, name from genres", new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        return namedParameterJdbcOperations.query("select id, name from genres where id=:id",
                param, new GenreRowMapper()).stream().findFirst();
    }

    private static class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            return new Genre(rs.getLong("id"), rs.getString("name"));
        }
    }
}

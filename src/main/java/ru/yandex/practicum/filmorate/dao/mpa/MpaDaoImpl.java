package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query("SELECT * FROM rating", MpaDaoImpl::makeMpa);
    }

    @Override
    public Optional<Mpa> getById(long id) {
        String sql = "SELECT * FROM rating WHERE id = ?";
        List<Mpa> mpaList = jdbcTemplate.query(sql, MpaDaoImpl::makeMpa, id);
        if(!mpaList.isEmpty()) {
            Mpa mpa = mpaList.stream().findFirst().get();
            log.info("Найден MPA: {} {}", mpa.getId(), mpa.getName());
            return Optional.of(mpa);
        } else {
            log.info("MPA с идентификатором {} не найден.", id);
            throw new NotFoundException("Такой жанр не найден");
        }
    }

    static Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(
                rs.getInt("id"),
                rs.getString("name"));
    }
}
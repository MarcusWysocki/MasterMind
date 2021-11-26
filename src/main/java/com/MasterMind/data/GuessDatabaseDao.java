package com.MasterMind.data;

import com.MasterMind.Models.Guess;
import com.MasterMind.Models.Game;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GuessDatabaseDao implements GuessDao {

    static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GuessDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Guess add(Guess guess) {
        final String sql = "INSERT INTO guesses(GameId, Guess, Result, TimeOfGuess) VALUES(?,?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) ->{

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            LocalDateTime dateTime = LocalDateTime.now();
            String formattedTime = dateTime.format(formatter);
            guess.setTime(formattedTime);


            int[] answer = guess.getGuess();
            int ans = answer[0] * 1000 + answer[1] * 100 + answer[2] * 10 + answer[3];



            statement.setInt(1, guess.getGameId());
            statement.setInt(2, ans);
            statement.setString(3, guess.getResult());
            statement.setString(4, guess.getTime());

            return statement;

        }, keyHolder);

        guess.setId(keyHolder.getKey().intValue());

        return guess;
    }

    @Override
    public List<Guess> getAll() {
        final String sql = "SELECT GuessId, GameId, Guess, Result, TimeOfGuess FROM guesses;";
        return jdbcTemplate.query(sql, new GuessDatabaseDao.GuessMapper());
    }

    @Override
    public List<Guess> findByGameId(int id) {

        final String sql = "SELECT GuessId, GameId, Guess, Result, TimeOfGuess FROM guesses "
                + "WHERE GameId = ?;";
        return jdbcTemplate.query(sql, new GuessMapper(), id);
    }

    @Override
    public boolean update(Guess guess) {

        final String sql = "UPDATE guesses SET "
                + "GameId = ?, "
                + "Guess = ?, "
                + "Result = ?, "
                + "TimeOfGuess = ? "
                + "WHERE GuessId = ?;";

        int[] answer = guess.getGuess();
        int ans = answer[0] * 1000 + answer[1] * 100 + answer[2] * 10 + answer[3];

        return jdbcTemplate.update(sql,
                guess.getGameId(),
                ans,
                guess.getResult(),
                guess.getTime(),
                guess.getId()) > 0;

    }

    @Override
    public boolean deleteById(int id) {
        final String sql = "DELETE FROM guesses WHERE  id = ?;";
        return jdbcTemplate.update(sql, id) > 0;
    }

    private static final class GuessMapper implements RowMapper<Guess> {

        @Override
        public Guess mapRow(ResultSet rs, int index) throws SQLException {
            Guess guess = new Guess();
            guess.setId(rs.getInt("GuessId"));
            guess.setGameId(rs.getInt("GameId"));
            guess.setTime(rs.getString("TimeOfGuess"));
            guess.setResult(rs.getString("Result"));

            int[] ans = {0,0,0,0};
            int rawAns = rs.getInt("Guess");
            for (int i = 0; i < 4; i++) {
                ans[3-i] = rawAns%10;
                rawAns = (rawAns - (rawAns%10))/10;
            }
            guess.setGuess(ans);
            return guess;
        }
    }
}

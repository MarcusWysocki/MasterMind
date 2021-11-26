package com.MasterMind.data;

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
public class GameDatabaseDao implements GameDao {

    static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Game add(Game game) {
        final String sql = "INSERT INTO games(TimeStarted, Completed, NoGuesses, Answer) VALUES(?,?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) ->{

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            LocalDateTime dateTime = LocalDateTime.now();
            String formattedTime = dateTime.format(formatter);
            game.setTimeStarted(formattedTime);


            int[] answer = generateAns();
            game.setAnswer(answer);
            int ans = answer[0] * 1000 + answer[1] * 100 + answer[2] * 10 + answer[3];



            statement.setString(1, game.getTimeStarted());
            statement.setBoolean(2, false);
            statement.setInt(3, 0);
            statement.setInt(4, ans);

            return statement;

        }, keyHolder);

        game.setId(keyHolder.getKey().intValue());

        return game;
    }

    @Override
    public List<Game> getAll() {
        final String sql = "SELECT GameId, TimeStarted, Completed, NoGuesses, Answer FROM games;";
        return jdbcTemplate.query(sql, new GameMapper());
    }

    @Override
    public Game findById(int id) {

        final String sql = "SELECT GameId, TimeStarted, Completed, NoGuesses, Answer "
                + "FROM games WHERE GameId = ?;";

        return jdbcTemplate.queryForObject(sql, new GameMapper(), id);
    }

    @Override
    public boolean update(Game game) {

        final String sql = "UPDATE games SET "
                + "TimeStarted = ?, "
                + "Completed = ?, "
                + "NoGuesses = ?, "
                + "Answer = ? "
                + "WHERE GameId = ?;";

        int[] answer = game.getAnswer();
        int ans = answer[0] * 1000 + answer[1] * 100 + answer[2] * 10 + answer[3];

        return jdbcTemplate.update(sql,
                game.getTimeStarted(),
                game.isCompleted(),
                game.getNoGuesses(),
                ans,
                game.getId()) > 0;
    }

    @Override
    public boolean deleteById(int id) {
        final String sql = "DELETE FROM games WHERE  id = ?;";
        return jdbcTemplate.update(sql, id) > 0;
    }

    @Override
    public int[] generateAns() {
        int[] used = {0,0,0,0,0,0,0,0,0,0};
        int[] ans = {0,0,0,0};

        for (int i = 0; i < 4; i++) {
            ans[i] = randomNum(used);
            used[ans[i]] = 1;
        }

        return ans;
    }

    @Override
    public int randomNum(int[] used) {
        Random rand = new Random();
        int num = rand.nextInt(10);

        if(used[num] == 1) {
            return randomNum(used);
        } else {
            return num;
        }
    }

    private static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setId(rs.getInt("GameId"));
            game.setTimeStarted(rs.getString("TimeStarted"));
            game.setCompleted(rs.getBoolean("Completed"));
            game.setNoGuesses(rs.getInt("NoGuesses"));

            int[] ans = {0,0,0,0};
            int rawAns = rs.getInt("Answer");
            for (int i = 0; i < 4; i++) {
                ans[3-i] = rawAns%10;
                rawAns = (rawAns - (rawAns%10))/10;
            }
            game.setAnswer(ans);
            return game;
        }
    }
}

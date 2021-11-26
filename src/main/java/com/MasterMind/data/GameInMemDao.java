package com.MasterMind.data;

import com.MasterMind.Models.Game;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Random;
import org.springframework.stereotype.Repository;


public class GameInMemDao implements GameDao {


    static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    private static final List<Game> games = new ArrayList<>();

    @Override
    public Game add(Game game) {

        int nextId = games.stream()
                .mapToInt(i -> i.getId())
                .max()
                .orElse(0) + 1;

        game.setId(nextId);

        LocalDateTime dateTime = LocalDateTime.now();
        String formattedTime = dateTime.format(formatter);
        game.setTimeStarted(formattedTime);

        int[] answer = generateAns();
        game.setAnswer(answer);

        games.add(game);
        return game;

    }

    @Override
    public List<Game> getAll() {
        return new ArrayList<>(games);
    }

    @Override
    public Game findById(int id) {
        return games.stream()
                .filter(i->i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean update(Game game) {

        int index = 0;
        while (index < games.size()
            && games.get(index).getId() != game.getId()) {
            index++;
        }

        if (index < games.size()) {
            games.set(index, game);
        }

        return index < games.size();
    }

    @Override
    public boolean deleteById(int id) {
        return games.removeIf(i -> i.getId() == id);
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

}

package com.MasterMind.data;

import com.MasterMind.Models.Game;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class GameInMemDao implements GameDao {

    private static final List<Game> games = new ArrayList<>();

    @Override
    public Game add(Game game) {

        int nextId = games.stream()
                .mapToInt(i -> i.getId())
                .max()
                .orElse(0) + 1;

        game.setId(nextId);
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
}

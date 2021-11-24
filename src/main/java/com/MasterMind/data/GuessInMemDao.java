package com.MasterMind.data;

import com.MasterMind.Models.Guess;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class GuessInMemDao implements GuessDao{

    private static final List<Guess> guesses = new ArrayList<>();

    @Override
    public Guess add(Guess guess) {
        int nextId = guesses.stream()
                .mapToInt(i -> i.getId())
                .max()
                .orElse(0) + 1;

        guess.setId(nextId);
        guesses.add(guess);
        return guess;
    }

    @Override
    public List<Guess> getAll() {
        return new ArrayList<>(guesses);
    }

    @Override
    public Guess findById(int id) {
        return guesses.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean update(Guess guess) {
        int index = 0;
        while(index < guesses.size()
            && guesses.get(index).getId() != guess.getId()) {
            index++;
        }

        if (index < guesses.size()) {
            guesses.set(index, guess);
        }

        return index < guesses.size();
    }

    @Override
    public boolean deleteById(int id) {
        return guesses.removeIf(i -> i.getId() == id);
    }
}

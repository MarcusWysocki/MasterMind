package com.MasterMind.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.MasterMind.Models.Guess;
import com.MasterMind.Models.Game;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class GuessInMemDao implements GuessDao{

    static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    private static final List<Guess> guesses = new ArrayList<>();

    @Override
    public Guess add(Guess guess) {
        int nextId = guesses.stream()
                .mapToInt(i -> i.getId())
                .max()
                .orElse(0) + 1;

        guess.setId(nextId);

        LocalDateTime dateTime = LocalDateTime.now();
        String formattedTime = dateTime.format(formatter);
        guess.setTime(formattedTime);
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
    public List<Guess> findByGameId(int id) {
        List<Guess> results = new ArrayList<>();

        for (Guess guess : guesses) {
            if (guess.getGameId() == id) {
                results.add(guess);
            }
        }
        return results;
        /**return guesses.stream()
                .filter(i -> i.getGameId() == id)
                .findFirst()
                .orElse(null);
         */
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

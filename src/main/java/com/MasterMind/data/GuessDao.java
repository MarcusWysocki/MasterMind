package com.MasterMind.data;

import com.MasterMind.Models.Guess;
import com.MasterMind.Models.Game;
import java.util.List;

public interface GuessDao {

    Guess add(Guess guess);

    List<Guess> getAll();

    Guess findById(int id);

    List<Guess> findByGameId(int id);

    boolean update(Guess guess);

    boolean deleteById(int id);
}

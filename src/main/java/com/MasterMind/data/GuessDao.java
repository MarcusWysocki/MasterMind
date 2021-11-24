package com.MasterMind.data;

import com.MasterMind.Models.Guess;
import java.util.List;

public interface GuessDao {

    Guess add(Guess guess);

    List<Guess> getAll();

    Guess findById(int id);

    boolean update(Guess guess);

    boolean deleteById(int id);
}

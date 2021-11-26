package com.MasterMind.data;

import com.MasterMind.Models.Game;
import java.util.List;

public interface GameDao {

    Game add(Game game);

    List<Game> getAll();

    Game findById(int id);

    //True if item exists and is updated
    boolean update(Game game);

    //True if item exists and is deleted
    boolean deleteById(int id);

    int[] generateAns();

    int randomNum(int[] used);
}

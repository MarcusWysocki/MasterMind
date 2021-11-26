package com.MasterMind.Controllers;

import com.MasterMind.data.GameDao;
import com.MasterMind.Models.Game;
import com.MasterMind.Models.Guess;
import com.MasterMind.data.GuessDao;
import java.util.List;
import java.lang.Math;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GameController {

    private final GameDao gameDao;
    private final GuessDao guessDao;

    /*public GameController(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public GameController(GuessDao guessDao) {
        this.guessDao = guessDao;
    }*/

    public GameController(GameDao gameDao, GuessDao guessDao) {
        this.gameDao = gameDao;
        this.guessDao = guessDao;
    }

    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public Game create(@RequestBody Game game) {
        return gameDao.add(game);
    }

    @PostMapping("/guess")
    @ResponseStatus(HttpStatus.CREATED)
    public Guess create(@RequestBody Guess guess) {
        Game toUpdate = gameDao.findById(guess.getGameId());
        toUpdate.incNoGuesses();

        int[] acc = calcAcc(toUpdate.getAnswer(), guess.getGuess());
        String accu = "e:" + acc[0] + ":p:" + acc[1];
        if (acc[0] == 4) {
            toUpdate.setCompleted(true);
        }
        guess.setResult(accu);
        gameDao.update(toUpdate);
        return guessDao.add(guess);
    }

    @GetMapping
    public List<Game> all() {
        return gameDao.getAll();
    }

    @GetMapping("/game/{id}")
    public ResponseEntity<Game> findById(@PathVariable int id) {
        Game result = gameDao.findById(id);
        if (result == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all-guesses")
    public List<Guess> allGuesses() {
        return guessDao.getAll();
    }

    /**@GetMapping("/rounds/{gameId}")
    public ResponseEntity<List<Guess>> findByGameId(@PathVariable int id) {
        List<Guess> results = guessDao.findByGameId(id);
        if (results == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Guess>>(results, HttpStatus.OK);
    }*/

    @GetMapping("/rounds/{id}")
    public List<Guess> findByGameId(@PathVariable int id) {
        return guessDao.findByGameId(id);
    }


    private int[] calcAcc(int[] game, int[] guess) {
        int[] acc = {0,0};
        int[] holder = {0,0,0,0,0,0,0,0,0,0};

        for (int i = 0; i < game.length; i++) {
            holder[game[i]] = 1;
        }

        for (int i = 0; i < game.length; i++) {
            if (guess[i] == game[i]) {
                acc[0]++;
                holder[guess[i]] = 0;
            }
        }

        for (int i = 0; i < game.length; i++) {

            if (holder[guess[i]] == 1) {
                acc[1]++;
                holder[guess[i]] = 0;

            }
        }

        String accu = "e:" + acc[0] + ":p:" + acc[1];
        return acc;
    }

}



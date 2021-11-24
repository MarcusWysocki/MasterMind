package com.MasterMind.Controllers;

import com.MasterMind.data.GameDao;
import com.MasterMind.Models.Game;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameDao dao;

    public GameController(GameDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public List<Game> all() {
        return dao.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Game create(@RequestBody Game game) {
        return dao.add(game);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> findById(@PathVariable int id) {
        Game result = dao.findById(id);
        if (result == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

}



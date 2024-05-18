package controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import entity.Joke;
import repository.JokeRepository;
import service.JokeService;

@RestController
@RequestMapping({"/jokes"})
public class JokeController {
    private final JokeService jokeService;
    private final JokeRepository jokeRepository;

    @Autowired
    public JokeController(JokeService jokeService, JokeRepository jokeRepository) {
        this.jokeService = jokeService;
        this.jokeRepository = jokeRepository;
    }

    @GetMapping
    public ResponseEntity<List<Joke>> getAllJokes(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<Joke> jokes = (List)this.jokeService.findAllJokes(PageRequest.of(page, size)).stream().collect(Collectors.toList());
        return ResponseEntity.ok(jokes);
    }

    @PostMapping
    public ResponseEntity<Joke> createJoke(@RequestBody Joke joke) {
        Joke newJoke = this.jokeService.saveJoke(joke);
        return ResponseEntity.ok(newJoke);
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<Joke> updateJoke(@PathVariable Long id, @RequestBody Joke joke) {
        Joke updatedJoke = this.jokeService.updateJoke(id, joke);
        return ResponseEntity.ok(updatedJoke);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<?> deleteJoke(@PathVariable Long id) {
        this.jokeService.deleteJoke(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping({"/top5"})
    public ResponseEntity<List<Joke>> getTop5Jokes() {
        List<Joke> jokes = this.jokeService.findTop5Jokes(PageRequest.of(0, 5));
        jokes.forEach((j) -> {
            j.setCalls((List)null);
        });
        return ResponseEntity.ok(jokes);
    }

    @GetMapping({"/random"})
    public ResponseEntity<Joke> getRandomJoke() {
        return ResponseEntity.ok(this.jokeService.getRandomJoke());
    }
}
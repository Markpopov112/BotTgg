package service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import entity.Joke;
import entity.JokeCall;
import repository.JokeRepository;

@Service
public class JokeService {
    private final JokeRepository jokeRepository;

    @Autowired
    public JokeService(JokeRepository jokeRepository) {
        this.jokeRepository = jokeRepository;
    }

    public Page<Joke> findAllJokes(Pageable pageable) {
        return this.jokeRepository.findAll(pageable);
    }

    public List<Joke> findTop5Jokes(PageRequest pageable) {
        return this.jokeRepository.findTopByOrderByCallsDesc(pageable);
    }

    public Joke saveJoke(Joke joke) {
        joke.setCreatedAt(LocalDateTime.now());
        return (Joke)this.jokeRepository.save(joke);
    }

    public Joke getRandomJoke() {
        return this.jokeRepository.findRandomJoke();
    }

    @Transactional
    public Joke findRandomJoke(Long userId) {
        Joke joke = this.jokeRepository.findRandomJoke();
        JokeCall jokeCall = new JokeCall();
        jokeCall.setJoke(joke);
        jokeCall.setCallTime(LocalDateTime.now());
        jokeCall.setUserId(userId);
        joke.getCalls().add(jokeCall);
        this.jokeRepository.save(joke);
        return joke;
    }

    public Joke updateJoke(Long id, Joke joke) {
        return (Joke)this.jokeRepository.findById(id).map((existingJoke) -> {
            existingJoke.setText(joke.getText());
            existingJoke.setUpdatedAt(LocalDateTime.now());
            return (Joke)this.jokeRepository.save(existingJoke);
        }).orElseThrow(() -> {
            return new IllegalArgumentException("Joke not found with id " + id);
        });
    }

    public void deleteJoke(Long id) {
        this.jokeRepository.deleteById(id);
    }
}
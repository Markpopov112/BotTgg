package repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import entity.Joke;

@Repository
public interface JokeRepository extends JpaRepository<Joke, Long> {
    @Query(
            value = "SELECT * FROM jokes ORDER BY RANDOM() LIMIT 1",
            nativeQuery = true
    )
    Joke findRandomJoke();

    @Query("SELECT j FROM Joke j ORDER BY SIZE(j.calls) DESC")
    List<Joke> findTopByOrderByCallsDesc(Pageable pageable);
}

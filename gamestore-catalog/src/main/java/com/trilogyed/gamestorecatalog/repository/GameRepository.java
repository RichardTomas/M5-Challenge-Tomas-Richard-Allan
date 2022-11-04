package com.trilogyed.gamestorecatalog.repository;

import com.trilogyed.gamestorecatalog.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
//Code was refactored from gamestore startercode provided for challenge-5
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByEsrbRating(String esrbRating);
    List<Game> findAllByStudio(String studio);
    List<Game> findAllByTitle(String title);
}
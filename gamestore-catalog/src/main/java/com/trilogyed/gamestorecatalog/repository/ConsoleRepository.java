package com.trilogyed.gamestorecatalog.repository;

import com.trilogyed.gamestorecatalog.model.Console;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
//Code was refactored from gamestore startercode provided for challenge-5
@Repository
public interface ConsoleRepository extends JpaRepository<Console, Long> {
    List<Console> findAllByManufacturer(String manufacturer);
}

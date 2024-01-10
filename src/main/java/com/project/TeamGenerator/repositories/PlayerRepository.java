package com.project.TeamGenerator.repositories;

import com.project.TeamGenerator.models.Player;
import com.project.TeamGenerator.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Optional<Player> getByFirstNameAndLastNameAndUser(String firstName, String lastName, User user);

    void deleteById(int id);
}

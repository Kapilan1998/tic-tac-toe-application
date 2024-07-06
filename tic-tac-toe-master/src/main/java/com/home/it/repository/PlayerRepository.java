package com.home.it.repository;

import com.home.it.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends JpaRepository<Player,Long> {
    @Query("SELECT p FROM Player p WHERE p.firstPlayerName =:winnerName  OR p.secondPlayerName =:winnerName")
    Player findWinnerId(@Param("winnerName") String winnerName);
}

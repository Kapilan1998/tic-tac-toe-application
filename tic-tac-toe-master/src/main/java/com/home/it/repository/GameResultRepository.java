package com.home.it.repository;

import com.home.it.entity.GameResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameResultRepository extends JpaRepository<GameResult,Long> {
    @Query(value = "SELECT gr FROM GameResult gr ORDER BY gr.id DESC")
    List<GameResult> findLastTenGameResults(Pageable pageable);
}

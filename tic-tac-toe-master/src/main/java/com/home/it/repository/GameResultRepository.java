package com.home.it.repository;

import com.home.it.entity.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameResultRepository extends JpaRepository<GameResult,Long> {
}

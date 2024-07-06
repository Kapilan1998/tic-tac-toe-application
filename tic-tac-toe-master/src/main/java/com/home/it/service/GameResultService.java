package com.home.it.service;

import com.home.it.entity.GameResult;
import com.home.it.repository.GameResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameResultService {
    private final GameResultRepository gameResultRepository;
    public GameResult saveGameResult(GameResult gameResult) {
        return gameResultRepository.save(gameResult);
    }

    public List<GameResult> showLastTenResults() {
        return gameResultRepository.findLastTenGameResults(PageRequest.of(0, 10));
    }
}

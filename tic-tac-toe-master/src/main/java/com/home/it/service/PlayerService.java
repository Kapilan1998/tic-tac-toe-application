package com.home.it.service;

import com.home.it.entity.Player;
import com.home.it.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {
private final PlayerRepository playerRepository;
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }
}

package com.home.it.controller;

import com.home.it.agent.GameResultAgent;
import com.home.it.model.dto.GameResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("api/v1/gameResult")
@RestController
@RequiredArgsConstructor
public class GameResultController {
    private final GameResultAgent gameResultAgent;

    @GetMapping("/showLastTenResult")
    public List<GameResultDto> showLastTenResult(){
        return gameResultAgent.showLastTenResult();
    }

}

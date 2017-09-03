package com.pm.server.manager;

import com.pm.server.datatype.GameState;
import com.pm.server.datatype.Pacdot;
import com.pm.server.registry.GameStateRegistry;
import com.pm.server.registry.PacdotRegistry;
import com.pm.server.registry.PlayerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameStateManagerImpl implements GameStateManager {

    private GameStateRegistry gameStateRegistry;
    private PacdotRegistry pacdotRegistry;
    private PlayerRegistry playerRegistry;

    @Autowired
    public GameStateManagerImpl(
            GameStateRegistry gameStateRegistry,
            PacdotRegistry pacdotRegistry,
            PlayerRegistry playerRegistry) {
        this.gameStateRegistry = gameStateRegistry;
        this.pacdotRegistry = pacdotRegistry;
        this.playerRegistry = playerRegistry;
    }

    @Override
    public Integer getScore() {
        Integer score = 0;

        List<Pacdot> pacdotList = pacdotRegistry.getAllPacdots();
        for(Pacdot pacdot : pacdotList) {
            if(pacdot.getEaten()) {
                if(pacdot.getPowerdot()) {
                    score += 50;
                }
                else {
                    score += 10;
                }
            }
        }

        score += playerRegistry.getCapturedGhosts() * 50;

        return score;
    }

    @Override
    public GameState getCurrentState() {
        return gameStateRegistry.getCurrentState();
    }

}

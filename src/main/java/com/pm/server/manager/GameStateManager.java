package com.pm.server.manager;

import com.pm.server.datatype.GameState;
import org.springframework.stereotype.Service;

@Service
public interface GameStateManager {

    Integer getScore();

    GameState getCurrentState();

}

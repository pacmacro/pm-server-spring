package com.pm.server.manager;

import com.pm.server.PmServerException;
import com.pm.server.datatype.GameState;
import com.pm.server.datatype.Player;
import com.pm.server.registry.GameStateRegistry;
import com.pm.server.registry.PacdotRegistry;
import com.pm.server.registry.PlayerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AdminGameStateManager {

    private PlayerRegistry playerRegistry;

    private PacdotRegistry pacdotRegistry;

    private GameStateRegistry gameStateRegistry;

    @Autowired
    public AdminGameStateManager(
            PlayerRegistry playerRegistry,
            PacdotRegistry pacdotRegistry,
            GameStateRegistry gameStateRegistry) {
        this.playerRegistry = playerRegistry;
        this.pacdotRegistry = pacdotRegistry;
        this.gameStateRegistry = gameStateRegistry;
    }

    public void changeGameState(GameState newState) throws PmServerException {

        try {
            switch(newState) {

                case INITIALIZING:
                    gameStateRegistry.resetGame();
                    playerRegistry.reset();
                    pacdotRegistry.resetPacdots();
                    break;

                case IN_PROGRESS:
                    gameStateRegistry.startGame();
                    playerRegistry.changePlayerStates(
                            Player.State.READY, Player.State.ACTIVE
                    );
                    break;

                case PAUSED:
                    gameStateRegistry.pauseGame();
                    break;

                case FINISHED_PACMAN_WIN:
                    gameStateRegistry.setWinnerPacman();
                    break;

                case FINISHED_GHOSTS_WIN:
                    gameStateRegistry.setWinnerGhosts();
                    playerRegistry
                            .getPlayerByName(Player.Name.Pacman)
                            .setState(Player.State.CAPTURED);
                    break;

            }
        }
        catch(IllegalStateException e) {
            throw new PmServerException(HttpStatus.CONFLICT, e.getMessage());
        }

    }

}

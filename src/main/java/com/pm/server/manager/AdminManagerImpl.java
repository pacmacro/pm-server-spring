package com.pm.server.manager;

import com.pm.server.PmServerException;
import com.pm.server.datatype.Player;
import com.pm.server.registry.PacdotRegistry;
import com.pm.server.registry.PlayerRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AdminManagerImpl implements AdminManager {

    private PacdotRegistry pacdotRegistry;

    private PlayerRegistry playerRegistry;

    private final static Logger log =
            LogManager.getLogger(AdminManagerImpl.class.getName());

    @Autowired
    public AdminManagerImpl(
            PacdotRegistry pacdotRegistry,
            PlayerRegistry playerRegistry) {
        this.pacdotRegistry = pacdotRegistry;
        this.playerRegistry = playerRegistry;
    }

    @Override
    public void resetPacdots() {
        pacdotRegistry.resetPacdots();
    }

    @Override
    public void setPlayerState(Player.Name name, Player.State state) throws PmServerException {

        Player.State currentState = playerRegistry.getPlayerState(name);

        // Illegal state changes
        if(currentState == Player.State.UNINITIALIZED &&
                currentState != state) {
            String errorMessage =
                    "This operation cannot change the state of an unselected/" +
                            "uninitialized player; use POST /player/{playerName} " +
                            "instead.";
            log.warn(errorMessage);
            throw new PmServerException(HttpStatus.CONFLICT, errorMessage);
        }
        else if(state == Player.State.UNINITIALIZED &&
                state != currentState) {
            String errorMessage =
                    "This operation cannot change the state of a selected/" +
                            "initialized player to uninitialized; use " +
                            "DELETE /player/{playerName} instead.";
            log.warn(errorMessage);
            throw new PmServerException(HttpStatus.CONFLICT, errorMessage);
        }

        // Illegal player states
        if(name != Player.Name.Pacman &&
                state == Player.State.POWERUP) {
            String errorMessage = "The POWERUP state is not valid for a Ghost.";
            log.warn(errorMessage);
            throw new PmServerException(HttpStatus.CONFLICT, errorMessage);
        }

        log.info(
                "Changing Player {} from state {} to {}",
                name, currentState, state
        );
        playerRegistry.setPlayerStateByName(name, state);
    }

}

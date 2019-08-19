package com.pm.server.manager;

import com.pm.server.PmServerException;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.GameState;
import com.pm.server.datatype.Player;
import com.pm.server.registry.GameStateRegistry;
import com.pm.server.registry.PlayerRegistry;
import com.pm.server.response.PlayerDetailsResponse;
import com.pm.server.response.PlayerNameAndLocationResponse;
import com.pm.server.response.PlayerNameAndPlayerStateResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.pm.server.datatype.Player.State.UNINITIALIZED;

@Service
public class PlayerManagerImpl implements PlayerManager {

    private PlayerRegistry playerRegistry;

    private GameStateRegistry gameStateRegistry;

    @Autowired
    public PlayerManagerImpl(PlayerRegistry playerRegistry, GameStateRegistry gameStateRegistry) {
        this.playerRegistry = playerRegistry;
        this.gameStateRegistry = gameStateRegistry;
    }

    private final static Logger log =
            LogManager.getLogger(PlayerManager.class.getName());

    @Override
    public void selectPlayer(Player.Name name, Coordinate location) throws PmServerException {
        if(playerRegistry.getPlayerState(name) != UNINITIALIZED) {
            String errorMessage =
                    "Player "+
                            name +
                            " has already been selected.";
            log.warn(errorMessage);
            throw new PmServerException(HttpStatus.CONFLICT, errorMessage);
        }

        playerRegistry.setPlayerLocationByName(name, location);

        if (gameStateRegistry.getCurrentState() != GameState.IN_PROGRESS) {
            playerRegistry.setPlayerStateByName(name, Player.State.READY);
        }
        else {
            playerRegistry.setPlayerStateByName(name, Player.State.ACTIVE);
        }
    }

    @Override
    public void setPlayerState(Player.Name name, Player.State state)
            throws PmServerException {

        // Pre-checks
        if (state == UNINITIALIZED) {
            if (playerRegistry.getPlayerState(name) == UNINITIALIZED) {
                String errorMessage =
                        "Player " +
                                name +
                                " has not yet been selected.";
                log.warn(errorMessage);
                throw new PmServerException(HttpStatus.BAD_REQUEST, errorMessage);
            }
        }

        // State modification
        playerRegistry.setPlayerStateByName(name, state);

        // Post checks
        if (state == UNINITIALIZED) {
            log.info("Deselected Player {}", name);
            log.debug("Setting Player {} to default location", name);
            playerRegistry.resetLocationOf(name);
        }
    }

    @Override
    public void setPlayerLocation(Player.Name name, Coordinate location)
            throws PmServerException {

        // Pre-checks
        if (playerRegistry.getPlayerState(name) == Player.State.UNINITIALIZED) {
            String errorMessage =
                    "Player " +
                            name +
                            " has not been selected yet, so a location cannot be set.";
            log.warn(errorMessage);
            throw new PmServerException(HttpStatus.CONFLICT, errorMessage);
        }

        playerRegistry.setPlayerLocationByName(name, location);
    }

    @Override
    public Coordinate getPlayerLocation(Player.Name name) {
        return playerRegistry.getPlayerLocation(name);
    }

    @Override
    public List<PlayerNameAndLocationResponse> getAllPlayerLocations() {
        List<PlayerNameAndLocationResponse> response =
                new ArrayList<>();

        for(Player.Name name : Player.Name.values()) {
            log.trace("Processing Player {}", name);
            response.add(new PlayerNameAndLocationResponse(
                    name, playerRegistry.getPlayerLocation(name)
            ));
        }

        return response;
    }

    @Override
    public Player.State getPlayerState(Player.Name name) {
        return playerRegistry.getPlayerState(name);
    }

    @Override
    public List<PlayerNameAndPlayerStateResponse> getAllPlayerStates() {
        List<PlayerNameAndPlayerStateResponse> response =
                new ArrayList<>();

        for(Player.Name name : Player.Name.values()) {
            log.trace("Processing Player {}", name);
            response.add(new PlayerNameAndPlayerStateResponse(
                    name, playerRegistry.getPlayerState(name)
            ));
        }

        return response;
    }

    @Override
    public List<PlayerDetailsResponse> getAllPlayerDetails() {
        List<PlayerDetailsResponse> response =
                new ArrayList<>();

        for(Player.Name name : Player.Name.values()) {
            log.trace("Processing Player {}", name);
            response.add(new PlayerDetailsResponse(
                    name,
                    playerRegistry.getPlayerState(name),
                    playerRegistry.getPlayerLocation(name)
            ));
        }

        return response;
    }

}

package com.pm.server.manager;

import com.pm.server.PmServerException;
import com.pm.server.datatype.GameState;
import com.pm.server.datatype.Player;
import com.pm.server.registry.GameStateRegistry;
import com.pm.server.registry.PlayerRegistry;
import com.pm.server.registry.TagRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.pm.server.datatype.Player.Name.Pacman;

@Service
public class TagManagerImpl implements TagManager {

    private GameStateRegistry gameStateRegistry;

    private PlayerRegistry playerRegistry;

    private TagRegistry tagRegistry;

    @Autowired
    public TagManagerImpl(
            GameStateRegistry gameStateRegistry,
            PlayerRegistry playerRegistry,
            TagRegistry tagRegistry) {
        this.gameStateRegistry = gameStateRegistry;
        this.playerRegistry = playerRegistry;
        this.tagRegistry = tagRegistry;
    }

    public void registerTag(
            Player.Name reporter,
            Player.Name source,
            Player.Name destination
    ) throws PmServerException {

        Boolean reporterIsTagger;
        Player.Name otherPlayer;
        if(source == null) {
            if(destination == null) {
                throw new PmServerException(
                        HttpStatus.BAD_REQUEST,
                        "Either source or destination must be sent."
                );
            }
            else {
                reporterIsTagger = true;
                otherPlayer = destination;
            }
        }
        else if(destination != null) {
            throw new PmServerException(
                    HttpStatus.BAD_REQUEST,
                    "Only one of source or destination can be sent."
            );
        }
        else {
            reporterIsTagger = false;
            otherPlayer = source;
        }

        if(!gameStateRegistry.getCurrentState().equals(GameState.IN_PROGRESS)) {
            throw new PmServerException(
                    HttpStatus.CONFLICT,
                    "The game is not currently in progress."
            );
        }

        if(reporter == otherPlayer) {
            throw new PmServerException(
                    HttpStatus.CONFLICT, "The players are the same."
            );
        }

        if(reporter != Pacman &&
                otherPlayer != Pacman) {
            throw new PmServerException(
                    HttpStatus.CONFLICT, "A ghost cannot tag a ghost."
            );
        }

        Player.State reporterState = playerRegistry.getPlayerState(reporter);
        if(!reporterState.equals(Player.State.ACTIVE) &&
                !reporterState.equals(Player.State.POWERUP)) {
            throw new PmServerException(
                    HttpStatus.CONFLICT,
                    "The player reporting the tag is not currently active " +
                    "in the game."
            );
        }

        Player.State otherPlayerState =
                playerRegistry.getPlayerState(otherPlayer);
        if(!otherPlayerState.equals(Player.State.ACTIVE) &&
                !otherPlayerState.equals(Player.State.POWERUP)) {

            if(reporterIsTagger) {
                throw new PmServerException(
                        HttpStatus.CONFLICT,
                        "The tagged player is not currently active " +
                        "in the game."
                );
            }
            else {
                throw new PmServerException(
                        HttpStatus.CONFLICT,
                        "That tagger is not currently active " +
                        "in the game."
                );
            }

        }

        Boolean success;
        if(reporterIsTagger) {
            success = tagRegistry.tagPlayer(reporter, otherPlayer);
            if(success) {
                completeTag(reporter, otherPlayer);
            }
        }
        else {
            success = tagRegistry.receiveTagFromPlayer(reporter, otherPlayer);
            if(success) {
                completeTag(otherPlayer, reporter);
            }
        }

    }

    private void completeTag(Player.Name tagger, Player.Name taggee) {
        playerRegistry.setPlayerStateByName(taggee, Player.State.CAPTURED);

        if(taggee.equals(Pacman)) {
            gameStateRegistry.setWinnerGhosts();
        }
        else if(playerRegistry.allGhostsCaptured()) {
            gameStateRegistry.setWinnerPacman();
        }

    }

}

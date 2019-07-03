package com.pm.server.manager;

import com.pm.server.PmServerException;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Player;
import com.pm.server.response.PlayerDetailsResponse;
import com.pm.server.response.PlayerNameAndLocationResponse;
import com.pm.server.response.PlayerNameAndPlayerStateResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlayerManager {

    void selectPlayer(Player.Name name, Coordinate location)
            throws PmServerException;

    /**
     * Modifies the Player state.
     *
     * If the new Player state is Player.State.UNINITIALIZED:
     *   The Player location is reset to the default.
     *
     * @param name Name of the Player
     * @param state New state of the Player
     */
    void setPlayerState(Player.Name name, Player.State state)
            throws PmServerException;

    /**
     * Modifies the Player location.
     *
     * @param name
     * @param location
     * @throws PmServerException if the Player is deselected (UNINITIALIZED).
     */
    void setPlayerLocation(Player.Name name, Coordinate location)
            throws PmServerException;

    Coordinate getPlayerLocation(Player.Name name);

    List<PlayerNameAndLocationResponse> getAllPlayerLocations();

    Player.State getPlayerState(Player.Name name);

    List<PlayerNameAndPlayerStateResponse> getAllPlayerStates();

    List<PlayerDetailsResponse> getAllPlayerDetails();

}

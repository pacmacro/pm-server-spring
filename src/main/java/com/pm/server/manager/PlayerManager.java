package com.pm.server.manager;

import com.pm.server.PmServerException;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Player;
import org.springframework.stereotype.Service;

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

}

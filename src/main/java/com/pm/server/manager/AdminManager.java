package com.pm.server.manager;

import com.pm.server.PmServerException;
import com.pm.server.datatype.GameState;
import com.pm.server.datatype.Player;
import org.springframework.stereotype.Service;

@Service
public interface AdminManager {

    void changeGameState(GameState newState) throws PmServerException;

    /**
     * Resets the statuses of all pacdots to uneaten.
     *
     */
    void resetPacdots();

    void setPlayerState(Player.Name name, Player.State newState) throws PmServerException;

}

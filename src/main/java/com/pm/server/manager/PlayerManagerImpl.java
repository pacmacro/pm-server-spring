package com.pm.server.manager;

import com.pm.server.PmServerException;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Player;
import com.pm.server.registry.PlayerRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PlayerManagerImpl implements PlayerManager {

    private PlayerRegistry playerRegistry;

    @Autowired
    public PlayerManagerImpl(PlayerRegistry playerRegistry) {
        this.playerRegistry = playerRegistry;
    }

    private final static Logger log =
            LogManager.getLogger(PlayerManager.class.getName());

    @Override
    public void selectPlayer(Player.Name name, Coordinate location) throws PmServerException {
        if(playerRegistry.getPlayerState(name) != Player.State.UNINITIALIZED) {
            String errorMessage =
                    "Player "+
                            name +
                            " has already been selected.";
            log.warn(errorMessage);
            throw new PmServerException(HttpStatus.CONFLICT, errorMessage);
        }

        playerRegistry.setPlayerLocationByName(name, location);
        playerRegistry.setPlayerStateByName(name, Player.State.READY);
    }
}

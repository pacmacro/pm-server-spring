package com.pm.server.manager;

import com.pm.server.PmServerException;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Player;
import org.springframework.stereotype.Service;

@Service
public interface PlayerManager {

    void selectPlayer(Player.Name name, Coordinate location) throws PmServerException;



}

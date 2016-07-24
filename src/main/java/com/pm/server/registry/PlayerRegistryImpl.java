package com.pm.server.registry;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.PlayerName;
import com.pm.server.datatype.PlayerState;
import com.pm.server.player.Player;
import com.pm.server.player.PlayerImpl;
import com.pm.server.repository.PlayerRepository;

@Repository
public class PlayerRegistryImpl implements PlayerRegistry {

	@Autowired
	PlayerRepository playerRepository;

	private final static Logger log =
			LogManager.getLogger(PlayerRegistryImpl.class.getName());

	public PlayerRegistryImpl() throws Exception {
		reset();
	}

	@Override
	public Player getPlayerByName(PlayerName name) {
		return playerRepository.getPlayerByName(name);
	}

	@Override
	public List<Player> getAllPlayers() {
		return playerRepository.getAllPlayers();
	}

	@Override
	public void setPlayerLocationByName(PlayerName name, Coordinate location) {
		playerRepository.setPlayerLocationByName(name, location);
	}

	@Override
	public void setPlayerStateByName(PlayerName name, PlayerState state) {
		playerRepository.setPlayerStateByName(name, state);
	}

	@Override
	public void reset() throws Exception {

		playerRepository.clearPlayers();

		Player player;
		for(PlayerName playerName : PlayerName.values()) {
			player = new PlayerImpl(playerName);
			try {
				playerRepository.addPlayer(player);
			}
			catch(Exception e) {
				log.error(e.getMessage());
				throw e;
			}
		}

	}

}

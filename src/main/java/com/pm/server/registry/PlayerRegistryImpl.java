package com.pm.server.registry;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

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
	private PlayerRepository playerRepository;

	@Autowired
	private PacdotRegistry pacdotRegistry;

	private static Integer activePowerups = 0;

	/**
	 * Units: Milliseconds
	 */
	private static final Integer POWERUP_TIME = 30 * 1000;

	private final static Logger log =
			LogManager.getLogger(PlayerRegistryImpl.class.getName());

	@PostConstruct
	public void init() throws Exception {
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

		if(name == PlayerName.Pacman) {
			Boolean powerDotEaten =
					pacdotRegistry.eatPacdotsNearLocation(location);
			if(powerDotEaten) {
				activatePowerup();
			}
		}

	}

	@Override
	public void setPlayerStateByName(PlayerName name, PlayerState state) {
		playerRepository.setPlayerStateByName(name, state);
	}

	@Override
	public void reset() throws NullPointerException, IllegalArgumentException {

		playerRepository.clearPlayers();

		log.debug("Attempting to recreate players");
		Player player;
		for(PlayerName playerName : PlayerName.values()) {
			player = new PlayerImpl(playerName);
			playerRepository.addPlayer(player);
		}
		log.debug("Recreation of players completed");

	}

	private void activatePowerup() {

		setPlayerStateByName(PlayerName.Pacman, PlayerState.POWERUP);
		activePowerups++;

		new Timer().schedule(new TimerTask() {
			@Override
			public void run()
			{

				activePowerups--;
				// TODO: Add check that gamestate is still active before
				// resetting Pacman's state to ACTIVE
				if(activePowerups == 0) {
					setPlayerStateByName(PlayerName.Pacman, PlayerState.ACTIVE);
				}

			}
		}, POWERUP_TIME);

	}

}

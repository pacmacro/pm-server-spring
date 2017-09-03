package com.pm.server.registry;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.GameState;
import com.pm.server.datatype.Player;
import com.pm.server.repository.PlayerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Repository
public class PlayerRegistryImpl implements PlayerRegistry {

	private PlayerRepository playerRepository;

	private PacdotRegistry pacdotRegistry;

	private GameStateRegistry gameStateRegistry;

	private static Integer activePowerups = 0;

	/**
	 * Units: Milliseconds
	 */
	private static final Integer POWERUP_TIME = 30 * 1000;

	private final static Logger log =
			LogManager.getLogger(PlayerRegistryImpl.class.getName());

	@Autowired
	public PlayerRegistryImpl(
			PlayerRepository playerRepository,
			PacdotRegistry pacdotRegistry,
			GameStateRegistry gameStateRegistry) {
		this.playerRepository = playerRepository;
		this.pacdotRegistry = pacdotRegistry;
		this.gameStateRegistry = gameStateRegistry;
	}

	@PostConstruct
	public void init() throws Exception {
		resetHard();
	}

	@Override
	public Player getPlayerByName(Player.Name name) {
		return playerRepository.getPlayerByName(name);
	}

	@Override
	public List<Player> getAllPlayers() {
		return playerRepository.getAllPlayers();
	}

	@Override
	public boolean allPlayersReady() {
		List<Player> playerList = playerRepository.getAllPlayers();
		for(Player player : playerList) {
			if(player.getState() != Player.State.READY) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void setPlayerLocationByName(Player.Name name, Coordinate location) {
		playerRepository.setPlayerLocationByName(name, location);

		if(name == Player.Name.Pacman &&
		   gameStateRegistry.getCurrentState() == GameState.IN_PROGRESS) {

			Boolean powerDotEaten =
					pacdotRegistry.eatPacdotsNearLocation(location);
			if(powerDotEaten) {
				activatePowerup();
			}

			if(pacdotRegistry.allPacdotsEaten()) {
				gameStateRegistry.setWinnerPacman();
			}

		}

	}

	@Override
	public void setPlayerStateByName(Player.Name name, Player.State state) {
		playerRepository.setPlayerStateByName(name, state);
	}

	@Override
	public void changePlayerStates(Player.State fromState, Player.State toState)
			throws NullPointerException {
		playerRepository.changePlayerStates(fromState, toState);
	}

	@Override
	public Boolean allGhostsCaptured() {

		Boolean atLeastOneCaptured = false;

		for(Player p : playerRepository.getAllPlayers()) {
			if(!p.getName().equals(Player.Name.Pacman)) {
				if(p.getState().equals(Player.State.ACTIVE)) {
					return false;
				}
				else if(p.getState().equals(Player.State.CAPTURED)) {
					atLeastOneCaptured = true;
				}
			}
		}

		return atLeastOneCaptured;
	}

	@Override
	public void reset() {

		List<Player> playerList = playerRepository.getAllPlayers();
		for(Player player : playerList) {
			player.setState(Player.State.UNINITIALIZED);
			player.resetLocation();
		}

	}

	@Override
	public void resetHard() throws NullPointerException, IllegalArgumentException {

		playerRepository.clearPlayers();

		log.debug("Attempting to recreate players");
		Player player;
		for(Player.Name playerName : Player.Name.values()) {
			player = new Player(playerName);
			playerRepository.addPlayer(player);
		}
		log.debug("Recreation of players completed");

	}

	private void activatePowerup() {

		setPlayerStateByName(Player.Name.Pacman, Player.State.POWERUP);
		activePowerups++;

		new Timer().schedule(new TimerTask() {
			@Override
			public void run()
			{

				activePowerups--;
				if(	activePowerups == 0 &&
						(gameStateRegistry.getCurrentState() == GameState.IN_PROGRESS ||
						gameStateRegistry.getCurrentState() == GameState.PAUSED)
						) {
					setPlayerStateByName(Player.Name.Pacman, Player.State.ACTIVE);
				}

			}
		}, POWERUP_TIME);

	}

}

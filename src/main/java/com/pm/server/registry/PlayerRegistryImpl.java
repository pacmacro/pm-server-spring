package com.pm.server.registry;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.EatenDots;
import com.pm.server.datatype.GameState;
import com.pm.server.datatype.Player;
import com.pm.server.repository.PlayerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

@Repository
public class PlayerRegistryImpl implements PlayerRegistry {

	private PlayerRepository playerRepository;

	private PacdotRegistry pacdotRegistry;

	private GameStateRegistry gameStateRegistry;

	private Integer powerupMillis;

	private static Integer activePowerups = 0;

	private final static Logger log =
			LogManager.getLogger(PlayerRegistryImpl.class.getName());

	@Autowired
	public PlayerRegistryImpl(
			PlayerRepository playerRepository,
			PacdotRegistry pacdotRegistry,
			GameStateRegistry gameStateRegistry,
			@Value("${powerup.millis}") Integer powerupMillis) {
		this.playerRepository = playerRepository;
		this.pacdotRegistry = pacdotRegistry;
		this.gameStateRegistry = gameStateRegistry;
		this.powerupMillis = powerupMillis;
	}

	@PostConstruct
	public void init() throws Exception {
		resetHard();
	}

	@Override
	public Coordinate getPlayerLocation(Player.Name name) {
		return Optional.ofNullable(playerRepository.getPlayerByName(name))
				.map(Player::getLocation)
				.orElse(null);
	}

	@Override
	public Player.State getPlayerState(Player.Name name) {
		return Optional.ofNullable(playerRepository.getPlayerByName(name))
				.map(Player::getState)
				.orElse(null);
	}

	@Override
	public void resetLocationOf(Player.Name name) {
		playerRepository.getPlayerByName(name).resetLocation();
	}

	@Override
	public void setPlayerLocationByName(Player.Name name, Coordinate location) {
		playerRepository.setPlayerLocationByName(name, location);

		if(name == Player.Name.Pacman &&
		   gameStateRegistry.getCurrentState() == GameState.IN_PROGRESS) {

			EatenDots eatenDotsReport =
					pacdotRegistry.eatPacdotsNearLocation(location);
			if(eatenDotsReport.getEatenPowerdots() > 0) {
				activatePowerup();
			}
			if(eatenDotsReport.getEatenPacdots() > 0 &&
					eatenDotsReport.getEatenPowerdots() > 0 &&
					pacdotRegistry.allPacdotsEaten()) {
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
		}, powerupMillis);

	}

}

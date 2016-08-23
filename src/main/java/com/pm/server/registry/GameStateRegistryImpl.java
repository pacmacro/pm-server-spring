package com.pm.server.registry;

import com.pm.server.game.GameState;

public class GameStateRegistryImpl implements GameStateRegistry {

	private static GameState state = GameState.INITIALIZING;

	@Override
	public GameState getCurrentState() {
		return state;
	}

	@Override
	public void resetGame() throws IllegalStateException {
		if(state == GameState.INITIALIZING) {
			throw new IllegalStateException(
					"The game state cannot be reset to INITIALIZING when it " +
					"is already in the INITIALIZING state."
			);
		}
		state = GameState.INITIALIZING;

	}

	@Override
	public void startGame() throws IllegalStateException {
		if(state != GameState.INITIALIZING && state != GameState.PAUSED) {
			throw new IllegalStateException(
					"The game state cannot be set to IN_PROGRESS when " +
					"it is not in the INITIALIZING or PAUSED states."
			);
		}
		state = GameState.IN_PROGRESS;

	}

	@Override
	public void pauseGame() throws IllegalStateException {
		if(state != GameState.IN_PROGRESS) {
			throw new IllegalStateException(
					"The game state cannot be set to PAUSED when the game " +
					"is not IN_PROGRESS."
			);
		}
		state = GameState.PAUSED;
	}

	@Override
	public void setWinnerPacman() throws IllegalStateException {
		if(state != GameState.IN_PROGRESS) {
			throw new IllegalStateException(
					"The game state cannot be set to a FINISHED state " +
					"when the game is not IN_PROGRESS."
			);
		}
		state = GameState.FINISHED_PACMAN_WIN;
	}

	@Override
	public void setWinnerGhosts() throws IllegalStateException {
		if(state != GameState.IN_PROGRESS) {
			throw new IllegalStateException(
					"The game state cannot be set to a FINISHED state " +
					"when the game is not IN_PROGRESS."
			);
		}
		state = GameState.FINISHED_GHOSTS_WIN;
	}

}

package com.pm.server.registry;

import com.pm.server.datatype.GameState;

public interface GameStateRegistry {

	/**
	 * @return The current state of the game.
	 */
	GameState getCurrentState();

	/**
	 * Sets the game state to <code>INITIALIZING</code>.
	 * 
	 * <p>
	 *   The current game state before this may be any state except
	 *   <code>INITIALIZING</code>.
	 * </p>
	 * 
	 * <p>
	 *   Side effects:
	 *     <ul>
	 *       <li>
	 *       <li>All pacdots and powerdots are reset to uneaten.</li>
	 *     </ul>
	 * </p>
	 * 
	 */
	void resetGame() throws IllegalStateException;

	/**
	 * Sets the game state to <code>IN_PROGRESS</code>.
	 * 
	 * <p>
	 *   The current game state before this must be <code>INITIALIZING</code>
	 *   or <code>PAUSED</code>.
	 * </p>
	 * 
	 * <p>
	 *   Side effects:
	 *     <ul>
	 *       <li>If the original state was <code>PAUSED</code>, players will
	 *         again be able to eat pacdots and tag other players.</li>
	 *     </ul>
	 * </p>
	 * 
	 */
	void startGame() throws IllegalStateException;

	/**
	 * Sets the game state to <code>PAUSED</code>.
	 * 
	 * <p>
	 *   The current game state before this must be <code>IN_PROGRESS</code>.
	 * </p>
	 * 
	 * <p>
	 *   Side effects:
	 *     <ul>
	 *       <li>No pacdots may be eaten and no players may be tagged when
	 *         the game is <code>PAUSED</code>.</li>
	 *     </ul>
	 * </p>
	 * 
	 */
	void pauseGame() throws IllegalStateException;

	/**
	 * Sets the game state to <code>FINISHED_PACMAN_WIN</code>.
	 * 
	 * <p>
	 *   The current game state before this must be <code>IN_PROGRESS</code>.
	 * </p>
	 * 
	 */
	void setWinnerPacman() throws IllegalStateException;


	/**
	 * Sets the game state to <code>FINISHED_GHOSTS_WIN</code>.
	 * 
	 * <p>
	 *   The current game state before this must be <code>IN_PROGRESS</code>.
	 * </p>
	 * 
	 */
	void setWinnerGhosts() throws IllegalStateException;

}

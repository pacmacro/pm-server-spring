package com.pm.server.manager;

import com.pm.server.PmServerException;
import com.pm.server.datatype.Player;

public interface TagManager {

    /**
     * Registers a tag by one player to another player.
     *
     * <p>
     *     Will expire in 20 seconds if it is not reciprocated by the other
     *     player.
     * </p>
     * <p>
     *     Will end the game if it reciprocates a tag by the opposite player,
     *     and captures the Pacman or the last Ghost.
     * </p>
     * <p>
     *     Either source or destination must be given, but not both.
     * </p>
     *
     * @param reporter Player who is registering a tag. May be tagger or taggee.
     * @param source Other player who is the tagger. If this is non-null,
     *               destination must be null.
     * @param destination Other player who is the taggee. If this is non-null,
     *               source must be null.
     * @throws PmServerException If both source and destination were given, or
     *               neither were given, or the game is not in progress, or
     *               the players are the same, or both players are ghosts, or
     *               a player is not active in the game,
     */
    void registerTag(
            Player.Name reporter,
            Player.Name source,
            Player.Name destination
    ) throws PmServerException;

}

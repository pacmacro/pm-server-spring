package com.pm.server.registry;

import com.pm.server.PmServerException;
import com.pm.server.datatype.Player;

public interface TagRegistry {

    /**
     * Registers a tag by one player to one player.
     *
     * <p>
     *     Has no effect if a player has already won.
     * </p>
     * <p>
     *     The tag will be removed after 15 seconds if not reciprocated.
     * </p>
     *
     * @param tagger Player who is tagging
     * @param taggee Player who is being tagged
     * @return Whether the tag reciprocates a previously registered tag
         from {@link #receiveTagFromPlayer(Player.Name, Player.Name)}
         (triggering a gamestate change)
     * @throws PmServerException if the tagger and taggee are the same
     *
     */
    Boolean tagPlayer(Player.Name tagger, Player.Name taggee) throws PmServerException;

    /**
     * Registers a tag by one player to one player.
     *
     * <p>
     *     Has no effect if a player has already won.
     * </p>
     * <p>
     *     The tag will be removed after 15 seconds if not reciprocated.
     * </p>
     *
     * @param taggee Player who is being tagged
     * @param tagger Player who is tagging
     * @return Whether the tag reciprocates a previously registered tag
         from {@link #tagPlayer(Player.Name, Player.Name)}
         (triggering a gamestate change)
     * @throws PmServerException if the tagger and taggee are the same
     *
     */
    Boolean receiveTagFromPlayer(Player.Name taggee, Player.Name tagger)
            throws PmServerException;

    /**
     * @return The player who has successfully tagged another player, or null
         if the game has not ended yet
     */
    Player.Name getWinner();

    /**
     * Removes all tags and recorded winners.
     */
    void clearTagsAndWinner();

}

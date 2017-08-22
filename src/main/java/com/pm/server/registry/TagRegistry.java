package com.pm.server.registry;

import com.pm.server.PmServerException;
import com.pm.server.datatype.Player;

public interface TagRegistry {

    /**
     * Registers a tag by one player to one player, sent by the tagger.
     *
     * <p>
     *     The tag will be removed after 15 seconds if not reciprocated.
     * </p>
     *
     * @param tagger Player who is tagging (and registering this tag)
     * @param taggee Player who is being tagged
     * @return Whether the tag reciprocates a previously registered tag
         from {@link #receiveTagFromPlayer(Player.Name, Player.Name)}
         (capturing one of the players)
     * @throws PmServerException if the tagger and taggee are the same
     *
     */
    Boolean tagPlayer(Player.Name tagger, Player.Name taggee)
            throws PmServerException;

    /**
     * Registers a tag by one player to one player, sent by the taggee.
     *
     * <p>
     *     The tag will be removed after 20 seconds if not reciprocated.
     * </p>
     *
     * @param taggee Player who is being tagged (and registering this tag)
     * @param tagger Player who is tagging
     * @return Whether the tag reciprocates a previously registered tag
         from {@link #tagPlayer(Player.Name, Player.Name)}
         (capturing one of the players)
     * @throws PmServerException if the tagger and taggee are the same
     *
     */
    Boolean receiveTagFromPlayer(Player.Name taggee, Player.Name tagger)
            throws PmServerException;

    /**
     * Removes all tags.
     */
    void clearTags();

}

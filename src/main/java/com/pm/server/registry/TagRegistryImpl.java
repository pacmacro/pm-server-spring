package com.pm.server.registry;

import com.pm.server.PmServerException;
import com.pm.server.datatype.Player;
import com.pm.server.datatype.PlayerTagRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import java.util.LinkedList;
import java.util.List;

public class TagRegistryImpl implements TagRegistry {

    private Player.Name winner;

    private List<PlayerTagRecord> tagsByTagger;

    private List<PlayerTagRecord> tagsByTaggee;

    private final static Logger log =
            LogManager.getLogger(TagRegistryImpl.class.getName());

    TagRegistryImpl() {
        tagsByTagger = new LinkedList<>();
        tagsByTaggee = new LinkedList<>();
    }

    @Override
    public Boolean tagPlayer(Player.Name tagger, Player.Name taggee)
            throws PmServerException {
        validateTagPlayers(tagger, taggee);
        log.info(
                "Registering tag sent by " + tagger + ": "+
                tagger + " tagged " + taggee + "."
        );

        PlayerTagRecord tag = new PlayerTagRecord(tagger, taggee);
        if(winner == null && !tagsByTagger.contains(tag)) {
            tagsByTagger.add(tag);

            if(tagsByTaggee.contains(tag)) {
                winner = tagger;
                return true;
            }
        }

        return false;
    }

    @Override
    public Boolean receiveTagFromPlayer(Player.Name taggee, Player.Name tagger)
            throws PmServerException {
        validateTagPlayers(tagger, taggee);
        log.info(
                "Registering tag sent by " + taggee + ": "+
                        tagger + " tagged " + taggee + "."
        );

        PlayerTagRecord tag = new PlayerTagRecord(tagger, taggee);
        if(winner == null && !tagsByTaggee.contains(tag)) {
            tagsByTaggee.add(tag);

            if(tagsByTagger.contains(tag)) {
                winner = tagger;
                return true;
            }
        }

        return false;
    }

    private void validateTagPlayers(Player.Name tagger, Player.Name taggee)
            throws PmServerException {
        if(tagger == null) {
            throw new NullPointerException(
                    "Player attempting to tag another player was null."
            );
        }
        else if(taggee == null) {
            throw new NullPointerException(
                    "Player being tagged by another player was null."
            );
        }
        else if(tagger == taggee) {
            throw new PmServerException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "The tagger and taggee were the same player."
            );
        }
    }

    @Override
    public Player.Name getWinner() {
        return winner;
    }

    @Override
    public void clearTagsAndWinner() {
        winner = null;
        tagsByTagger.clear();
        tagsByTaggee.clear();
    }
}

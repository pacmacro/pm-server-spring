package com.pm.server.registry;

import com.pm.server.PmServerException;
import com.pm.server.datatype.Player;
import com.pm.server.datatype.PlayerTagRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Repository
public class TagRegistryImpl implements TagRegistry {

    private List<PlayerTagRecord> tagsByTagger;

    private List<PlayerTagRecord> tagsByTaggee;

    private Timer timer;

    private final static Logger log =
            LogManager.getLogger(TagRegistryImpl.class.getName());

    TagRegistryImpl() {
        tagsByTagger = new LinkedList<>();
        tagsByTaggee = new LinkedList<>();
        timer = new Timer();
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
        if(!tagsByTagger.contains(tag)) {
            tagsByTagger.add(tag);

            if(tagsByTaggee.contains(tag)) {
                removeTagNow(tagsByTagger, tag);
                return true;
            }
            else {
                removeTagAfterTimeout(tagsByTagger, tag);
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
        if(!tagsByTaggee.contains(tag)) {
            tagsByTaggee.add(tag);

            if(tagsByTagger.contains(tag)) {
                removeTagNow(tagsByTaggee, tag);
                return true;
            }
            else {
                removeTagAfterTimeout(tagsByTaggee, tag);
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

    private void removeTagNow(List list, PlayerTagRecord tag) {
        list.remove(tag);
    }

    private void removeTagAfterTimeout(List list, PlayerTagRecord tag) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removeTagNow(list, tag);
            }
        }, 20 * 1000);  // 20 seconds (expressed in milliseconds)
    }

    @Override
    public void clearTags() {
        tagsByTagger.clear();
        tagsByTaggee.clear();
    }
}

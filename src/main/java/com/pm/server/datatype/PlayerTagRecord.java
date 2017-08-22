package com.pm.server.datatype;

public class PlayerTagRecord {

    private Player.Name tagger;

    private Player.Name taggee;

    public PlayerTagRecord(Player.Name tagger, Player.Name taggee) {
        this.tagger = tagger;
        this.taggee = taggee;
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }

        if(other instanceof PlayerTagRecord) {
            PlayerTagRecord var = (PlayerTagRecord) other;
            return this.tagger.equals(var.getTagger()) &&
                    this.taggee.equals(var.getTaggee());
        }

        return false;
    }

    public Player.Name getTagger() {
        return tagger;
    }

    public void setTagger(Player.Name tagger) {
        this.tagger = tagger;
    }

    public Player.Name getTaggee() {
        return taggee;
    }

    public void setTaggee(Player.Name taggee) {
        this.taggee = taggee;
    }
}

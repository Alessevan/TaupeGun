package fr.aless.TaupeGun.game;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerTaupe {

    private static final List<PlayerTaupe> playerTaupeList = new ArrayList<>();

    private Player player;
    private Teams teams;
    private Teams taupe;

    private boolean reveal;
    private boolean claim;

    public PlayerTaupe(Player player) {
        this.player = player;
        PlayerTaupe.playerTaupeList.add(this);
        this.taupe = null;
        this.reveal = false;
        this.claim = false;
    }

    public static PlayerTaupe getPlayerTaupe(final Player player) {
        for (final PlayerTaupe playerTaupes : PlayerTaupe.getPlayerTaupeList()) {
            if (playerTaupes.getPlayer().getName().equalsIgnoreCase(player.getName())) {
                return playerTaupes;
            }
        }
        return null;
    }

    public static List<PlayerTaupe> getPlayerTaupeList() {
        return PlayerTaupe.playerTaupeList;
    }

    public boolean isTaupe() {
        return this.taupe != null;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(final Player player) {
        this.player = player;
    }

    public Teams getTeam() {
        return this.teams;
    }

    public void setTeam(final Teams teams) {
        this.teams = teams;
    }

    public boolean isReveal() {
        return this.reveal;
    }

    public void setReveal(final boolean reveal) {
        this.reveal = reveal;
    }

    public boolean hasClaim() {
        return this.claim;
    }

    public void setClaim(final boolean claim) {
        this.claim = claim;
    }

    public Teams getTaupe() {
        return this.taupe;
    }

    public void setTaupe(final Teams teams) {
        this.taupe = teams;
    }

}

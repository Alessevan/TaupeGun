package fr.SPFF.TaupeGun.game;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerTaupe {

    private static final List<PlayerTaupe> playerTaupeList;

    static {
        playerTaupeList = new ArrayList<>();
    }

    private final Player player;
    private Teams teams;
    private Teams taupe;

    public PlayerTaupe(Player player) {
        this.player = player;
        PlayerTaupe.playerTaupeList.add(this);
        this.taupe = null;
    }

    public Player getPlayer(){
        return this.player;
    }

    public boolean isTaupe() {
        return this.taupe != null;
    }

    public void setTaupe(final Teams teams){
        this.taupe = teams;
    }

    public Teams getTeam(){
        return this.teams;
    }

    public void setTeam(final Teams teams){
        this.teams = teams;
    }

    public static PlayerTaupe getPlayerTaupe(final Player player){
        final Optional<PlayerTaupe> playerTaupe = PlayerTaupe.playerTaupeList.parallelStream().filter(playerTaupe1 -> playerTaupe1.player.equals(player)).findFirst();
        return playerTaupe.orElse(null);
    }

    public static List<PlayerTaupe> getPlayerTaupeList(){
        return PlayerTaupe.playerTaupeList;
    }
}

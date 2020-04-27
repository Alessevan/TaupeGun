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
    private boolean taupe;

    public PlayerTaupe(Player player) {
        this.player = player;
        PlayerTaupe.playerTaupeList.add(this);
        this.taupe = false;
    }

    public Player getPlayer(){
        return this.player;
    }

    public boolean isTaupe() {
        return this.taupe;
    }

    public void setTaupe(final boolean taupe){
        this.taupe = taupe;
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

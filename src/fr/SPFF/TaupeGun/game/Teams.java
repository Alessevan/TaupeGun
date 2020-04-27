package fr.SPFF.TaupeGun.game;

import fr.SPFF.TaupeGun.plugin.TaupeGunPlugin;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Teams {

    private final static List<Teams> teamsList;

    static {
        teamsList = new ArrayList<>();
    }

    private final TaupeGunPlugin main;
    private final TeamsColor color;
    final List<Player> players;

    public Teams(boolean isTaupe){
        this.main = TaupeGunPlugin.getInstance();
        if(isTaupe) color = TeamsColor.TAUPE;
        else color = this.main.getTaupeGunManager().getAvailableColors().remove(new Random().nextInt(this.main.getTaupeGunManager().getAvailableColors().size()));
        this.players = new ArrayList<>();
        Teams.teamsList.add(this);
    }

    public void destroy(){
        this.players.clear();
        Teams.teamsList.remove(this);
    }

    public TeamsColor getColor() {
        return color;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public static Teams getPlayerTeam(final Player player){
        return Teams.teamsList.parallelStream().filter(team -> team.players.contains(player)).findFirst().orElseGet(null);
    }

    public static Teams getPlayerTeam(final PlayerTaupe player){
        return Teams.teamsList.parallelStream().filter(team -> team.players.contains(player.getPlayer())).findFirst().orElseGet(null);
    }
}

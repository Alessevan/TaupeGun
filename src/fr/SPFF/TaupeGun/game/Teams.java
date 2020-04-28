package fr.SPFF.TaupeGun.game;

import fr.SPFF.TaupeGun.plugin.TaupeGunPlugin;
import fr.SPFF.TaupeGun.utils.MiscUtils;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

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

    private final Team team;

    public Teams(boolean isTaupe){
        this.main = TaupeGunPlugin.getInstance();
        if(isTaupe) color = TeamsColor.TAUPE;
        else color = this.main.getTaupeGunManager().getAvailableColors().remove(new Random().nextInt(this.main.getTaupeGunManager().getAvailableColors().size()));
        this.players = new ArrayList<>();
        Teams.teamsList.add(this);
        this.team = this.main.getServer().getScoreboardManager().getMainScoreboard().registerNewTeam(MiscUtils.randomize(10));
        this.team.setColor(TeamsColor.getColor(this.getColor().getValue()));
        if(isTaupe) this.team.setPrefix("[Taupe] ");
        this.team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }

    public void destroy(){
        for(final Player player : this.getPlayers()){
            if(this.team.getEntries().contains(player.getName()))
                this.team.removeEntry(player.getName());
        }
        this.players.clear();
        Teams.teamsList.remove(this);
    }

    public TeamsColor getColor() {
        return color;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(final Player player){
        this.team.addEntry(player.getName());
    }

    public void removePlayer(final Player player){
        this.team.removeEntry(player.getName());
    }

    public void removeAllPlayers(){
        this.getPlayers().clear();
    }

    public void show(final Player player){
        for(final Player players : this.getPlayers()) {
            if(players.equals(player))
                if(!this.team.getEntries().contains(player.getName()))
                    this.team.addEntry(player.getName());
        }
    }

    public void hide(final Player player){
        for(final Player players : this.getPlayers()) {
            if(players.equals(player))
                if(this.team.getEntries().contains(player.getName()))
                    this.team.removeEntry(player.getName());
        }
    }

    public boolean isFull(){
        return (color.equals(TeamsColor.TAUPE) ? this.getPlayers().size() == Teams.teamsList.size() / 2 : this.getPlayers().size() == this.main.getServer().getOnlinePlayers().size() / Teams.teamsList.size());
    }

    public static Teams getPlayerTeam(final Player player){
        return Teams.teamsList.parallelStream().filter(team -> team.players.contains(player)).findFirst().orElseGet(null);
    }

    public static Teams getPlayerTeam(final PlayerTaupe player){
        return Teams.teamsList.parallelStream().filter(team -> team.players.contains(player.getPlayer())).findFirst().orElseGet(null);
    }

    public static List<Teams> getTeams(){
        return Teams.teamsList;
    }
}

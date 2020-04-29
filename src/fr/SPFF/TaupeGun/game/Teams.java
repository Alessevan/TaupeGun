package fr.SPFF.TaupeGun.game;

import com.nametagedit.plugin.NametagEdit;
import fr.SPFF.TaupeGun.plugin.TaupeGunPlugin;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Teams {

    private final static List<Teams> teamsList = new ArrayList<>();


    private final TaupeGunPlugin main;
    private final TeamsColor color;
    private final String name;
    final List<Player> players;

    //private final Team team;

    public Teams(boolean isTaupe) {
        String name1;
        this.main = TaupeGunPlugin.getInstance();
        if (isTaupe) this.color = TeamsColor.TAUPE;
        else
            this.color = this.main.getTaupeGunManager().getAvailableColors().get(new Random().nextInt(this.main.getTaupeGunManager().getAvailableColors().size()));
        this.main.getTaupeGunManager().getAvailableColors().remove(this.color);
        this.players = new ArrayList<>();
        name1 = TeamsName.values()[new Random().nextInt(TeamsName.values().length)].getValue();
        if (!isTaupe) {
            boolean b = true;
            while (b) {
                b = false;
                for (final Teams teams : Teams.getTeams()) {
                    if (!teams.equals(this) && teams.getName().equalsIgnoreCase(name1)) {
                        name1 = TeamsName.values()[new Random().nextInt(TeamsName.values().length)].getValue();
                        b = true;
                        break;
                    }
                }
            }
        } else {
            int i = 0;
            for (final Teams teams : Teams.getTeams()) {
                if (teams.isTaupe()) i++;
            }
            name1 = "Taupes" + i;
        }
        this.name = name1;
        //this.team = this.main.getScoreboard().registerNewTeam(MiscUtils.randomize(10));
        //this.team.setColor(TeamsColor.getColor(this.getColor().getValue()));
        //if(isTaupe) this.team.setPrefix(TeamsColor.getColor(this.getColor().getValue()) + "[Taupe] ");
        //else this.team.setPrefix(TeamsColor.getColor(this.getColor().getValue()) + "");
        Teams.teamsList.add(this);
    }

    public static Optional<Teams> getPlayerTeam(final Player player) {
        return Teams.teamsList.parallelStream().filter(team -> team.players.contains(player)).findFirst();
    }

    public static Optional<Teams> getPlayerTeam(final PlayerTaupe player) {
        return Teams.teamsList.parallelStream().filter(team -> team.players.contains(player.getPlayer())).findFirst();
    }

    public TeamsColor getColor() {
        return color;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void destroy() {
        for (final Player player : this.getPlayers()) {
            // if(this.team.getEntries().contains(player.getName()))
            //this.team.removeEntry(player.getName());
            NametagEdit.getApi().setPrefix(player, "");
        }
        this.players.clear();
        //this.team.unregister();
        Teams.teamsList.remove(this);
    }

    public String getName() {
        return name;
    }

    public void removeAllPlayers() {
        this.getPlayers().clear();
    }

    public void addPlayer(final Player player) {
        this.getPlayers().add(player);
    }

    public void removePlayer(final Player player) {
        //this.team.removeEntry(player.getName());
        NametagEdit.getApi().setPrefix(player, "");
        this.getPlayers().remove(player);
    }

    public void show(final Player player) {
        for (final Player players : this.getPlayers()) {
            if (players.equals(player))
                NametagEdit.getApi().setPrefix(player, TeamsColor.getColor(this.getColor().getValue()) + (this.isTaupe() ? this.getName() + " " : ""));
            //if(!this.team.getEntries().contains(player.getName()))
            //this.team.addEntry(player.getName());
        }
    }

    public void hide(final Player player) {
        for (final Player players : this.getPlayers()) {
            if (players.equals(player))
                NametagEdit.getApi().setPrefix(player, "");
            //if(this.team.getEntries().contains(player.getName()))
            //this.team.removeEntry(player.getName());
        }
    }

    public boolean isTaupe() {
        return this.color.equals(TeamsColor.TAUPE);
    }

    public boolean isFull() {
        return (color.equals(TeamsColor.TAUPE) ? this.getPlayers().size() >= ((double) Teams.teamsList.size()) / 2D : this.getPlayers().size() >= ((double) this.main.getServer().getOnlinePlayers().size()) / ((double) Teams.teamsList.size()));
    }

    public static List<Teams> getTeams() {
        return Teams.teamsList;
    }
}

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
    private String name;
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
            NametagEdit.getApi().setPrefix(player, "");
        }
        this.players.clear();
        Teams.teamsList.remove(this);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void removeAllPlayers() {
        this.getPlayers().clear();
    }

    public void addPlayer(final Player player) {
        this.getPlayers().add(player);
    }

    public void removePlayer(final Player player) {
        NametagEdit.getApi().setPrefix(player, "");
        this.getPlayers().remove(player);
    }

    public void show(final Player player) {
        NametagEdit.getApi().setPrefix(player, TeamsColor.getColor(this.getColor().getValue()) + (this.isTaupe() ? this.getName() + " " : ""));
    }

    public void hide(final Player player) {
        NametagEdit.getApi().setPrefix(player, "");
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

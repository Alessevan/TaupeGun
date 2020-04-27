package fr.SPFF.TaupeGun.utils;

import fr.SPFF.TaupeGun.game.Teams;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MiscUtils {

    public static List<Player> shufflePlayers(final List<Player> list){
        final List<Player> shuffled = new ArrayList<>();
        while(list.size() > 0){
            shuffled.add(list.remove(new Random().nextInt(list.size())));
        }
        return shuffled;
    }

    public static List<Teams> shuffleTeams(final List<Teams> list){
        final List<Teams> shuffled = new ArrayList<>();
        final List<Teams> clone = new ArrayList<>();
        for(final Teams team : list){
            clone.add(team);
        }
        while(clone.size() > 0){
            shuffled.add(clone.remove(new Random().nextInt(clone.size())));
        }
        return shuffled;
    }
}

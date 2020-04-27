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
        while(list.size() > 0){
            shuffled.add(list.remove(new Random().nextInt(list.size())));
        }
        return shuffled;
    }
}

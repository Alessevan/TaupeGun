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

    public static String randomize(final int length){
        String[] dataSet = {
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                "u", "v", "w", "x", "y", "z", "A", "B", "C", "D",
                "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
                "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
                "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7",
                "8", "9"
        };
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < length; i++)
            result.append(dataSet[new Random().nextInt(dataSet.length)]);
        return result.toString();
    }
}

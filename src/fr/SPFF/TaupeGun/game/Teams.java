package fr.SPFF.TaupeGun.game;

import org.bukkit.ChatColor;

public enum Teams {

    WHITE(0),
    DARK_GRAY(1),
    RED(2),
    BLUE(3),
    GOLD(4),
    CYAN(5);

    private final int value;

    Teams(final int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static ChatColor getColor(final int value){
        switch(value){
            case 0:
                return ChatColor.WHITE;
            case 1:
                return ChatColor.DARK_GRAY;
            case 2:
                return ChatColor.RED;
            case 3:
                return ChatColor.BLUE;
            case 4:
                return ChatColor.GOLD;
            case 5:
                return ChatColor.DARK_AQUA;
            default:
                return ChatColor.MAGIC;
        }
    }
}

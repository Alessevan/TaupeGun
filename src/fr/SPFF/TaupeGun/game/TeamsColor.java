package fr.SPFF.TaupeGun.game;

import org.bukkit.ChatColor;

public enum TeamsColor {

    WHITE(0),
    DARK_GRAY(1),
    RED(2),
    BLUE(3),
    GOLD(4),
    CYAN(5),
    AQUA(6),
    LIGHT_PURPLE(7),
    GREEN(8),
    TAUPE(9999);

    private final int value;

    TeamsColor(final int value){
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
            case 6:
                return ChatColor.AQUA;
            case 7:
                return ChatColor.LIGHT_PURPLE;
            case 8:
                return ChatColor.GREEN;
            case 9999:
                return ChatColor.DARK_RED;
            default:
                return ChatColor.MAGIC;
        }
    }

    public static TeamsColor fromValue(final int value){
        switch(value){
            case 0:
                return TeamsColor.WHITE;
            case 1:
                return TeamsColor.DARK_GRAY;
            case 2:
                return TeamsColor.RED;
            case 3:
                return TeamsColor.BLUE;
            case 4:
                return TeamsColor.GOLD;
            case 5:
                return TeamsColor.CYAN;
            case 6:
                return TeamsColor.AQUA;
            case 7:
                return TeamsColor.LIGHT_PURPLE;
            case 8:
                return TeamsColor.GREEN;
            case 9999:
                return TeamsColor.TAUPE;
            default:
                return null;
        }
    }

    public static TeamsColor[] getColors() {
        TeamsColor[] teamsColors = new TeamsColor[TeamsColor.values().length - 1];
        for (int i = 0; i < TeamsColor.values().length - 1; i++) {
            teamsColors[i] = TeamsColor.fromValue(i);
        }
        return teamsColors;
    }
}

package fr.SPFF.TaupeGun.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class MathUtils {

    public static double randomRange(final double n, final double n2) {
        return (Math.random() < 0.5) ? ((1.0 - Math.random()) * (n2 - n) + n) : (Math.random() * (n2 - n) + n);
    }

    public static Integer getDistance(final Location start, final Location stop){
        return (int) Math.floor(Math.sqrt(Math.pow(start.getX() - stop.getX(), 2) + Math.pow(start.getZ() - stop.getZ(), 2)));
    }

    public static Integer getFlatDistance(final Location start, final Location stop){
        start.setY(0);
        stop.setY(0);
        return MathUtils.getDistance(start, stop);
    }

    /**
     * Code par SamaGames
     * <p>
     * https://github.com/SamaGames/SurvivalAPI/blob/master/src/main/java/net/samagames/survivalapi/game/SurvivalGameLoop.java#L330
     **/
    public static String getDirection(final Location l1, final Location l2) {
        Location ploc = l1.clone();
        Location point = l2.clone();

        if(!ploc.getWorld().getEnvironment().equals(point.getWorld().getEnvironment()))
            return "•";

        ploc.setY(0);
        point.setY(0);

        Vector d = ploc.getDirection();
        Vector v = point.subtract(ploc).toVector().normalize();

        double a = Math.toDegrees(Math.atan2(d.getX(), d.getZ()));
        a -= Math.toDegrees(Math.atan2(v.getX(), v.getZ()));
        a = (int) (a + 22.5) % 360;

        if (a < 0)
            a += 360;

        return Character.toString("⬆⬈➡⬊⬇⬋⬅⬉".charAt((int) a / 45));
    }
}

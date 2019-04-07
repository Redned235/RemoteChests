package me.redned.remotechests.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by Redned on 4/6/2019.
 */
public class Utils {

    public static String getLocString(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
    }

    public static String getFancyLocString(Location loc) {
        return loc.getX() + " " + loc.getY() + " " + loc.getZ() + " in world " + loc.getWorld().getName();
    }

    public static Location getLocFromString(String loc) {
        String[] split = loc.split(",");

        World world = Bukkit.getWorld(split[0]);
        if (world == null)
            throw new IllegalArgumentException("World is null");

        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);

        return new Location(world, x, y, z);
    }
}

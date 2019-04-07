package me.redned.remotechests;

import org.bukkit.Location;

import java.util.UUID;

/**
 * Created by Redned on 4/6/2019.
 */
public class RemoteChest {

    private Location loc;
    private String name;
    private UUID owner;
    private boolean restricted;

    public RemoteChest(Location loc, String name, UUID owner) {
        this(loc, name, owner, false);
    }

    public RemoteChest(Location loc, String name, UUID owner, boolean restricted) {
        this.loc = loc;
        this.name = name;
        this.owner = owner;
        this.restricted = restricted;
    }

    public Location getLocation() {
        return loc;
    }

    public void setLocation(Location loc) {
        this.loc = loc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }
}

package me.redned.remotechests;

import me.redned.remotechests.commands.RemoteChestCommand;
import me.redned.remotechests.utils.Utils;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Redned on 4/6/2019.
 */
public class RemoteChests extends JavaPlugin {

    private File chestFile;
    private FileConfiguration chestConfig;

    private Map<String, RemoteChest> remoteChests = new HashMap<String, RemoteChest>();

    @Override
    public void onEnable() {
        this.getDataFolder().mkdirs();

        chestFile = new File(this.getDataFolder(), "chests.yml");
        if (!chestFile.exists()) {
            try {
                chestFile.createNewFile();
                this.saveResource("chests.yml", true);
            } catch (IOException ex) {
                this.getLogger().severe("Could not create chests.yml !!!");
                ex.printStackTrace();
            }
        }

        chestConfig = YamlConfiguration.loadConfiguration(chestFile);
        for (String str : chestConfig.getConfigurationSection("chests").getKeys(false)) {
            Location loc = Utils.getLocFromString(chestConfig.getString("chests." + str + ".location"));
            UUID owner = UUID.fromString(chestConfig.getString("chests." + str + ".owner"));
            // TODO: Give this feature some sort of use
            boolean restricted = chestConfig.getBoolean("chests." + str + ".restricted", false);

            remoteChests.put(str, new RemoteChest(loc, str, owner, restricted));
        }

        getCommand("remotechest").setExecutor(new RemoteChestCommand(this));
    }

    @Override
    public void onDisable() {
        for (String str : remoteChests.keySet()) {
            RemoteChest remoteChest = remoteChests.get(str);
            chestConfig.set("chests." + remoteChest.getName() + ".location", Utils.getLocString(remoteChest.getLocation()));
            chestConfig.set("chests." + remoteChest.getName() + ".owner", remoteChest.getOwner().toString());
            //chestConfig.set("chests." + remoteChest.getName() + ".restricted", remoteChest.isRestricted());
        }

        try {
            chestConfig.save(chestFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        remoteChests.clear();
    }

    public File getChestFile() {
        return chestFile;
    }

    public FileConfiguration getChestConfig() {
        return chestConfig;
    }

    public Map<String, RemoteChest> getRemoteChests() {
        return remoteChests;
    }
}

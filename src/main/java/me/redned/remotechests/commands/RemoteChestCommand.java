package me.redned.remotechests.commands;

import me.redned.remotechests.RemoteChest;
import me.redned.remotechests.RemoteChests;
import me.redned.remotechests.utils.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Created by Redned on 4/6/2019.
 */
public class RemoteChestCommand implements CommandExecutor {

    private RemoteChests plugin;

    public RemoteChestCommand(RemoteChests plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("remotechest"))
            return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to run this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 1) {
            sendHelpMessage(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            sendHelpMessage(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(ChatColor.GOLD + "Remote Chests:");
            for (String str : plugin.getRemoteChests().keySet()) {
                RemoteChest remoteChest = plugin.getRemoteChests().get(str);
                player.sendMessage(str + " " + ChatColor.YELLOW + Utils.getFancyLocString(remoteChest.getLocation()));
            }
            return true;
        }

        if (args.length < 2) {
            sendHelpMessage(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("open")) {
            if (!player.hasPermission("remotechests.open")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return true;
            }

            RemoteChest remoteChest = plugin.getRemoteChests().get(args[1]);
            if (remoteChest == null) {
                player.sendMessage(ChatColor.RED + "That remote chest does not exist.");
                return true;
            }

            if (!remoteChest.getOwner().equals(player.getUniqueId()) && !player.hasPermission("remotechests.open.others")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to open other player's chests.");
                return true;
            }

            Block block = remoteChest.getLocation().getWorld().getBlockAt(remoteChest.getLocation());
            if (!block.getChunk().isLoaded()) {
                block.getChunk().load();
            }

            if (!(block.getState() instanceof Chest)) {
                player.sendMessage(ChatColor.RED + "The block located at " + Utils.getFancyLocString(remoteChest.getLocation()) + " is not a chest.");
                return true;
            }

            Chest chest = (Chest) remoteChest.getLocation().getWorld().getBlockAt(remoteChest.getLocation()).getState();
            player.openInventory(chest.getInventory());
            player.sendMessage(ChatColor.GREEN + "Opened remote chest " + remoteChest.getName() + ".");
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (!player.hasPermission("remotechests.create")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return true;
            }

            if (plugin.getRemoteChests().containsKey(args[1])) {
                player.sendMessage(ChatColor.RED + "A remote chest by that name already exists. Run /remotechest delete " + args[1] + " to delete it.");
                return true;
            }

            Block targetBlock = player.getTargetBlock((Set<Material>) null, 5);
            if (!(targetBlock.getState() instanceof Chest)) {
                player.sendMessage(ChatColor.RED + "The block you are looking at is not a chest.");
                return true;
            }

            boolean restricted = false;
            if (args.length > 2) {
                restricted = Boolean.parseBoolean(args[1]);
            }

            RemoteChest remoteChest = new RemoteChest(targetBlock.getLocation(), args[1], player.getUniqueId(), restricted);
            plugin.getRemoteChests().put(args[1], remoteChest);
            player.sendMessage(ChatColor.GREEN + "Successfully added new remote chest with name " + remoteChest.getName() + " at " + Utils.getFancyLocString(remoteChest.getLocation()) + ".");
            return true;
        }

        if (args[0].equalsIgnoreCase("delete")) {
            if (!player.hasPermission("remotechests.delete")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return true;
            }

            RemoteChest remoteChest = plugin.getRemoteChests().get(args[1]);
            if (remoteChest == null) {
                player.sendMessage(ChatColor.RED + "That remote chest does not exist.");
                return true;
            }

            if (!remoteChest.getOwner().equals(player.getUniqueId()) && !player.hasPermission("remotechests.delete.others")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to delete other player's chests.");
                return true;
            }

            plugin.getRemoteChests().remove(args[1]);
            player.sendMessage(ChatColor.GREEN + "Successfully deleted remote chest " + remoteChest.getName() + ".");
            return true;
        }
        return false;
    }

    public void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "Remote Chests Help:");
        sender.sendMessage(ChatColor.YELLOW + "/remotechest help " + ChatColor.WHITE + "Show this help message.");
        sender.sendMessage(ChatColor.YELLOW + "/remotechest list " + ChatColor.WHITE + "List all the remote chests.");
        sender.sendMessage(ChatColor.YELLOW + "/remotechest open <name> " + ChatColor.WHITE + "Open a remote chest.");
        sender.sendMessage(ChatColor.YELLOW + "/remotechest create <name> " + ChatColor.WHITE + "Create a remote chest.");
        sender.sendMessage(ChatColor.YELLOW + "/remotechest delete <name> " + ChatColor.WHITE + "Delete a remote chest.");
    }
}

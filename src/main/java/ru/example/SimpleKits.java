package ru.example;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class SimpleKits extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (cmd.getName().equalsIgnoreCase("kitcreate")) {
            if (args.length < 2) {
                player.sendMessage("§cИспользование: /kitcreate <имя> <разрешение>");
                return true;
            }
            String kitName = args[0];
            String perm = args[1];

            getConfig().set("kits." + kitName + ".permission", perm);
            getConfig().set("kits." + kitName + ".items", player.getInventory().getContents());
            saveConfig();

            player.sendMessage("§aКит " + kitName + " успешно сохранен!");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("kitdel")) {
            if (args.length < 1) {
                player.sendMessage("§cИспользование: /kitdel <имя>");
                return true;
            }
            getConfig().set("kits." + args[0], null);
            saveConfig();
            player.sendMessage("§aКит удален!");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("kit")) {
            if (args.length < 1) {
                player.sendMessage("§cИспользование: /kit <имя>");
                return true;
            }
            String kitName = args[0];

            if (!getConfig().contains("kits." + kitName)) {
                player.sendMessage("§cТакого кита не существует.");
                return true;
            }

            String requiredPerm = getConfig().getString("kits." + kitName + ".permission");
            if (requiredPerm != null && !requiredPerm.equals("none") && !player.hasPermission(requiredPerm)) {
                player.sendMessage("§cУ вас нет прав (" + requiredPerm + ") на этот кит.");
                return true;
            }

            List items = getConfig().getList("kits." + kitName + ".items");
            if (items != null) {
                for (Object obj : items) {
                    if (obj instanceof ItemStack item) {
                        player.getInventory().addItem(item);
                    }
                }
            }
            player.sendMessage("§aВы получили кит: " + kitName);
            return true;
        }
        return true;
    }
                }

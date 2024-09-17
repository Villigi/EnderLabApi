package de.villigi.enderlab.commands;

import de.villigi.enderlab.EnderLabApi;
import de.villigi.enderlab.api.MessageApi;
import de.villigi.enderlab.api.MessageInv;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SettingsCMD implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player p = (Player) commandSender;
        if(p.hasPermission("api.commands.settings")) {
            //TODO: Open settings inventory
            if(args[0].equalsIgnoreCase("messages")) {
                MessageInv messageInv = new MessageInv();
                messageInv.openPagedInventory(p, 1);
            }




        }else{
            p.sendMessage(EnderLabApi.prefix + new MessageApi("message.noperms").getMessage());
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> list = new ArrayList<>();
        if(strings.length == 1) {
            list.add("messages");
        }

        return list;
    }
}

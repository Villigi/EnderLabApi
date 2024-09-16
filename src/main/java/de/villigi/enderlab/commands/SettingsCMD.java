package de.villigi.enderlab.commands;

import de.villigi.enderlab.EnderLabApi;
import de.villigi.enderlab.api.MessageApi;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SettingsCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player p = (Player) commandSender;
        if(p.hasPermission("api.commands.settings")) {
            //TODO: Open settings inventory



        }else{
            p.sendMessage(EnderLabApi.prefix + new MessageApi("message.noperms").getMessage());
        }

        return false;
    }
}

package de.villigi.enderlab;

import de.villigi.enderlab.api.MessageApi;
import de.villigi.enderlab.api.MessageInv;
import de.villigi.enderlab.commands.SettingsCMD;
import de.villigi.enderlab.database.DatabaseManager;
import de.villigi.enderlab.listener.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class EnderLabApi extends JavaPlugin {

    private static EnderLabApi instance;

    public DatabaseManager databaseManager;

    public static String prefix = " ";



    @Override
    public void onEnable() {
        System.out.println("Enableing...");
        instance = this;
        databaseManager = new DatabaseManager();
        databaseManager.loadFiles();

        try {
            databaseManager.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        register();

        prefix = new MessageApi("prefix").getMessage();

        getCommand("settings").setExecutor(new SettingsCMD());
        getCommand("settings").setTabCompleter(new SettingsCMD());

        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new MessageInv(), this);

        System.out.println("Finnish!");
    }

    public void register() {
        new MessageApi("prefix").addMessage("§bEnderLab §7| ");
        new MessageApi("message.noperms").addMessage("§cDazu hast du keine Rechte!");

        new MessageApi("message.1").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("message.2").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("message.3").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("message.4").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("message.5").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("message.6").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("message.7").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("message.8").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("message.9").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("message.10").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("message.11").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("message.12").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("message.13").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("message.14").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("message.15").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("1message.1").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("2message.2").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("3message.3").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("4message.4").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("5message.5").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("6message.6").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("7message.7").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("8message.8").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("9message.9").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("10message.10").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("11message.11").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("12message.12").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("13message.13").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("14message.14").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("15message.15").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("16message.5").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("16message.6").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("18message.7").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("19message.1").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("20message.2").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("21message.3").addMessage("§cDazu hast du keine Rechte!");
        new MessageApi("22message.4").addMessage("§cDazu hast du keine Rechte!");

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static EnderLabApi getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}

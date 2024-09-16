package de.villigi.enderlab;

import de.villigi.enderlab.api.MessageApi;
import de.villigi.enderlab.database.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class EnderLabApi extends JavaPlugin {

    private static EnderLabApi instance;
    public DatabaseManager databaseManager;

    public static String prefix = " ";



    @Override
    public void onEnable() {
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
    }

    public void register() {
        new MessageApi("prefix").addMessage("§bEnderLab §7| ");
        new MessageApi("message.noperms").addMessage("§cDazu hast du keine Rechte!");
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

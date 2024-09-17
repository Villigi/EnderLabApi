package de.villigi.enderlab.api;

import de.villigi.enderlab.EnderLabApi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageApi {
    private String placeholder;
    public MessageApi(String placeholder) {
        this.placeholder = placeholder;
    }

    public void addMessage(String message) {
        //messages      Placeholder    Messages

        if(!isPlaceholderInDatabase(placeholder)) {
            try {
                PreparedStatement statement = EnderLabApi.getInstance().getDatabaseManager().getConnection().prepareStatement("INSERT INTO messages (Placeholder, Message) VALUES ('" + placeholder + "', '" + message + "');");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String getMessage() {
        String message = "";
        try {
            PreparedStatement statement = EnderLabApi.getInstance().getDatabaseManager().getConnection().prepareStatement("SELECT Message FROM `messages` WHERE Placeholder = ?;");
            statement.setString(1, placeholder);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                message = resultSet.getString("Message");
            }else{
                message = "§cDie Nachricht mit dem Namen ("+placeholder+") wurde nicht in der Datenbank gefunden!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }


    public static HashMap<String, String> getMessages() throws SQLException {
        HashMap<String, String> messages = new HashMap<>();
        String sql = "SELECT Placeholder, Message FROM messages";
        Connection connection = EnderLabApi.getInstance().getDatabaseManager().getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String placeholder = resultSet.getString("Placeholder");
            String message = resultSet.getString("Message");
            messages.put(placeholder, message);
        }

        resultSet.close();
        statement.close();

        return messages;
    }


    public void changeMessage(String message) {
        try {
            PreparedStatement statement = EnderLabApi.getInstance().getDatabaseManager().getConnection().prepareStatement("UPDATE messages SET Message = '" + message + "' WHERE Placeholder = '" + placeholder + "';");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaceholderInDatabase(String placeholder) {
        boolean exists = false;
        try {
            // Bereite die SQL-Abfrage vor, um nach dem Placeholder zu suchen
            PreparedStatement statement = EnderLabApi.getInstance().getDatabaseManager().getConnection()
                    .prepareStatement("SELECT COUNT(*) FROM `messages` WHERE `Placeholder` = ?;");

            // Setze den Placeholder in das PreparedStatement ein
            statement.setString(1, placeholder);

            // Führe die Abfrage aus
            ResultSet resultSet = statement.executeQuery();

            // Prüfe das Ergebnis
            if (resultSet.next()) {
                // Wenn COUNT(*) > 0, dann existiert der Placeholder
                exists = resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }


}



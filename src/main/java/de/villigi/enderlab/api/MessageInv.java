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
import java.util.List;

public class MessageInv {
    private final Connection connection;

    public MessageInv() {
        this.connection = EnderLabApi.getInstance().getDatabaseManager().getConnection();
    }

    public  void openPagedInventory(Player player, int page) {
        int entriesPerPage = 27; // Anzahl der Einträge pro Seite (ohne Navigationszeile)
        List<MessageEntry> messageEntries = getMessageEntries(); // Alle Nachrichten aus der Datenbank

        // Berechne die maximale Anzahl der Seiten
        int totalPages = (int) Math.ceil((double) messageEntries.size() / entriesPerPage);

        // Stelle sicher, dass die Seite im gültigen Bereich liegt
        if (page < 0) page = 0;
        if (page >= totalPages) page = totalPages - 1;

        // Erstelle das Inventar mit 6 Reihen (54 Slots)
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_PURPLE + "Messages - Seite " + (page + 1));

        // Fülle das Inventar mit den Einträgen der aktuellen Seite
        int startIndex = page * entriesPerPage;
        int endIndex = Math.min(startIndex + entriesPerPage, messageEntries.size());

        for (int i = startIndex; i < endIndex; i++) {
            MessageEntry entry = messageEntries.get(i);

            // Erstelle ein ItemStack für die Feder
            ItemStack feather = new ItemStack(Material.FEATHER);
            ItemMeta meta = feather.getItemMeta();
            if (meta != null) {
                // Setze den Titel (Placeholder)
                meta.setDisplayName(ChatColor.GOLD + entry.getPlaceholder());

                // Setze die Lore (Message)
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.WHITE + entry.getMessage());
                meta.setLore(lore);

                feather.setItemMeta(meta);
            }

            // Füge das Item dem Inventar hinzu
            inventory.addItem(feather);
        }

        // Füge Navigations-Items hinzu
        ItemStack previousPage = new ItemStack(Material.ARROW);
        ItemMeta previousMeta = previousPage.getItemMeta();
        if (previousMeta != null) {
            previousMeta.setDisplayName(ChatColor.YELLOW + "Vorherige Seite");
            previousPage.setItemMeta(previousMeta);
        }

        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextPage.getItemMeta();
        if (nextMeta != null) {
            nextMeta.setDisplayName(ChatColor.YELLOW + "Nächste Seite");
            nextPage.setItemMeta(nextMeta);
        }

        // Setze die Navigations-Items in die Slots 48 und 50
        if (page > 0) {
            inventory.setItem(48, previousPage); // Linke Navigationspfeil
        }

        if (page < totalPages - 1) {
            inventory.setItem(50, nextPage); // Rechte Navigationspfeil
        }

        // Öffne das Inventar für den Spieler
        player.openInventory(inventory);
    }

    // Methode zum Abrufen der Daten aus der Datenbank
    private List<MessageEntry> getMessageEntries() {
        List<MessageEntry> messageEntries = new ArrayList<>();

        try {
            // PreparedStatement statement = connection.prepareStatement("SELECT Placeholder, Message FROM messages;");
            PreparedStatement statement = EnderLabApi.getInstance().getDatabaseManager().getConnection().prepareStatement("SELECT Placeholder, Message FROM messages;");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String placeholder = resultSet.getString("Placeholder");
                String message = resultSet.getString("Message");
                messageEntries.add(new MessageEntry(placeholder, message));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messageEntries;
    }

    // Event-Handler für Inventar-Interaktionen
    public void onInventoryClick(InventoryClickEvent event, Player player, int currentPage) {
        if (event.getCurrentItem() == null) return;

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem.getType() == Material.ARROW) {
            ItemMeta meta = clickedItem.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                if (meta.getDisplayName().equals(ChatColor.YELLOW + "Vorherige Seite")) {
                    openPagedInventory(player, currentPage - 1);
                } else if (meta.getDisplayName().equals(ChatColor.YELLOW + "Nächste Seite")) {
                    openPagedInventory(player, currentPage + 1);
                }
            }
        }

        event.setCancelled(true); // Verhindere, dass der Spieler Items aus dem Inventar nimmt
    }

    // Event-Handler für das Schließen des Inventars (optional, falls Cleanup nötig ist)
    public void onInventoryClose(InventoryCloseEvent event) {
        // Hier könnte man Cleanup-Operationen durchführen, falls nötig
    }
}

class MessageEntry {
    private final String placeholder;
    private final String message;

    public MessageEntry(String placeholder, String message) {
        this.placeholder = placeholder;
        this.message = message;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getMessage() {
        return message;
    }
}

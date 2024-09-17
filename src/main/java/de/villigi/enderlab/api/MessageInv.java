package de.villigi.enderlab.api;

import de.villigi.enderlab.EnderLabApi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.w3c.dom.events.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageInv implements Listener {
    private final Connection connection;

    private Map<Player, Integer> playerPages = new HashMap<>();
    private List<Player> inPage = new ArrayList<>();

    public MessageInv() {
        this.connection = EnderLabApi.getInstance().getDatabaseManager().getConnection();
    }

    public  void openPagedInventory(Player player, int page) {
        int entriesPerPage = 45; // Anzahl der Einträge pro Seite (ohne Navigationszeile)
        List<MessageEntry> messageEntries = getMessageEntries(); // Alle Nachrichten aus der Datenbank

        // Berechne die maximale Anzahl der Seiten
        int totalPages = (int) Math.ceil((double) messageEntries.size() / entriesPerPage);

        // Stelle sicher, dass die Seite im gültigen Bereich liegt
        if (page < 0) page = 0;
        if (page >= totalPages) page = totalPages - 1;

        // Erstelle das Inventar mit 6 Reihen (54 Slots)
        Inventory inventory = Bukkit.createInventory(null, 54,"Messages - §bSeite " + (page + 1));

        // Fülle das Inventar mit den Einträgen der aktuellen Seite
        int startIndex = page * entriesPerPage;
        int endIndex = Math.min(startIndex + entriesPerPage, messageEntries.size());
        if(getMessageEntries() != null) {
            for (int i = startIndex; i < endIndex; i++) {
                MessageEntry entry = messageEntries.get(i);

                // Erstelle ein ItemStack für die Feder
                ItemStack feather = new ItemStack(Material.FEATHER);
                ItemMeta meta = feather.getItemMeta();
                if (meta != null) {
                    // Setze den Titel (Placeholder)
                    meta.setDisplayName("§7Placeholder: §b" + entry.getPlaceholder());

                    // Setze die Lore (Message)
                    List<String> lore = new ArrayList<>();
                    lore.add("§7Lore: §b" + entry.getMessage());
                    meta.setLore(lore);

                    feather.setItemMeta(meta);
                }

                // Füge das Item dem Inventar hinzu
                inventory.addItem(feather);
            }
        }else{
            System.out.println("Es sind keine Messageeinträge vorhanden.");
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
        inPage.add(player);
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        if(inPage.contains(event.getPlayer())) {
            inPage.remove(event.getPlayer());
        }
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


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked(); // Hole den Spieler aus dem Event
        int currentPage = getCurrentPage(player); // Hole die aktuelle Seite (falls du diese Information speicherst)

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



    public void openEditInventory(Player player, String placeholder) {
        // Setze den Placeholder im Titel des Inventars
        Inventory editInventory = Bukkit.createInventory(null, 9, "Bearbeite: " + placeholder);

        ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta meta = book.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Bearbeite die Nachricht für " + placeholder);
            book.setItemMeta(meta);
        }

        editInventory.setItem(4, book); // Buch in die Mitte des Inventars legen
        player.openInventory(editInventory); // Öffne das neue Inventar für den Spieler
    }


    public int getCurrentPage(Player player) {
        return playerPages.getOrDefault(player, 1); // Standardwert 1, falls keine Seite gespeichert ist
    }

    public void setCurrentPage(Player player, int page) {
        playerPages.put(player, page);
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

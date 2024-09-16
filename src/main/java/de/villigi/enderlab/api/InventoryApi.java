package de.villigi.enderlab.api;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class InventoryApi {


    //TODO: Mehrere Seiten einbauen

    public void openMessageInventory(Player player) {
        try {
            HashMap<String, String> messages = MessageApi.getMessages();
            Inventory inv = Bukkit.createInventory(player, 6*9, "Messages");
            player.openInventory(inv);
            inv.setItem(53, new ItemBuilder(Material.ARROW).setDisplayname("Next Site §7(Rightclick)").build());

            int slots = 0;
            for (Map.Entry<String, String> entry : messages.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                inv.setItem(slots, new ItemBuilder(Material.ENCHANTED_BOOK).setDisplayname("§7Placeholder: §b" + key).setLore("§b" + value).build());
                slots++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





}

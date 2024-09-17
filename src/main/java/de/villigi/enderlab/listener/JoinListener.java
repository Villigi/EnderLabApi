package de.villigi.enderlab.listener;

import de.villigi.enderlab.EnderLabApi;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class JoinListener implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(!EnderLabApi.getInstance().getDatabaseManager().isConnectet()) {
            try {
                EnderLabApi.getInstance().getDatabaseManager().connect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

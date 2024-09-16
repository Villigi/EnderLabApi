package de.villigi.enderlab.api;

import de.villigi.enderlab.EnderLabApi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlApi {
    public static void updatePreparedStatement(String statement) {
        try {
            PreparedStatement statemnt1 = EnderLabApi.getInstance().getDatabaseManager().getConnection().prepareStatement(statement);
            statemnt1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnectionOfApi() {
        return EnderLabApi.getInstance().getDatabaseManager().getConnection();
    }
}

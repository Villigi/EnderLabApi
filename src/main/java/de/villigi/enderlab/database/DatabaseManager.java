package de.villigi.enderlab.database;

import de.villigi.enderlab.EnderLabApi;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {



    public File sql_file;
    public YamlConfiguration configuration;
    public FileConfiguration fileConfiguration;

    private String host;
    private int port;
    private String portstring;
    private String database;
    private String username;
    private String password;

    private Connection connection;

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false",
                username,
                password);
        connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `addon_availability`(`Addon` VARCHAR(255), `Availability` VARCHAR(255));");
        connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `messages`(`Placeholder` VARCHAR(255), `Message` VARCHAR(255));");
    }


    public void loadFiles() {
        if(!EnderLabApi.getInstance().getDataFolder().exists()) {
            EnderLabApi.getInstance().getDataFolder().mkdirs();
        }





        sql_file = new File(EnderLabApi.getInstance().getDataFolder(), "mysql.yml");

        if(!sql_file.exists()) {
            try {
                sql_file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileConfiguration =  YamlConfiguration.loadConfiguration(sql_file);
            fileConfiguration.options().copyDefaults(true);

            fileConfiguration.set("mysql.HOST", "localhost");
            fileConfiguration.set("mysql.PORT", "portInNumber");
            fileConfiguration.set("mysql.DATABASE", "API");
            fileConfiguration.set("mysql.USERNAME", "Plugin");
            fileConfiguration.set("mysql.PASSWORD", "YourPassword");
            try {
                fileConfiguration.save(sql_file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        fileConfiguration =  YamlConfiguration.loadConfiguration(sql_file);

        host = fileConfiguration.getString("mysql.HOST");
        portstring = fileConfiguration.getString("mysql.PORT");
        database = fileConfiguration.getString("mysql.DATABASE");
        username = fileConfiguration.getString("mysql.USERNAME");
        password = fileConfiguration.getString("mysql.PASSWORD");

        port = Integer.parseInt(portstring);


    }


    public boolean isConnectet() {
        return connection != null;
    }

    public Connection getConnection() {
        return connection;
    }


    public void disconnect() {

        if(isConnectet()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }

}

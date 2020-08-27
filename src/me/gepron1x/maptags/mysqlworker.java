package me.gepron1x.maptags;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.google.gson.Gson;

public class mysqlworker {
private String host,username,database,password,table;
private Connection connection;
private int port;
public mysqlworker(MapTagsPlugin plugin) {
	Statement statement = null;
	this.host = plugin.getConfig().getString("host").split(":")[0];
	this.database = plugin.getConfig().getString("database");
	this.port = Integer.parseInt(plugin.getConfig().getString("host").split(":")[1]);
	this.username = plugin.getConfig().getString("username");
	this.password = plugin.getConfig().getString("password");
	this.table = "maptags";
	try {

		synchronized (this) {
			if (connection  != null && !connection.isClosed()) {
				return;
			}

			Class.forName("com.mysql.jdbc.Driver");
			setConnection(
					DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database,
							this.username, this.password));

			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MYSQL CONNECTED");
		}
	} catch (SQLException e) {
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	try {
		statement = connection.createStatement();
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS maptags " +
                "(`id` VARCHAR(5), " +
                " name VARCHAR(10), " +
                " lore TINYTEXT" +
                " location VARCHAR(30), " +
                " owner VARCHAR(36),"
              + " icon BLOB" +
                " PRIMARY KEY (id))");
	} catch (SQLException e) {
		// TODO Автоматически созданный блок catch
		e.printStackTrace();
	}
	
	
}
public void setConnection(Connection c) {
	this.connection = c;
}
public void createMapTag(MapTag tag) {
	try {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO maptags (id,name,location,owner,icon) VALUES (?,?,?,?,?)");
		statement.setString(1, tag.getId());
		statement.setString(2, tag.getName());
		statement.setString(3, tag.getLocation());
		statement.setString(4, tag.getOwner().toString());
		statement.setString(5, tag.getIcon());
		 statement.executeUpdate();
	} catch (SQLException e) {
		// TODO Автоматически созданный блок catch
		e.printStackTrace();
	}
}
public List<MapTag> getMapTags() {
	List<MapTag> tags = new ArrayList<MapTag>();
	try {
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM maptags");
		ResultSet result = statement.getResultSet();
		Gson gson = new Gson();
		
		while(result.next()) {
		 String id = result.getString("id");
		 String lore = result.getString("lore");
		 String name = result.getString("name");
		 Location loc = fromString(result.getString("location"));
		 UUID owner = UUID.fromString(result.getString("owner"));
		 ItemStack icon = gson.fromJson(result.getString("icon"), ItemStack.class);
		 tags.add(new MapTag(id,name,lore,owner,loc,icon));
		}
	} catch (SQLException e) {
		// TODO Автоматически созданный блок catch
		e.printStackTrace();
	}
	
return tags;	
}
private Location fromString(String location) {
	String[] locs = location.split(",");
	Location loc = new Location(Bukkit.getWorld(locs[0]),Double.parseDouble(locs[1]),Double.parseDouble(locs[2]),Double.parseDouble(locs[3]));
	return loc;
}
public void deleteTag(String id) {
	try {
		PreparedStatement statement = connection.prepareStatement("DELETE FROM maptags WHERE id=?");
		statement.setString(1, id);
		statement.executeUpdate();
	} catch (SQLException e) {
		// TODO Автоматически созданный блок catch
		e.printStackTrace();
	} 
}
public List<MapTag>	getPlayerTags(UUID player) {
	List<MapTag> result = new ArrayList<MapTag>();
	Gson gson = new Gson();
	PreparedStatement statement;
	try {
		statement = connection.prepareStatement("SELECT * FROM maptags WHERE owner=?");
	
	statement.setString(1, player.toString());
	ResultSet resultset = statement.getResultSet();
	
	while(resultset.next()) {
		String id =  resultset.getString("id");
		 String lore = resultset.getString("lore");
		 String name = resultset.getString("name");
		 Location loc = fromString(resultset.getString("location"));
		 UUID owner = UUID.fromString(resultset.getString("owner"));
		 ItemStack icon = gson.fromJson(resultset.getString("icon"), ItemStack.class);
		 result.add(new MapTag(id,name,lore,owner,loc,icon));
	}
	} catch (SQLException e) {
		// TODO Автоматически созданный блок catch
		e.printStackTrace();
	}
	return result;
}
public String getTable() {
	return table;
}

	
}



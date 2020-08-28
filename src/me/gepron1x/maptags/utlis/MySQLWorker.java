package me.gepron1x.maptags.utlis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.gepron1x.maptags.MapTagsPlugin;

public class MySQLWorker {

	private MapTagsPlugin plugin;
	private String host, username, database, password, table;
	private Connection connection;

	public MySQLWorker() {
	
         plugin = MapTagsPlugin.getInstance();
		this.host = plugin.getConfig().getString("mysql.host");
		this.database = plugin.getConfig().getString("mysql.database");
		this.username = plugin.getConfig().getString("mysql.username");
		this.password = plugin.getConfig().getString("mysql.password");
		this.table = "maptags";
		final String hst = this.host;
		final String db = this.database;
		final String usr = this.username;
		final String psw = this.password;

		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				
				try {
					synchronized (this) {
						if (connection != null && !connection.isClosed()) {
							return;
						}
						Class.forName("com.mysql.jdbc.Driver");
						setConnection(
								DriverManager.getConnection("jdbc:mysql://" + hst + "/" + db,
										usr, psw));

						Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MYSQL CONNECTED");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				try {
					Statement statement = connection.createStatement();
					statement.executeUpdate("CREATE TABLE IF NOT EXISTS maptags " + 
					"(`id` VARCHAR(10), "
					+ " name VARCHAR(20), "
					+ " lore TINYTEXT," + " location TINYTEXT, " + " owner VARCHAR(36)," + " icon BLOB,"
					+ " PRIMARY KEY (id))");
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				plugin.loadTagsfromMySQL(getMapTags());
			}
			
		});
		

	}

	public void setConnection(Connection c) {
		this.connection = c;
	}

	public void createMapTag(final MapTag tag) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			public void run() {
				try {
					PreparedStatement statement = connection
							.prepareStatement("INSERT INTO maptags (id,name,lore,location,owner,icon) VALUES (?,?,?,?,?,?)");
					statement.setString(1, tag.getId());
					statement.setString(2, tag.getName());
					statement.setString(3, tag.getLoreJson());
					statement.setString(4, tag.getSerializedLocation());
					statement.setString(5, tag.getOwner().toString());
					statement.setString(6, tag.getSerializedIcon());
					statement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public List<MapTag> getMapTags() {
		List<MapTag> tags = new ArrayList<MapTag>();
		//Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			//@Override
			//public void run() {
				// TODO Автоматически созданная заглушка метода
			
				try {
					Statement statement = connection.createStatement(
		                    ResultSet.TYPE_FORWARD_ONLY,
		                    ResultSet.CONCUR_READ_ONLY
		            );
					 
					ResultSet result = statement.executeQuery("SELECT * FROM maptags");
					final Gson gson = new GsonBuilder().create();
					while (result.next()) {
						String id = result.getString("id");
						@SuppressWarnings("unchecked")
						List<String> lore = gson.fromJson(result.getString("lore"), new ArrayList<String>().getClass());
						String name = result.getString("name");
						@SuppressWarnings("unchecked")
						Location loc = Location.deserialize(gson.fromJson(result.getString("location"), new HashMap<String,Object>().getClass()));
						UUID owner = UUID.fromString(result.getString("owner"));
						@SuppressWarnings("unchecked")
						Map<String, Object> map = gson.fromJson(result.getString("icon"), new HashMap<String,Object>().getClass());
						ItemStack icon = ItemStack.deserialize(map);
						tags.add(new MapTag(id, name, lore, owner, loc, icon));
						
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			  
			//}
			
		//});
		for(MapTag tag : tags) {
			Bukkit.getConsoleSender().sendMessage(tag.getId());
		}
		return tags;
		
	}


	public String getTable() {
		return table;
	}

	public void deleteTag(final String id) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				try {
					PreparedStatement statement = connection.prepareStatement("DELETE FROM maptags WHERE id=?");
					statement.setString(1, id);
					statement.executeUpdate();

				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
		});
		
	}

	public List<MapTag> getPlayerMapTags(UUID player) {
		List<MapTag> tags = new ArrayList<MapTag>();
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				try {
					PreparedStatement statement = connection.prepareStatement("SELECT * FROM maptags where owner=?");
					statement.setString(1, player.toString());
					ResultSet result = statement.getResultSet();
					Gson gson = new Gson();
					while (result.next()) {
						String id = result.getString("id");
						@SuppressWarnings("unchecked")
						List<String> lore = gson.fromJson(result.getString("lore"),new ArrayList<String>().getClass());
						String name = result.getString("name");
						@SuppressWarnings("unchecked")
						Location loc = Location.deserialize(gson.fromJson(result.getString("location"), new HashMap<String,Object>().getClass()));
						UUID owner = UUID.fromString(result.getString("owner"));
						@SuppressWarnings("unchecked")
						Map<String, Object> map = gson.fromJson(result.getString("icon"), new HashMap<String,Object>().getClass());
						ItemStack icon = ItemStack.deserialize(map);
						tags.add(new MapTag(id, name, lore, owner, loc, icon));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		});
	
		return tags;
	}

}

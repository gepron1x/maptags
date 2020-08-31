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
						setConnection(DriverManager.getConnection("jdbc:mysql://" + hst + "/" + db, usr, psw));

						Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MYSQL CONNECTED");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				try {
					Statement statement = connection.createStatement();
					statement.executeUpdate("CREATE TABLE IF NOT EXISTS maptags " + "(`id` VARCHAR(10), "
							+ " name VARCHAR(20), " + " lore TINYTEXT," + " location TINYTEXT, " + " owner VARCHAR(36),"
							+ " icon BLOB," + " type ENUM('GLOBAL','LOCAL')," + " PRIMARY KEY (id))");
					Statement statement2 = connection.createStatement();
					statement2.executeUpdate("CREATE TABLE IF NOT EXISTS permissions " +
					"(`user` VARCHAR(36), " +
					"permission VARCHAR(10))");
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

	public void createMapTag(final MapTag tag, boolean isLocal) {
		String dump;
		if (isLocal == false) {
			dump = "GLOBAL";
		} else {
			dump = "LOCAL";
		}
		final String dump2 = dump;
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			public void run() {
				try {
					PreparedStatement statement = connection.prepareStatement(
							"INSERT INTO maptags (id,name,lore,location,owner,icon,type) VALUES (?,?,?,?,?,?,?)");
					statement.setString(1, tag.getId());
					statement.setString(2, tag.getName());
					statement.setString(3, tag.getLoreJson());
					statement.setString(4, tag.getSerializedLocation());
					statement.setString(5, tag.getOwner().toString());
					statement.setString(6, tag.getSerializedIcon());
					statement.setString(7, dump2);
					statement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public List<MapTag> getMapTags() {
		List<MapTag> tags = new ArrayList<MapTag>();
		// Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

		// @Override
		// public void run() {
		// TODO Автоматически созданная заглушка метода

		try {
			Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

			ResultSet result = statement.executeQuery("SELECT * FROM maptags WHERE type='GLOBAL'");
			final Gson gson = new GsonBuilder().create();
			while (result.next()) {
				String id = result.getString("id");
				@SuppressWarnings("unchecked")
				List<String> lore = gson.fromJson(result.getString("lore"), new ArrayList<String>().getClass());
				String name = result.getString("name");
				@SuppressWarnings("unchecked")
				Location loc = Location.deserialize(
						gson.fromJson(result.getString("location"), new HashMap<String, Object>().getClass()));
				UUID owner = UUID.fromString(result.getString("owner"));
				@SuppressWarnings("unchecked")
				Map<String, Object> map = gson.fromJson(result.getString("icon"),
						new HashMap<String, Object>().getClass());
				ItemStack icon = ItemStack.deserialize(map);
				tags.add(new MapTag(id, name, lore, owner, loc, icon));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// }

		// });
		for (MapTag tag : tags) {
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
		
		// Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

		// @Override
		// public void run() {
		String vars = getPlayerPermissions(player);
	 String sql = "SELECT * FROM maptags WHERE owner='" + player.toString() + "' AND type='LOCAL'";
	 if(vars != "") sql = "SELECT * FROM maptags WHERE owner='" + player.toString() + "' AND type='LOCAL' OR id IN ("+vars+")";
	 
		try {
			
				
			
			Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet result = statement
					.executeQuery(sql);
			

			Gson gson = new Gson();
			while (result.next()) {
				String id = result.getString("id");
				@SuppressWarnings("unchecked")
				List<String> lore = gson.fromJson(result.getString("lore"), new ArrayList<String>().getClass());
				String name = result.getString("name");
				@SuppressWarnings("unchecked")
				Location loc = Location.deserialize(
						gson.fromJson(result.getString("location"), new HashMap<String, Object>().getClass()));
				UUID owner = UUID.fromString(result.getString("owner"));
				@SuppressWarnings("unchecked")
				Map<String, Object> map = gson.fromJson(result.getString("icon"),
						new HashMap<String, Object>().getClass());
				ItemStack icon = ItemStack.deserialize(map);
				tags.add(new MapTag(id, name, lore, owner, loc, icon));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// }

		// });

		return tags;
	}
public String getPlayerPermissions(UUID player) {
	Statement statement;
	 List<String> perms = new ArrayList<String>();
	try {
		statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	    ResultSet permz = statement.executeQuery("SELECT * FROM permissions WHERE `user`='"+player.toString()+"'");
	   
	    while(permz.next()) {
	    	perms.add(permz.getString("permission"));
	    }
	} catch (SQLException e) {
		// TODO Автоматически созданный блок catch
		e.printStackTrace();
	} 
String result = "";
for(String perm : perms) {
	result = result+"'"+perm+"'";
	if(!perms.get(perms.size()-1).equalsIgnoreCase(perm)) result = result+",";
	plugin.send("\""+result+"\"");
}
    return result;
}

	public void setPlayerPermission(final UUID player,final String id) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				try {
					PreparedStatement statement = connection.prepareStatement(
							"INSERT INTO permissions (`user`,permission) VALUES (?,?)");
					statement.setString(1, player.toString());
					statement.setString(2, id);
					statement.executeUpdate();
					
				} catch (SQLException e) {
					// TODO Автоматически созданный блок catch
					e.printStackTrace();
				}
				
			}
			
		});
	}
public void removePermission(final UUID player,final String id) {
	Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

		@Override
		public void run() {
			try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM permissions WHERE id=? AND user=?");
			statement.setString(1, id);
			statement.setString(2, player.toString());
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		}
		
	});
}


}

package de.web.bungeeplugin.systemtimoliaapply.util;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class API {
	
	public static Set<UUID> reportlog = new HashSet<UUID>(); //Set of Player-UUIDs who is logged into the report system
	public static Map<UUID, ReportData> reports = new HashMap<UUID, ReportData>();
	
	/**
	 * Initialize MySQL Connection and load Config
	 */
	public static void init() {
		File dir = new File("plugins/System/");
		if (!dir.exists()) dir.mkdirs();
		
		File mysqlFile = new File("plugins/System/", "mysql.yml");
		if (!mysqlFile.exists()) {
			try {
				mysqlFile.createNewFile();
				Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(mysqlFile);
				
				cfg.set("Host", "localhost");
				cfg.set("Port", 3306);
				cfg.set("Database", "Bungeesystem");
				cfg.set("Username", "root");
				cfg.set("Password", "password");
				
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, mysqlFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			//Read Config and connect to specified Host with Username and Password
			Configuration mysqlConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(mysqlFile);
			
			MySQL.connect("jdbc:mysql://" + mysqlConfig.getString("Host") + ":" + mysqlConfig.getInt("Port") + "/" + mysqlConfig.getString("Database"),
					mysqlConfig.getString("Username"), mysqlConfig.getString("Password"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			//Create Bans Table in Database if not exists
			MySQL.con.prepareStatement("CREATE TABLE IF NOT EXISTS Bans(UUID VARCHAR(40), Name VARCHAR(20), Reason VARCHAR(30), BannedBy VARCHAR(20), Time VARCHAR(15), Ending BIGINT, Mute BOOLEAN);").executeUpdate();
			MySQL.con.prepareStatement("CREATE TABLE IF NOT EXISTS BanPermissions(UUID VARCHAR(40), Name VARCHAR(20), Exempt BOOLEAN);").executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Adds a ban/mute to the database and kicks the player if he is online
	 * 
	 * @param uuid
	 * @param name
	 * @param reason
	 * @param bannedBy
	 * @param time
	 * @param seconds
	 * @param mute
	 */
	@SuppressWarnings("deprecation")
	public static void ban(UUID uuid, String name, String reason, String bannedBy, String time, long seconds, boolean mute) {
		long end = -1;
		
		if (seconds != -1) {
			end = System.currentTimeMillis() + (seconds * 1000);
		}
		
		try {
			PreparedStatement ps = MySQL.con.prepareStatement("DELETE FROM Bans WHERE UUID=?;");
			ps.setString(1, uuid.toString());
			ps.executeUpdate();
			
			PreparedStatement ps1 = MySQL.con.prepareStatement("INSERT INTO Bans VALUES (?, ?, ?, ?, ?, ?, ?);");
			ps1.setString(1, uuid.toString());
			ps1.setString(2, name);
			ps1.setString(3, reason);
			ps1.setString(4, bannedBy);
			ps1.setString(5, time);
			ps1.setLong(6, end);
			ps1.setBoolean(7, mute);
			ps1.executeUpdate();
			
			//Kick if online
			ProxiedPlayer t = ProxyServer.getInstance().getPlayer(name);
			if (t != null && t.isConnected()) {
				if (mute) {
					t.sendMessage(SystemCMD.pre + "§7Du wurdest gemutet");
					t.sendMessage(SystemCMD.pre + "§7Grund: §a" + reason);
					t.sendMessage(SystemCMD.pre + "§7Verbleibend: §a" + translateTime(end));
					t.sendMessage(SystemCMD.pre + "§7Du kannst auf §atimolia.de §7einen Entmuteantrag stellen");
				} else {
					if (end == -1) {
						t.disconnect("§7Du wurdest §6" + time + " §7gebannt§r\n§r\n§r§7Grund: §a" + reason + "§r\n§r\n§r§7Du kannst auf §atimolia.de§r\n§r§7einen Entbannungsantrag stellen");
					} else {
						t.disconnect("§7Du wurdest für §a" + time + " §7gebannt§r\n§r\n§r§7Grund: §a" + reason + "§r\n§r\n§r§7Verbleibende Zeit: §a" + translateTime(end) + "§r\n§r\n§r§7Du kannst auf §atimolia.de§r\n§r§7einen Entbannungsantrag stellen");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Check if a Player is banned
	 * Will return false when only muted
	 * 
	 * @param uuid
	 * @return
	 */
	public static boolean isBanned(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.con.prepareStatement("SELECT Mute FROM Bans WHERE UUID=?;");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getBoolean("Mute")) return false;
				return true;
			} else {
				return false;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return true;
		}
	}
	
	/**
	 * 
	 * Removes a BanEntry from the database
	 * 
	 * @param uuid
	 */
	public static void unban(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.con.prepareStatement("DELETE FROM Bans WHERE UUID=?;");
			ps.setString(1, uuid.toString());
			ps.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return;
		}
	}
	
	/**
	 * 
	 * Convert time left to readable form
	 * 
	 * @param end
	 * @return
	 */
	public static String translateTime(long end) {
		long current = System.currentTimeMillis();
		long remaining = end - current;
		
		//Check for too small remaining time
		if (remaining <= 0) {
			return "§4Abgelaufen";
		} else if (remaining < 1100) {
			return "§a0 §aTag(e) §a0 §aStunde(n) §a0 §aMinute(n) §a1 §aSekunde(n)";
		}
		
		long seconds = 0;
		long minutes = 0;
		long hours = 0;
		long days = 0;
		
		seconds = remaining / 1000; 
		while (seconds >= 60) {
			seconds -= 60;
			minutes++;
		}
		while (minutes >= 60) {
			minutes -= 60;
			hours++;
		}
		while (hours >= 24) {
			hours -= 24;
			days++;
		}
		return "§a" + days + " §aTag(e) §a" + hours + " §aStunde(n) §a" + minutes + " §aMinute(n) §a" + seconds + " §aSekunde(n)";
	}
	
	/**
	 * 
	 * Get BanInfo of Player
	 * Returns null if Ban not found
	 * 
	 * @param uuid
	 * @return
	 */
	public static BanInfo getBanInfo(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.con.prepareStatement("SELECT * FROM Bans WHERE UUID=?;");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new BanInfo(uuid, rs.getString("Name"), rs.getString("Reason"), rs.getLong("Ending"), rs.getString("Time"), rs.getString("BannedBy"),
						rs.getBoolean("Mute"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * Check if a Player is muted
	 * Will return false when banned
	 * 
	 * @param uuid
	 * @return
	 */
	public static boolean isMuted(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.con.prepareStatement("SELECT Mute FROM Bans WHERE UUID=?;");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getBoolean("Mute");
			} else {
				return false;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return true;
		}
	}
	
}

package de.web.bungeeplugin.systemtimoliaapply.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
	
	public static Connection con;
	
	/**
	 * 
	 * Connect to MySQL Server
	 * 
	 * @param host
	 * @param user
	 * @param password
	 */
	public static void connect(String host, String user, String password) {
		if (isConnected()) {
			System.out.println("[BungeeSystem][MySQL] Tried to connect whilst already connected");
		} else {
			try {
				con = DriverManager.getConnection(host, user, password);
				System.out.println("[BungeeSystem][MySQL] Connected to " + host);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("[BungeeSystem][MySQL] Failed to connect to " + host);
			}
		}
	}
	
	/**
	 * Disconnect from Server is there is a Connection
	 */
	public static void disconnect() {
		if (isConnected()) {
			try {
				con.close();
				con = null;
				System.out.println("[BungeeSystem][MySQL] Disconnected");
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("[BungeeSystem][MySQL] Failed to disconnect");
			}
		} else {
			System.out.println("[BungeeSystem][MySQL] Tried to disconnect whilst not connected");
		}
	}
	
	public static boolean isConnected() {
		return (con != null);
	}
	
}

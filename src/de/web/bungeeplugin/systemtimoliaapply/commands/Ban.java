package de.web.bungeeplugin.systemtimoliaapply.commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import de.web.bungeeplugin.systemtimoliaapply.util.API;
import de.web.bungeeplugin.systemtimoliaapply.util.BanReason;
import de.web.bungeeplugin.systemtimoliaapply.util.MySQL;
import de.web.bungeeplugin.systemtimoliaapply.util.SystemCMD;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Ban extends SystemCMD {

	public Ban() {
		super("ban", "system.ban", "mute");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender s, String[] args) {
		if (s instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) s;
			
			if (args.length == 1) {
				//Send all possible ban reasons
				sendBanReasons(p);
				return;
			}
			
			if (args.length < 2) {
				//Show Syntax
				p.sendMessage(syntax + "<Spieler> <ID>");
				return;
			}
			
			int banid = 0;
			try {
				//Parse BanID
				banid = Integer.parseInt(args[1]);
			} catch (NumberFormatException ex) {
				sendBanReasons(p);
				return;
			}
			
			BanReason br = BanReason.getReason(banid);
			if (br == null) {
				//Send all possible ban reasons
				sendBanReasons(p);
				return;
			}
			
			ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[0]);
			UUID uuid;
			
			//Get UUID of Target-Player
			if ((t == null || !t.isConnected())) {
				try {
					PreparedStatement ps = MySQL.con.prepareStatement("SELECT UUID,Exempt FROM BanPermissions WHERE Name=?;");
					ps.setString(1, args[0].toLowerCase());
					ResultSet rs = ps.executeQuery();
					
					if (rs.next()) {
						//Check for some permissions to prevent the ban of admins
						if (rs.getBoolean("Exempt") && !p.hasPermission("system.ban.override")) {
							p.sendMessage(pre + "§cDu darfst diesen Spieler nicht bannen");
							return;
						}
						uuid = UUID.fromString(rs.getString("UUID"));
					} else {
						p.sendMessage(pre + "§cDer Spieler wurde nicht gefunden");
						return;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return;
				}
			} else {
				//Check for some permissions to prevent the ban of admins
				if (t.hasPermission("system.ban.exempt") && !p.hasPermission("system.ban.override")) {
					p.sendMessage(pre + "§cDu darfst diesen Spieler nicht bannen");
					return;
				}
				uuid = t.getUniqueId();
			}
			
			//Ban the player
			API.ban(uuid, args[0].toLowerCase(), br.getDisplay(), p.getName(), br.getTime(), br.getSeconds(), br.isMute());
			p.sendMessage(pre + "§a" + args[0] + " §7wurde §" + (br.isMute() ? "cgemutet" : "4gebannt"));
		} else {
			if (args.length == 1) {
				//Send all possible ban reasons
				sendBanReasons(s);
				return;
			}
			
			if (args.length < 2) {
				//Show Syntax
				s.sendMessage(syntax + "<Spieler> <ID>");
				return;
			}
			
			int banid = 0;
			try {
				//Parse BanID
				banid = Integer.parseInt(args[1]);
			} catch (NumberFormatException ex) {
				sendBanReasons(s);
				return;
			}
			
			BanReason br = BanReason.getReason(banid);
			if (br == null) {
				//Send all possible ban reasons
				sendBanReasons(s);
				return;
			}
			
			ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[0]);
			UUID uuid;
			
			//Get UUID of Target-Player
			if ((t == null || !t.isConnected())) {
				try {
					PreparedStatement ps = MySQL.con.prepareStatement("SELECT UUID FROM BanPermissions WHERE Name=?;");
					ps.setString(1, args[0].toLowerCase());
					ResultSet rs = ps.executeQuery();
					
					if (rs.next()) {
						uuid = UUID.fromString(rs.getString("UUID"));
					} else {
						s.sendMessage(pre + "§cDer Spieler wurde nicht gefunden");
						return;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return;
				}
			} else {
				uuid = t.getUniqueId();
			}
			
			//Actually ban/mute the player
			API.ban(uuid, args[0].toLowerCase(), br.getDisplay(), "§4Console", br.getTime(), br.getSeconds(), br.isMute());
			s.sendMessage(pre + "§a" + args[0] + " §7wurde §" + (br.isMute() ? "cgemutet" : "4gebannt"));
		}
	}
	
	@SuppressWarnings("deprecation")
	private void sendBanReasons(CommandSender s) {
		for (BanReason reason : BanReason.values()) {
			s.sendMessage("§8- §c" + reason.getId() + " §8| §c" + reason.getDisplay() + " §8> §4" + reason.getTime() + " §8| " + (reason.isMute() ? "§cMute" : "§4Ban"));
		}
	}

}

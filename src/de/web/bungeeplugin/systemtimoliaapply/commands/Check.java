package de.web.bungeeplugin.systemtimoliaapply.commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import de.web.bungeeplugin.systemtimoliaapply.util.API;
import de.web.bungeeplugin.systemtimoliaapply.util.BanInfo;
import de.web.bungeeplugin.systemtimoliaapply.util.MySQL;
import de.web.bungeeplugin.systemtimoliaapply.util.SystemCMD;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Check extends SystemCMD {

	public Check() {
		super("check", "system.check");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender s, String[] args) {
		if (s instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) s;
			if (args.length == 0) {
				p.sendMessage(syntax + "check <Spieler>");
				return;
			}
			UUID uuid;
			
			//Get UUID of Target
			try {
				PreparedStatement ps = MySQL.con.prepareStatement("SELECT UUID FROM BanPermissions WHERE Name=?;");
				ps.setString(1, args[0].toLowerCase());
				ResultSet rs = ps.executeQuery();
				
				if (rs.next()) {
					uuid = UUID.fromString(rs.getString("UUID"));
				} else {
					p.sendMessage(pre + "§cDer Spieler wurde nicht gefunden");
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}
			
			//Show Infos
			p.sendMessage(pre + "§5§m-----§r §dInfos §düber §d" + args[0] + " §5§m-----");
			if (API.isBanned(uuid)) {
				p.sendMessage(pre + "§7Gebannt§7: §aJa");
				p.sendMessage(pre + "§7Gemutet§7: §cNein");
			} else {
				p.sendMessage(pre + "§7Gebannt§7: §cNein");
				if (API.isMuted(uuid)) {
					p.sendMessage(pre + "§7Gemutet§7: §aJa");
				} else {
					p.sendMessage(pre + "§7Gemutet§7: §cNein");
					return;
				}
			}
			BanInfo info = API.getBanInfo(uuid);
			p.sendMessage(pre + "§7Von§7: §a" + info.getBannedBy());
			p.sendMessage(pre + "§7Grund§7: §a" + info.getReason());
			if (info.getEnd() == -1) {
				p.sendMessage(pre + "§7Verbleibend§7: §6PERMANENT");
			} else {
				p.sendMessage(pre + "§7Verbleibend§7: §a" + API.translateTime(info.getEnd()));
			}
		} else {
			if (args.length == 0) {
				s.sendMessage(syntax + "check <Spieler>");
				return;
			}
			UUID uuid;
			
			//Get UUID of Target
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
			
			s.sendMessage(pre + "§5§m-----§r §dInfos §düber §d" + args[0] + " §5§m-----");
			if (API.isBanned(uuid)) {
				s.sendMessage(pre + "§7Gebannt§7: §aJa");
				s.sendMessage(pre + "§7Gemutet§7: §cNein");
			} else {
				s.sendMessage(pre + "§7Gebannt§7: §cNein");
				if (API.isMuted(uuid)) {
					s.sendMessage(pre + "§7Gemutet§7: §aJa");
				} else {
					s.sendMessage(pre + "§7Gemutet§7: §cNein");
					return;
				}
			}
			BanInfo info = API.getBanInfo(uuid);
			s.sendMessage(pre + "§7Von§7: §a" + info.getBannedBy());
			s.sendMessage(pre + "§7Grund§7: §a" + info.getReason());
			if (info.getEnd() == -1) {
				s.sendMessage(pre + "§7Verbleibend§7: §6PERMANENT");
			} else {
				s.sendMessage(pre + "§7Verbleibend§7: §a" + API.translateTime(info.getEnd()));
			}
		}
	}

}

package de.web.bungeeplugin.systemtimoliaapply.commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import de.web.bungeeplugin.systemtimoliaapply.util.API;
import de.web.bungeeplugin.systemtimoliaapply.util.MySQL;
import de.web.bungeeplugin.systemtimoliaapply.util.SystemCMD;
import net.md_5.bungee.api.CommandSender;

public class Unban extends SystemCMD {

	public Unban() {
		super("unban", "system.unban", "unmute");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender s, String[] args) {
		UUID uuid;
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
		
		if (!API.isBanned(uuid) && !API.isMuted(uuid)) {
			s.sendMessage(pre + "§cDieser Spieler ist nicht gebannt");
			return;
		}
		
		API.unban(uuid);
		s.sendMessage(pre + "§a" + args[0] + " §7wurde entbannt");
	}

}

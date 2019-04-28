package de.web.bungeeplugin.systemtimoliaapply.listener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.web.bungeeplugin.systemtimoliaapply.commands.Report;
import de.web.bungeeplugin.systemtimoliaapply.main.Main;
import de.web.bungeeplugin.systemtimoliaapply.util.API;
import de.web.bungeeplugin.systemtimoliaapply.util.BanInfo;
import de.web.bungeeplugin.systemtimoliaapply.util.MySQL;
import de.web.bungeeplugin.systemtimoliaapply.util.SystemCMD;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventListener implements Listener {
	
	@EventHandler
	public void onJoin(PostLoginEvent e) {
		ProxiedPlayer p = e.getPlayer();
		
		ProxyServer.getInstance().getScheduler().schedule(Main.instance, new Runnable() {

			@Override
			public void run() {
				//Send Report Status Message if has permissions
				if (p.hasPermission("bsys.report.log")) {
					TextComponent t1 = new TextComponent(Report.reportPre + "§7Status: ");
					TextComponent t2;
					if (API.reportlog.contains(p.getUniqueId())) {
						t2 = new TextComponent("§aEingeloggt");
						t2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								new ComponentBuilder("§7Klicke um dich §causzuloggen").create()));
						t2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report logout"));
					} else {
						t2 = new TextComponent("§cAusgeloggt");
						t2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								new ComponentBuilder("§7Klicke um dich §aeinzuloggen").create()));
						t2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report login"));
					}
					t1.addExtra(t2);
					p.sendMessage(t1);
				}
				
				try {
					//Update Name and Permissions in database
					PreparedStatement ps = MySQL.con.prepareStatement("SELECT Name FROM BanPermissions WHERE UUID=?;");
					ps.setString(1, p.getUniqueId().toString());
					ResultSet rs = ps.executeQuery();
					
					if (rs.next()) {
						PreparedStatement ps1 = MySQL.con.prepareStatement("UPDATE BanPermissions SET Exempt=? AND Name=? WHERE UUID=?;");
						ps1.setBoolean(1, p.hasPermission("system.ban.exempt"));
						ps1.setString(2, p.getName().toLowerCase());
						ps1.setString(3, p.getUniqueId().toString());
						ps1.executeUpdate();
					} else {
						PreparedStatement ps1 = MySQL.con.prepareStatement("INSERT INTO BanPermissions VALUES (?, ?, ?);");
						ps1.setString(1, p.getUniqueId().toString());
						ps1.setString(2, p.getName().toLowerCase());
						ps1.setBoolean(3, p.hasPermission("system.ban.exempt"));
						ps1.executeUpdate();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}, 1, TimeUnit.SECONDS);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onLogin(LoginEvent e) {
		UUID uuid = e.getConnection().getUniqueId();
		if (API.isBanned(uuid)) {
			BanInfo info = API.getBanInfo(uuid);
			
			if (info.getEnd() == -1) {
				e.setCancelReason("§7Du wurdest §6" + info.getTime() + " §7gebannt§r\n§r\n§r§7Grund: §a" + info.getReason() + "§r\n§r\n§r§7Du kannst auf §atimolia.de§r\n§r§7einen Entbannungsantrag stellen");
			} else {
				e.setCancelReason("§7Du wurdest für §a" + info.getTime() + " §7gebannt§r\n§r\n§r§7Grund: §a" + info.getReason() + "§r\n§r\n§r§7Verbleibende Zeit: §a" + API.translateTime(info.getEnd()) + "§r\n§r\n§r§7Du kannst auf §atimolia.de§r\n§r§7einen Entbannungsantrag stellen");
			}
			e.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChat(ChatEvent e) {
		ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		
		if (API.isMuted(p.getUniqueId())) {
			BanInfo info = API.getBanInfo(p.getUniqueId());
			p.sendMessage(SystemCMD.pre + "§7Du bist gemutet");
			p.sendMessage(SystemCMD.pre + "§7Grund: §a" + info.getReason());
			p.sendMessage(SystemCMD.pre + "§7Verbleibend: §a" + API.translateTime(info.getEnd()));
			p.sendMessage(SystemCMD.pre + "§7Du kannst auf §atimolia.de §7einen Entmuteantrag stellen");
			
			e.setCancelled(true);
		}
	}
}

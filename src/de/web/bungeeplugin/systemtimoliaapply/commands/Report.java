package de.web.bungeeplugin.systemtimoliaapply.commands;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.web.bungeeplugin.systemtimoliaapply.main.Main;
import de.web.bungeeplugin.systemtimoliaapply.util.API;
import de.web.bungeeplugin.systemtimoliaapply.util.ReportData;
import de.web.bungeeplugin.systemtimoliaapply.util.ReportReason;
import de.web.bungeeplugin.systemtimoliaapply.util.SystemCMD;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Report extends SystemCMD {
	
	public static final String reportPre = "§4Report §8» §7";

	public Report() {
		super("report");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender s, String[] args) {
		if (s instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) s;
			
			if (args.length == 1) {
				//Check if player has permission to login/logout
				if ((args[0].equalsIgnoreCase("login") || args[0].equalsIgnoreCase("logout")) && p.hasPermission("system.report.log")) {
					if (args[0].equalsIgnoreCase("login")) {
						if (API.reportlog.contains(p.getUniqueId())) {
							p.sendMessage(reportPre + "§cDu bist bereits eingeloggt");
							return;
						}
						API.reportlog.add(p.getUniqueId());
						p.sendMessage(reportPre + "§7Du bist nun §aeingeloggt");
						return;
					} else if (args[0].equalsIgnoreCase("logout")) {
						if (!API.reportlog.contains(p.getUniqueId())) {
							p.sendMessage(reportPre + "§cDu bist nicht eingeloggt");
							return;
						}
						API.reportlog.remove(p.getUniqueId());
						p.sendMessage(reportPre + "§7Du bist nun §causgeloggt");
						return;
					}
				}
				
				//If no permission list all possible report reasons
				listReasons(p, args[0]);
				return;
			}
			
			if (args.length == 2 && args[0].equalsIgnoreCase("check") && p.hasPermission("system.report.check")) { //Check if player has Reportcheck permissions
				UUID uuid;
				try {
					uuid = UUID.fromString(args[1]);
				} catch (Exception ex) {
					p.sendMessage(reportPre + "§cDieser Report ist ungültig");
					return;
				}
				
				if (!API.reports.containsKey(uuid)) {
					p.sendMessage(reportPre + "§cEs wird sich bereits um diesen Spieler gekümmert");
					return;
				}
				
				ReportData report = API.reports.get(uuid);
				
				ProxiedPlayer t = ProxyServer.getInstance().getPlayer(report.getAbout());
				if (t == null || !t.isConnected()) {
					p.sendMessage(notOnline);
					return;
				}
				
				ServerInfo server = t.getServer().getInfo();
				ProxyServer.getInstance().getPlayer(report.getFrom()).sendMessage(reportPre + "§7Ein Teammitglied kümmert sich jetzt um deinen Report über §a" + t.getDisplayName());
				p.sendMessage(pre + "§7Verbinde zu §a" + server.getName() + "§7...");
				p.connect(server);
				return;
			}
			
			if (args.length != 2) {
				p.sendMessage(syntax + "<Spieler> <Grund>");
				return;
			}
			
			ReportReason reason = ReportReason.getByName(args[1]);
			
			if (reason == null) {
				//List all possible report reasons of an non-existant reason was specified
				listReasons(p, args[0]);
				return;
			}

			ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[0]);
			
			if (t == null || !t.isConnected()) {
				p.sendMessage(notOnline);
				return;
			}
			
			if (p.getUUID().equalsIgnoreCase(t.getUUID())) {
				p.sendMessage(reportPre + "§cDu kannst dich nicht selbst reporten");
				return;
			}
			
			UUID uuid = p.getUniqueId();
			ReportData report = new ReportData(uuid, t.getUniqueId(), t.getName(), t.getDisplayName(), reason);
			API.reports.put(uuid, report);
			
			p.sendMessage(reportPre + "§7Du hast §a" + t.getName() + " §7für §a" + report.getReason().getShow() + " §7reportet");
			p.sendMessage(reportPre + "§7Ein Teammitglied wird sich in Kürze darum kümmern");
			
			TextComponent msg = new TextComponent(reportPre + t.getName() + " §8§l┃§3 " + report.getReason().getShow());
			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Von: §a" + p.getDisplayName() + "\n§7Gemeldet: §a" + t.getDisplayName() + "\n§7Grund: §a" + report.getReason().getShow()).create()));
			msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report check " + p.getUniqueId().toString()));
			
			for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
				if (API.reportlog.contains(all.getUniqueId())) {
					all.sendMessage(msg);
				}
			}

			ProxyServer.getInstance().getScheduler().schedule(Main.instance, new Runnable() {
				
				@Override
				public void run() {
					API.reports.remove(uuid);
				}
			}, 5, TimeUnit.MINUTES);
		} else {
			s.sendMessage("§cNur Spieler dürfen diesen Command verwenden");
		}
	}
	
	private void listReasons(CommandSender sender, String name) {
		for (ReportReason reason : ReportReason.values()) {
			TextComponent t1 = new TextComponent("§8- ");
			TextComponent t2 = new TextComponent("§a" + reason.getUsualInput());
			
			t2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report " + name + " " + reason.getUsualInput()));
			
			t1.addExtra(t2);
			
			sender.sendMessage(t1);
		}
	}

}

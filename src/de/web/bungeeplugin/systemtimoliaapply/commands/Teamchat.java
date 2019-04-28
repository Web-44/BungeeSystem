package de.web.bungeeplugin.systemtimoliaapply.commands;

import de.web.bungeeplugin.systemtimoliaapply.util.SystemCMD;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Teamchat extends SystemCMD {

	public Teamchat() {
		super("teamchat", "system.teamchat", "tc");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender s, String[] args) {
		if (s instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) s;
			if (args.length == 0) {
				p.sendMessage(syntax + "teamchat <Nachricht>");
				return;
			}
			StringBuilder msgBuilder = new StringBuilder();
			for (String a : args) {
				msgBuilder.append(a + " "); //Append all arguments to message
			}
			String msg = "§2TeamChat §8┃ §c" + p.getName() + " §8» §f§l" + msgBuilder.toString();
			if (p.hasPermission("system.teamchat.color")) {
				msg = ChatColor.translateAlternateColorCodes('&', msg);
			}
			//p.sendMessage(msg);
			for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
				/*if (all.getName().equalsIgnoreCase(p.getName())) {
					continue;
				}*/
				if (all.hasPermission(getPermission())) {
					all.sendMessage(msg); //Send message if player has system.teamchat
				}
			}
		} else {
			if (args.length == 0) {
				s.sendMessage(syntax + "teamchat <Nachricht>");
				return;
			}
			StringBuilder msgBuilder = new StringBuilder();
			for (String a : args) {
				msgBuilder.append(a + " "); //Append all arguments to message
			}
			String msg = "§2TeamChat §8┃ §4Console §8» §f§l" + ChatColor.translateAlternateColorCodes('&', msgBuilder.toString());

			for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
				if (all.hasPermission(getPermission())) {
					all.sendMessage(msg); //Send message if player has system.teamchat
				}
			}
		}
	}

}

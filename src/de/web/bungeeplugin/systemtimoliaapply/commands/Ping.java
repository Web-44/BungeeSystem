package de.web.bungeeplugin.systemtimoliaapply.commands;

import de.web.bungeeplugin.systemtimoliaapply.util.SystemCMD;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Ping extends SystemCMD {

	public Ping() {
		super("ping");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender s, String[] args) {
		if (s instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) s;
			if (args.length == 0 || !p.hasPermission("system.ping.other")) {
				//Show own ping
				p.sendMessage(pre + "Dein Ping: §a" + p.getPing() + "ms");
			} else {
				//Show ping of other players
				ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[0]);
				if (t == null || !t.isConnected()) {
					p.sendMessage(notOnline);
					return;
				}
				p.sendMessage(pre + "Ping von §a" + t.getDisplayName() + "§7: §a" + t.getPing() + "ms");
			}
		} else {
			if (args.length == 0) {
				s.sendMessage(syntax + "<Name>");
			} else {
				//Show ping of other players
				ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[0]);
				if (t == null || !t.isConnected()) {
					s.sendMessage(notOnline);
					return;
				}
				s.sendMessage(pre + "Ping von §a" + t.getDisplayName() + "§7: §a" + t.getPing() + "ms");
			}
		}
	}

}

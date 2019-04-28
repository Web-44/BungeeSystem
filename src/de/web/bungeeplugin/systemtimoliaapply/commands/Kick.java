package de.web.bungeeplugin.systemtimoliaapply.commands;

import de.web.bungeeplugin.systemtimoliaapply.util.SystemCMD;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Kick extends SystemCMD {

	public Kick() {
		super("kick", "system.kick");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender s, String[] args) {
		if (s instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) s;
			if (args.length == 0) {
				p.sendMessage(syntax + "<Spieler>");
				return;
			}
			
			ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[0]);
			if (t == null || !t.isConnected()) {
				p.sendMessage(notOnline);
				return;
			}
			
			t.disconnect("§7Du wurdest von §a" + p.getName() + " §7gekickt");
		} else {
			if (args.length == 0) {
				s.sendMessage(syntax + "<Spieler>");
				return;
			}
			
			ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[0]);
			if (t == null || !t.isConnected()) {
				s.sendMessage(notOnline);
				return;
			}
			
			t.disconnect("§7Du wurdest von §4Console §7gekickt");
		}
	}

}

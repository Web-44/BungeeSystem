package de.web.bungeeplugin.systemtimoliaapply.commands;

import de.web.bungeeplugin.systemtimoliaapply.util.SystemCMD;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Jumpto extends SystemCMD {

	public Jumpto() {
		super("jumpto");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender s, String[] args) {
		if (s instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) s;
			if (args.length != 1) {
				p.sendMessage(syntax + "jumpto <Spieler>");
				return;
			}
			
			ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[0]);
			if (t == null || !t.isConnected()) {
				p.sendMessage(notOnline);
				return;
			}
			
			//Connect to server of target
			ServerInfo server = t.getServer().getInfo();
			p.sendMessage(pre + "§7Verbinde zu §a" + server.getName() + "§7...");
			p.connect(server);
		} else {
			s.sendMessage("§cSyntax: /send <Spieler> <Server>");
		}
	}

}

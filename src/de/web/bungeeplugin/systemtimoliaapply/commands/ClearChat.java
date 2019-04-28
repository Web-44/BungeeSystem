package de.web.bungeeplugin.systemtimoliaapply.commands;

import de.web.bungeeplugin.systemtimoliaapply.util.SystemCMD;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ClearChat extends SystemCMD {

	public ClearChat() {
		super("clearchat", "system.chatclear", "cc");
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender s, String[] args) {
		for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
			//Send messages to players without bypass permission
			if (!all.hasPermission("system.chatclear.exempt")) {
				//Spam the chat
				for (int i = 0; i < 150; i++) {
					all.sendMessage(" ");
				}
			}
		}
		if (s instanceof ProxiedPlayer) {
			ProxyServer.getInstance().broadcast(pre + "Der Chat wurde von §a" + s.getName() + " §7geleert");
		} else {
			ProxyServer.getInstance().broadcast(pre + "Der Chat wurde von §4Console §7geleert");
		}
	}

}

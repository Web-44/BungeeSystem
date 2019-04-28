package de.web.bungeeplugin.systemtimoliaapply.commands;

import de.web.bungeeplugin.systemtimoliaapply.util.NumberWord;
import de.web.bungeeplugin.systemtimoliaapply.util.SystemCMD;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class List extends SystemCMD {

	public List() {
		super("list");
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender s, String[] args) {
		int online = ProxyServer.getInstance().getOnlineCount();
		NumberWord nw = NumberWord.getByInteger(online);
		String in = "";
		if (nw == null) {
			in = " sind §a" + online + " §7";
		} else {
			if (nw.isSingular()) {
				in = " ist §a" + nw.getSingular() + " §7";
			} else {
				in = " sind §a" + nw.getWord() + " §7";
			}
		}
		s.sendMessage(pre + "Derzeit" + in + "Spieler online");
	}
	
}

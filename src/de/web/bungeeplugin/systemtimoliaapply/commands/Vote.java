package de.web.bungeeplugin.systemtimoliaapply.commands;

import de.web.bungeeplugin.systemtimoliaapply.util.SystemCMD;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Vote extends SystemCMD {
	
	public Vote() {
		super("vote");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		//Clickable Components: Vote hier für uns
		TextComponent t1 = new TextComponent(pre + "Vote ");
		TextComponent t2 = new TextComponent("§ahier");
		TextComponent t3 = new TextComponent(" §7für uns");
		
		t2.setUnderlined(true);
		t2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aKlicke um zu voten!").create()));
		t2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://minecraft-mp.com/server/100252/vote/"));
		
		t1.addExtra(t2);
		t1.addExtra(t3);
		
		//Send Component
		sender.sendMessage(t1);
	}
}

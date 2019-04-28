package de.web.bungeeplugin.systemtimoliaapply.commands;

import de.web.bungeeplugin.systemtimoliaapply.util.SystemCMD;
import net.md_5.bungee.api.CommandSender;

public class Help extends SystemCMD {

	public Help() {
		super("help");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender s, String[] args) {
		s.sendMessage("§7Dieses Plugin dient allein zur Feststellung meiner Programmierkenntnisse");
		s.sendMessage("§7und wurde speziell für die Bewerbung bei §atimolia.de §7entwickelt.");
		s.sendMessage("§7Ich §8(§aWeb44§8) §7bin hiervon der alleinige Programmierer!");
		s.sendMessage(" ");
		s.sendMessage("§7Befehle des Plugins:");
		s.sendMessage("§a/ban §8- §7Bannt/Mutet Spieler");
		s.sendMessage("§a/check §8- §7Zeigt Infos zum Bann eines Spielers an");
		s.sendMessage("§a/clearchat §8- §7Leert den Chat");
		s.sendMessage("§a/help §8- §7Zeigt diese Hilfe an");
		s.sendMessage("§a/jumpto §8- §7Sendet dich auf den Server des Spielers");
		s.sendMessage("§a/kick §8- §7Kickt Spieler vom Netzwerk");
		s.sendMessage("§a/list §8- §7Zeigt die Anzahl der Spieler auf dem Netzwerk");
		s.sendMessage("§a/ping §8- §7Zeigt den eigenen Ping oder den Ping anderer Spieler");
		s.sendMessage("§a/report §8- §7Melde Spieler / Logge dich ein");
		s.sendMessage("§a/teamchat §8- §7Sende eine Nachricht an dein Team");
		s.sendMessage("§a/unban §8- §7Entbanne/Entmute Spieler");
		s.sendMessage("§a/vote §8- §7Zeigt den Votelink an");
	}

}

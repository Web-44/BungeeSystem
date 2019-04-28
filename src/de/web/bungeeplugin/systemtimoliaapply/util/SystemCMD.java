package de.web.bungeeplugin.systemtimoliaapply.util;

import net.md_5.bungee.api.plugin.Command;

//Class to ease usage of some variables
public abstract class SystemCMD extends Command {
	
	public static final String pre = "§2Timolia §8» §7",
			notOnline = pre + "§cDieser Spieler ist offline";

	//Syntax: /<command> 
	public String syntax;

	public SystemCMD(String name) {
		super(name);
		syntax = "§cSyntax: /" + name + " ";
	}
	
	public SystemCMD(String name, String permission, String... aliases) {
		super(name, permission, aliases);
		syntax = "§cSyntax: /" + name + " ";
	}
	
}

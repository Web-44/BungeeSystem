package de.web.bungeeplugin.systemtimoliaapply.main;

import de.web.bungeeplugin.systemtimoliaapply.commands.*;
import de.web.bungeeplugin.systemtimoliaapply.listener.EventListener;
import de.web.bungeeplugin.systemtimoliaapply.util.API;
import de.web.bungeeplugin.systemtimoliaapply.util.MySQL;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class Main extends Plugin {
	
	public static Main instance;
	
	public void onEnable() {
		instance = this;
		
		//Load Configs and connect MySQL
		API.init();
		
		//Register Commands
		PluginManager pm = ProxyServer.getInstance().getPluginManager();
		pm.registerCommand(this, new Ban());
		pm.registerCommand(this, new Check());
		pm.registerCommand(this, new ClearChat());
		pm.registerCommand(this, new Help());
		pm.registerCommand(this, new Jumpto());
		pm.registerCommand(this, new Kick());
		pm.registerCommand(this, new List());
		pm.registerCommand(this, new Ping());
		pm.registerCommand(this, new Report());
		pm.registerCommand(this, new Teamchat());
		pm.registerCommand(this, new Unban());
		pm.registerCommand(this, new Vote());
		
		//Register EventListener
		pm.registerListener(this, new EventListener());
	}
	
	public void onDisable() {
		//Disconnect
		MySQL.disconnect();
	}
	
}

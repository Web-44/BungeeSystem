package de.web.bungeeplugin.systemtimoliaapply.util;

public enum ReportReason {
	
	HACKING("hacking", "Hacking", "Hacking"),
	BUGUSING("bugusing", "Bugusing", "Bugusing"),
	TEAMING("teaming", "Teaming", "Teaming"),
	SPAWNTRAP("spawntrapping", "Spawntrapping", "Spawn Trapping"),
	CHAT("chatverhalten", "Chatverhalten", "Chat Verhalten"),
	SKIN("skin", "Skin", "Unangebrachter Skin"),
	NAME("name", "Name", "Unangebrachter Name"),
	RECHTE("rechte-ausnutzung", "Rechte-Ausnutzung", "Rechte Ausnutzung");
	
	private String name;
	private String show;
	private String usualInput;
	
	private ReportReason(String name, String usualInput, String show) {
		this.name = name;
		this.usualInput = usualInput;
		this.show = show;
	}
	
	public String getName() {
		return name;
	}
	
	public String getShow() {
		return show;
	}
	
	public String getUsualInput() {
		return usualInput;
	}
	
	public static ReportReason getByShow(String show) {
		for (ReportReason reason : values()) {
			if (show.equalsIgnoreCase(reason.show)) {
				return reason;
			}
		}
		return null;
	}
	
	public static ReportReason getByName(String name) {
		for (ReportReason reason : values()) {
			if (name.equalsIgnoreCase(reason.name)) {
				return reason;
			}
		}
		return null;
	}
}

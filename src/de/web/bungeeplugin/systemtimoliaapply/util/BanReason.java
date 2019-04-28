package de.web.bungeeplugin.systemtimoliaapply.util;

public enum BanReason {
	
	HACKING(1, "Hacking", 2592000, "30 Tage", false),
	TEAMING(2, "Teaming", 864000, "10 Tage", false),
	WERBUNG(3, "Werbung", 432000, "5 Tage", true),
	SKIN(4, "Skin/Cosmetics", 259200, "3 Tage", false),
	NAME(5, "Name", 2592000,  "30 Tage", false),
	WORTWAHL(6, "Wortwahl", 86400, "1 Tag", true),
	SPAM(7, "Spamming", 86400, "1 Tag", true),
	BANNUMGEHUNG(8, "Bannumgehung", -1, "PERMANENT", false),
	EXTREM(9, "Extrem", -1, "PERMANENT", false);
	
	private int id;
	private String display, time;
	private long seconds;
	private boolean mute;
	
	private BanReason(int id, String display, long seconds, String time, boolean mute) {
		this.id = id;
		this.display = display;
		this.seconds = seconds;
		this.time = time;
		this.mute = mute;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public int getId() {
		return id;
	}
	
	public long getSeconds() {
		return seconds;
	}
	
	public String getTime() {
		return time;
	}
	
	public boolean isMute() {
		return mute;
	}
	
	public static BanReason getReason(String reason) {
		for (BanReason br : values()) {
			if (br.display.equalsIgnoreCase(reason)) {
				return br;
			}
		}
		return null;
	}
	
	public static BanReason getReason(int banid) {
		for (BanReason br : values()) {
			if (br.id == banid) {
				return br;
			}
		}
		return null;
	}
	
}

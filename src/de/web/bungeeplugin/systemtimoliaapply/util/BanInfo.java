package de.web.bungeeplugin.systemtimoliaapply.util;

import java.util.UUID;

public class BanInfo {
	
	private UUID uuid;
	private String name, reason, BannedBy, time;
	private long end;
	private boolean mute;
	
	public BanInfo(UUID uuid, String name, String reason, long end, String time, String BannedBy, boolean mute) {
		this.uuid = uuid;
		this.name = name;
		this.reason = reason;
		this.end = end;
		this.BannedBy = BannedBy;
		this.mute = mute;
		this.time = time;
	}
	
	public String getTime() {
		return time;
	}
	
	public String getBannedBy() {
		return BannedBy;
	}
	
	public long getEnd() {
		return end;
	}
	
	public String getName() {
		return name;
	}
	
	public String getReason() {
		return reason;
	}
	
	public UUID getUniqueId() {
		return uuid;
	}
	
	public boolean isMute() {
		return mute;
	}
}

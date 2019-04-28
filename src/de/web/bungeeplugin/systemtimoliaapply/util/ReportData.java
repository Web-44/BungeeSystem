package de.web.bungeeplugin.systemtimoliaapply.util;

import java.util.UUID;

//Class to save all Data of a report
public class ReportData {
	
	private UUID from;
	private ReportReason reason;
	private UUID about;
	private String name;
	private String displayName;
	
	public ReportData(UUID from, UUID about, String targetName, String targetDisplayName, ReportReason reason) {
		this.about = about;
		this.from = from;
		this.reason = reason;
		this.name = targetName;
		this.displayName = targetDisplayName;
	}
	
	public ReportReason getReason() {
		return reason;
	}
	
	public UUID getFrom() {
		return from;
	}

	public UUID getAbout() {
		return about;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}

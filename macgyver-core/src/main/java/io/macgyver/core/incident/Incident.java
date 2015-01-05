package io.macgyver.core.incident;

public interface Incident {

	public String getIncidentKey();
	public String getDescription();
	
	public boolean isOpen();
	public boolean isResolved();
	public boolean isAcknowledged();
}

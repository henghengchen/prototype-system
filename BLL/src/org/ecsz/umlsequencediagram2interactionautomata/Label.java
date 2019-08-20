package org.ecsz.umlsequencediagram2interactionautomata;

import org.ecsz.sequencediagram.OccurrenceSpecification;

public class Label {

	private OccurrenceSpecification event;
	private String guard;
	private String action;
	
	public Label() {
		// TODO Auto-generated constructor stub
	}

	public OccurrenceSpecification getEvent() {
		return event;
	}

	public void setEvent(OccurrenceSpecification event) {
		this.event = event;
	}

	public String getGuard() {
		return guard;
	}

	public void setGuard(String guard) {
		this.guard = guard;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}

package org.ecsz.umlsequencediagram2interactionautomata;

import org.ecsz.sequencediagram.OccurrenceSpecification;

public class TransitionRelation {

	private State original_state;
	private OccurrenceSpecification event;
	private String guard;
	private String action;
	private State target_state;
	
	public TransitionRelation() {
		// TODO Auto-generated constructor stub
		//this.event=new OccurrenceSpecification();
	}

	public State getOriginal_state() {
		return original_state;
	}

	public void setOriginal_state(State original_state) {
		this.original_state = original_state;
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

	public State getTarget_state() {
		return target_state;
	}

	public void setTarget_state(State target_state) {
		this.target_state = target_state;
	}

}

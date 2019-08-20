package org.ecsz.umlsequencediagram2interactionautomata;

public class State {

	private String name;
	private boolean isInitial;
	private boolean isAccepting;
	private Phase phase;
	
	public Phase getPhase() {
		return phase;
	}

	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	public boolean isInitial() {
		return isInitial;
	}

	public void setInitial(boolean isInitial) {
		this.isInitial = isInitial;
	}

	public boolean isAccepting() {
		return isAccepting;
	}

	public void setAccepting(boolean isAccepting) {
		this.isAccepting = isAccepting;
	}

	public State(String name,Phase p) {
		// TODO Auto-generated constructor stub
		this.name=name;
		this.phase=p;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

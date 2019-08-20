package org.ecsz.umlstatediagram2hautomata;

import java.util.ArrayList;
import java.util.List;

public class StateAndPath {

	private List<String> path;
	private State state;
	
	public StateAndPath() {
		this.path=new ArrayList<String>();
		this.state=null;
		// TODO Auto-generated constructor stub
	}
	public List<String> getPath() {
		return path;
	}
	public void setPath(List<String> path) {
		this.path = path;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	
	
}

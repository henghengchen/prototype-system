package org.ecsz.statediagram;

import java.util.HashSet;

public class StateMachine {

	private String statemachine_name;
	private String statemachine_id;
	private HashSet<Region> hs_region;

	public StateMachine() {
		this.statemachine_name=null;
		this.statemachine_id=null;
		this.hs_region=new HashSet<Region>();
		// TODO Auto-generated constructor stub
	}

	public String getname() {
		return statemachine_name;
	}

	public void setname(String statemachine_name) {
		this.statemachine_name = statemachine_name;
	}

	public HashSet<Region> getregionsSet() {
		return hs_region;
	}

	public String getid() {
		return statemachine_id;
	}

	public void setid(String statemachine_id) {
		this.statemachine_id = statemachine_id;
	}
	
	
} 

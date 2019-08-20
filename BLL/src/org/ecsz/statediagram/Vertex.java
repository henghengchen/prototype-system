package org.ecsz.statediagram;

import java.util.HashSet;

public class Vertex {

	private String vertex_name;
	private String vertex_id;
	private String type;
	private String owner_region_id;
	//private HashSet<Transition> outgoing_transition;
	//private HashSet<Transition> incoming_transition;
	
	public Vertex() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getname() {
		return vertex_name;
	}

	public void setname(String state_name) {
		this.vertex_name = state_name;
	}

	public String getid() {
		return vertex_id;
	}

	public void setid(String state_id) {
		this.vertex_id = state_id;
	}

	public String getOwner_region_id() {
		return owner_region_id;
	}

	public void setOwner_region_id(String owner_region_id) {
		this.owner_region_id = owner_region_id;
	}
	
	
}

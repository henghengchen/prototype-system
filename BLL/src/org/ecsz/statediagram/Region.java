package org.ecsz.statediagram;

import java.util.HashSet;

public class Region {
	
	private String region_name;
	private String region_id;
	private String owner_vertex_id;
	private HashSet<Vertex> hs_vertex;
	private HashSet<Transition> hs_outgoing_transition;
	
	public Region() {
		this.region_id=null;
		this.region_name=null;
		this.owner_vertex_id=null;
		this.hs_vertex=new HashSet<Vertex>();
		this.hs_outgoing_transition=new HashSet<Transition>();
		// TODO Auto-generated constructor stub
	}

	public String getname() {
		return region_name;
	}

	public void setname(String region_name) {
		this.region_name = region_name;
	}

	public String getid() {
		return region_id;
	}

	public void setid(String region_id) {
		this.region_id = region_id;
	}

	public String getOwner_vertex_id() {
		return owner_vertex_id;
	}

	public void setOwner_vertex_id(String owner_vertex_id) {
		this.owner_vertex_id = owner_vertex_id;
	}

	public HashSet<Vertex> getvertexsSet() {
		return hs_vertex;
	}

	public HashSet<Transition> getoutgoingtransitionsSet() {
		return hs_outgoing_transition;
	}
	
	
}

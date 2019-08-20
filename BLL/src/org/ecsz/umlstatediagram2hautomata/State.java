package org.ecsz.umlstatediagram2hautomata;

import java.util.HashSet;

public class State {

	private String state_name;
	private String state_id;
	private String type;
	private SAutoMata up_sautomata;
	private HashSet<SAutoMata> hs_nested_sautomata;
	
	public State() {
		this.hs_nested_sautomata=new HashSet<SAutoMata>();
		this.state_name=null;
		// TODO Auto-generated constructor stub
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public HashSet<SAutoMata> getHs_nested_sautomata() {
		return hs_nested_sautomata;
	}

	public String getname() {
		return state_name;
	}

	public void setname(String state_name) {
		this.state_name = state_name;
	}

	public String getid() {
		return state_id;
	}

	public void setid(String state_id) {
		this.state_id = state_id;
	}

	public SAutoMata getUp_sautomata() {
		return up_sautomata;
	}

	public void setUp_sautomata(SAutoMata up_sautomata) {
		this.up_sautomata = up_sautomata;
	}

}

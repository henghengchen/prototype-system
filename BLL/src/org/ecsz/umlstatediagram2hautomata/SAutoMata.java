package org.ecsz.umlstatediagram2hautomata;

import java.util.HashSet;
import java.util.Iterator;

public class SAutoMata {
 
	private String sautomata_name;
	private String sautomata_id;
	private HashSet<State> hs_state;
	private State initial_state;
	private State up_state;
	private HashSet<TransitionLabel> hs_transition_label;
	private HashSet<TransitionRelation> hs_transition_relation;
	
	public SAutoMata() {
		this.sautomata_name=null;
		this.hs_state=new HashSet<State>();
		this.initial_state=null;
		this.up_state=null;
		this.hs_transition_label=new HashSet<TransitionLabel>();
		this.hs_transition_relation=new HashSet<TransitionRelation>();
		// TODO Auto-generated constructor stub
	}

	public String getname() {
		return sautomata_name;
	}

	public void setname(String sautomata_name) {
		this.sautomata_name = sautomata_name;
	}

	public String getid() {
		return sautomata_id;
	}

	public void setid(String sautomata_id) {
		this.sautomata_id = sautomata_id;
	}

	public HashSet<State> getHs_state() {
		return hs_state;
	}

	public State getInitial_state() {
		return initial_state;
	}

	public void setInitial_state(State initial_state) {
		this.initial_state = initial_state;
	}

	public State getStateById(String id) {
		for(Iterator<State> it=this.hs_state.iterator();it.hasNext();) {
			State state=it.next();
			if(state.getid().equals(id)) {
				return state;
			}
		}
		return null;
	}
	
	public State getUp_state() {
		return up_state;
	}

	public void setUp_state(State up_state) {
		this.up_state = up_state;
	}

	public HashSet<TransitionLabel> getHs_transition_label() {
		return hs_transition_label;
	}

	public HashSet<TransitionRelation> getHs_transition_relation() {
		return hs_transition_relation;
	}

}

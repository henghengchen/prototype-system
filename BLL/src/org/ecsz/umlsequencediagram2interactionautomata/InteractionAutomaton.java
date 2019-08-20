package org.ecsz.umlsequencediagram2interactionautomata;

import java.util.ArrayList;
import java.util.List;

public class InteractionAutomaton {

	private List<State> state_list;
	private List<Counter> counter_list;
	private List<TransitionRelation> transitionrelation_list;
	private State initial_state;
	private List<State> accepting_state_list;
	private List<State> recurrence_state_list;
	
	public InteractionAutomaton() {
		// TODO Auto-generated constructor stub
		this.state_list=new ArrayList<State>();
		this.counter_list=new ArrayList<Counter>();
		this.transitionrelation_list=new ArrayList<TransitionRelation>();
		this.accepting_state_list=new ArrayList<State>();
		this.recurrence_state_list=new ArrayList<State>();
	}

	public State getInitial_state() {
		return initial_state;
	}

	public void setInitial_state(State initial_state) {
		this.initial_state = initial_state;
	}

	public List<State> getState_list() {
		return state_list;
	}

	public List<Counter> getCounter_list() {
		return counter_list;
	}

	public List<TransitionRelation> getTransitionrelation_list() {
		return transitionrelation_list;
	}

	public List<State> getAccepting_state_list() {
		return accepting_state_list;
	}

	public List<State> getRecurrence_state_list() {
		return recurrence_state_list;
	}

}

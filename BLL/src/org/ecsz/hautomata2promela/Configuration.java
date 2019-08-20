package org.ecsz.hautomata2promela;

import java.util.ArrayList;
import java.util.List;

import org.ecsz.umlstatediagram2hautomata.State;

public class Configuration {

	private List<State> state_list;
	
	public Configuration() {
		this.state_list=new ArrayList<State>();
		// TODO Auto-generated constructor stub
	}

	public List<State> getState_list() {
		return state_list;
	}
	
	

}

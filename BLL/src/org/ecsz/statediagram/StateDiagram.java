package org.ecsz.statediagram;


public class StateDiagram {
	/*
	 * 一个状态图包含一个状态机，这个状态机又包含多个region，这些region含有层次关系,通过owner_state_id属性表示。每个region只包含从该region中状态出发的边
	 */
	private String statediagram_name;
	
	private StateMachine state_machine;

	public StateDiagram(StateMachine sm) {
		this.state_machine=sm;
	}

	public String getname() {
		return statediagram_name;
	}

	public void setname(String statediagram_name) {
		this.statediagram_name = statediagram_name;
	}

	public StateMachine getState_machine() {
		return state_machine;
	}

	public void setState_machine(StateMachine state_machine) {
		this.state_machine = state_machine;
	}
	
}

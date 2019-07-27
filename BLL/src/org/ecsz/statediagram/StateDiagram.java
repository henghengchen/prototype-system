package org.ecsz.statediagram;


public class StateDiagram {
	/*
	 * һ��״̬ͼ����һ��״̬�������״̬���ְ������region����Щregion���в�ι�ϵ,ͨ��owner_state_id���Ա�ʾ��ÿ��regionֻ�����Ӹ�region��״̬�����ı�
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

package org.ecsz.statediagram;

public class Transition {

	private String transition_id;
	private String source_vertex_id;
	private String target_vertex_id;
	private String trigger;
	private String guard;
	private String action;
	
	public Transition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getid() {
		return transition_id;
	}

	public void setid(String transition_id) {
		this.transition_id = transition_id;
	}

	public String getSource_vertex_id() {
		return source_vertex_id;
	}

	public void setSource_vertex_id(String source_vertex_id) {
		this.source_vertex_id = source_vertex_id;
	}

	public String getTarget_vertex_id() {
		return target_vertex_id;
	}

	public void setTarget_vertex_id(String target_vertex_id) {
		this.target_vertex_id = target_vertex_id;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public String getGuard() {
		return guard;
	}

	public void setGuard(String guard) {
		this.guard = guard;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	
}

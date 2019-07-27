package org.ecsz.umlmodel2hautomata;

public class TransitionLabel {

	private State sr_state;
	private String ev;
	private String g;
	private String ac;
	private State td_state;
	
	public TransitionLabel(State a,String b,String c,String d,State e) {
		// TODO Auto-generated constructor stub
		this.sr_state=a;
		this.ev=b;
		this.g=c;
		this.ac=d;
		this.td_state=e;
	}

	public State getSr_state() {
		return sr_state;
	}

	public void setSr_state(State sr_state) {
		this.sr_state = sr_state;
	}

	public State getTd_state() {
		return td_state;
	}

	public void setTd_state(State td_state) {
		this.td_state = td_state;
	}

	public String getEv() {
		return ev;
	}

	public void setEv(String ev) {
		this.ev = ev;
	}

	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}

	public String getAc() {
		return ac;
	}

	public void setAc(String ac) {
		this.ac = ac;
	}

	
}

package org.ecsz.umlstatediagram2hautomata;

import java.util.HashSet;

public class TransitionLabel {

	private HashSet<State> hs_sr_state;
	private String ev;
	private String g;
	private String ac;
	private HashSet<State> hs_td_state;
	
	public TransitionLabel(String b,String c,String d) {
		// TODO Auto-generated constructor stub
		this.hs_sr_state=new HashSet<State>();
		this.ev=b;
		this.g=c;
		this.ac=d;
		this.hs_td_state=new HashSet<State>();
	}

	public HashSet<State> getHs_sr_state() {
		return hs_sr_state;
	}

	public HashSet<State> getHs_td_state() {
		return hs_td_state;
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

package org.ecsz.umlmodel2hautomata;

public class TransitionRelation {

	private State src_state;
	private TransitionLabel tl;
	private State tgt_state;
	
	public TransitionRelation(State sid,TransitionLabel tranl,State spid) {
		// TODO Auto-generated constructor stub
		this.src_state=sid;
		this.tl=tranl;
		this.tgt_state=spid;
	}

	public TransitionLabel getTl() {
		return tl;
	}

	public void setTl(TransitionLabel tl) {
		this.tl = tl;
	}

	public State getSrc_state() {
		return src_state;
	}

	public void setSrc_state(State src_state) {
		this.src_state = src_state;
	}

	public State getTgt_state() {
		return tgt_state;
	}

	public void setTgt_state(State tgt_state) {
		this.tgt_state = tgt_state;
	}

	
}

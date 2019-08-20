package org.ecsz.umlstatediagram2hautomata;

import java.util.HashSet;

public class HAutoMata {

	private SAutoMata top_sautomata;
	private HashSet<String> hs_event;
	
	public HAutoMata() {
		this.top_sautomata=new SAutoMata();
		this.hs_event=new HashSet<String>();
		// TODO Auto-generated constructor stub
	}

	public SAutoMata getTop_sautomata() {
		return top_sautomata;
	}

	public void setTop_sautomata(SAutoMata top_sautomata) {
		this.top_sautomata = top_sautomata;
	}

	public HashSet<String> getHs_event() {
		return hs_event;
	}

}

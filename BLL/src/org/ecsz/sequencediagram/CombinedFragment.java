package org.ecsz.sequencediagram;

import java.util.ArrayList;
import java.util.List;

public class CombinedFragment {

	private String id;
	private String name;
	private String interactionOperator;
	private List<String> covered_lifeline_ids_list;
	private List<InteractionOperand> opd_list;
	
	public CombinedFragment() {
		// TODO Auto-generated constructor stub
		this.covered_lifeline_ids_list=new ArrayList<String>();
		this.opd_list=new ArrayList<InteractionOperand>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInteractionOperator() {
		return interactionOperator;
	}

	public void setInteractionOperator(String interactionOperator) {
		this.interactionOperator = interactionOperator;
	}

	public List<String> getCovered_lifeline_ids_list() {
		return covered_lifeline_ids_list;
	}

	public List<InteractionOperand> getOpd_list() {
		return opd_list;
	}

}

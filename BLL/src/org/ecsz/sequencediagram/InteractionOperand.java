package org.ecsz.sequencediagram;

import java.util.ArrayList;
import java.util.List;

public class InteractionOperand {

	private String id;
	private String guard;
	private List<OccurrenceSpecification> occspec_list;
	
	public InteractionOperand() {
		this.occspec_list=new ArrayList<OccurrenceSpecification>();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGuard() {
		return guard;
	}

	public void setGuard(String guard) {
		this.guard = guard;
	}

	public List<OccurrenceSpecification> getOccspec_list() {
		return occspec_list;
	}

}

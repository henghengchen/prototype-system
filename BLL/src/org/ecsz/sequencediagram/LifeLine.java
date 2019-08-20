package org.ecsz.sequencediagram;

import java.util.ArrayList;
import java.util.List;

public class LifeLine {

	private String id;
	private String name;
	private String represent_id;
	private List<OccurrenceSpecification>  accspec_list;
	
	public LifeLine() {
		super();
		// TODO Auto-generated constructor stub
		this.accspec_list=new ArrayList<OccurrenceSpecification>();
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

	public String getRepresent_id() {
		return represent_id;
	}

	public void setRepresent_id(String represent_id) {
		this.represent_id = represent_id;
	}

	public List<OccurrenceSpecification> getAccspec_list() {
		return accspec_list;
	}

}

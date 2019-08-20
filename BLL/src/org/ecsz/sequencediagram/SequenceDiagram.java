package org.ecsz.sequencediagram;

import java.util.ArrayList;
import java.util.List;

public class SequenceDiagram {

	private String name;
	
	private String id;
	//包含有序的OccurrenceSpecification
	private List<LifeLine> ll_list;
	//由Lifeline的OccurrenceSpecification引用，同时也包含该组合片段中有序的OccurrenceSpecification
	private List<CombinedFragment> fg_list;
	//连接两个不同lifeline的OccurrenceSpecification，同时也用作同一的lifeline的有OccurrenceSpecification的排序
	private List<Message> msg_list;
	
	public SequenceDiagram() {
		this.ll_list=new ArrayList<LifeLine>();
		this.fg_list=new ArrayList<CombinedFragment>(); 
		this.msg_list=new ArrayList<Message>(); 
		// TODO Auto-generated constructor stub
	}

	public List<LifeLine> getLl_list() {
		return ll_list;
	}

	public List<CombinedFragment> getFg_list() {
		return fg_list;
	}

	public List<Message> getMsg_list() {
		return msg_list;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}

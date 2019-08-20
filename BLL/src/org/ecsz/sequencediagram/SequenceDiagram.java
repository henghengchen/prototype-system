package org.ecsz.sequencediagram;

import java.util.ArrayList;
import java.util.List;

public class SequenceDiagram {

	private String name;
	
	private String id;
	//���������OccurrenceSpecification
	private List<LifeLine> ll_list;
	//��Lifeline��OccurrenceSpecification���ã�ͬʱҲ���������Ƭ���������OccurrenceSpecification
	private List<CombinedFragment> fg_list;
	//����������ͬlifeline��OccurrenceSpecification��ͬʱҲ����ͬһ��lifeline����OccurrenceSpecification������
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

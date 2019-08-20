package org.ecsz.sequencediagram;

public class Message {

	private String id;
	private String name;
	private String sendEvent_id;
	private OccurrenceSpecification sendEvent;
	private OccurrenceSpecification receiveEvent;
	private String receiveEvent_id;
	private int order;
	
	public Message() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getSendEvent_id() {
		return sendEvent_id;
	}

	public void setSendEvent_id(String sendEvent_id) {
		this.sendEvent_id = sendEvent_id;
	}

	public String getReceiveEvent_id() {
		return receiveEvent_id;
	}

	public void setReceiveEvent_id(String receiveEvent_id) {
		this.receiveEvent_id = receiveEvent_id;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public OccurrenceSpecification getSendEvent() {
		return sendEvent;
	}

	public void setSendEvent(OccurrenceSpecification sendEvent) {
		this.sendEvent = sendEvent;
	}

	public OccurrenceSpecification getReceiveEvent() {
		return receiveEvent;
	}

	public void setReceiveEvent(OccurrenceSpecification receiveEvent) {
		this.receiveEvent = receiveEvent;
	}
	
}

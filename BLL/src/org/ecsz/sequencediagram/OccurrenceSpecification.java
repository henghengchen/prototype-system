package org.ecsz.sequencediagram;

import java.util.ArrayList;
import java.util.List;

public class OccurrenceSpecification implements Comparable<OccurrenceSpecification> {

	private String id;
	private String covered_lifeline_id;
	private LifeLine covered_lifeline;
	private String send_or_receive;
	private Message message;
	private CombinedFragment up_cf;
	private InteractionOperand up_io;
	
	public OccurrenceSpecification() {
		// TODO Auto-generated constructor stub
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getSend_or_receive() {
		return send_or_receive;
	}

	public String getCovered_lifeline_id() {
		return covered_lifeline_id;
	}

	public void setCovered_lifeline_id(String covered_lifeline_id) {
		this.covered_lifeline_id = covered_lifeline_id;
	}

	public void setSend_or_receive(String send_or_receive) {
		this.send_or_receive = send_or_receive;
	}

	public InteractionOperand getUp_io() {
		return up_io;
	}

	public void setUp_io(InteractionOperand up_io) {
		this.up_io = up_io;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LifeLine getCovered_lifeline() {
		return covered_lifeline;
	}

	public void setCovered_lifeline(LifeLine covered_lifeline) {
		this.covered_lifeline = covered_lifeline;
	}

	public CombinedFragment getUp_cf() {
		return up_cf;
	}

	public void setUp_cf(CombinedFragment up_cf) {
		this.up_cf = up_cf;
	}

	@Override
	public int compareTo(OccurrenceSpecification o) {
		// TODO Auto-generated method stub
		return this.message.getOrder()-o.getMessage().getOrder();
	}

}

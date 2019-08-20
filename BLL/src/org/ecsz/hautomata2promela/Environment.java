package org.ecsz.hautomata2promela;

import java.util.LinkedList;
import java.util.Queue;

public class Environment {

	private Queue<String> queue;
	
	public Environment() {
		this.queue = new LinkedList<String>();
		// TODO Auto-generated constructor stub
	}

	public Queue<String> getQueue() {
		return queue;
	}
	
}

package org.ecsz.hautomata2promela;

public class Status {

	private Configuration cfg;
	private Environment env;
	
	public Status() {
		this.cfg=new Configuration();
		this.env=new Environment();
		// TODO Auto-generated constructor stub
	}

	public Configuration getCfg() {
		return cfg;
	}

	public Environment getEnv() {
		return env;
	}
	
	
}

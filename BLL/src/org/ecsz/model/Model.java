package org.ecsz.model;

import java.util.HashSet;

public class Model {

	private String model_name;
	private String model_id;
	private HashSet<Package> hs_package;

	public Model() {
		this.model_name=null;
		this.model_id=null;
		this.hs_package=new HashSet<Package>();
		// TODO Auto-generated constructor stub
	}

	public String getname() {
		return model_name;
	}

	public void setname(String model_name) {
		this.model_name = model_name;
	}

	public String getid() {
		return model_id;
	}

	public void setid(String model_id) {
		this.model_id = model_id;
	}
	
	public HashSet<Package> getPackagesSet() {
		return hs_package;
	}
	
	
}

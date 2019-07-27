package org.ecsz.model;

import java.util.HashSet;

import org.ecsz.activitydiagram.ActivityDiagram;
import org.ecsz.classdiagram.ClassDiagram;
import org.ecsz.sequencediagram.SequenceDiagram;
import org.ecsz.statediagram.StateDiagram;

public class Package {

	private String package_name;
	private String package_id;
	private HashSet<ActivityDiagram> hs_ad;
	private ClassDiagram cd;
	private HashSet<SequenceDiagram> hs_sed;
	private HashSet<StateDiagram> hs_std;
	
	public Package() {
		this.hs_ad=new HashSet<ActivityDiagram>();
		this.hs_sed=new HashSet<SequenceDiagram>();
		this.hs_std=new HashSet<StateDiagram>();
		// TODO Auto-generated constructor stub
	}

	public String getname() {
		return package_name;
	}

	public void setname(String package_name) {
		this.package_name = package_name;
	}

	public String getid() {
		return package_id;
	}

	public void setid(String package_id) {
		this.package_id = package_id;
	}

	public HashSet<ActivityDiagram> getActivityDiagramsSet() {
		return hs_ad;
	}

	public HashSet<SequenceDiagram> getSequenceDiagramsSet() {
		return hs_sed;
	}

	public HashSet<StateDiagram> getStateDiagramsSet() {
		return hs_std;
	}
	
}

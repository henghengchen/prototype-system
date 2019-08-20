package org.ecsz.umlsequencediagram2interactionautomata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.ecsz.model.Package;
import org.ecsz.sequencediagram.LifeLine;
import org.ecsz.sequencediagram.OccurrenceSpecification;

public class Phase {

	private HashMap<String,Integer> map_counter = new HashMap<>();
	private HashMap<String, List<OccurrenceSpecification>> map_occurrencespecification_list = new HashMap<>();
	private List<LifeLine> lifeline_list;
	
	public Phase(List<LifeLine> ll_list) {
		// TODO Auto-generated constructor stub
		this.lifeline_list=ll_list;
		for (Iterator<LifeLine> it = ll_list.iterator(); it.hasNext();) {
			LifeLine ll=it.next();
			map_counter.put(ll.getName(), -1);
			map_occurrencespecification_list.put(ll.getName(), new ArrayList<OccurrenceSpecification>());
		}
	}

	public HashMap<String, Integer> getMap_counter() {
		return map_counter;
	}

	public HashMap<String, List<OccurrenceSpecification>> getMap_occurrencespecification_list() {
		return map_occurrencespecification_list;
	}

	public List<LifeLine> getLifeline_list() {
		return lifeline_list;
	}


}

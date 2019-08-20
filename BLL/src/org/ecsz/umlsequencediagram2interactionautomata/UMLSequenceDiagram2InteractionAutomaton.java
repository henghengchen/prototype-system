package org.ecsz.umlsequencediagram2interactionautomata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ecsz.model.Model;
import org.ecsz.model.Package;
import org.ecsz.sequencediagram.LifeLine;
import org.ecsz.sequencediagram.OccurrenceSpecification;
import org.ecsz.sequencediagram.SequenceDiagram;
import org.ecsz.statediagram.StateDiagram;
import org.ecsz.umlstatediagram2hautomata.HAutoMata;

public class UMLSequenceDiagram2InteractionAutomaton {

	private Model model;
	
	public UMLSequenceDiagram2InteractionAutomaton(Model m) {
		// TODO Auto-generated constructor stub
		this.model=m;
	}

	public List<InteractionAutomaton> getInteractionAutomaton(){
		List<InteractionAutomaton> interactionautomaton_list=new ArrayList<InteractionAutomaton>();
		HashSet<Package> hs_pack=model.getPackagesSet();
		for (Iterator<Package> it1 = hs_pack.iterator(); it1.hasNext();) {
			Package pack=it1.next();
			for (Iterator<SequenceDiagram> it2 = pack.getSequenceDiagramsSet().iterator(); it2.hasNext();) {
				SequenceDiagram sd=it2.next();
				InteractionAutomaton ham=create_interactionautomaton(sd);
				
				interactionautomaton_list.add(ham);
			}
		}
		return interactionautomaton_list;
	}
	
	protected InteractionAutomaton create_interactionautomaton(SequenceDiagram sd) {
		List<LifeLine> ll_list=sd.getLl_list();
		Phase initial_phase=new Phase(ll_list);
		InteractionAutomaton ia=new InteractionAutomaton();
		unwind(ll_list,initial_phase,ia);
		return ia;
	}
	
	protected State unwind(List<LifeLine> ll_list,Phase phase,InteractionAutomaton ia) {//ll_list用来与phase的history做比较
		//找出该phase下所有可行的OccurrenceSpecification
		List<Label> enabled_label_list=ready(ll_list,phase);
		//将状态添加到自动机
		State state=new State("",phase);
		if(isintial(phase)) {
			state.setInitial(true);
			ia.setInitial_state(state);
		}
		if(enabled_label_list.size()==0) {
			state.setAccepting(true);
			ia.getAccepting_state_list().add(state);
		}
		ia.getState_list().add(state);
		
		for(Iterator<Label> it=enabled_label_list.iterator();it.hasNext();) {
			Label label=it.next();
			TransitionRelation tr=new TransitionRelation();
			tr.setOriginal_state(state);
			tr.setEvent(label.getEvent());
			tr.setGuard(label.getGuard());
			tr.setAction(label.getAction());
			Phase nextphase=nextPhase(phase,label.getEvent());
			tr.setTarget_state(unwind(ll_list, nextphase, ia));
			ia.getTransitionrelation_list().add(tr);
		}
		return state;
	}
	
	protected Phase nextPhase(Phase p,OccurrenceSpecification os) {
		Phase nextphase=new Phase(p.getLifeline_list());
		List<OccurrenceSpecification> os_list=nextphase.getMap_occurrencespecification_list().get(os.getCovered_lifeline().getName());
		os_list.add(os);
		return nextphase;
	}
	
	protected boolean isintial(Phase p) {
		boolean isinitial=true;
		for(Iterator it=p.getMap_occurrencespecification_list().entrySet().iterator();it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			List<OccurrenceSpecification> val = (List<OccurrenceSpecification>)entry.getValue();
			if(val.size()!=0) {
				isinitial=false;
			}
		}
		return isinitial;
	}
	
	protected List<Label> ready(List<LifeLine> ll_list,Phase phase) {
		List<Label> enabled_label_list=new ArrayList<Label>();
		for(Iterator it=phase.getMap_occurrencespecification_list().entrySet().iterator();it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String)entry.getKey();
			//该生命线的counter
			int count=phase.getMap_counter().get(key);
			//每条生命线上已经完成的OccurrenceSpecification
			List<OccurrenceSpecification> val = (List<OccurrenceSpecification>)entry.getValue();
			int len=val.size();
			//找到相同的生命线
			for(Iterator<LifeLine> it2=ll_list.iterator();it2.hasNext();) {
				LifeLine ll = it2.next();
				if(ll.getName().equals(key)) {
					List<OccurrenceSpecification> ls_list=ll.getAccspec_list();
					//该生命线上下一个可能发生的OccurrenceSpecification
					OccurrenceSpecification os=ls_list.get(len);
					//该OccurrenceSpecification是否在一个loop组合片段中
					if(os.getUp_cf().getInteractionOperator().equals("loop")) {
						if(os.getSend_or_receive().equals("send")) {
							Label label_temp=new Label();
							label_temp.setEvent(os);
							label_temp.setGuard("");
							label_temp.setAction("");
							enabled_label_list.add(label_temp);
						}
						if(os.getSend_or_receive().equals("receive")) {
							String lifeline_name=os.getMessage().getSendEvent().getCovered_lifeline().getName();
							int count_other=phase.getMap_counter().get(lifeline_name);
							if(count_other>count||(count_other==count&&is_in_phase(os,phase))) {
								Label label_temp=new Label();
								label_temp.setEvent(os);
								label_temp.setGuard("");
								label_temp.setAction("");
								enabled_label_list.add(label_temp); 
							}
						}
					}else {
						if(os.getSend_or_receive().equals("send")) {
							Label label_temp=new Label();
							label_temp.setEvent(os);
							label_temp.setGuard("");
							label_temp.setAction("");
							enabled_label_list.add(label_temp);
						}
						if(os.getSend_or_receive().equals("receive")) {
							if(is_in_phase(os,phase)) {
								Label label_temp=new Label();
								label_temp.setEvent(os);
								label_temp.setGuard("");
								label_temp.setAction("");
								enabled_label_list.add(label_temp);
							}
						}
					}
				}
			}
		}
		return enabled_label_list;
	}
	
	protected boolean is_in_phase(OccurrenceSpecification os,Phase phase) {
		String id=os.getMessage().getSendEvent_id();
		boolean is_in=false;
		//寻找该接收事件的发送事件是否在phase中
		for(Iterator it3=phase.getMap_occurrencespecification_list().entrySet().iterator();it3.hasNext();) {
			Map.Entry entry2 = (Map.Entry) it3.next();
			String key2 = (String)entry2.getKey();
			List<OccurrenceSpecification> val2 = (List<OccurrenceSpecification>)entry2.getValue();
			for(Iterator<OccurrenceSpecification> it4=val2.iterator();it4.hasNext();) {
				OccurrenceSpecification os2=it4.next();
				if(os2.getId().equals(id)) {
					is_in=true;
					break;
				}
			}
			if(is_in) {
				break;
			}
		}
		return is_in;
	}
}

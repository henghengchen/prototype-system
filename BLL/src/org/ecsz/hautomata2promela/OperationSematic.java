package org.ecsz.hautomata2promela;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import org.ecsz.umlstatediagram2hautomata.HAutoMata;
import org.ecsz.umlstatediagram2hautomata.SAutoMata;
import org.ecsz.umlstatediagram2hautomata.State;
import org.ecsz.umlstatediagram2hautomata.TransitionRelation;

public class OperationSematic {

	private HAutoMata hautomata;
	private Status status;
	
	public OperationSematic(HAutoMata hm) {
		this.hautomata=hm;
		this.status=new Status();
		// 设置初始status
		SAutoMata sm=this.hautomata.getTop_sautomata();
		this.status.getCfg().getState_list().addAll(get_initialstates(sm));
		
		this.status.getEnv().getQueue().offer("");
	}

	public void runhautomata() {
		
	}
	//获得该顺序自动机及其下层自动机中所有enable的转换
	protected HashSet<TransitionRelation> getenabledtransition(SAutoMata sa){
		HashSet<TransitionRelation> enable_transitionrelation=new HashSet<TransitionRelation>();
		for(Iterator<State> it1=sa.getHs_state().iterator();it1.hasNext();) {
			State state=it1.next();
			if(state.getHs_nested_sautomata().size()!=0) {
				for(Iterator<SAutoMata> it2=state.getHs_nested_sautomata().iterator();it2.hasNext();) {
					SAutoMata sm=it2.next();
					HashSet<TransitionRelation> hs_tr=getenabledtransition(sm);
					enable_transitionrelation.addAll(hs_tr);
				}
			}
		}
		//enable_transitionrelation.addAll(getenabledlocaltransition(sa));
		return enable_transitionrelation;
	}
	
	protected List<State> get_initialstates(SAutoMata sm){
		List<State> state_list=new ArrayList<State>();
		State initial_state=sm.getInitial_state();
		state_list.add(initial_state);
		if(initial_state.getHs_nested_sautomata().size()!=0) {
			for(Iterator<SAutoMata> it1=initial_state.getHs_nested_sautomata().iterator();it1.hasNext();) {
				SAutoMata sm1=it1.next();
				state_list.addAll(get_initialstates(sm1));
			}
		}
		return state_list;
	}
	
	//获得该顺序自动机中所有enable的转换
	/*
	protected HashSet<TransitionRelation> getenabledlocaltransition(SAutoMata sa){
		HashSet<TransitionRelation> enable_transitionrelation=new HashSet<TransitionRelation>();
		HashSet<TransitionRelation> hs_transitionrelation=sa.getHs_transition_relation();
		List<State> state_list =status.getCfg().getState_list();
		Queue<String> env_queue=status.getEnv().getQueue();
		for(Iterator<TransitionRelation> it_tr=hs_transitionrelation.iterator();it_tr.hasNext();) {
			TransitionRelation tr=it_tr.next();
			State src_state=tr.getSrc_state();
			HashSet<State> sr_state=tr.getTl().getHs_sr_state();
			String event=tr.getTl().getEv();
			//缺少满足guard条件
			if(is_stateExist(state_list,src_state)&&is_stateExist(state_list,sr_state)&&event.equals(env_queue.peek())) {
				enable_transitionrelation.add(tr);
			}
		}
		return enable_transitionrelation;
	}
	*/
	protected boolean is_stateExist(List<State> state_list,State state) {
		for(int i=0;i<state_list.size();i++) {
			State temp_state=state_list.get(0);
			if(temp_state.getid().equals(state.getid())) {
				return true;
			}
		}
		return false;
	}
}

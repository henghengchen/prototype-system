package org.ecsz.hautomata2promela;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.dom4j.Element;
import org.ecsz.statediagram.Transition;
import org.ecsz.umlstatediagram2hautomata.HAutoMata;
import org.ecsz.umlstatediagram2hautomata.SAutoMata;
import org.ecsz.umlstatediagram2hautomata.State;
import org.ecsz.umlstatediagram2hautomata.StateAndPath;
import org.ecsz.umlstatediagram2hautomata.TransitionRelation;

import com.sun.jdi.event.Event;

public class HAutoMata2Promela {

	private List<HAutoMata> hautomata_list;
	
	public HAutoMata2Promela(List<HAutoMata> hm_list) {
		this.hautomata_list=hm_list;
		// TODO Auto-generated constructor stub
	}

	public String getPromela(){
		String events="";
		String environment="bit";
		String configuration="bit";
		String ev="int Ev\n";
		String select_event_set="";
		String select_fire="";
		String candidate="";
		String initialize="";
        FileWriter writer = null;
        try {
            writer = new FileWriter("C:\\Users\\衡辰\\Desktop\\项目组\\项目实现情况\\UML-workspace\\BLL\\output\\promela.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        for(Iterator<HAutoMata> it=this.hautomata_list.iterator();it.hasNext();) {
        	HAutoMata hm=it.next();
        	SAutoMata sm=hm.getTop_sautomata();
        	//定义事件event和环境environment
        	int i=1;
        	int len=hm.getHs_event().size();
        	for(Iterator<String> it2=hm.getHs_event().iterator();it2.hasNext();) {
        		String event=it2.next();
        		events=events+"#define "+event+" "+i+"\n";
        		if(i!=len) {
        			environment=environment+" Q"+event+",";
        		}else {
        			environment=environment+" Q"+event+";\n";
        		}
        		i++;
        	}
        	//定义configuration
        	List<String> state_names=getStates(sm);
        	for(int j=0;j<state_names.size();j++) {
        		//System.out.println("------------------------------------------"+state_names.size());
        		if(j==(state_names.size()-1)) {
        			configuration=configuration+" S"+state_names.get(j)+";\n";
        		}else {
        			configuration=configuration+" S"+state_names.get(j)+",";
        		}
        	}
        	//选择事件
        	select_event_set=select_event_set+"if\n";
        	for(Iterator<String> it2=hm.getHs_event().iterator();it2.hasNext();) {
        		String event=it2.next();
        		select_event_set=select_event_set+":: Q"+event+" -> Ev="+event+";Q"+event+" = 0\n";
        	}
        	select_event_set=select_event_set+"fi\n";
        	//候选转换的条件
    		Queue<SAutoMata> SAutoMata_queue = new LinkedList<SAutoMata>();
    		SAutoMata_queue.offer(sm);
    		while(SAutoMata_queue.size()!=0) {
    			SAutoMata sautomata_temp=SAutoMata_queue.poll();
    			for(Iterator<State> it2=sautomata_temp.getHs_state().iterator();it2.hasNext();) {
    				State state=it2.next();
    				if(state.getHs_nested_sautomata().size()!=0) {
    					for(Iterator<SAutoMata> it3=state.getHs_nested_sautomata().iterator();it3.hasNext();) {
    						SAutoMata_queue.add(it3.next());
    					}
    				}
    			}
    			for(Iterator<TransitionRelation> it2=sautomata_temp.getHs_transition_relation().iterator();it2.hasNext();) {
    				TransitionRelation tr=it2.next();
    				List<TransitionRelation> up_tr=get_up_transitionrelation(tr);
    				List<TransitionRelation> down_tr=get_down_transitionrelation(tr);
    				//该transitionrelation本身enable
    				candidate=candidate+"#define Cand"+tr.getId()+" = "+" S"+tr.getSrc_state().getname();
    				for(Iterator<State> it3=tr.getTl().getHs_sr_state().iterator();it3.hasNext();) {
    					candidate=candidate+" &"+" S"+it3.next().getname();
    				}
    				candidate=candidate+" & (Ev=="+tr.getTl().getEv()+")\n";
    				//上层优先级更高的transitionrelation
    				for(int k=0;k<up_tr.size();k++) {
    					TransitionRelation tr_temp=up_tr.get(k);
        				candidate=candidate+"& !(S"+tr_temp.getSrc_state().getname();
        				for(Iterator<State> it3=tr_temp.getTl().getHs_sr_state().iterator();it3.hasNext();) {
        					candidate=candidate+" &"+" S"+it3.next().getname();
        				}
        				candidate=candidate+" & (Ev=="+tr_temp.getTl().getEv()+") )\n";
    				}
    				//下层优先级更高的transitionrelation
    				for(int k=0;k<down_tr.size();k++) {
    					TransitionRelation tr_temp=down_tr.get(k);
        				candidate=candidate+"& !(S"+tr_temp.getSrc_state().getname();
        				for(Iterator<State> it3=tr_temp.getTl().getHs_sr_state().iterator();it3.hasNext();) {
        					candidate=candidate+" &"+" S"+it3.next().getname();
        				}
        				candidate=candidate+" & (Ev=="+tr_temp.getTl().getEv()+") )\n";
    				}
    			}
    		}
        	//select and fire
    		select_fire=select_fire+selectandfire(sm);
    		//初始化
    		initialize=initial_configuration(sm,1)+"\n";
        	for(Iterator<String> it2=hm.getHs_event().iterator();it2.hasNext();) {
        		String event=it2.next();
        		if(event.equals("e2")) {
        			initialize=initialize+"Q"+event+"=1;";
        		}else {
        			initialize=initialize+"Q"+event+"=0;";
        		}
        	}
        	initialize=initialize+"\n";
        }
        String step="proctype STEP()\n{\ndo\n::\natomic{\n"+select_event_set+select_fire+"}\nod\n}\n";
        String init="init\n{\natomic{\n"+initialize+"};\nrun STEP()\n}\n";
        try {
        	writer.write(events+environment+configuration+ev+candidate+step+init);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return null;
	}
	
	protected String initial_environment() {
		
		
		return  null;
	}
	
	protected String initial_configuration(SAutoMata sm_temp,int isinit) {
		String temp="";
		if(isinit==1) {
			for(Iterator<State> it=sm_temp.getHs_state().iterator();it.hasNext();) {
				State state=it.next();
				if(state.getid().equals(sm_temp.getInitial_state().getid())) {
					temp=temp+"S"+state.getname()+"=1;";
					for(Iterator<SAutoMata> it2=state.getHs_nested_sautomata().iterator();it2.hasNext();) {
						temp=temp+initial_configuration(it2.next(),1);
					}
				}else {
					temp=temp+"S"+state.getname()+"=0;";
					for(Iterator<SAutoMata> it2=state.getHs_nested_sautomata().iterator();it2.hasNext();) {
						temp=temp+initial_configuration(it2.next(),0);
					}
				}
			} 
		}else {
			for(Iterator<State> it=sm_temp.getHs_state().iterator();it.hasNext();) {
				State state=it.next();
				temp=temp+"S"+state.getname()+"=0;";
				for(Iterator<SAutoMata> it2=state.getHs_nested_sautomata().iterator();it2.hasNext();) {
					temp=temp+initial_configuration(it2.next(),0);
				}
			} 
		}
		return temp;
	}
	
	protected String selectandfire(SAutoMata sm_temp) {
		String sf="if\n";
		for(Iterator<TransitionRelation> it=sm_temp.getHs_transition_relation().iterator();it.hasNext();) {
			TransitionRelation tr=it.next();
			sf=sf+":: Cand"+tr.getId()+"->";
			State src_state=tr.getSrc_state();
			//添加当前状态
			sf=sf+"S"+src_state.getname()+"=0;";
			//添加下层的所有状态
    		Queue<SAutoMata> SAutoMata_queue = new LinkedList<SAutoMata>();
    		for(Iterator<SAutoMata> it2=src_state.getHs_nested_sautomata().iterator();it2.hasNext();) {
    			SAutoMata_queue.offer(it2.next());
    		}
    		while(SAutoMata_queue.size()!=0) {
    			for(Iterator<State> it3=SAutoMata_queue.poll().getHs_state().iterator();it3.hasNext();) {
    				State state=it3.next();
    				for(Iterator<SAutoMata> it4=state.getHs_nested_sautomata().iterator();it4.hasNext();) {
    					SAutoMata_queue.offer(it4.next());
    				}
    				sf=sf+"S"+state.getname()+"=0;";
    			}
    		}
    		//target
    		State tgt_state=tr.getTgt_state();
    		HashSet<State> hs_td_state=tr.getTl().getHs_td_state();
    		for(Iterator<State> it2=hs_td_state.iterator();it2.hasNext();) {
    			State state_temp=it2.next();
    			sf=sf+"S"+state_temp.getname()+"=1;";
    			while(!state_temp.getUp_sautomata().getUp_state().getid().equals(tgt_state.getid())) {
    				state_temp=state_temp.getUp_sautomata().getUp_state();
    				sf=sf+"S"+state_temp.getname()+"=1;";
    			}
    		}
    		sf=sf+"S"+tgt_state.getname()+"=1;";
    		if(!tr.getTl().getAc().equals("-")) {
    			sf=sf+"Q"+tr.getTl().getAc()+"=1\n";
    		}else {
    			sf=sf+"\n";
    		}
		}
		Queue<SAutoMata> SAutoMata_queue = new LinkedList<SAutoMata>();
		for(Iterator<State> it=sm_temp.getHs_state().iterator();it.hasNext();) {
			State state_temp=it.next();
			for(Iterator<SAutoMata> it2=state_temp.getHs_nested_sautomata().iterator();it2.hasNext();) {
				SAutoMata_queue.offer(it2.next());
			}
		}
		if(SAutoMata_queue.size()!=0) {
			sf=sf+"::";
			int p=0;
			while(SAutoMata_queue.size()!=0) {
				SAutoMata sm=SAutoMata_queue.poll();
				for(Iterator<State> it=sm.getHs_state().iterator();it.hasNext();) {
					State state_temp=it.next();
					for(Iterator<SAutoMata> it2=state_temp.getHs_nested_sautomata().iterator();it2.hasNext();) {
						SAutoMata_queue.offer(it2.next());
					}
				}
				for(Iterator<TransitionRelation> it=sm.getHs_transition_relation().iterator();it.hasNext();) {
					TransitionRelation tr=it.next();
					if(p==0) {
						sf=sf+"(Cand"+tr.getId();
						p=1;
					}else {
						sf=sf+"||Cand"+tr.getId();
					}
					
				}
			}
			sf=sf+")->\n";
		}
		for(Iterator<State> it=sm_temp.getHs_state().iterator();it.hasNext();) {
			State state_temp=it.next();
			for(Iterator<SAutoMata> it2=state_temp.getHs_nested_sautomata().iterator();it2.hasNext();) {
				sf=sf+selectandfire(it2.next());
			}
		}
		sf=sf+"::else->skip\nfi\n";
		return sf;
	}
	
	protected List<TransitionRelation> get_up_transitionrelation(TransitionRelation tr) {
		List<TransitionRelation> tr_list=new ArrayList<TransitionRelation>();
		State state=tr.getSrc_state().getUp_sautomata().getUp_state();
		while(state!=null) {
			for(Iterator<TransitionRelation> it=state.getUp_sautomata().getHs_transition_relation().iterator();it.hasNext();) {
				TransitionRelation tr_temp=it.next();
				//transitionrelation开始于上层状态且优先级高于当前的transitionrelation
				State state_tr=null;
				if(tr.getTl().getHs_sr_state().size()==0) {
					state_tr=tr.getSrc_state();
				}else {
					state_tr=tr.getTl().getHs_sr_state().iterator().next();
				}
				State state_tr_temp=null;
				if(tr_temp.getTl().getHs_sr_state().size()==0) {
					state_tr_temp=tr_temp.getSrc_state();
				}else {
					state_tr_temp=tr_temp.getTl().getHs_sr_state().iterator().next();
				}
				
				if(tr_temp.getSrc_state().getid().equals(state.getid())&&is_highprecedence(state_tr,state_tr_temp)) {
					tr_list.add(tr_temp);
				}
			}
			state=state.getUp_sautomata().getUp_state();
		}
		return tr_list;
	}
	
	protected List<TransitionRelation> get_down_transitionrelation(TransitionRelation tr) {
		List<TransitionRelation> tr_list=new ArrayList<TransitionRelation>();
		//同一层的transitionrelation
		State state=tr.getSrc_state();
		for(Iterator<TransitionRelation> it=state.getUp_sautomata().getHs_transition_relation().iterator();it.hasNext();) {
			TransitionRelation tr_temp=it.next();
			State state_tr=null;
			if(tr.getTl().getHs_sr_state().size()==0) {
				state_tr=tr.getSrc_state();
			}else {
				state_tr=tr.getTl().getHs_sr_state().iterator().next();
			}
			State state_tr_temp=null;
			if(tr_temp.getTl().getHs_sr_state().size()==0) {
				state_tr_temp=tr_temp.getSrc_state();
			}else {
				state_tr_temp=tr_temp.getTl().getHs_sr_state().iterator().next();
			}
			if(tr_temp.getSrc_state().getid().equals(state.getid())&&is_highprecedence(state_tr,state_tr_temp)) {
				tr_list.add(tr_temp);
			}
		}
		//下层的transitionrelation
		Queue<SAutoMata> SAutoMata_queue = new LinkedList<SAutoMata>();
		for(Iterator<SAutoMata> it=state.getHs_nested_sautomata().iterator();it.hasNext();) {
			SAutoMata_queue.offer(it.next());
		}
		while(SAutoMata_queue.size()!=0) {
			System.out.println(SAutoMata_queue.size());
			SAutoMata sm=SAutoMata_queue.poll();
			for(Iterator<State> it=sm.getHs_state().iterator();it.hasNext();) {
				State state_temp=it.next();
				if(state_temp.getHs_nested_sautomata().size()!=0) {
					for(Iterator<SAutoMata> it2=state_temp.getHs_nested_sautomata().iterator();it2.hasNext();) {
						SAutoMata_queue.offer(it2.next());
					}
				}
			}
			for(Iterator<TransitionRelation> it=sm.getHs_transition_relation().iterator();it.hasNext();) {
				TransitionRelation tr_temp=it.next();
				State state_tr=null;
				if(tr.getTl().getHs_sr_state().size()==0) {
					state_tr=tr.getSrc_state();
				}else {
					state_tr=tr.getTl().getHs_sr_state().iterator().next();
				}
				State state_tr_temp=null;
				if(tr_temp.getTl().getHs_sr_state().size()==0) {
					state_tr_temp=tr_temp.getSrc_state();
				}else {
					state_tr_temp=tr_temp.getTl().getHs_sr_state().iterator().next();
				}
				if(is_highprecedence(state_tr,state_tr_temp)) {
					tr_list.add(tr_temp);
				}
			}
		}
		return tr_list;
	}
	
	protected List<String> getStates(SAutoMata sm) {
		List<String> states=new ArrayList<String>();
    	for(Iterator<State> it2=sm.getHs_state().iterator();it2.hasNext();) {
    		State state=it2.next();
    		states.add(state.getname());
    		if(state.getHs_nested_sautomata().size()!=0) {
    			for(Iterator<SAutoMata> it3=state.getHs_nested_sautomata().iterator();it3.hasNext();) {
    				SAutoMata sm_temp=it3.next();
    				List<String> names=getStates(sm_temp);
    				states.addAll(names);
    			}
    		}
    	}
		return states;
	}
	
	protected boolean is_orthogonal(State s1,State s2) {
		List<String> s1_upids=new ArrayList<String>();
		List<String> s2_upids=new ArrayList<String>();
		State temp_state;
		temp_state=s1;
		while(temp_state!=null) {
			if(temp_state.getUp_sautomata()!=null) {
				s1_upids.add(temp_state.getUp_sautomata().getid());
				if(temp_state.getUp_sautomata().getUp_state()!=null) {
					temp_state=temp_state.getUp_sautomata().getUp_state();
					s1_upids.add(temp_state.getUp_sautomata().getUp_state().getid());
				}else {
					temp_state=null;
				}
			}
		}
		temp_state=s2;
		while(temp_state!=null) {
			if(temp_state.getUp_sautomata()!=null) {
				s2_upids.add(temp_state.getUp_sautomata().getid());
				if(temp_state.getUp_sautomata().getUp_state()!=null) {
					temp_state=temp_state.getUp_sautomata().getUp_state();
					s2_upids.add(temp_state.getUp_sautomata().getUp_state().getid());
				}else {
					temp_state=null;
				}
			}
		}
		int s1_upids_len=s1_upids.size();
		int s2_upids_len=s2_upids.size();
		for(;s1_upids_len>0&&s2_upids_len>0;s1_upids_len--,s2_upids_len--) {
			if(!s1_upids.get(s1_upids_len-1).equals(s2_upids.get(s2_upids_len-1))) {
				break;
			}
		}
		if((s1_upids.size()-s1_upids_len)%2==0) {
			return true;
		}
		return false;
	}
	
	protected boolean is_conflict(TransitionRelation t1,TransitionRelation t2) {
		State source_state1=t1.getSrc_state();
		State source_state2=t2.getSrc_state();
		if(!source_state1.getid().equals(source_state2.getid())) {//不是同一个状态
			if(is_highprecedence(source_state1,source_state2)||is_highprecedence(source_state2,source_state1)) {
				return true;
			}
		}
		return false;
	}
	
	//判断是否sp的优先级更高，即sp是否在s的下层自动机中，或与s为同一个状态
	protected boolean is_highprecedence(State s,State sp) {
		if(s.getid().equals(sp.getid())) {
			return false;
		}else {
			HashSet<SAutoMata> hs_sm=s.getHs_nested_sautomata();
			for (Iterator<SAutoMata> it1 = hs_sm.iterator(); it1.hasNext();) {
				SAutoMata sm=it1.next();
				boolean is_exist=getStatebyid(sm,sp.getid());
				if(is_exist) {
					return true;
				}
			}
		}
		return false;
	}
	
	protected boolean getStatebyid(SAutoMata sautomata,String id) {
		HashSet<State> hs_state=sautomata.getHs_state();
		if(hs_state.size()!=0) {
			for (Iterator<State> it1 = hs_state.iterator(); it1.hasNext();) {
				State state1=it1.next();
				//如果状态就在该自动机中
				if(state1.getid().equals(id)) {
					return true;
				}else if(state1.getHs_nested_sautomata()!=null) {
					//状态不在该自动机中
					//状态有子自动机
					HashSet<SAutoMata> hs_sautomata=state1.getHs_nested_sautomata();
					for (Iterator<SAutoMata> it2 = hs_sautomata.iterator(); it2.hasNext();) {
						SAutoMata nested_sautomata=it2.next();
						boolean is_exist=getStatebyid(nested_sautomata, id);
						//子自动机中有该状态
						if(is_exist) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}

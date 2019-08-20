package org.ecsz.umlstatediagram2hautomata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.dom4j.Element;
import org.ecsz.model.Model;
import org.ecsz.model.Package;
import org.ecsz.statediagram.Region;
import org.ecsz.statediagram.StateDiagram;
import org.ecsz.statediagram.StateMachine;
import org.ecsz.statediagram.Transition;
import org.ecsz.statediagram.Vertex;

public class UMLStateDiagram2HAutoMata {

	private Model model;
	
	public UMLStateDiagram2HAutoMata(Model m) {
		this.model=m;
		// TODO Auto-generated constructor stub
	}

	public List<HAutoMata> getHAutoMata()
	{
		List<HAutoMata> hautomata_list=new ArrayList<HAutoMata>();
		HashSet<Package> hs_pack=model.getPackagesSet();
		for (Iterator<Package> it1 = hs_pack.iterator(); it1.hasNext();) {
			Package pack=it1.next();
			for (Iterator<StateDiagram> it2 = pack.getStateDiagramsSet().iterator(); it2.hasNext();) {
				StateDiagram sd=it2.next();
				HAutoMata ham=create_hautomata(sd);
				
				hautomata_list.add(ham);
			}
		}
		return hautomata_list;
	}
	
	protected HAutoMata create_hautomata(StateDiagram sd) {
		HAutoMata hautomata=new HAutoMata();
		StateMachine sm=sd.getState_machine();
		HashSet<Region> hs_region=sm.getregionsSet();
		HashSet<Transition> hs_transition=getTransition(hs_region);
		System.out.println("---"+hs_transition.size());
		//����Ϊnull ��ȡ����region
		HashSet<Region> hs_region_top=getRegion(hs_region,null);
		
		SAutoMata sautomata=null;
		if(hs_region_top.size()==1) {
			sautomata=create_sautomata(hs_region,hs_region_top.iterator().next());
		}
		if(sautomata!=null) {
			addtransition2sautomata(sautomata,hs_transition);
		}
		hautomata.setTop_sautomata(sautomata);
		
		//����¼�����                                                                                                                             
		HashSet<String> hs_event=hautomata.getHs_event();
		for(Iterator<Transition> it=hs_transition.iterator();it.hasNext();) {
			Transition t=it.next();
			if(t.getTrigger()!=null) {
				hs_event.add(t.getTrigger());
				System.out.println("---"+t.getid()+" "+t.getTrigger());
			}
		}
		
		return hautomata;
	}
	
	
	/*
	 * ���⣺
	 * 2����ĳ״̬����һ������״̬ʱ������״̬Ϊ����״̬���ڲ�״̬
	 */
	protected void addtransition2sautomata(SAutoMata sautomata,HashSet<Transition> hs_transition) {
		for (Iterator<Transition> it1 = hs_transition.iterator(); it1.hasNext();) {
			Transition transition=it1.next();
			StateAndPath sp_s=getStatebyid(sautomata,transition.getSource_vertex_id());
			//System.out.println("=======");
			List<String> path_s=sp_s.getPath();
			StateAndPath sp_t=getStatebyid(sautomata,transition.getTarget_vertex_id());
			List<String> path_t=sp_t.getPath();
			int i=0;
			while(path_s.get(i)!=null&&path_t.get(i)!=null)
			{
				if(!(path_s.get(i).equals(path_t.get(i)))) {
					//��һ��Ԫ�ؾͲ����
					break;
				}else if(path_s.get(i).equals(path_t.get(i))&&!(path_s.get(i+1).equals(path_t.get(i+1)))) {
					//��i��Ԫ����� ��i+1��Ԫ�ز����
					break;
				}else {
					//��i��Ԫ����� ��i+1��Ԫ��Ҳ���
					i++;
				}
			}
			if(i==0&&!(path_s.get(0).equals(path_t.get(0)))) {
				//����
			}else{
				//i������״̬�����²㹫�����Զ�����Transition����ӵ����Զ���
				String sautomata_id=path_s.get(i);
				SAutoMata public_sautomata=getSAutoMatabyid(sautomata,sautomata_id);
				TransitionLabel temp_tl=new TransitionLabel(transition.getTrigger(),transition.getGuard(),transition.getAction());
				temp_tl.getHs_sr_state().add(sp_s.getState());
				temp_tl.getHs_td_state().add(sp_t.getState());
				TransitionRelation temp_tr=new TransitionRelation(transition.getid(),public_sautomata.getStateById(path_s.get(i+1)),temp_tl,public_sautomata.getStateById(path_t.get(i+1)));
				public_sautomata.getHs_transition_label().add(temp_tl);
				public_sautomata.getHs_transition_relation().add(temp_tr);
			}
		}
		//
		Queue<SAutoMata> SAutoMata_queue = new LinkedList<SAutoMata>();
		SAutoMata_queue.offer(sautomata);
		while(SAutoMata_queue.size()!=0) {
			SAutoMata sm=SAutoMata_queue.poll();
			State pseudostate = null;
			//System.out.println("--------------------------name: "+sm.getname());
			for(Iterator<State> it=sm.getHs_state().iterator();it.hasNext();) {
				State state=it.next();
				if(state.getHs_nested_sautomata().size()!=0) {
					for(Iterator<SAutoMata> it2=state.getHs_nested_sautomata().iterator();it2.hasNext();) {
						SAutoMata_queue.add(it2.next());
					}
				}
				if(state.getType().equals("uml:Pseudostate")) {
					pseudostate=state;
				}
			}
			//���ó�ʼ״̬��ɾ��transition
			for(Iterator<TransitionRelation> it2=sm.getHs_transition_relation().iterator();it2.hasNext();) {
				TransitionRelation tr=it2.next();
				if(tr.getSrc_state().getid().equals(pseudostate.getid())) {
					sm.setInitial_state(tr.getTgt_state());
					TransitionLabel tl=tr.getTl();
					sm.getHs_transition_relation().remove(tr);
					sm.getHs_transition_label().remove(tl);
					break;
				}
			}
			//ɾ��״̬
			sm.getHs_state().remove(pseudostate);
		}
		
		SAutoMata_queue.clear();
		SAutoMata_queue.offer(sautomata);
		while(SAutoMata_queue.size()!=0) {
			SAutoMata sm=SAutoMata_queue.poll();
			for(Iterator<State> it=sm.getHs_state().iterator();it.hasNext();) {
				State state=it.next();
				if(state.getHs_nested_sautomata().size()!=0) {
					for(Iterator<SAutoMata> it2=state.getHs_nested_sautomata().iterator();it2.hasNext();) {
						SAutoMata_queue.add(it2.next());
					}
				}
			}
			for(Iterator<TransitionRelation> it2=sm.getHs_transition_relation().iterator();it2.hasNext();) {
				TransitionRelation tr=it2.next();
				TransitionLabel tl=tr.getTl();
				//ͼ�л�õ�״̬
				State true_ori_state=tl.getHs_sr_state().iterator().next();
				State true_tar_state=tl.getHs_td_state().iterator().next();
				State cur_ori_state=tr.getSrc_state();
				State cur_tar_state=tr.getTgt_state();
				if(true_ori_state.getid().equals(cur_ori_state.getid())) {
					tl.getHs_sr_state().clear();
				}
				HashSet<State> hs_state=tl.getHs_td_state();
				if(true_tar_state.getid().equals(cur_tar_state.getid())) {//˳���Զ�����Դ����������Դ�ڵ���ͬ������ǲ�����״̬
					if(true_tar_state.getHs_nested_sautomata().size()!=0) {
						hs_state.clear();
						for(Iterator<SAutoMata> it3=true_tar_state.getHs_nested_sautomata().iterator();it3.hasNext();) {
							hs_state.add(it3.next().getInitial_state());
						}
					}else {
						hs_state.clear();
					}
				}else {//˳���Զ�����Դ����������Դ�ڵ㲻ͬ�����������Դ״̬�в���
					for(Iterator<SAutoMata> it3=true_tar_state.getUp_sautomata().getUp_state().getHs_nested_sautomata().iterator();it3.hasNext();) {
						SAutoMata sautoMata=it3.next();
						State initial_state=sautoMata.getInitial_state();
						//System.out.println("initial_state name="+initial_state.getname()+" id="+initial_state.getid()+"   true_tar_state name="+true_tar_state.getname()+" id="+true_tar_state.getid());
						if(!true_tar_state.getUp_sautomata().getInitial_state().getid().equals(initial_state.getid())) {
							//System.out.println("add state name="+initial_state.getname());
							hs_state.add(initial_state);
						}
					}
				}
			}
		}
		
	}
	
	protected SAutoMata getSAutoMatabyid(SAutoMata sautomata,String id) {
		if(sautomata.getid().equals(id)) {
			return sautomata;
		}else {
			for (Iterator<State> it1 = sautomata.getHs_state().iterator(); it1.hasNext();) {
				State state=it1.next();
				if(state.getHs_nested_sautomata()!=null) {
					for (Iterator<SAutoMata> it2 = state.getHs_nested_sautomata().iterator(); it2.hasNext();) {
						SAutoMata sm=it2.next();
						SAutoMata result_sm=getSAutoMatabyid(sm,id);
						if(result_sm!=null) {
							return result_sm;
						}
					}
				}
			}
		}
		return null;
	}
	
	protected StateAndPath getStatebyid(SAutoMata sautomata,String id) {
		//System.out.println(id);
		StateAndPath sap=new StateAndPath();
		List<String> ls=sap.getPath();
		HashSet<State> hs_state=sautomata.getHs_state();
		if(hs_state.size()!=0) {
			for (Iterator<State> it1 = hs_state.iterator(); it1.hasNext();) {
				State state1=it1.next();
				//System.out.println(state1.getname());
				//���״̬���ڸ��Զ����У���path�������Զ�����id
				if(state1.getid().equals(id)) {
					sap.setState(state1);
					ls.add(sautomata.getid());
					ls.add(state1.getid());
					return sap;
				}else if(state1.getHs_nested_sautomata()!=null) {
					//״̬���ڸ��Զ�����
					//״̬�����Զ���
					HashSet<SAutoMata> hs_sautomata=state1.getHs_nested_sautomata();
					for (Iterator<SAutoMata> it2 = hs_sautomata.iterator(); it2.hasNext();) {
						SAutoMata nested_sautomata=it2.next();
						StateAndPath nested_sap=getStatebyid(nested_sautomata, id);
						//���Զ������и�״̬
						if(nested_sap!=null) {
							sap.setState(nested_sap.getState());
							ls.add(sautomata.getid());
							ls.add(state1.getid());
							ls.addAll(nested_sap.getPath());
							return sap;
						}
					}
				}
			}
		}
		return null;
	}
	
	protected HashSet<Region> getRegion(HashSet<Region> hs_reg,String owner_id){
		HashSet<Region> hs_region=new HashSet<Region>();
		if(owner_id==null) {
			for (Iterator<Region> it_region=hs_reg.iterator();it_region.hasNext();) {
				Region region=it_region.next();
				if(region.getOwner_vertex_id()==null) {
					hs_region.add(region);
				}
			}
		}else {
			for (Iterator<Region> it_region=hs_reg.iterator();it_region.hasNext();) {
				Region region=it_region.next();
				if(region.getOwner_vertex_id()!=null) {
					if(region.getOwner_vertex_id().equals(owner_id)) {
						hs_region.add(region);
					}
				}
			}
		}
		return hs_region;
	}
	
	protected HashSet<Transition> getTransition(HashSet<Region> hs_reg){
		HashSet<Transition> hs_transition=new HashSet<Transition>();
		for (Iterator<Region> it_reg=hs_reg.iterator(); it_reg.hasNext();) {
			Region region=it_reg.next();
			hs_transition.addAll(region.getoutgoingtransitionsSet());
		}
		return hs_transition;
	}
	
	protected SAutoMata create_sautomata(HashSet<Region> it_region, Region region) {//���ϲ���Զ���û���ϲ�״̬
		//System.out.println("--------------------------region "+region.getname());
		SAutoMata sautomata=new SAutoMata();
		sautomata.setname(region.getname());
		sautomata.setid(region.getid());
		for (Iterator<Vertex> it1=region.getvertexsSet().iterator(); it1.hasNext();) {
			Vertex temp_vertex=it1.next();
			State state=new State();
			state.setname(temp_vertex.getname());
			state.setid(temp_vertex.getid());
			state.setType(temp_vertex.getType());
			state.setUp_sautomata(sautomata);//����״̬���ϲ��Զ���
			HashSet<Region> hs_region=getRegion(it_region,temp_vertex.getid());
			if(hs_region.size()!=0) {
				for (Iterator<Region> it2=hs_region.iterator(); it2.hasNext();) {
					Region temp_region=it2.next();
					SAutoMata nested_sautomata=create_sautomata(it_region,temp_region);
					state.getHs_nested_sautomata().add(nested_sautomata);//����״̬���²���Զ���
					nested_sautomata.setUp_state(state);//�����Զ������ϲ�״̬
				}
			}
			sautomata.getHs_state().add(state);//�Զ��������״̬
		}
		return sautomata;
	}

}

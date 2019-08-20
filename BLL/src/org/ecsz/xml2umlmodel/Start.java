package org.ecsz.xml2umlmodel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.ecsz.hautomata2promela.HAutoMata2Promela;
import org.ecsz.model.Model;
import org.ecsz.model.Package;
import org.ecsz.sequencediagram.CombinedFragment;
import org.ecsz.sequencediagram.LifeLine;
import org.ecsz.sequencediagram.Message;
import org.ecsz.sequencediagram.OccurrenceSpecification;
import org.ecsz.sequencediagram.SequenceDiagram;
import org.ecsz.statediagram.Region;
import org.ecsz.statediagram.StateDiagram;
import org.ecsz.statediagram.StateMachine;
import org.ecsz.statediagram.Transition;
import org.ecsz.statediagram.Vertex;
import org.ecsz.umlstatediagram2hautomata.HAutoMata;
import org.ecsz.umlstatediagram2hautomata.SAutoMata;
import org.ecsz.umlstatediagram2hautomata.State;
import org.ecsz.umlstatediagram2hautomata.TransitionRelation;
import org.ecsz.umlstatediagram2hautomata.UMLStateDiagram2HAutoMata;

public class Start {

	public static void main(String[] args)
	{
		XML2UMLModel xm=new XML2UMLModel("C:\\Users\\衡辰\\Desktop\\项目组\\项目实现情况\\UML-workspace\\BLL\\testmodels\\test5.xml");
		Model model=xm.getUMLModel();
		//输出模型
		//out_model_statediagram(model);
		//out_model_sequencediagram(model);
		UMLStateDiagram2HAutoMata uh=new UMLStateDiagram2HAutoMata(model);
		
		List<HAutoMata> hautomata_list=uh.getHAutoMata();   
		//输出自动机
		//out_hautomata(hautomata_list.get(0).getTop_sautomata(),"");
		
		HAutoMata2Promela hmp=new HAutoMata2Promela(hautomata_list);
		
		hmp.getPromela();
	}
	
	protected static void out_hautomata(SAutoMata hm,String prefix) {
		SAutoMata top_sm=hm;
		System.out.println(prefix+"SAutoMata name="+top_sm.getname()+" id="+top_sm.getid());
		for(Iterator<TransitionRelation> it=top_sm.getHs_transition_relation().iterator();it.hasNext();) {
			TransitionRelation tr=it.next();
			String sr_state="";
			for(Iterator<State> it2=tr.getTl().getHs_sr_state().iterator();it2.hasNext();) {
				sr_state=sr_state+it2.next().getname();
			}
			String td_state="";
			for(Iterator<State> it3=tr.getTl().getHs_td_state().iterator();it3.hasNext();) {
				State state=it3.next();
				td_state=td_state+state.getname();
			}
			System.out.println(prefix+"---transitionrelation SRC="+tr.getSrc_state().getname()+" SR="+sr_state+" EV="+tr.getTl().getEv()+" G="+tr.getTl().getG()+" AC="+tr.getTl().getAc()+" TD="+td_state+" TGT="+tr.getTgt_state().getname());
		}
		for(Iterator<State> it=top_sm.getHs_state().iterator();it.hasNext();) {
			State state=it.next();
			System.out.println(prefix+"---State name="+state.getname()+" id="+state.getid()+" up_name="+state.getUp_sautomata().getname());
			HashSet<SAutoMata> hs_sautomata=state.getHs_nested_sautomata();
			for(Iterator<SAutoMata> it2=hs_sautomata.iterator();it2.hasNext();) {
				SAutoMata sm2=it2.next();
				out_hautomata(sm2,prefix+"---");
			}
		}
	}
	
	protected static void out_model_sequencediagram(Model model){
		System.out.println("Mode name="+model.getname()+" id="+model.getid());
		HashSet<Package> hs_package=model.getPackagesSet();
		for(Iterator<Package> it_p=hs_package.iterator();it_p.hasNext();) {
			Package p=it_p.next();
			System.out.println("---Package name="+p.getname()+" id="+p.getid());
			HashSet<SequenceDiagram> hs_sd=p.getSequenceDiagramsSet();
			for(Iterator<SequenceDiagram> it_sd=hs_sd.iterator();it_sd.hasNext();) {
				SequenceDiagram sd=it_sd.next();
				System.out.println("------SequenceDiagram name="+sd.getName());
				for(Iterator<LifeLine> it_ll=sd.getLl_list().iterator();it_ll.hasNext();) {
					LifeLine ll=it_ll.next();
					System.out.println("--------LifeLine name="+ll.getName());
					for(Iterator<OccurrenceSpecification> it_os=ll.getAccspec_list().iterator();it_os.hasNext();) {
						OccurrenceSpecification os=it_os.next();
						System.out.println("----------OccurrenceSpecification related_message_order="+os.getMessage().getOrder());
					}
				}
				for(Iterator<CombinedFragment> it_cf=sd.getFg_list().iterator();it_cf.hasNext();) {
					CombinedFragment cf=it_cf.next();
					System.out.println("--------CombinedFragment InteractionOperator="+cf.getInteractionOperator());
				}
				for(Iterator<Message> it_msg=sd.getMsg_list().iterator();it_msg.hasNext();) {
					Message msg=it_msg.next();
					System.out.println("--------Message name="+msg.getName()+" order="+msg.getOrder());
				}
			}
		}
	}
	
	protected static void out_model_statediagram(Model model){
		System.out.println("Mode name="+model.getname()+" id="+model.getid());
		HashSet<Package> hs_package=model.getPackagesSet();
		for(Iterator<Package> it_p=hs_package.iterator();it_p.hasNext();) {
			Package p=it_p.next();
			System.out.println("---Package name="+p.getname()+" id="+p.getid());
			HashSet<StateDiagram> hs_sd=p.getStateDiagramsSet();
			for(Iterator<StateDiagram> it_sd=hs_sd.iterator();it_sd.hasNext();) {
				StateDiagram sd=it_sd.next();
				System.out.println("------StateDiagram name="+sd.getname());
				StateMachine sm=sd.getState_machine();
				System.out.println("---------StateMachine name="+sm.getname()+" id="+sm.getid());
				HashSet<Region> hs_region=sm.getregionsSet();
				for(Iterator<Region> it_region=hs_region.iterator();it_region.hasNext();) {
					Region region=it_region.next();
					System.out.println("------------Region name="+region.getname()+" id="+region.getid()+" owner_vertex_id="+region.getOwner_vertex_id());
					HashSet<Vertex> hs_vertex=region.getvertexsSet();
					for(Iterator<Vertex> it_vertex=hs_vertex.iterator();it_vertex.hasNext();) {
						Vertex vertex=it_vertex.next();
						System.out.println("---------------Vertex name="+vertex.getname()+" id="+vertex.getid()+" owner_region_id="+vertex.getOwner_region_id());
					}
					HashSet<Transition> hs_transition=region.getoutgoingtransitionsSet();
					for(Iterator<Transition> it_transition=hs_transition.iterator();it_transition.hasNext();) {
						Transition transition=it_transition.next();
						System.out.println("---------------Transition id="+transition.getid()+" source_vertex_id="+transition.getSource_vertex_id()+" target_vertex_id="+transition.getTarget_vertex_id()+" trigger="+transition.getTrigger()+" guard="+transition.getGuard()+" action="+transition.getAction());
					}
				}
			}
		}
	}
}

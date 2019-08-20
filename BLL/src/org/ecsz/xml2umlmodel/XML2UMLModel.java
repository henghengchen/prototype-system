package org.ecsz.xml2umlmodel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.ecsz.activitydiagram.ActivityDiagram;
import org.ecsz.classdiagram.ClassDiagram;
import org.ecsz.model.Model;
import org.ecsz.model.Package;
import org.ecsz.sequencediagram.CombinedFragment;
import org.ecsz.sequencediagram.InteractionOperand;
import org.ecsz.sequencediagram.LifeLine;
import org.ecsz.sequencediagram.Message;
import org.ecsz.sequencediagram.OccurrenceSpecification;
import org.ecsz.sequencediagram.SequenceDiagram;
import org.ecsz.statediagram.Region;
import org.ecsz.statediagram.Vertex;
import org.ecsz.statediagram.StateDiagram;
import org.ecsz.statediagram.StateMachine;
import org.ecsz.statediagram.Transition;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element; 
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class XML2UMLModel {
	
	private String xml_file;
	private Model uml_model=null;
	
	public XML2UMLModel(String path) {
		this.xml_file=path;
		this.uml_model=new Model();
		// TODO Auto-generated constructor stub
	}
    /*
     * Document
     * 
     * Model        element1
     * 		packagedElement---模型model     element2
     * 			packagedElement---包                element3
     * 				packagedElement---模型元素
     * 				packagedElement
     * 				。。。
     * Extension
     */

	public Model getUMLModel(){
		
		SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new File(xml_file));
        } catch (DocumentException e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();            
        }
        //根节点
        Element root = document.getRootElement();
    	//it1包含 document   model   extension
    	List<Node> node_diagrams_list= root.selectNodes("/xmi:XMI/xmi:Extension/diagrams");//diagrams
    	Element element_diagrams=(Element) node_diagrams_list.get(0);
    	List<Node> node_connectors_list= root.selectNodes("/xmi:XMI/xmi:Extension/connectors");//connectors
    	Element element_connectors=(Element) node_connectors_list.get(0);
    	List<Node> model_list= root.selectNodes("/xmi:XMI/uml:Model/packagedElement");//model
    	Element element_model=(Element) model_list.get(0);
		//创建模型
    	uml_model.setname(element_model.attributeValue("name"));
    	uml_model.setid(element_model.attributeValue("id"));
		//创建包
		List<Node> package_list=element_model.selectNodes("./packagedElement");//package
		for (Iterator<Node> iter = package_list.iterator(); iter.hasNext();) {
			Element element_package=(Element)iter.next();//package
			Package model_package1=new Package();
			model_package1.setname(element_package.attributeValue("name"));
			model_package1.setid(element_package.attributeValue("id"));
			//创建类图
			List<ClassDiagram> classdiagram_list=createClassDiagram(element_package,element_diagrams);
			
			//创建状态图
			List<StateDiagram> statediagram_list=createStateDiagram(element_package,element_diagrams);
			model_package1.getStateDiagramsSet().addAll(statediagram_list);
			//创建活动图
			//List<ActivityDiagram> activitydiagram_list= createActivityDiagram(element_package,element_diagrams);
			//model_package1.getActivityDiagramsSet().addAll(activitydiagram_list);
			//创建顺序图
			List<SequenceDiagram> sequencediagram_list=createSequenceDiagram(element_package,element_diagrams,element_connectors);
			model_package1.getSequenceDiagramsSet().addAll(sequencediagram_list);
			
			uml_model.getPackagesSet().add(model_package1);
		}
		return uml_model;
	}
	
	protected List<ClassDiagram> createClassDiagram(Element pack,Element diagrams){
		
		return null;
	}
	
	protected List<ActivityDiagram> createActivityDiagram(Element pack,Element diagrams){
		
		return null;
	}
	protected List<SequenceDiagram> createSequenceDiagram(Element pack,Element diagrams,Element connectors){//只支持一层组合片段
		List<SequenceDiagram> sequencediagram_list=new ArrayList<SequenceDiagram>();
		//顺序图中的所有元素的上层标签Interaction
		Element element_Interaction=(Element) pack.selectNodes("./packagedElement/ownedBehavior").get(0);
		List<Node> attribute_list=pack.selectNodes("./packagedElement/ownedAttribute");
		//从diagrams元素中找出并创建顺序图
		for(Iterator<Element> it=diagrams.elementIterator("diagram");it.hasNext();) {
			Element element_diagram=it.next();
			List<Node> node_properties=element_diagram.selectNodes("./properties");
			Element element_properties=(Element) node_properties.get(0);
			if(element_properties.attributeValue("type").equals("Sequence")) {
				SequenceDiagram sd=new SequenceDiagram();
				sd.setName(element_properties.attributeValue("name"));
				sd.setId(element_diagram.attributeValue("id"));
				//根据id找到lifeline、combine fragment和message
				List<Node> node_element=element_diagram.selectNodes("./elements/element");
				int is_found=0;
				for(Iterator<Node> it2=node_element.iterator();it2.hasNext();) {
					//该diagram中的元素
					Element element_element = (Element) it2.next();
					//遍历上层标签Interaction的子元素、找出id指定的元素
					for(Iterator<Element> it3=element_Interaction.elementIterator();it3.hasNext();) {
						Element element_temp=it3.next();
						if(element_temp.attributeValue("id").equals(element_element.attributeValue("subject"))) {
							if(element_temp.attributeValue("type").equals("uml:Lifeline")) {
								is_found=1;
								LifeLine ll_temp=new LifeLine();
								ll_temp.setId(element_temp.attributeValue("id"));
								ll_temp.setName(element_temp.attributeValue("name"));
								ll_temp.setRepresent_id(element_temp.attributeValue("represents"));
								//添加不在组合片段中的OccurrenceSpecification
								for(Iterator<Element> it4=element_Interaction.elementIterator("fragment");it4.hasNext();) {
									Element element_fragment=it4.next();
									if(element_fragment.attributeValue("type").equals("uml:OccurrenceSpecification")) {
										if(element_fragment.attributeValue("covered").equals(element_temp.attributeValue("id"))) {
											OccurrenceSpecification sccspec_temp=new OccurrenceSpecification();
											sccspec_temp.setId(element_fragment.attributeValue("id"));
											sccspec_temp.setCovered_lifeline_id(element_temp.attributeValue("id"));
											sccspec_temp.setCovered_lifeline(ll_temp);
											ll_temp.getAccspec_list().add(sccspec_temp);
										}
									}
								}
								sd.getLl_list().add(ll_temp);
							}else if(element_temp.attributeValue("type").equals("uml:CombinedFragment")) {
								is_found=1;
								CombinedFragment cf=new CombinedFragment();
								cf.setId(element_temp.attributeValue("id"));
								cf.setName(element_temp.attributeValue("name"));
								cf.setInteractionOperator(element_temp.attributeValue("interactionOperator"));
								//添加覆盖生命线id
								for(Iterator<Element> it4=element_temp.elementIterator("covered");it4.hasNext();) {
									Element element_covered=it4.next();
									cf.getCovered_lifeline_ids_list().add(element_covered.attributeValue("idref"));
								}
								//添加操作对象
								for(Iterator<Element> it4=element_temp.elementIterator("operand");it4.hasNext();) {
									Element element_operand=it4.next();
									InteractionOperand iaod=new InteractionOperand();
									//id
									iaod.setId(element_operand.attributeValue("id"));
									//gurad
									Element guard=(Element) element_operand.selectNodes("./guard/specification").get(0);
									iaod.setGuard(guard.attributeValue("body"));
									//OccurrenceSpecification
									for(Iterator<Element> it5=element_operand.elementIterator("fragment");it5.hasNext();) {
										Element element_occurrencespecification=it5.next();
										OccurrenceSpecification sccspec_temp=new OccurrenceSpecification();
										sccspec_temp.setId(element_occurrencespecification.attributeValue("id"));
										sccspec_temp.setCovered_lifeline_id(element_occurrencespecification.attributeValue("covered"));
										sccspec_temp.setUp_cf(cf);
										sccspec_temp.setUp_io(iaod);
										iaod.getOccspec_list().add(sccspec_temp);
									}
									cf.getOpd_list().add(iaod);
								}
								sd.getFg_list().add(cf);
							}else if(element_temp.attributeValue("type").equals("uml:Message")) {
								is_found=1;
								Message m=new Message();
								m.setId(element_temp.attributeValue("id"));
								m.setName(element_temp.attributeValue("name"));
								m.setSendEvent_id(element_temp.attributeValue("sendEvent"));
								m.setReceiveEvent_id(element_temp.attributeValue("receiveEvent"));
								for(Iterator<Element> it4=connectors.elementIterator("connector");it4.hasNext();) {
									Element element_connector=it4.next();
									if(element_connector.attributeValue("idref").equals(element_temp.attributeValue("id"))) {
										Element element_appearance=(Element) element_connector.selectNodes("./appearance").get(0);
										m.setOrder(Integer.parseInt(element_appearance.attributeValue("seqno")));
										break;
									}
								}
								sd.getMsg_list().add(m);
							}else {
								
							}
						}
					}
					if(is_found==0) {//元素为生命线Actor
						for(Iterator<Node> it4=attribute_list.iterator();it4.hasNext();) {
							Element element_attribute=(Element) it4.next();
							for(Iterator<Element> it5=element_attribute.elementIterator("type");it5.hasNext();) {
								Element element_type=it5.next();
								if(element_type.attributeValue("idref").equals(element_element.attributeValue("subject"))) {
									//element_attribute.attributeValue("id")
									for(Iterator<Element> it6=element_Interaction.elementIterator("lifeline");it6.hasNext();) {
										Element element_lifeline=it6.next();
										if(element_lifeline.attributeValue("represents").equals(element_attribute.attributeValue("id"))) {
											LifeLine ll_temp=new LifeLine();
											ll_temp.setId(element_lifeline.attributeValue("id"));
											ll_temp.setName(element_lifeline.attributeValue("name"));
											ll_temp.setRepresent_id(element_lifeline.attributeValue("represents"));
											//添加不在组合片段中的OccurrenceSpecification
											for(Iterator<Element> it7=element_Interaction.elementIterator("fragment");it7.hasNext();) {
												Element element_fragment=it7.next();
												if(element_fragment.attributeValue("type").equals("uml:OccurrenceSpecification")) {
													if(element_fragment.attributeValue("covered").equals(element_lifeline.attributeValue("id"))) {
														OccurrenceSpecification sccspec_temp=new OccurrenceSpecification();
														sccspec_temp.setId(element_fragment.attributeValue("id"));
														sccspec_temp.setCovered_lifeline_id(element_lifeline.attributeValue("id"));
														sccspec_temp.setCovered_lifeline(ll_temp);
														ll_temp.getAccspec_list().add(sccspec_temp);
													}
												}
											}
											sd.getLl_list().add(ll_temp);
										}
									}
								}
							}
						}
					}
					is_found=0;
				}
				List<LifeLine> ll_list=sd.getLl_list();
				List<CombinedFragment> cf_list=sd.getFg_list();
				List<Message> msg_list=sd.getMsg_list();
				//添加在组合片段中的OccurrenceSpecification
				for(Iterator<LifeLine> it2=ll_list.iterator();it2.hasNext();) {
					LifeLine ll=it2.next();
					String lifeline_id=ll.getId();
					for(Iterator<CombinedFragment> it3=cf_list.iterator();it3.hasNext();) {
						CombinedFragment cf=it3.next();
						//该CombinedFragment里面包含生命线上的fragment
						if(cf.getCovered_lifeline_ids_list().indexOf(lifeline_id)!=-1) {
							for(Iterator<InteractionOperand> it4=cf.getOpd_list().iterator();it4.hasNext();) {
								InteractionOperand io=it4.next();
								for(Iterator<OccurrenceSpecification> it5=io.getOccspec_list().iterator();it5.hasNext();) {
									OccurrenceSpecification os=it5.next();
									if(os.getCovered_lifeline_id().equals(lifeline_id)) {
										os.setCovered_lifeline(ll);
										ll.getAccspec_list().add(os);
									}
								}
								
							}
						}
					}
				}
				//按message的order给fragment添加顺序号
				for(Iterator<LifeLine> it2=ll_list.iterator();it2.hasNext();) {
					LifeLine ll=it2.next();
					for(Iterator<OccurrenceSpecification> it3=ll.getAccspec_list().iterator();it3.hasNext();) {
						OccurrenceSpecification os=it3.next();
						for(Iterator<Message> it4=msg_list.iterator();it4.hasNext();) {
							Message msg=it4.next();
							if(msg.getSendEvent_id().equals(os.getId())) {
								msg.setSendEvent(os);
								os.setMessage(msg);
								os.setSend_or_receive("send");
								break;
							}
							if(msg.getReceiveEvent_id().equals(os.getId())) {
								msg.setReceiveEvent(os);
								os.setMessage(msg);
								os.setSend_or_receive("receive");
								break;
							}
						}
					}
				}
				//排序生命线的OccurrenceSpecification
			    for(Iterator<LifeLine> it2=ll_list.iterator();it2.hasNext();) {
			    	LifeLine ll=it2.next();
			    	Collections.sort(ll.getAccspec_list());
			    }
			    
				sequencediagram_list.add(sd);
			}
		}
		return sequencediagram_list;
	}
	
	protected List<StateDiagram> createStateDiagram(Element pack,Element diagrams){
		List<StateDiagram> statediagram_list=new ArrayList<StateDiagram>();
		List<Node> statemachine_list=pack.selectNodes("./packagedElement[@xmi:type=\"uml:StateMachine\"]");
		//所有trigger的定义（某状态下的自动机的trigger的标签不一样，要区分开）
		List<Node> trigger_list=pack.selectNodes("./packagedElement[@xmi:type=\"uml:Trigger\"]");
		if(statemachine_list.size()!=0) {
			for (Iterator<Node> iter = statemachine_list.iterator(); iter.hasNext();) {
				Element element_statemachine=(Element)iter.next();//StateMachine
				//System.out.println("statemachine name="+element_statemachine.attributeValue("name"));
				StateMachine sm=new StateMachine();
				sm.setname(element_statemachine.attributeValue("name"));
				//System.out.println(element_statemachine.attributeValue("name"));
				sm.setid(element_statemachine.attributeValue("id"));
				List<Region> region_list=createRegionbehindstatemachine(element_statemachine,trigger_list);
				sm.getregionsSet().addAll(region_list);
				//根据状态机创建状态图，用vertex_id匹配图 找到图的name
				StateDiagram statediagram=new StateDiagram(sm);
				Iterator<Region> it1=sm.getregionsSet().iterator();
				Region region=it1.next();
				Iterator<Vertex> it2=region.getvertexsSet().iterator();
				Vertex vertex=it2.next();
				String vertex_id=vertex.getid();
				for(Iterator<Element> it3=diagrams.elementIterator("diagram");it3.hasNext();) {
					Element element_diagram = it3.next();
					int is_this_diagram=0;
					List<Node> node_element=element_diagram.selectNodes("./elements/element");
					//System.out.println(node_element.size());
					for(Iterator<Node> it4=node_element.iterator();it4.hasNext();) {
						//该diagram中的元素
						Element element_element = (Element) it4.next();
						if(element_element.attributeValue("subject").equals(vertex_id)) {
							is_this_diagram=1;
							break;
						}
					}
					if(is_this_diagram==1) {
						List<Node> node_properties=element_diagram.selectNodes("./properties");
						Element element_properties=(Element) node_properties.get(0);
						statediagram.setname(element_properties.attributeValue("name"));
						break;
					}
				}
				statediagram_list.add(statediagram);
			}
		}
		return statediagram_list;
	}
	
	protected List<Region> createRegionbehindstatemachine(Element statemachine,List<Node> trigger_list) {//状态机下的region  (且状态机不是类下的状态机)
		List<Region> region_list=new ArrayList<Region>();
		Region region=new Region();
		List<Node> node_region=statemachine.selectNodes("./region");
		Element element_region=(Element) node_region.get(0);
		//System.out.println("region name="+element_region.attributeValue("name"));
		region.setname(element_region.attributeValue("name"));
		region.setid(element_region.attributeValue("id"));
		for(Iterator<Element> it=element_region.elementIterator("subvertex");it.hasNext();) {
			Element element_vertex = it.next();
			//System.out.println("--vertex type="+element_vertex.attributeValue("type"));
			Vertex vertex=new Vertex();
			vertex.setname(element_vertex.attributeValue("name"));
			vertex.setid(element_vertex.attributeValue("id"));
			vertex.setType(element_vertex.attributeValue("type"));
			vertex.setOwner_region_id(region.getid());
			region.getvertexsSet().add(vertex);
			if(element_vertex.selectNodes("./region").size()!=0){
				List<Region> nested_region_list=createRegiondehindstate(element_vertex,trigger_list);
				region_list.addAll(nested_region_list);
			}
		}
		for(Iterator<Element> it=element_region.elementIterator("transition");it.hasNext();) {
			Element element_transition = it.next();
			Transition transition=new Transition();
			transition.setid(element_transition.attributeValue("id"));
			transition.setSource_vertex_id(element_transition.attributeValue("source"));
			transition.setTarget_vertex_id(element_transition.attributeValue("target"));
			//设置Trigger
			if(element_transition.selectNodes("./trigger").size()!= 0) {
				Element element_trigger_ref=(Element) element_transition.selectNodes("./trigger").get(0);
				String trigger_id=element_trigger_ref.attributeValue("idref");
				for (Iterator<Node> iter = trigger_list.iterator(); iter.hasNext();) {
					Element element_trigger=(Element)iter.next();//Trigger
					if(element_trigger.attributeValue("id").equals(trigger_id)) {
						transition.setTrigger(element_trigger.attributeValue("name"));
						break;
					}
				}
			}
			//设置guard
			if(element_transition.selectNodes("./guard").size()!= 0) {
				Element element_guard=(Element) element_transition.selectNodes("./guard/specification").get(0);
				transition.setGuard(element_guard.attributeValue("body"));
			}
			//设置action
			if(element_transition.selectNodes("./effect").size()!= 0) {
				Element element_action=(Element) element_transition.selectNodes("./effect").get(0);
				transition.setAction(element_action.attributeValue("body"));
			}
			region.getoutgoingtransitionsSet().add(transition);
		}
		region_list.add(region);
		return region_list;
	}
	
	protected boolean is_validvertex(Region region,String vertex_id) {//是否已经访问过
		boolean isvalid=true;
		HashSet<Vertex> vertex_list=region.getvertexsSet();
		for(Iterator<Vertex> it=vertex_list.iterator();it.hasNext();) {
			Vertex vertex=it.next();
			if(vertex.getid().equals(vertex_id)) {
				isvalid=false;
			}
		}
		return isvalid;
	}
	
	protected List<Region> getregionfrompseudostate(Element last_region,Element pseudostate,Element element_region,Element pre_vertex,List<Node> trigger_list) {
		List<Region> region_list=new ArrayList<Region>();
		Region region=new Region();
		region.setname(element_region.attributeValue("name"));
		region.setid(element_region.attributeValue("id"));
		region.setOwner_vertex_id(pre_vertex.attributeValue("id"));
		Queue<Element> vertex_queue = new LinkedList<Element>();
		vertex_queue.offer(pseudostate);
		while(vertex_queue.size()!=0) {
			Element vertex_temp=vertex_queue.poll();
			String id=vertex_temp.attributeValue("id");
			if(is_validvertex(region,id)) {
				//添加顶点
				Vertex vertex=new Vertex();
				vertex.setname(vertex_temp.attributeValue("name"));
				vertex.setid(vertex_temp.attributeValue("id"));
				vertex.setType(vertex_temp.attributeValue("type"));
				vertex.setOwner_region_id(region.getid());
				
				if(vertex_temp.selectNodes("./region").size()!=0){
					List<Region> nested_region_list=createRegiondehindstate(vertex_temp,trigger_list);
					region_list.addAll(nested_region_list);
				}
				region.getvertexsSet().add(vertex);
				//添加边
				List<Node> transition_list=last_region.selectNodes("./transition[@source=\""+vertex_temp.attributeValue("id")+"\"]");
				for(Iterator<Node> it2=transition_list.iterator();it2.hasNext();) {
					Element element_transition=(Element) it2.next();
					Transition transition=new Transition();
					transition.setid(element_transition.attributeValue("id"));
					transition.setSource_vertex_id(element_transition.attributeValue("source"));
					transition.setTarget_vertex_id(element_transition.attributeValue("target"));
					//设置Trigger
					if(element_transition.selectNodes("./trigger").size()!= 0) {
						Element element_trigger_ref=(Element) element_transition.selectNodes("./trigger").get(0);
						String trigger_id=element_trigger_ref.attributeValue("idref");
						for (Iterator<Node> iter2 = trigger_list.iterator(); iter2.hasNext();) {
							Element element_trigger=(Element)iter2.next();//Trigger
							if(element_trigger.attributeValue("id").equals(trigger_id)) {
								transition.setTrigger(element_trigger.attributeValue("name"));
								break;
							}
						}
					}
					//设置guard
					if(element_transition.selectNodes("./guard").size()!= 0) {
						Element element_guard=(Element) element_transition.selectNodes("./guard/specification").get(0);
						transition.setGuard(element_guard.attributeValue("body"));
					}
					//设置action
					if(element_transition.selectNodes("./effect").size()!= 0) {
						Element element_action=(Element) element_transition.selectNodes("./effect").get(0);
						transition.setAction(element_action.attributeValue("body"));
					}
					region.getoutgoingtransitionsSet().add(transition);
					//transition的target vertex添加到队列
					List<Node> node_list=last_region.selectNodes("./subvertex[@xmi:id=\""+element_transition.attributeValue("target")+"\"]");
					for(Iterator<Node> it3=node_list.iterator();it3.hasNext();) {
						Element target_vertex=(Element) it3.next();
						vertex_queue.offer(target_vertex);
					}
				}
			}
		}
		region_list.add(region);
		return region_list;
	}
	
	protected List<Region> createRegiondehindstate(Element pre_vertex,List<Node> trigger_list) {//状态下的region
		List<Region> region_list=new ArrayList<Region>();
		//vertex下所有的region
		List<Node> node_list=pre_vertex.selectNodes("./region");
		//System.out.println("----region numer="+node_list.size());
		//最后一个包含所有vertex和transition的region
		Element element_lastregion=(Element) node_list.get(node_list.size()-1);
		List<Node> pseudostate_list=element_lastregion.selectNodes("./subvertex[@xmi:type=\"uml:Pseudostate\"]");
		for(int i=0;i<node_list.size()-1;i++) {
			Element element_region=(Element) node_list.get(i);
			Element element_pseudostate=(Element) pseudostate_list.get(i);
			List<Region> region_list_temp=getregionfrompseudostate(element_lastregion,element_pseudostate,element_region,pre_vertex,trigger_list);
			region_list.addAll(region_list_temp);
		}
		return region_list;
	}
	
	protected ActivityDiagram createactivityDiagram(Element e){
		return null;
	}
	protected ClassDiagram createclassDiagram(Element e){
		return null;
	}
}

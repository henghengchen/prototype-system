package org.ecsz.xml2umlmodel;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ecsz.activitydiagram.ActivityDiagram;
import org.ecsz.classdiagram.ClassDiagram;
import org.ecsz.model.Model;
import org.ecsz.model.Package;
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
    	List<Node> model_list= root.selectNodes("/xmi:XMI/uml:Model/ownedMember");//model
    	Element element_model=(Element) model_list.get(0);
		//创建模型
    	uml_model.setname(element_model.attributeValue("name"));
    	uml_model.setid(element_model.attributeValue("id"));
		//创建包
		List<Node> package_list=element_model.selectNodes("./ownedMember");//package
		for (Iterator<Node> iter = package_list.iterator(); iter.hasNext();) {
			Element element_package=(Element)iter.next();//package
			Package model_package1=new Package();
			model_package1.setname(element_package.attributeValue("name"));
			model_package1.setid(element_package.attributeValue("id"));
			//创建类图
			
			//创建状态图
			List<StateDiagram> statediagram_list=createStateDiagram(element_package,element_diagrams);
			model_package1.getStateDiagramsSet().addAll(statediagram_list);
			uml_model.getPackagesSet().add(model_package1);
			
			//创建活动图
			
			//创建顺序图
		}
		return uml_model;
	}

	protected List<StateDiagram> createStateDiagram(Element pack,Element diagrams){//状态机还没有存储
		List<StateDiagram> statediagram_list=new ArrayList<StateDiagram>();
		List<Node> statemachine_list=pack.selectNodes("./ownedMember[@xmi:type=\"uml:StateMachine\"]");
		//所有trigger的定义（某状态下的自动机的trigger的标签不一样，要区分开）
		List<Node> trigger_list=pack.selectNodes("./ownedMember[@xmi:type=\"uml:Trigger\"]");
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
			//System.out.println("--vertex name="+element_vertex.attributeValue("name"));
			Vertex vertex=new Vertex();
			vertex.setname(element_vertex.attributeValue("name"));
			vertex.setid(element_vertex.attributeValue("id"));
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
					element_trigger.attributeValue("id").equals(trigger_id);
					transition.setTrigger(element_trigger.attributeValue("name"));
				}
			}
			//设置guard
			if(element_transition.selectNodes("./guard").size()!= 0) {
				Element element_guard=(Element) element_transition.selectNodes("./guard/specification/body").get(0);
				transition.setGuard(element_guard.getText());
			}
			//设置action
			if(element_transition.selectNodes("./effect").size()!= 0) {
				Element element_action=(Element) element_transition.selectNodes("./effect/body").get(0);
				transition.setAction(element_action.getText());
			}
			region.getoutgoingtransitionsSet().add(transition);
		}
		region_list.add(region);
		return region_list;
	}
	
	protected List<Region> createRegiondehindstate(Element pre_vertex,List<Node> trigger_list) {//状态下的region
		List<Region> region_list=new ArrayList<Region>();
		List<Node> node_list=pre_vertex.selectNodes("./region");
		//System.out.println("----region numer="+node_list.size());
		for (Iterator<Node> iter = node_list.iterator(); iter.hasNext();) {
			Element element_region=(Element) iter.next();//Region
			//System.out.println("----region name="+element_region.attributeValue("name"));
			Region region=new Region();
			region.setname(element_region.attributeValue("name"));
			region.setid(element_region.attributeValue("id"));
			region.setOwner_vertex_id(pre_vertex.attributeValue("id"));
			for(Iterator<Element> it=element_region.elementIterator("subvertex");it.hasNext();) {
				Element element_vertex = it.next();
				//System.out.println("------vertex name="+element_vertex.attributeValue("name"));
				Vertex vertex=new Vertex();
				vertex.setname(element_vertex.attributeValue("name"));
				vertex.setid(element_vertex.attributeValue("id"));
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
					for (Iterator<Node> iter2 = trigger_list.iterator(); iter2.hasNext();) {
						Element element_trigger=(Element)iter2.next();//Trigger
						element_trigger.attributeValue("id").equals(trigger_id);
						transition.setTrigger(element_trigger.attributeValue("name"));
					}
				}
				//设置guard
				if(element_transition.selectNodes("./guard").size()!= 0) {
					Element element_guard=(Element) element_transition.selectNodes("./guard/specification/body").get(0);
					transition.setGuard(element_guard.getText());
				}
				//设置action
				if(element_transition.selectNodes("./effect").size()!= 0) {
					Element element_action=(Element) element_transition.selectNodes("./effect/body").get(0);
					transition.setAction(element_action.getText());
				}
				region.getoutgoingtransitionsSet().add(transition);
			}
			region_list.add(region);
		}
		return region_list;
	}
	protected ActivityDiagram createactivityDiagram(Element e){
		return null;
	}
	protected SequenceDiagram createSequenceDiagram(Element e){
		return null;
	}
	protected ClassDiagram createclassDiagram(Element e){
		return null;
	}
}

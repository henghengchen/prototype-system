package org.umlverification.xml2umlmodel;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.UMLFactory;

public class XML2UMLModel {

	public static boolean DEBUG = true;
	
	private String xml_file;
	private Model uml_model=null;
	
	public XML2UMLModel(String path) {
		this.xml_file=path;
		// TODO Auto-generated constructor stub
	}

    protected static void out(String format, Object... args) {
        if (DEBUG) {
            System.out.printf(format, args);
            if (!format.endsWith("%n")) {
                System.out.println();
            }
        }
    }

    protected static void err(String format, Object... args) {
        System.err.printf(format, args);
        if (!format.endsWith("%n")) {
            System.err.println();
        }
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
        
        for (Iterator<Element> it1 = root.elementIterator(); it1.hasNext();) {
        	//it1包含 document   model   extension
            Element element1 = it1.next();
            
            if(element1.getName().equals("Documentation")){

            }else if(element1.getName().equals("Model")) {  
            	for(Iterator<Element> it2=element1.elementIterator();it2.hasNext();) 
            	{
            		//it2包含 模型 
            		Element element2=it2.next();
            		//创建模型
            		Model model=createModel(element2.attributeValue("name"));
            		
            		for(Iterator<Element> it3=element2.elementIterator();it3.hasNext();) 
            		{
            			//it3包含包
            			Element element3=it3.next();
            			
            			Package model_package1=createPackage(model,element3.attributeValue("name"));
            			
                		for(Iterator<Element> it4=element3.elementIterator();it4.hasNext();) 
                		{
                			//it4包含包中所有图中元素
                			Element element4=it4.next();
                			if(element4.attributeValue("type").equals("uml:Class")) {
                				
                				org.eclipse.uml2.uml.Class class1=createClass(model_package1,element4.attributeValue("name"),false);
                				
                				for(Iterator<Element> it5=element4.elementIterator();it5.hasNext();)  {
                					Element element5=it5.next();
                					if(element5.getName().contentEquals("generalization")) {
                						//createGeneralization(class1,class1);
                					}
                				}
                			}else if(element4.attributeValue("type").equals("uml:StateMachine")) {
                				
                				//StateMachine s = createStateMachine(model_package1,element4.attributeValue("name"));
                				
                			}else if(element4.attributeValue("type").equals("uml:Trigger")) {
                				
                			}else {
                				
                			}
                		}
            		}
            	}
            }else {
            	for(Iterator<Element> it2=element1.elementIterator();it2.hasNext();) 
            	{
            		//it2包含 elements connectors primitivetypes profiles diagrams
            		Element element2=it2.next();
            		System.out.println(element2.getName());
            		if(element2.getName().equals("elements")){
            			
            		}else if(element2.getName().equals("connectors")){
            			
            		}else if(element2.getName().equals("primitivetypes")) {
            			
            		}else if(element2.getName().equals("profiles")) {
            			
            		}else if(element2.getName().equals("diagrams")) {
            			for(Iterator<Element> it3=element2.elementIterator();it3.hasNext();) {
            				//it3包含所有图
            				Element element3=it3.next();
                			for(Iterator<Element> it4=element3.elementIterator();it4.hasNext();) {
                				//it4包含单个图的属性
                				Element element4=it4.next();
                				System.out.println(element4.getName());
                				if(element4.getName().equals("elements")) {
                					for(Iterator<Element> it5=element4.elementIterator();it5.hasNext();) {
                						//it5包含单个图的所有元素
                						
                					}
                				}
                			}
            			}
            		}
            	}
            }
        }
        
		return uml_model;
	}
	
    protected static Model createModel(String name) {
        Model model = UMLFactory.eINSTANCE.createModel();
        model.setName(name);

        out("Model '%s' created.", model.getQualifiedName());

        return model;
    }
    
    protected static Package createPackage(Package nestingPackage, String name) {
        Package package_ = nestingPackage.createNestedPackage(name);

        out("Package '%s' created.", package_.getQualifiedName());

        return package_;
    }
    
    protected static org.eclipse.uml2.uml.Class createClass(Package package_, String name, boolean isAbstract) {
        org.eclipse.uml2.uml.Class class_ = package_.createOwnedClass(name, isAbstract);
        out("Class '%s' created.", class_.getQualifiedName());

        return class_;
    }
    
    protected static Generalization createGeneralization(Classifier specificClassifier, Classifier generalClassifier) {
        Generalization generalization = specificClassifier.createGeneralization(generalClassifier);

        out("Generalization %s --|> %s created.", specificClassifier.getQualifiedName(), generalClassifier.getQualifiedName());

        return generalization;
    }
    /*
    protected org.eclipse.uml2.uml.StateMachine createStateMachine(Package package_, String name) {
        StateMachine s = null;             
        PackageableElement p=package_.createPackagedElement(name, EcorePackage.getEClass());
        
        out("Class '%s' created.", p.getQualifiedName());
        
		return (StateMachine) p;
    }
    */
}

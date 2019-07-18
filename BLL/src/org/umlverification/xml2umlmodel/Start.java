package org.umlverification.xml2umlmodel;

import org.eclipse.uml2.uml.Model;

public class Start {

	public static XML2UMLModel xm=new XML2UMLModel("C:\\Users\\衡辰\\Desktop\\项目组\\项目实现情况\\UML-workspace\\UMLVerification\\testmodels\\test5.xml");

	public static void main(String[] args)
	{
		xm.getUMLModel();
	}
	
}

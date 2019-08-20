package org.ecsz.ui;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;



public class Start extends JFrame {

	public Start() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//MainFrame mf=new MainFrame();
		try
	    {
	        //org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
	        BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.osLookAndFeelDecorated;
	        //org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
	        UIManager.put("", false);
	    }
	    catch(Exception e)
	    {
	        //TODO exception
	    }
	}

}
//org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.FrameBorderStyle.osLookAndFeelDecorated
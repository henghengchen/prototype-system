package org.ecsz.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class MainFrame extends JFrame {

	public MainFrame() {
        setTitle("Java ��һ�� GUI ����");    //������ʾ���ڱ���
        setSize(400,200);    //���ô�����ʾ�ߴ�
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //�ô����Ƿ���Թر�
        JLabel jl=new JLabel("����ʹ��JFrame�ഴ���Ĵ���");    //����һ����ǩ
        Container c=getContentPane();    //��ȡ��ǰ���ڵ����ݴ���
        c.add(jl);    //����ǩ�����ӵ����ݴ�����
        setVisible(true);    //���ô����Ƿ�ɼ�
	}

}

import java.awt.*;
import java.awt.event.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
import java.io.*;
//import java.io.DataOutputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
import java.net.Socket;

import javax.swing.*;
import javax.swing.event.*;

import javax.swing.*;

public class SearchUI extends JFrame{
	JFrame owner;
	DataOutputStream toServer;
	DataInputStream fromServer;
	
	JButton quit = new JButton("退出");
	JLabel title = new JLabel("MyDictory");
	JTextField input = new JTextField(15);
	JButton search = new JButton("Search");
	JButton BD = new JButton();
	//JRadioButton BD = new JRadioButton();
	JRadioButton YD = new JRadioButton();
	JRadioButton BY = new JRadioButton();
	JTextArea BDText = new JTextArea(3,20);
	JTextArea YDText = new JTextArea(3,20);
	JTextArea BYText = new JTextArea(3,20);
	//JTextArea BDText2 = new JTextArea(2,20);
	
	public  SearchUI(JFrame owner) throws IOException{
		this.owner = owner;
		//title.setBounds(0, 20, 20, 5);
		//add(title);
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT,20,10));
		p1.add(new JLabel("input"));
		p1.add(input);
		p1.add(search);
		JPanel p2 = new JPanel();
		ImageIcon icon = new ImageIcon("kongxin.png");
		BD.setIcon(icon);
		p2.add(BD);
		p2.add(new JLabel("百度"));
		BDText.setLineWrap(true);//自动换行
		BDText.setWrapStyleWord(true);//根据单词换行，而不是根据字符换行
		BDText.setEditable(false);//不可编辑
		//BDText.setText("沈思远是一个大打打打打打打打打打打打打打打打打打打打打打打打打打打打打打打打打打打傻逼");
		JScrollPane bd = new JScrollPane(BDText);//添加滑动条
		p2.add(bd);
		//p2.add(BDText);
		JPanel p3 = new JPanel();
		p3.add(YD);
		p3.add(new JLabel("有道"));
		YDText.setLineWrap(true);
		YDText.setWrapStyleWord(true);
		YDText.setEditable(false);
		//YDText.setText("傻萌是一个大打打打打打打打打打打打打打打打打打打打打打打打打打打打打打打打打打打傻逼");
		JScrollPane yd = new JScrollPane(YDText);
		p3.add(yd);
		//p3.add(YDText);
		JPanel p4 = new JPanel();
		p4.add(BY);
		p4.add(new JLabel("必应"));
		BYText.setLineWrap(true);
		BYText.setWrapStyleWord(true);
		BYText.setEditable(false);
		//BYText.setText("沈玉是一个大打打打打打打打打打打打打打打打打打打打打打打打打打打打打打打打打打打傻逼");
		JScrollPane by = new JScrollPane(BYText);
		p4.add(by);
		//p4.add(BYText);
		JPanel p5 = new JPanel();
		p5.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		p5.add(p1);
		p5.add(p2);
		p5.add(p3);
		p5.add(p4);
		JPanel p6 = new JPanel();//添加标题
		p6.add(title);
		JPanel p7 = new JPanel();//添加退出按钮
		p7.add(quit);
		add(p6,BorderLayout.NORTH);
		add(p5,BorderLayout.CENTER);
		add(p7,BorderLayout.SOUTH);
		
		this.setTitle("Dictionary");
		this.setSize(450, 350);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		quit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(SearchUI.this, "退出成功!");  
				owner.setVisible(true);
				SearchUI.this.dispose();
	           // System.exit(0); 
			}
		}
		);
		
		try {
			Socket socket = new Socket("localhost",9083);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException ex){
			JOptionPane.showMessageDialog(SearchUI.this, ex.toString()); ;
		}
		
		search.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					String content = input.getText();
					toServer.writeUTF(content);
					toServer.flush();
					String result = fromServer.readUTF();
					BDText.setText(result);
					BDText.repaint();
					YDText.setText(result);
					YDText.repaint();
					BYText.setText(result);
					BYText.repaint();
				}
				catch(IOException ex){
					//System.out.println("错误"+'\n');
					System.err.println(ex);
				}
			}
		}
		);
	}
	
	//public static void main(String[] args) throws IOException{
		//SearchUI x = new SearchUI(owner);
	//}
}

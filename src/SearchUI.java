import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
import java.io.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.net.Socket;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.*;

public class SearchUI extends JFrame{
	JFrame owner;
	String user;
	DataOutputStream toServer;
	DataInputStream fromServer;
	FileInputStream fis=null;//文件流
	FileOutputStream fos=null;
	Associate Ass;
	String path;//单词卡保存的路径
	Socket socket;
	int sendornot=0;
	
	String SearchWord;
	int likeBD=0;//爱词的点赞状态
	int likeYD=0;//有道的点赞状态
	int likeBY=0;//海词的点赞状态
	JButton quit = new JButton("退出");
	JLabel title = new JLabel("MyDictory");
	JTextField input = new JTextField(18);
	JButton search = new JButton();
	//JButton search = new JButton("Search");
	//JButton BD = new JButton();
	JRadioButton BD = new JRadioButton();
	JRadioButton YD = new JRadioButton();
	JRadioButton BY = new JRadioButton();
	JTextArea BDText = new JTextArea(5,30);
	JTextArea YDText = new JTextArea(5,30);
	JTextArea BYText = new JTextArea(5,30);
	JComboBox otherUser=new JComboBox();
	JButton send = new JButton("发送单词卡");
	JButton message = new JButton("查看消息");
	JList jlst=new JList();
	JScrollPane jsp=new JScrollPane(jlst);
	String type1,type2,type3;//用于传输图片的保存当前翻译的结果
	
	//JTextArea BDText2 = new JTextArea(2,20);
	
	public  SearchUI(JFrame owner,String user,DataOutputStream toServer,DataInputStream fromServer,Socket socket) throws IOException{
		//path = "//Users//wfsm//Desktop//"+user;
		search.setIcon(new ImageIcon("搜索.png"));
		this.socket=socket;
		this.toServer=toServer;
		this.fromServer=fromServer;
		SearchWord="";
		this.owner = owner;
		this.user = user;
		path = "//Users//wfsm//Desktop//"+user;
		Ass = new Associate();
		Ass.readfile();
		//title.setBounds(0, 20, 20, 5);
		//add(title);
		//jlst.setFixedCellHeight(43);
		//jlst.setFixedCellWidth(80);
		type1=type2=type3= "";
		jsp.setPreferredSize(new Dimension(100,30));
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT,30,10));
		JLabel tcu= new JLabel();
		tcu.setIcon(new ImageIcon("已登陆.png"));
		p1.add(tcu);
		p1.add(new JLabel(user));
		//p1.add(new JLabel("当前用户:"+user));
		p1.add(new JLabel("input"));
		p1.add(input);
		p1.add(search);
		//p1.add(new JLabel(user));
		JPanel p2 = new JPanel();
		ImageIcon icon = new ImageIcon("心1.png");
		BD.setIcon(icon);
		p2.add(BD);
		p2.add(new JLabel("爱词"));
		BDText.setLineWrap(true);//自动换行
		BDText.setWrapStyleWord(true);//根据单词换行，而不是根据字符换行
		BDText.setEditable(false);//不可编辑
		//BDText.setText("沈思远是一个大傻逼");
		JScrollPane bd = new JScrollPane(BDText);//添加滑动条
		p2.add(bd);
		//p2.add(BDText);
		JPanel p3 = new JPanel();
		YD.setIcon(new ImageIcon("心1.png"));
		p3.add(YD);
		p3.add(new JLabel("有道"));
		YDText.setLineWrap(true);
		YDText.setWrapStyleWord(true);
		YDText.setEditable(false);
		//YDText.setText("傻萌是一个大傻逼");
		JScrollPane yd = new JScrollPane(YDText);
		p3.add(yd);
		//p3.add(YDText);
		JPanel p4 = new JPanel();
		BY.setIcon(new ImageIcon("心1.png"));
		p4.add(BY);
		p4.add(new JLabel("海词"));
		BYText.setLineWrap(true);
		BYText.setWrapStyleWord(true);
		BYText.setEditable(false);
		//BYText.setText("沈玉是一个大傻逼");
		JScrollPane by = new JScrollPane(BYText);
		p4.add(by);
		//p4.add(BYText);
		otherUser.setEditable(true);
		//otherUser.addItem("141220085");
        //otherUser.addItem("141220086");
        //otherUser.addItem("141220087");
        JPanel p9 = new JPanel();
        p9.setLayout(new FlowLayout(FlowLayout.LEFT,25,10));
        p9.add(otherUser);
        p9.add(send);
        p9.add(message);
		JPanel p5 = new JPanel();
		p5.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		//p5.add(p1);
		p5.add(p2);
		p5.add(p3);
		p5.add(p4);
		p5.add(p9);
		JPanel p6 = new JPanel();//添加标题
		p6.add(p1);
		JPanel p7 = new JPanel();//添加退出按钮
		p7.add(quit);
		JPanel jsp2=new JPanel(new BorderLayout());
		jsp2.add(new JLabel("联想及纠错") , BorderLayout.NORTH);
		jsp2.add(jsp,BorderLayout.CENTER);
		//add(p9,BorderLayout.EAST);
		add(p6,BorderLayout.NORTH);
		add(jsp2,BorderLayout.WEST);
		add(p5,BorderLayout.CENTER);
		add(p7,BorderLayout.SOUTH);
		
		this.setTitle("Dictionary");
		this.setSize(600, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		//每过两秒询问服务器是否有消息
		int delay=5000;
		ActionListener taskEvent=new ActionListener(){
			public void actionPerformed(ActionEvent aen){
				//count++;
				try {
					//System.out.println("visit");
					toServer.writeUTF("HaveMessage");
					toServer.writeUTF(user);
					toServer.flush();
					//String have=fromServer.readUTF();
					///////////////////////////////
					String currentUsers=fromServer.readUTF();
					otherUser.removeAllItems();
					if(currentUsers.equals("null")){
						;
					}
					else{
					String[] CU=currentUsers.split(" ");
					//otherUser.removeAllItems();
					for(int i=0;i<CU.length;i++)
					{
						if(!CU[i].equals(user))
							otherUser.addItem(CU[i]);
					}
					}
					//////////////////////////////
					String have=fromServer.readUTF();
					if(have.equals("yes")){
						System.out.println("开始接收");
						//String word=fromServer.readUTF();
						//String trans1=fromServer.readUTF();
						//String trans2=fromServer.readUTF();
						//String trans3=fromServer.readUTF();
						//sendornot=1;
						createDir(path);
						String[] FileList = new File(path).list();
						String tn=""+(FileList.length);
						DataInputStream dis=new DataInputStream(socket.getInputStream());
						fos=new FileOutputStream(new File(path+"//"+tn+".jpg"));
						byte[] inputByte =new byte[1024];
						//byte[] inputByte =new byte[64];
						int length=0;
						length=dis.read(inputByte,0,inputByte.length);
						while(length>1023){
							System.out.println(length);
							fos.write(inputByte, 0, length);
							fos.flush();
							length=dis.read(inputByte,0,inputByte.length);
						}
						System.out.println(length);
						fos.write(inputByte, 0, length);
						fos.flush();
						System.out.println("接收完成");
						//sendornot=0;
						//CallOn.start();
						//CreateImage(word,trans1,trans2,trans3,path);
						//createDir("//User//wfsm//Desktop//"+user);
						JOptionPane.showMessageDialog(SearchUI.this, "有新消息");
					}
					else
						;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//String t=otherUser.getSelectedItem().toString();
				//otherUser.addItem(count);
				//System.out.println(t);
			}
		};
		Timer CallOn=new Timer(delay,taskEvent);
		//if(sendornot==0)
		//{//Timer CallOn=new Timer(delay,taskEvent);
		CallOn.start();
		//sendornot=1;
		//}
		
		input.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void changedUpdate(DocumentEvent e) {
                String s = input.getText().trim();
                if(!s.equals(""))
                {
                	if(s.matches("[a-zA-Z _-]+"))
                	{
                		String[] result=Ass.FindSimilar(s);
                		// String[] result=Ass.ErrorCorrection(s);
                		Vector words;
                		words = new Vector();
                		for(int i=0;i<result.length;i++)
                			words.add(result[i]);
                		jlst.setListData(words);
                	}
                	else
                		JOptionPane.showMessageDialog(SearchUI.this, "请勿输入中文数字及其他特殊字符");
                }
                //key=s;
                //search_imagine(key,dic);
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
            	String s = input.getText().trim();
            	if(!s.equals(""))
            	{
            		if(s.matches("[a-zA-Z _-]+")){
            			String[] result=Ass.FindSimilar(s);
            			//String[] result=Ass.ErrorCorrection(s);
            			Vector words;
            			words = new Vector();
            			for(int i=0;i<result.length;i++)
            				words.add(result[i]);
            			jlst.setListData(words);
            		}
            		else
            			JOptionPane.showMessageDialog(SearchUI.this, "请勿输入中文数字及其他特殊字符");
            	}
            }    
            @Override
            public void removeUpdate(DocumentEvent e) {
            	String s = input.getText().trim();
            	if(!s.equals(""))
            	{
            		if(s.matches("[a-zA-Z _-]+")){
            			String[] result=Ass.FindSimilar(s);
            			//	String[] result=Ass.ErrorCorrection(s);
            			Vector words;
            			words = new Vector();
            			for(int i=0;i<result.length;i++)
            				words.add(result[i]);
            			jlst.setListData(words);
            		}
            	}
            }
        });
		
		jlst.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				Object keyx=jlst.getSelectedValue();
	            String key1;
	            if(keyx!=null){
	                key1 = jlst.getSelectedValue().toString();
	                input.setText(key1);
	                //key=key1;
	                //search_meaning(key, dic);
	                //jtf.setText(key);
	            }
			} 
		});
		
		message.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new ViewImage(path);
			}
		});
		
		send.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//sendornot=1;
				String sendpath="//Users//wfsm//Desktop//Image";
				sendpath=CreateImage(input.getText(),type1,type2,type3,sendpath);//修改过的地方
				File f=new File(sendpath);
				//fis=new FileInputStream(f);
				
				String sm="card";
				try {
					//CallOn.stop();
					sendornot=0;
					fis=new FileInputStream(f);
					byte[] sendBytes=null;
					sendBytes=new byte[1024];
					//sendBytes=new byte[64];
					toServer.writeUTF(sm);
					//toServer.flush();
					//toServer.writeUTF(input.getText());
					toServer.writeUTF(otherUser.getSelectedItem().toString());
					//CallOn.stop();
					//toServer.flush();
					int length=0;
					length=fis.read(sendBytes,0,sendBytes.length);
					while(length>1023){
						toServer.write(sendBytes, 0, length);
						toServer.flush();
						length=fis.read(sendBytes,0,sendBytes.length);
					}
					toServer.write(sendBytes, 0, length);
					toServer.flush();
					//toServer.writeUTF("abc");
					//toServer.writeUTF(type1);
					//toServer.writeUTF(type2);
					//toServer.writeUTF(type3);
					toServer.flush();
					//File f=new File()
					System.out.println("发送成功");
					fis.close();
					//sendornot=0;
					//CallOn.start();
					JOptionPane.showMessageDialog(SearchUI.this, "发送成功!");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		BD.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(likeBD==0)
				{
					BD.setIcon(new ImageIcon("心2.png"));
					BD.repaint();
					likeBD=1;
				}
				else
				{
					BD.setIcon(new ImageIcon("心1.png"));
					BD.repaint();
					likeBD=0;
				}
				
				//向服务器发送点赞状态
				try {
					String content = ""+likeBD+likeYD+likeBY;
					toServer.writeUTF("like");
					toServer.writeUTF(SearchWord);
					toServer.writeUTF(content);
					toServer.flush();
					String result=fromServer.readUTF();
					if(result.equals("success"))
						;
				}
				catch(IOException ex){
					//System.out.println("错误"+'\n');
					System.err.println(ex);
				}
			}
		}
		);
		
		YD.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//YD.setIcon(new ImageIcon("IMG2.gif"));
				if(likeYD==0)
				{
					YD.setIcon(new ImageIcon("心2.png"));
					YD.repaint();
					likeYD=1;
				}
				else{
					YD.setIcon(new ImageIcon("心1.png"));
					YD.repaint();
					likeYD=0;
				}
				//向服务器发送点赞状态
				try {
					String content = ""+likeBD+likeYD+likeBY;
					toServer.writeUTF("like");
					toServer.writeUTF(SearchWord);
					toServer.writeUTF(content);
					toServer.flush();
					String result=fromServer.readUTF();
					if(result.equals("success"))
						;
				}
				catch(IOException ex){
					//System.out.println("错误"+'\n');
					System.err.println(ex);
				}
			}
		}
		);
		
		BY.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(likeBY==0)
				{
					BY.setIcon(new ImageIcon("心2.png"));
					BY.repaint();
					likeBY=1;
				}
				else
				{
					BY.setIcon(new ImageIcon("心1.png"));
					BY.repaint();
					likeBY=0;
				}
				//向服务器发送点赞状态
				try {
					String content = ""+likeBD+likeYD+likeBY;
					toServer.writeUTF("like");
					toServer.writeUTF(SearchWord);
					toServer.writeUTF(content);
					toServer.flush();
					String result=fromServer.readUTF();
					if(result.equals("success"))
						;
				}
				catch(IOException ex){
					//System.out.println("错误"+'\n');
					System.err.println(ex);
				}
			}
		}
		);
		
		quit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					toServer.writeUTF("logout");
					toServer.writeUTF(user);
					toServer.flush();
					String t=fromServer.readUTF();
					if(t.equals("success"))
						;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(SearchUI.this, "退出成功!");  
				owner.setVisible(true);
				SearchUI.this.dispose();
				////////////////////
				CallOn.stop();
	           // System.exit(0); 
			}
		}
		);
		
		/*try {
			//Socket socket = new Socket("localhost",9083);
			Socket socket = new Socket("192.168.1.103",9083);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException ex){
			JOptionPane.showMessageDialog(SearchUI.this, ex.toString()); ;
		}*/
		
		input.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					//if(sendornot==0)
						//CallOn.start();
					likeBD=likeYD=likeBY=0;
					BD.setIcon(new ImageIcon("心1.png"));
					YD.setIcon(new ImageIcon("心1.png"));
					BY.setIcon(new ImageIcon("心1.png"));
					String content = input.getText().trim();
					SearchWord=content;
					toServer.writeUTF(content);
					toServer.flush();
					///////////////////////////
					//String find = "fail";
					String find=fromServer.readUTF();
					if(find.equals("fail")){
						if(!content.equals(""))
		            	{
		                //String[] result=Ass.FindSimilar(s);
		            	String[] result=Ass.ErrorCorrection(content);
		                Vector words;
		                words = new Vector();
		                for(int i=0;i<result.length;i++)
		                	words.add(result[i]);
		                jlst.setListData(words);
							///////////////////////////
						BDText.setText("很遗憾，未能找到释义");
						BDText.repaint();
						YDText.setText("很遗憾，未能找到释义");
						YDText.repaint();
						BYText.setText("很遗憾，未能找到释义");
						BYText.repaint();
		            	}
					}
					else if(find.equals("success")){
					String result1= fromServer.readUTF();
					String result2 = fromServer.readUTF();
					String result3 = fromServer.readUTF();
					String order=fromServer.readUTF();//接收顺序
					BDText.setText(result1);
					BDText.repaint();
					YDText.setText(result2);
					YDText.repaint();
					BYText.setText(result3);
					BYText.repaint();
					//p5.remove(p2);
					if(order.equals("123"))
					{
						type1="爱词："+BDText.getText();
						type2="有道："+YDText.getText();
						type3="海词："+BYText.getText();
					}
					else if(order.equals("132")){
						p5.remove(p3);p5.remove(p4);p5.remove(p9);
						p5.add(p4);p5.add(p3);p5.add(p9);
						type1="爱词："+BDText.getText();
						type3="有道："+YDText.getText();
						type2="海词："+BYText.getText();
					}
					else if(order.equals("213")){
						p5.remove(p2);p5.remove(p3);p5.remove(p4);p5.remove(p9);
						p5.add(p3);p5.add(p2);p5.add(p4);p5.add(p9);
						type2="爱词："+BDText.getText();
						type1="有道："+YDText.getText();
						type3="海词："+BYText.getText();
					}
					else if(order.equals("231")){
						p5.remove(p2);p5.remove(p3);p5.remove(p4);p5.remove(p9);
						/*p5.add(p3);p5.add(p4);p5.add(p2);*/p5.add(p4);p5.add(p2);p5.add(p3);p5.add(p9);
						type2="爱词："+BDText.getText();
						type3="有道："+YDText.getText();
						type1="海词："+BYText.getText();
					}
					else if(order.equals("312")){
						p5.remove(p2);p5.remove(p3);p5.remove(p4);p5.remove(p9);
						/*p5.add(p4);p5.add(p2);p5.add(p3);*/p5.add(p3);p5.add(p4);p5.add(p2);p5.add(p9);
						type3="爱词："+BDText.getText();
						type1="有道："+YDText.getText();
						type2="海词："+BYText.getText();
					}
					else{
						p5.remove(p2);p5.remove(p3);p5.remove(p4);p5.remove(p9);
						p5.add(p4);p5.add(p3);p5.add(p2);p5.add(p9);
						type3="爱词："+BDText.getText();
						type2="有道："+YDText.getText();
						type1="海词："+BYText.getText();
					}
					p5.setVisible(false);
					p5.setVisible(true);
					}
					//p5.repaint();
					//SearchUI.this.setVisible(false);
					//SearchUI.this.setVisible(true);
				}
				catch(IOException ex){
					//System.out.println("错误"+'\n');
					System.err.println(ex);
				}
			}
		}
		);
		
		search.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					likeBD=likeYD=likeBY=0;
					BD.setIcon(new ImageIcon("心1.png"));
					YD.setIcon(new ImageIcon("心1.png"));
					BY.setIcon(new ImageIcon("心1.png"));
					String prefix="search";
					String content = input.getText().trim();
					SearchWord=content;
					toServer.writeUTF(prefix);
					toServer.writeUTF(content);
					toServer.flush();
					String find=fromServer.readUTF();
					if(find.equals("fail")){
						if(!content.equals(""))
		            	{
		                //String[] result=Ass.FindSimilar(s);
		            	String[] result=Ass.ErrorCorrection(content);
		                Vector words;
		                words = new Vector();
		                for(int i=0;i<result.length;i++)
		                	words.add(result[i]);
		                jlst.setListData(words);
						BDText.setText("很遗憾，未能找到释义");
						BDText.repaint();
						YDText.setText("很遗憾，未能找到释义");
						YDText.repaint();
						BYText.setText("很遗憾，未能找到释义");
						BYText.repaint();
		            	}
					}
					if(find.equals("success")){
					String result1 = fromServer.readUTF();
					String result2 = fromServer.readUTF();
					String result3 = fromServer.readUTF();
					String order=fromServer.readUTF();//接收顺序
					//String order="321";
					BDText.setText(result1);
					BDText.repaint();
					YDText.setText(result2);
					YDText.repaint();
					BYText.setText(result3);
					BYText.repaint();
					if(order.equals("123")){
						type1="爱词："+BDText.getText();
						type2="有道："+YDText.getText();
						type3="海词："+BYText.getText();
					}
					else if(order.equals("132")){
						p5.remove(p3);p5.remove(p4);p5.remove(p9);
						p5.add(p4);p5.add(p3);p5.add(p9);
						type1="爱词："+BDText.getText();
						type3="有道："+YDText.getText();
						type2="海词："+BYText.getText();
					}
					else if(order.equals("213")){
						p5.remove(p2);p5.remove(p3);p5.remove(p4);p5.remove(p9);
						p5.add(p3);p5.add(p2);p5.add(p4);p5.add(p9);
						type2="爱词："+BDText.getText();
						type1="有道："+YDText.getText();
						type3="海词："+BYText.getText();
					}
					else if(order.equals("231")){
						p5.remove(p2);p5.remove(p3);p5.remove(p4);p5.remove(p9);
						/*p5.add(p3);p5.add(p4);p5.add(p2);*/p5.add(p4);p5.add(p2);p5.add(p3);p5.add(p9);
						type2="爱词："+BDText.getText();
						type3="有道："+YDText.getText();
						type1="海词："+BYText.getText();
					}
					else if(order.equals("312")){
						p5.remove(p2);p5.remove(p3);p5.remove(p4);p5.remove(p9);
						/*p5.add(p4);p5.add(p2);p5.add(p3);*/p5.add(p3);p5.add(p4);p5.add(p2);p5.add(p9);
						type3="爱词："+BDText.getText();
						type1="有道："+YDText.getText();
						type2="海词："+BYText.getText();
					}
					else{
						p5.remove(p2);p5.remove(p3);p5.remove(p4);p5.remove(p9);
						p5.add(p4);p5.add(p3);p5.add(p2);p5.add(p9);
						type3="爱词："+BDText.getText();
						type2="有道："+YDText.getText();
						type1="海词："+BYText.getText();
					}
					p5.setVisible(false);
					p5.setVisible(true);
					}
					//p5.repaint();
					//SearchUI.this.setVisible(false);
					//SearchUI.this.repaint();
					//SearchUI.this.setVisible(true);
				}
				catch(IOException ex){
					//System.out.println("错误"+'\n');
					System.err.println(ex);
				}
			}
		}
		);
	}
	
	
	public  String CreateImage(String word,String trans1,String trans2,String trans3,String filepath){
		//System.out.println("t");
		int imageWidth=400;
		int imageHeight=400;
		BufferedImage bi=new BufferedImage(400,400,BufferedImage.TYPE_INT_RGB);
		Graphics graphics=bi.getGraphics();
		//aphics graphics = image.getGraphics();
		 graphics.setColor(Color.WHITE);
		 graphics.fillRect(0, 0, imageWidth, imageHeight);
		 graphics.setColor(Color.BLACK);
		 Font fontTitle=new Font("楷体",Font.BOLD,35);
		 graphics.setFont(fontTitle);
		 Font font=new Font("宋体",Font.PLAIN,15);
		 graphics.setFont(font);
		 graphics.drawString(word, 50, 25);
		 //graphics.drawString(trans1, 50, 100);
		 //graphics.drawString(trans2, 50, 125);
		 //graphics.drawString(trans3, 50, 150);
		 int h=75;
		 int b=0;
		 while((b+25)<trans1.length())
		 {
			 String s=trans1.substring(b, b+25);
			 b=b+25;
			 graphics.drawString(s, 20,h);
			 h+=20;
		 }
		 if(b==0){
			 String s3=trans1.substring(0,trans1.length());
			 graphics.drawString(s3, 20, h);
			 h+=20;
		 }
		 else if(b!=0&&b<trans1.length())
		 {
			 String s2=trans1.substring(b,trans1.length());
			 graphics.drawString(s2, 20,h);
			 h+=20;
		 }
		  b=0;
		 while((b+25)<trans2.length())
		 {
			 String s=trans2.substring(b, b+25);
			 b=b+25;
			 graphics.drawString(s, 20,h);
			 h+=20;
		 }
		 if(b==0){
			 String s3=trans2.substring(0,trans2.length());
			 graphics.drawString(s3, 20, h);
			 h+=20;
		 }
		 else if(b!=0&&b<trans2.length())
		 {
			 String s2=trans2.substring(b,trans2.length());
			 graphics.drawString(s2, 20,h);
			 h+=20;
		 }
		 //int h=75;
		 b=0;
		 while((b+25)<trans3.length())
		 {
			 String s=trans3.substring(b, b+25);
			 b=b+25;
			 graphics.drawString(s, 20,h);
			 h+=20;
		 }
		 if(b==0){
			 String s3=trans3.substring(0,trans3.length());
			 graphics.drawString(s3, 20, h);
			 h+=20;
		 }
		 else if(b!=0&&b<trans3.length())
		 {
			 String s2=trans3.substring(b,trans3.length());
			 graphics.drawString(s2, 20,h);
			 h+=20;
		 }
		//g.setColor(Color.WHITE);
		///g.setFont(new Font("宋体",Font.PLAIN,25));
		//g.drawString("2", 0, 50);
		//System.out.println("t");
		 String p="";
		try {
			String[] FileList=new File(filepath).list();
			//String tn=new String();
			String tn=""+(FileList.length);
			//String tn=""+(FileList.length+1);
			ImageIO.write(bi, "jpg", new File(filepath+"//"+tn+".jpg"));
			p = filepath+"//"+tn+".jpg";
			//return p;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
	
	public static boolean createDir(String destDirName) {
	     File dir = new File(destDirName);
	     if(dir.exists()) {
	     // System.out.println("创建目录" + destDirName + "失败，目标目录已存在！");
	      return false;
	     }
	     if(!destDirName.endsWith(File.separator))
	      destDirName = destDirName + File.separator;
	     // 创建单个目录
	     if(dir.mkdirs()) {
	     // System.out.println("创建目录" + destDirName + "成功！");
	      return true;
	     } else {
	      //System.out.println("创建目录" + destDirName + "成功！");
	      return false;
	     }
	 }
	//public static void main(String[] args) throws IOException{
		//SearchUI x = new SearchUI(owner);
	//}
}

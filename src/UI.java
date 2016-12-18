import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

import javax.swing.*;
import javax.swing.event.*;

import javax.swing.*;

public class UI extends JFrame {
	DataOutputStream toServer;
	DataInputStream fromServer;
	
	JFrame w=new JFrame();
	//JFrame w=new ImageJFrame();
	//getContentPane().add(w);
	JTextField user = new JTextField(15);
	JPasswordField password = new JPasswordField(15);
	JButton LogIn = new JButton("登陆:");
	JButton Register = new JButton("注册:");
	
	public UI() throws IOException{
		//getContentFrame().add(w);
		password.setEchoChar('*');
		JPanel p1 = new JPanel();
		p1.add(new JLabel("用户名:"));
		p1.add(user);
		JPanel p4 = new JPanel();
		p4.add(new Label("密码  :"));
		p4.add(password);
		JPanel p5 = new JPanel();
		//p5.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		p5.add(p1);
		p5.add(p4);
		w.add(p5,BorderLayout.CENTER);
		JPanel p2 = new JPanel();
		p2.add(LogIn);
		p2.add(Register);
		w.add(p2,BorderLayout.SOUTH);
		JPanel p3 = new JPanel();
		p3.add(new Label("Welcome to MyDictionary"));
		w.add(p3,BorderLayout.NORTH);
		w.setTitle("Dictionary");
		w.setSize(450, 350);
		w.setLocationRelativeTo(null);
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//JPanel panel = new JPanel();
		//BufferedImage bi =new BufferedImage( "心1.png");
		//panel.setBackgroundPainter(new ImagePainter(bi));
		//w.setContentPane(panel);
		//w.setBackground(new Color(99,00,180));
		//w.setBackground(Color.YELLOW);
		//paint(w);
		w.setVisible(true);
		
		try {
			//Socket socket = new Socket("localhost",9083);
			Socket socket = new Socket("192.168.1.103",9083);
			//Socket socket = new Socket("192.168.148.1",9083);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException ex){
			JOptionPane.showMessageDialog(UI.this, ex.toString()); ;
		}
		
		LogIn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String username = user.getText();
				String userpw = password.getText();
				//System.out.println(username);
				//System.out.println(user.getText().equals(""));
				//System.out.println(password.getText().equals(""));
				if(user.getText().equals(""))
				{
					JOptionPane.showMessageDialog(UI.this, "用户名不能为空!");
				}
				else if(password.getText().equals(""))
					JOptionPane.showMessageDialog(UI.this, "密码不能为空!");
				else
				{
					/*JOptionPane.showMessageDialog(UI.this, "登陆成功!");  
					w.setVisible(false);
				//RegisterUI x= new RegisterUI(UI.this);
					try {
						new SearchUI(w,user.getText()).setVisible(true);
					} catch (IOException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
					}*/
					String L="login";
					try{
						toServer.writeUTF(L);
						toServer.writeUTF(username);
						toServer.writeUTF(userpw);
						toServer.flush();
						String result = fromServer.readUTF();
						
						if(result.equals("fail1")){
							JOptionPane.showMessageDialog(UI.this, "用户名不存在!");
						}
						else if(result.equals("fail2"))
							JOptionPane.showMessageDialog(UI.this, "密码错误!");
						else{
							JOptionPane.showMessageDialog(UI.this, "登陆成功!");  
							w.setVisible(false);
						//RegisterUI x= new RegisterUI(UI.this);
							try {
								new SearchUI(w,user.getText(),toServer,fromServer).setVisible(true);
							} catch (IOException e1) {
							// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							}
						//user.setText("");
						//password.setText("");
						//user.repaint();
						//password.repaint();
               // System.exit(0); 
					}
					catch(IOException ex){
						System.err.println(ex);
					}
				}
				user.setText("");
				password.setText("");
				user.repaint();
				password.repaint();
			}
		}
		);
		
		Register.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//if(e.getSource()==Register)
				//{
				w.setVisible(false);
				//RegisterUI x= new RegisterUI(UI.this);
				try {
					new RegisterUI(w).setVisible(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//}
				//JOptionPane.showMessageDialog(UI.this, "登陆成功!");  
               // System.exit(0); 
			}
		}
		);
		
	}
	
	public static void main(String[] args) throws IOException{
		//Dictionary A = new Dictionary();
		//A.readfile();
		//String s = "a lot";
		//A.FindSimilar(s);
		//System.out.println(s);
		//A.PrintResult();
		//UI frame = new UI();
		new UI();
		/*frame.setTitle("Dictionary");
		frame.setSize(450, 350);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);*/
	}
	/*class ImageJFrame extends JFrame {

		protected void paintComponent(Graphics g) {
		super.paintComponents(g);
		ImageIcon icon = new ImageIcon("心2.jpg");
		g.drawImage(icon.getImage(), 0, 0, null);

		}
	}*/
}

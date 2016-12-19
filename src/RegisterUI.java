import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

import javax.swing.*;
import javax.swing.event.*;

import javax.swing.*;
public class RegisterUI extends JFrame{
	JFrame owner;
	JFrame RUI;
	DataOutputStream toServer;
	DataInputStream fromServer;
	
	JTextField user = new JTextField(15);
	JPasswordField password1 = new JPasswordField(15);
	JPasswordField password2 = new JPasswordField(15);
	//JButton LogIn = new JButton("登陆:");
	JButton Register = new JButton("注册:");
	
	public RegisterUI(JFrame owner) throws IOException{
		this.owner = owner;
		password1.setEchoChar('*');
		password2.setEchoChar('*');
		JPanel p1 = new JPanel();
		p1.add(new JLabel("用户名  :"));
		p1.add(user);
		JPanel p2 = new JPanel();
		p2.add(new JLabel("密码    :"));
		p2.add(password1);
		JPanel p3 = new JPanel();
		p3.add(new JLabel("确认密码:"));
		p3.add(password2);
		JPanel p4 = new JPanel();
		p4.add(p1);
		p4.add(p2);
		p4.add(p3);
		add(p4,BorderLayout.CENTER);
		JPanel p5 =new JPanel();
		p5.add(new JLabel("注册账号"));
		add(p5,BorderLayout.NORTH);
		JPanel p6 = new JPanel();
		//p6.add(LogIn);
		p6.add(Register);
		add(p6,BorderLayout.SOUTH);
		this.setTitle("Dictionary");
		this.setSize(450, 350);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	//}
	
		try {
			//Socket socket = new Socket("localhost",9083);
			Socket socket = new Socket("114.212.133.159",9083);
			//Socket socket = new Socket("192.168.148.1",9083);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException ex){
			JOptionPane.showMessageDialog(RegisterUI.this, ex.toString()); ;
		}
	
		
	Register.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			String username = user.getText();
			String pw1 = password1.getText();
			String pw2 = password2.getText();
			if(username.equals(""))
				JOptionPane.showMessageDialog(RegisterUI.this, "用户名不能为空"); 
			else if(pw1.equals("")||pw2.equals(""))
				JOptionPane.showMessageDialog(RegisterUI.this, "密码不能为空"); 
			if(pw1.equals(pw2)!=true)
				JOptionPane.showMessageDialog(RegisterUI.this, "两次密码输入不一致");
			else
			{
				String R = "register";
				try{
					toServer.writeUTF(R);
					toServer.writeUTF(username);
					toServer.writeUTF(pw1);
					toServer.flush();
					String result = fromServer.readUTF();
					if(result.equals("fail"))
						JOptionPane.showMessageDialog(RegisterUI.this, "用户名已存在"); 
					//if(pw1.equals(pw2)!=true)
						//JOptionPane.showMessageDialog(RegisterUI.this, "两次密码输入不一致");
					else{
						JOptionPane.showMessageDialog(RegisterUI.this, "注册成功");  
						owner.setVisible(true);
						RegisterUI.this.dispose();
					}
				}
				catch(IOException ex){
					//System.out.println("错误"+'\n');
					System.err.println(ex);
				}
			}
			user.setText("");
			password1.setText("");
			password2.setText("");
			user.repaint();
			password1.repaint();
			password2.repaint();
           // System.exit(0); 
		}
	}
	);
	//public static void main(String[] args)throws IOException{
		//RegisterUI t = new RegisterUI();
	//}
}
}

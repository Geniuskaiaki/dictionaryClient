import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Server extends JFrame{
	private JTextArea jta = new JTextArea();
	//private String user;
	private String user,word,trans1,trans2,trans3;
	
	public static void main(String[] args){
		new Server();
	}
	
	public Server(){
		user=word=trans1=trans2=trans3="";
		setLayout(new BorderLayout());
		add(new JScrollPane(jta),BorderLayout.CENTER);
		setTitle("Server");
		setSize(500,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		try{
			int ClientNo=1;
			ServerSocket serverSocket = new ServerSocket(9083);
			jta.append("Server started at " + new Date() + '\n');
			while(true){
			Socket socket = serverSocket.accept();
			jta.append("Client " + ClientNo + ":");
			//DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
			//DataOutputStream ouputToClient = new DataOutputStream(socket.getOutputStream());
			HandleAClient task = new HandleAClient(socket);
			new Thread(task).start();
			ClientNo++;
			//while(true){
				//String word = inputFromClient.readUTF();
				
				//对接收到的单词进行处理
				//String result;
				//ouputToClient.writeUTF(word);
				//jta.append("success!"+'\n');
				
			}
		}
		catch(IOException ex){
			System.err.println(ex);
		}
	}
	
	class HandleAClient implements Runnable{
		private Socket socket;
		
		public HandleAClient(Socket socket){
			this.socket = socket;
		}
		
		public void run(){
			try{
				DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream ouputToClient = new DataOutputStream(socket.getOutputStream());
				while(true){
					String type = inputFromClient.readUTF();
					if(type.equals("card"))
					{
						word=inputFromClient.readUTF();
						user=inputFromClient.readUTF();
						trans1=inputFromClient.readUTF();
						trans2=inputFromClient.readUTF();
						trans3=inputFromClient.readUTF();
					}
					if(type.equals("HaveMessage"))
					{
						String user2=inputFromClient.readUTF();
						if(user2.equals(user))
						{
							ouputToClient.writeUTF("yes");
							ouputToClient.writeUTF(word);
							//ouputToClient.writeUTF(user);
							ouputToClient.writeUTF(trans1);
							ouputToClient.writeUTF(trans2);
							ouputToClient.writeUTF(trans3);
							user="";
						}
						else
							ouputToClient.writeUTF("no");
					}
					//对接收到的单词进行处理
					//String result;
					//ouputToClient.writeUTF(word);
					jta.append("success!"+'\n');
				}
			}
			catch(IOException e){
				System.err.println(e);
			}
		}
	}

}

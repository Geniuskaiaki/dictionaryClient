import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ViewImage extends JFrame{
	JLabel image = new JLabel();
	JButton last = new JButton("上一张");
	JButton next = new JButton("下一张");
	JButton quit = new JButton("退出");
	String[] FileList;
	int num;
	String path;
	public ViewImage(String path){
		//num=0;
		this.path=path;
		FileList=new File(path).list();
		num=FileList.length-1;
		if(num==-1)
			num=0;
		ImageIcon vimage=new ImageIcon(path+"//"+FileList[num]);
		image.setIcon(vimage);
		JPanel jp=new JPanel();
		jp.add(image);
		JPanel jp2=new JPanel();
		jp2.add(last);
		jp2.add(next);
		jp2.add(quit);
		add(jp,BorderLayout.NORTH);
		add(jp2,BorderLayout.SOUTH);
		this.setTitle("view word cards");
		this.setSize(500, 525);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		last.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(num==0)
					num=FileList.length-1;
				else
					num=num-1;
				//vimage=new ImageIcon(path+"//"+FileList[num]);
				image.setIcon(new ImageIcon(path+"//"+FileList[num]));
				image.repaint();
			}
		});
		
		next.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(num==(FileList.length-1))
					num=0;
				else
					num=num+1;
				//vimage=new ImageIcon(path+"//"+FileList[num]);
				image.setIcon(new ImageIcon(path+"//"+FileList[num]));
				image.repaint();
			}
		});
		
		quit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(ViewImage.this, "退出成功!");  
				//owner.setVisible(true);
				ViewImage.this.dispose();
				//CallOn.stop();
	           // System.exit(0); 
			}
		}
		);
	}
	
/*	public static void main(String[] args){
		new ViewImage(  "//Users//wfsm//Desktop//Image");
	}
	*/
}

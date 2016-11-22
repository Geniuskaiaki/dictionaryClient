import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.*;

import javax.swing.*;

public class SearchUI extends JFrame{
	JLabel title = new JLabel("MyDictory");
	
	public  SearchUI() throws IOException{
		title.setBounds(0, 20, 20, 5);
		add(title);
		this.setTitle("Dictionary");
		this.setSize(450, 350);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public static void main(String[] args) throws IOException{
		SearchUI x = new SearchUI();
	}
}

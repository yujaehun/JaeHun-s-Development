/**
 * A panel for signing up a new account for this great game
 * 
 * @author 2016311221 YoungHoon Lim & 2016313746 JaeHun Yu
 * 
 */
package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

public class Sign_up extends JPanel {
	private JTextField tf = new JTextField(12);
	private JLabel idLabel = new JLabel("ID: ");
	private JLabel pwLabel = new JLabel("PW: ");
	private JLabel ckpw = new JLabel("check PW:");
	private JButton sign_up = new JButton("Sign Up");
	private JTextField pf = new JTextField(10);
	private JTextField ckpf = new JTextField(10);
	private String user_id, user_pw;
	private Main m;
	/**
	 * 
	 * @param m same as the comment in the Login.java
	 * @param writeIDPW same as the comment in the Login.java
	 * @param printIDPW same as the comment in the Login.java
	 * @param tmp same as the comment in the Login.java
	 */
	public Sign_up(Main m, TemporaryData tmp){
		setSize(400,450);
		setLayout(null);
		idLabel.setBounds(50, 75, 20, 20);
		pwLabel.setBounds(45, 100, 30, 20);
		tf.setBounds(75, 75, 100, 20);
		pf.setBounds(75,100,100,20);
		sign_up.setBounds(75, 150, 100, 50);

		add(idLabel);
		add(pwLabel);
		add(ckpw);
		add(tf);
		add(pf);
		add(sign_up);

		sign_up.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				user_id = tf.getText();
				user_pw = pf.getText();
				boolean idchk = false;
				boolean pwchk = false;
				boolean login = false;
				if(user_id.length()>=17||user_id.length()==0){
					JLabel printIDError = new JLabel("Your ID.length()>=17 ok? rewrite ID");
					add(printIDError);
				}
				else idchk = true;
				if(user_pw.length()>=20||user_pw.length()==0){
					JLabel printPWError = new JLabel("Your pw.length() >=20 ok? rewrite pw");
					add(printPWError);
				}
				else pwchk = true;
				if(idchk&&pwchk){
					try {
						Socket soc;
						soc = new Socket("localhost", 5001);
						DataInputStream printIDPW = new DataInputStream(soc.getInputStream());
						DataOutputStream writeIDPW = new DataOutputStream(soc.getOutputStream());
						
						writeIDPW.writeUTF(user_id);
						writeIDPW.writeUTF(user_pw);
						writeIDPW.writeUTF("signup");
						String loginsuccess = printIDPW.readUTF();
						if(loginsuccess.equals("Making your new profile!")) login = true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(login){
					System.out.println("asdfasdfasd");
					tmp.setID(user_id);
					tmp.setLogin(true);
					m.getCardLayout().show(m.getContentPane(), "home");
				}
			}
		});
	}
}

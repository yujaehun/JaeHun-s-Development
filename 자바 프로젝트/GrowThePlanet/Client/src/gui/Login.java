/**
 * A panel for logging in to the game home
 * 
 * @author 2016311221 YoungHoon Lim & 2016313746 JaeHun Yu
 * 
 */
package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Login extends JPanel {
	private JTextField tf = new JTextField(12);
	private Font ft = new Font("Gothic",Font.PLAIN,20);
	private JLabel idLabel = new JLabel("ID: ");
	private JLabel pwLabel = new JLabel("PW: ");
	private JLabel pz_lg = new JLabel("Please Login!");
	private JTextField pf = new JTextField(10);
	private JButton login = new JButton("Login");
	private JButton sign_up = new JButton("Sing up");
	private String user_id, user_pw;
	private Main m;
	/**
	 * 
	 * @param m this is the Main frame object
	 * @param writeIDPW data output stream for writting id,pw and state
	 * @param printIDPW data input stream for getting the successness of loging in
	 * @param tmp the shared obejct for main object
	 */
	public Login(Main m, TemporaryData tmp){

		setSize(400,450);
		setLayout(null);
		pz_lg.setBounds(75, 15, 200, 50);
		pz_lg.setFont(ft);
		idLabel.setBounds(50, 75, 20, 20);
		pwLabel.setBounds(45, 100, 30, 20);
		tf.setBounds(75, 75, 100, 20);
		pf.setBounds(75, 100, 100, 20);
		login.setBounds(190, 75, 75, 45);
		sign_up.setBounds(75, 135, 100, 40);


		sign_up.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				m.getCardLayout().show(m.getContentPane(), "signup");
			}
		});

		login.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){

				//log in parts
				user_id = tf.getText();
				user_pw = pf.getText();
				boolean idchk = false;
				boolean pwchk = false;
				boolean re_enterb = true;
				boolean login = false;
				if(user_id.length()>=17||user_id.length()==0){
					String printIDError = "Your ID.length()>=17 ok? rewrite ID";
					JLabel statusLabel = new JLabel(); 
					statusLabel.setText(printIDError);
				}
				else idchk = true;
				if(user_pw.length()>=20||user_pw.length()==0){
					JLabel printPWError = new JLabel("Your pw.length() >=20 ok? rewrite pw");
					printPWError.setBounds(300,300,300,50);
					add(printPWError);
				}
				else pwchk = true;
				if(idchk && pwchk){
					try {
						Socket soc;
						soc = new Socket("localhost", 5001);
						DataInputStream printIDPW = new DataInputStream(soc.getInputStream());
						DataOutputStream writeIDPW = new DataOutputStream(soc.getOutputStream());
						
						writeIDPW.writeUTF(user_id);
						writeIDPW.writeUTF(user_pw);
						writeIDPW.writeUTF("login");
						String loginsuccess = printIDPW.readUTF();
						if(loginsuccess.equals("Logged In")) login = true;

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					JLabel re_enter = new JLabel("아이디/비밀번호 재입력 해!");
					add(re_enter);
					re_enterb = false;
				}
				if(login&&re_enterb){
					tmp.setID(user_id);
					tmp.setLogin(true);
					m.getCardLayout().show(m.getContentPane(), "home");
				}
				else{
					JLabel re_enter = new JLabel("아이디/비밀번호 재입력 해!");
					add(re_enter);
				}
			}
		});	
		add(sign_up);
		add(login);
		add(pz_lg);
		add(idLabel);
		add(pwLabel);
		add(tf);
		add(pf);
	}
	public String getID(){
		return this.user_id;
	}
	public String getPW(){
		return this.user_pw;
	}

}
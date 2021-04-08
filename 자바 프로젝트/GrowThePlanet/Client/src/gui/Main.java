/**
 * Main class for make and change Frames and also setting the valuables.
 * 
 * @author 2016311221 YoungHoon Lim & 2016313746 JaeHun Yu
 * 
 */
package gui;

import java.awt.CardLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main extends JFrame {

	private long light = 0;
	private long weight = 0 ;
	private long level = 1;
	private long rate = 1;
	private long upgrade = 0;
	private long gravity = 1;
	private CardLayout cards = new CardLayout();
	private Login log;
	private Home hom;
	private Grow_Gravity gg;
	private int i=0;
	private boolean saveornot = false;

	/**
	 * 
	 * @param writeIDPW data output stream for giving the data for login
	 * @param printIDPW data input stream for getting the data for the main object
	 * @param tmp an object to share the same values 
	 */
	public Main(TemporaryData tmp){
		setTitle("Growing Planet!");
		setSize(400,500);
		getContentPane().setLayout(cards);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		log = new Login(this,tmp);
		getContentPane().add("login", log);
		getContentPane().add("signup", new Sign_up(this,tmp));
		setVisible(true);
	}
	public void setPanel(){
		if(i == 0){
			getContentPane().add("home", new Home(this)).repaint();
		}
		else if(i == 1){
			getContentPane().add("grow_gravity", new Grow_Gravity(this)).repaint();
		}
		else{
			getContentPane().add("buying element",new Buying_Element(this)).repaint();
		}
	}

/**
 * 
 * @return
 */
	public CardLayout getCardLayout(){
		return cards;
	}
	public String getID(){
		return this.log.getID();
	}
	public String getPW(){
		return this.log.getPW();
	}
	//set field data
	public void setLight(long l){
		this.light = l;
	}
	public void setWeight(long weight){
		this.weight = weight;
	}
	public void setLevel(long level){
		this.level = level;
	}
	public void setRate(long rate){
		this.rate = rate;
	}
	public void setUpgrade(long upgrade){
		this.upgrade = upgrade;
	}
	public void setGravity(long gravity){
		this.gravity = gravity;
	}
	//get field data
	public long getLight(){
		return this.light;
	}
	public long getWeight(){
		return this.weight;
	}
	public long getLevel(){
		return this.level;
	}
	public long getRate(){
		return this.rate;
	}
	public long getUpgrade(){
		return this.upgrade;
	}
	public long getGravity(){
		return this.gravity;
	}
	public void printAll(){
		System.out.println("Light amount: "+this.light);
		System.out.println("Weight amount: "+this.weight);
		System.out.println("Level amount: "+this.level);
		System.out.println("Rate amount: "+this.rate);
		System.out.println("Upgrade amount: "+this.upgrade);
		System.out.println("Gravity amount: "+this.gravity);
	}
	public int getPanelCode(){
		return this.i;
	}
	public void setPanelCode(int i){
		this.i = i;
	}
	public boolean getSaveOrNot(){
		return this.saveornot;
	}
	public void setSaveOrNot(boolean sss){
		this.saveornot = sss;
	}
}

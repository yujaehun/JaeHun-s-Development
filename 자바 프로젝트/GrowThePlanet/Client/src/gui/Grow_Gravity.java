/**
 * A panel for gravity upgrades for the mass inclining and level up
 * 
 * @author 2016311221 YoungHoon Lim & 2016313746 JaeHun Yu
 * 
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import planet.Upgrade;


public class Grow_Gravity extends JPanel {
	private JLabel level ;
	private JLabel weight ;
	private JLabel light;
	private JLabel ggup;
	private JButton home = new JButton("Home");
	private JButton gravity_up = new JButton("Gravity UP");
	private JButton buying_element = new JButton("Buying Element");
	private Main m;

	/**
	 * @param m this is the Main frame object
	 */
	public Grow_Gravity(Main m){
		ggup = new JLabel("LV "+m.getGravity()+"->"+(m.getGravity()+1) +"             " +"Price: "+(long)((1000000+(30000*m.getGravity()*Math.sqrt(2)))));
		level = new JLabel("LV "+ m.getLevel());
		weight = new JLabel("Mass "+ m.getWeight());
		light = new JLabel("Light "+ m.getLight());
		setSize(400,450);
		setLayout(null);
		level.setBounds(50, 0, 50, 50);
		weight.setBounds(150, 0, 100, 50);
		light.setBounds(275,0, 100, 50);
		gravity_up.setBounds(50,100,300,50);
		ggup.setBounds(100, 192, 200, 50);

		gravity_up.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				if(m.getGravity() < 1001){
					Upgrade upup = new Upgrade(m);
					boolean chkLight = upup.chkEnoughLight(100);
					if(chkLight){
						upup.GUpgrade();
					}
				}
				else if(m.getGravity() == 1000){
					JOptionPane.showMessageDialog(null, "No, Go Back, Click more (Full Upgrade)");
				}
			}
		});

		add(gravity_up);
		add(level);
		add(weight);
		add(light);
		add(ggup);

		home.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				m.setPanelCode(0);
				m.getCardLayout().show(m.getContentPane(), "home");
			}
		});
		home.setBounds(50,400,100,50);
		add(home);

		buying_element.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				m.setPanelCode(2);
				m.getCardLayout().show(m.getContentPane(), "buying element");
			}
		});
		buying_element.setBounds(200,400,150,50);
		add(buying_element);
		setVisible(true);
	}

}

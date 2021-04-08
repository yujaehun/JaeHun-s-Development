/**
 * a panel for buying the elements (upgrades for lights per second).
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

import planet.Element;
import planet.Upgrade;

public class Buying_Element extends JPanel {
	private JLabel level;
	private JLabel weight;
	private JLabel light;
	private JButton gravity_upgrade = new JButton("Gravity UP");
	private JButton home = new JButton("Home");
	private JButton hydrogen = new JButton("Hydrogen");
	private JButton helium = new JButton("Helium");
	private JButton carbon = new JButton("Carbon");
	private JButton nitrogen = new JButton("Nitrogen");
	private JButton oxygen = new JButton("Oxygen");
	private JLabel hy_up;
	private JLabel he_up;
	private JLabel ca_up;
	private JLabel ni_up;
	private JLabel ox_up;
	private Upgrade upup;
	private Element ele;

	/**
	 * @param m this is the Main frame object
	 */
	public Buying_Element(Main m){
		ele = new Element(m);
		setSize(400,450);
		setLayout(null);
		level = new JLabel("LV "+ m.getLevel());
		weight = new JLabel("Mass "+ m.getWeight());
		light = new JLabel("Light "+ m.getLight());
		hy_up = new JLabel("LV "+ele.getHLevel()+"->"+(ele.getHLevel()+1) +"             " +"Price: "+(int)(1+(10*ele.getHLevel()*Math.sqrt(2))));
		he_up = new JLabel("LV "+ele.getHeLevel()+"->"+(ele.getHeLevel()+1)+"             " + "Price: "+(int)(100+(40*ele.getHeLevel()*Math.sqrt(2))));
		ca_up = new JLabel("LV "+ele.getCLevel()+"->"+(ele.getCLevel()+1)+"             " + "Price: "+(int)(1000+(80*ele.getCLevel()*Math.sqrt(2))));
		ni_up = new JLabel("LV "+ele.getNLevel()+"->"+(ele.getNLevel()+1)+"             " + "Price: "+(int)(10000+(120*ele.getNLevel()*Math.sqrt(2))));
		ox_up = new JLabel("LV "+ele.getOLevel()+"->"+(ele.getOLevel()+1)+"             " + "Price: "+(int)(100000+(300*ele.getOLevel()*Math.sqrt(2))));
		level.setBounds(50, 0, 50, 50);
		weight.setBounds(150, 0, 100, 50);
		light.setBounds(275,0, 100, 50);
		hy_up.setBounds(150, 91, 200, 50);
		he_up.setBounds(150, 141, 200, 50);
		ca_up.setBounds(150, 192, 200, 50);
		ni_up.setBounds(150, 243, 200, 50);
		ox_up.setBounds(150, 295, 200, 50);
		add(level);
		add(weight);
		add(light);
		add(hy_up);
		add(he_up);
		add(ca_up);
		add(ni_up);
		add(ox_up);

		gravity_upgrade.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				m.setPanelCode(1);
				m.getCardLayout().show(m.getContentPane(), "grow_gravity");
			}
		});
		gravity_upgrade.setBounds(50,400,100,50);
		add(gravity_upgrade);

		home.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				m.setPanelCode(0);
				m.getCardLayout().show(m.getContentPane(), "home");
			}
		});
		home.setBounds(200,400,150,50);
		add(home);

		hydrogen.setBounds(20, 100, 80, 40);

		hydrogen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(ele.getHLevel() < 99){
					upup = new Upgrade(m);
					boolean chkLight = upup.chkEnoughLight(1);
					if(chkLight){
						upup.HUpgrade();
					}
				}
				else if(ele.getHLevel() == 99){
					JOptionPane.showMessageDialog(null, "No, Go Back (Full Upgrade)");
				}
			}
		});

		helium.setBounds(20, 150, 80, 40);

		helium.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(ele.getHeLevel()<99){
					upup = new Upgrade(m);
					boolean chkLight = upup.chkEnoughLight(2);
					if(chkLight){
						upup.HeUpgrade();
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "No, Go Back (Full Upgrade)");
				}
			}
		});

		carbon.setBounds(20, 200, 80, 40);

		carbon.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(ele.getCLevel()<99){
					upup = new Upgrade(m);
					boolean chkLight = upup.chkEnoughLight(4);
					if(chkLight){
						upup.CUpgrade();
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "No, Go Back (Full Upgrade)");
				}
			}
		});

		nitrogen.setBounds(20, 250, 80, 40);

		nitrogen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(ele.getNLevel()<99){
					upup = new Upgrade(m);
					boolean chkLight = upup.chkEnoughLight(7);
					if(chkLight){
						upup.NUpgrade();
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "No, Go Back (Full Upgrade)");
				}
			}
		});

		oxygen.setBounds(20, 300, 80, 40);

		oxygen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(ele.getOLevel()<9){
					upup = new Upgrade(m);
					boolean chkLight = upup.chkEnoughLight(11);
					if(chkLight){
						upup.OUpgrade();
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "No, Go Back (Full Upgrade)");
				}
			}
		});

		add(hydrogen);
		add(helium);
		add(carbon);
		add(nitrogen);
		add(oxygen);

		setVisible(true);
	}
}

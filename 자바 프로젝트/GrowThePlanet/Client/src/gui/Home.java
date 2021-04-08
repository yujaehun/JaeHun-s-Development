/**
 * A panel for main window for watching the great picture of the planet
 * 
 * @author 2016311221 YoungHoon Lim & 2016313746 JaeHun Yu
 * 
 */
package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Home extends JPanel {
	
	private JButton mainImage;
	private JButton gravity_upgrade = new JButton("Gravity UP");
	private JButton buying_element = new JButton("Buying Element");
	private JButton save = new JButton("Save");
	/**
	 * 
	 * @param m this is the Main frame object
	 */
	public Home(Main m){
		if(m.getLevel() == 1){
			ImageIcon asdf = new ImageIcon("./imageSource/1.jpeg");
			mainImage = new JButton(asdf);
		}
		else if(m.getLevel() == 2){
			ImageIcon asdf = new ImageIcon("./imageSource/2.jpeg");
			mainImage = new JButton(asdf);
		}else if(m.getLevel() == 3){
			ImageIcon asdf = new ImageIcon("./imageSource/3.jpeg");
			mainImage = new JButton(asdf);
		}else if(m.getLevel() == 4){
			ImageIcon asdf = new ImageIcon("./imageSource/4.jpeg");
			mainImage = new JButton(asdf);
		}else if(m.getLevel() == 5){
			ImageIcon asdf = new ImageIcon("./imageSource/5.jpeg");
			mainImage = new JButton(asdf);
		}else if(m.getLevel() == 6){
			ImageIcon asdf = new ImageIcon("./imageSource/6.jpeg");
			mainImage = new JButton(asdf);
		}
		JLabel level = new JLabel();
		JLabel weight = new JLabel();
		JLabel light = new JLabel();
		setSize(400,450);
		setLayout(null);
		level.setBounds(50, 1, 50, 50);
		weight.setBounds(150, 1, 100, 50);
		light.setBounds(275,1, 100, 50);
		mainImage.setBounds(22,40,356,350);
		
		level.setText("LV " +m.getLevel());
		weight.setText("Mass "+m.getWeight());
		light.setText("Light "+m.getLight());
		
		
		add(level);
		add(weight);
		add(light);
		add(mainImage);
		
		gravity_upgrade.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				m.setPanelCode(1);
				m.getCardLayout().show(m.getContentPane(), "grow_gravity");
			}
		});
		gravity_upgrade.setBounds(10,400,100,50);
		add(gravity_upgrade);
		
		buying_element.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				m.setPanelCode(2);
				m.getCardLayout().show(m.getContentPane(), "buying element");
			}
		});
		buying_element.setBounds(150,400,150,50);
		add(buying_element);
		
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				m.setSaveOrNot(true);
			}
		});
		save.setBounds(320,400,65,50);
		add(save);
		
		mainImage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				m.setWeight((long)(m.getWeight()+m.getGravity()*Math.PI));
			}
		});
		setVisible(true);
	}
	
}

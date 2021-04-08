/**
 * Client class which is used by users and handle the threads
 * 
 * @author 2016311221 YoungHoon Lim & 2016313746 JaeHun Yu
 * 
 */
package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import gui.Home;
import gui.Main;
import gui.TemporaryData;
import planet.Element;
import saveData.SaveData;


class Test extends Thread { 
	private Main character;
	private TemporaryData tmp = new TemporaryData();
	public void run () {

		SaveData someone = null;
		try {
			

			this.character = new Main(this.tmp);
			this.character.show();
			//read data from the save file 
			Socket afterLogin = null;
			while(true){
				if(this.tmp.getLogin()){
					afterLogin = new Socket("localhost", 5000);
					break;
				}
			}
			DataInputStream dis = new DataInputStream (afterLogin.getInputStream());
			ObjectInputStream in = new ObjectInputStream(dis);
			someone = (SaveData)in.readObject();
			character.setLight(someone.getLight());
			character.setWeight(someone.getWeight());
			character.setLevel(someone.getLevel());
			character.setRate(someone.getRate());
			character.setUpgrade(someone.getUpgrade());
			character.setGravity(someone.getGravity());
			//main code starts
			//gui랑 연결하는거 , 멀티쓰레딩
			
			while(true){
				this.character.getContentPane().removeAll();
				this.character.setPanel();
				chkLevel(character);
				Element element = new Element(this.character);
				element.setRate();
				long light = character.getLight();
				long rate = character.getRate();
				character.setLight((long)(light + rate*Math.sqrt(1.5)));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//saving progress...
				if(this.character.getSaveOrNot()){
					this.character.setSaveOrNot(false);
					
					Socket ss = new Socket("localhost", 5002);
					OutputStream os = ss.getOutputStream (); 
					ObjectOutputStream dos = new ObjectOutputStream (os);
					someone.setLight(character.getLight());
					someone.setWeight(character.getWeight());
					someone.setLevel(character.getLevel());
					someone.setRate(character.getRate());
					someone.setUpgrade(character.getUpgrade());
					someone.setGravity(character.getGravity());
					dos.writeObject(someone);
					System.exit(0);
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	public void chkLevel(Main ff){
		long level = ff.getLevel();
		if(ff.getWeight()>=1000*Math.pow(level, 5) && level <= 6){
			ff.setLevel(ff.getLevel()+1);
		}
	}

}

public class Clients {
	public static void main (String[] args) {
		new Test ().start ();
	}
}
/**
 * This thread gives the serialized object to the client and then get the save data back when the client want
 * 
 * @author 2016311221 YoungHoon Lim & 2016313746 JaeHun Yu
 * 
 */
package server;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import dataLoader.SaveFileReader;
import dataLoader.SaveFileWriter;
import saveData.SaveData;

public class GiveDataThread extends Thread{
	private Socket soc;
	private ServerSocket sss;
	private String ID,PW;
	private boolean login = false;
	private String id,pw;
	private TemporaryData tmp;
	public GiveDataThread (Socket soc,ServerSocket saveSocket, String ID, String PW, TemporaryData tmp) {
		this.soc = soc;
		this.sss = saveSocket;
		this.ID = ID;
		this.PW = PW;
		this.tmp = tmp;
	}

	SaveData someone = new SaveData(this.id, this.pw);
	public void run(){
		try{
			String Loader = "./savedata/"+ID;
			ArrayList<ArrayList<Long>> load;
			SaveFileReader sfr = new SaveFileReader(Loader);
			load = sfr.read();

			someone.setLight(load.get(1).get(0));
			someone.setWeight(load.get(1).get(1));
			someone.setLevel(load.get(1).get(2));
			someone.setRate(load.get(1).get(3));
			someone.setUpgrade(load.get(1).get(4));
			someone.setGravity(load.get(1).get(5));
			someone.setid(ID);
			someone.setPW(PW);

			OutputStream os = soc.getOutputStream (); 
			ObjectOutputStream dos = new ObjectOutputStream (os);
			dos.writeObject(someone);
			soc.close();

			//saving data
			Socket ss = this.sss.accept();
			DataInputStream dis = new DataInputStream (ss.getInputStream());
			ObjectInputStream ins = new ObjectInputStream(dis);
			ArrayList<ArrayList<?>> save = new ArrayList<ArrayList<?>>();
			someone = null;
			someone = (SaveData) ins.readObject();
			
			File ff = new File("./savedata/"+someone.getID());
			while(true){
				if(ff.exists() &&!ff.isDirectory()){
					System.out.println("Player: "+someone.getID()+" has left");
					SaveFileWriter sfw = new SaveFileWriter("./savedata/"+someone.getID());
					ArrayList<String> arr2 = new ArrayList<String>();
					arr2.add(someone.getPW());
					save.add(arr2);

					ArrayList<Long> baseField = new ArrayList<Long>();
					baseField.add(someone.getLight());
					baseField.add(someone.getWeight());
					baseField.add(someone.getLevel());
					baseField.add(someone.getRate());
					baseField.add(someone.getUpgrade());
					baseField.add(someone.getGravity());
					save.add(baseField);

					sfw.write(save);
					break;
				}
			}
			//ss.close();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}


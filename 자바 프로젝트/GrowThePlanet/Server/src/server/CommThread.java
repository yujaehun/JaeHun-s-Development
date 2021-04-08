/**
 * This is the main communication thread for login.
 * 
 * @author 2016311221 YoungHoon Lim & 2016313746 JaeHun Yu
 * 
 */
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import dataLoader.SaveFileReader;
import dataLoader.SaveFileWriter;
import saveData.SaveData;

public class CommThread extends Thread { 
	private Socket soc;
	private int id;
	private TemporaryData tmp;
	public CommThread (Socket soc, int id, TemporaryData tmp) {
		this.soc = soc;
		this.id = id; 
		this.tmp = tmp;
	}

	public void run () { 
		try {

			DataOutputStream IdPwChk = new DataOutputStream(soc.getOutputStream());
			DataInputStream dis = new DataInputStream(soc.getInputStream());
			String ID = dis.readUTF();
			String pw = dis.readUTF();
			String loginOrSign = dis.readUTF();
			ArrayList<ArrayList<?>> save = new ArrayList<ArrayList<?>>();
			File chkID = new File("./savedata/"+ID);
			if(loginOrSign.equals("login")&&chkID.exists() && !chkID.isDirectory()){
				SaveFileReader readUser = new SaveFileReader("./savedata/"+ID);
				String tmp = readUser.readPW();
				if(pw.equals(tmp)){
					IdPwChk.writeUTF("Logged In");
					this.tmp.setID(ID);
					this.tmp.setPW(pw);
					this.tmp.setLogin(true);
				}
				else{
					IdPwChk.writeUTF("Incorrect PW, please re-try!");
					this.tmp.setLogin(false);
				}
			}
			else if(loginOrSign.equals("signup")){
				if(chkID.exists() && !chkID.isDirectory()){
					IdPwChk.writeUTF("Already Existing ID!");
				}
				else{
					IdPwChk.writeUTF("Making your new profile!");
					SaveFileWriter sfw = new SaveFileWriter("./savedata/"+ID);

					ArrayList<String> arr2 = new ArrayList<String>();
					arr2.add(pw);
					save.add(arr2);

					ArrayList<Long> baseField = new ArrayList<Long>();
					baseField.add((long)1);
					baseField.add((long)0);
					baseField.add((long)1);
					baseField.add((long)0);
					baseField.add((long)1);
					baseField.add((long)1);
					save.add(baseField);

					sfw.write(save);
					this.tmp.setID(ID);
					this.tmp.setPW(pw);
					this.tmp.setLogin(true);
				}
			}
			else{
				IdPwChk.writeUTF("You need to sign up!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
}

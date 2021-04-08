/**
 * This is a server
 * 
 * @author 2016311221 YoungHoon Lim & 2016313746 JaeHun Yu
 * 
 */
package server;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Server {
	private static ArrayList<Thread> arr = new ArrayList<Thread> ();
	private static SimpleDateFormat sdfDate = new SimpleDateFormat ("yyy-MM-dd HH:mm:SSS");

	public static String getLog (String msg) {
		return "[" + sdfDate.format(new Date ()) + "] Server thread: " + msg;
	}

	public static void main (String[] args) { 
		ServerSocket ss = null;
		ServerSocket loginSocket = null;
		ServerSocket saveSocket = null;
		int id = 0;
		try {
			ss = new ServerSocket (5000); 
			loginSocket = new ServerSocket(5001);
			saveSocket = new ServerSocket(5002);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("server is ready");
		boolean chkQuit = false;
		while (true) {
			try {
				chkQuit = false;
				Socket soc = loginSocket.accept ();
				System.out.println(Server.getLog ("new connection arrived"));
				TemporaryData tmp = new TemporaryData();
				Thread t = new CommThread (soc, id++, tmp);
				t.start ();

				arr.add(t);
				
				Socket mmm = ss.accept();
				
				if(tmp.getLogin()){
					Thread t2 = new GiveDataThread(mmm,saveSocket, tmp.getID(), tmp.getPW(), tmp);
					t2.start();
					arr.add(t2);
				}
				Iterator<Thread> iter = arr.iterator ();
				while (iter.hasNext ()) {
					t = iter.next ();
					if (!t.isAlive ()) {
						iter.remove ();
					}
				}
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

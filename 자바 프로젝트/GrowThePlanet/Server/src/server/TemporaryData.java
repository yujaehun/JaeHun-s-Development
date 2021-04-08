/**
 * This is the shared object for server and threads
 * 
 * @author 2016311221 YoungHoon Lim & 2016313746 JaeHun Yu
 * 
 */
package server;

public class TemporaryData {
	private boolean login;
	private String ID,PW;
	public void setID(String ID){
		this.ID = ID;
	}
	public void setPW(String PW){
		this.PW = PW;
	}
	public void setLogin(boolean login){
		this.login = login;
	}
	public synchronized boolean getLogin(){
		return this.login;
	}
	public synchronized String getID(){
		return this.ID;
	}
	public synchronized String getPW(){
		return this.PW;
	}
}

/**
 * Shared class object for the main frame object
 * 
 * @author 2016311221 YoungHoon Lim & 2016313746 JaeHun Yu
 * 
 */
package gui;

public class TemporaryData {
	private boolean login;
	private String ID;
	public void setID(String ID){
		this.ID = ID;
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
}

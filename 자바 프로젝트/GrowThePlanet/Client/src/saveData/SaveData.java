/**
 * this class is for the serialization purpose, communicating with the server 
 * 
 * @author 2016311221 YoungHoon Lim & 2016313746 JaeHun Yu
 * 
 */
package saveData;

import java.io.Serializable;

import client.Clients;

public class SaveData implements Serializable {
	private long light;
	private long weight;
	private long level;
	private long rate;
	private long upgrade;
	private long gravity;
	private String id;
	private String pw;

	public SaveData (long light, long weight, long level, long rate, long upgrade, long gravity, String id, String pw) {
		this.light = light;
		this.weight = weight;
		this.level = level;
		this.rate = rate;
		this.upgrade = upgrade;
		this.gravity = gravity;
		this.id = id;
		this.pw = pw;
	}
	//@override
	public SaveData (String id, String pw){
		this.light = 1;
		this.weight = 0;
		this.level = 1;
		this.rate = 0;
		this.upgrade = 1;
		this.gravity = 1;
		this.id = id;
		this.pw = pw;
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
	public long getGravity() {
		return this.gravity;
	}
	public String getID(){
		return this.id;
	}
	public String getPW(){
		return this.pw;
	}
	//set field data
	public void setid(String long1){
		this.id = long1;
	}
	public void setLight(long light){
		this.light = light;
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
	public void setPW(String long11){
		this.pw = long11;
	}
	public void printAll(){
		System.out.println("Light amount: "+this.light);
		System.out.println("Weight amount: "+this.weight);
		System.out.println("Level amount: "+this.level);
		System.out.println("Rate amount: "+this.rate);
		System.out.println("Upgrade amount: "+this.upgrade);
		System.out.println("Gravity amount: "+this.gravity);
		System.out.println("id = "+this.id);
	}
}
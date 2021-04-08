/**
 * the class for controlling the inner data for the upgrading elements
 * 
 * @author 2016311221 YoungHoon Lim & 2016313746 JaeHun Yu
 * 
 */
package planet;

import gui.Main;

public class Element {
	private long upgrade;
	Hydrogen H = new Hydrogen();
	Helium He = new Helium();
	Nitrogen N = new Nitrogen();
	Carbon C = new Carbon();
	Oxygen O = new Oxygen();
	private Main planet;
	
	public Element(Main planet){
		this.planet = planet;
		this.upgrade = this.planet.getUpgrade();
		long temp;
		temp = H.calcLevel(this.upgrade);
		H.setHLevel(temp);
		temp = He.calcLevel(this.upgrade);
		He.setHeLevel(temp);
		temp = N.calcLevel(this.upgrade);
		N.setNLevel(temp);
		temp = C.calcLevel(this.upgrade);
		C.setCLevel(temp);
		temp = O.calcLevel(this.upgrade);
		O.setOLevel(temp);
	}
	
	public void setRate(){
		long rate = H.getHLevel()+2*He.getHeLevel()+14*N.getNLevel()+12*C.getCLevel()+16*O.getOLevel();
		this.planet.setRate(rate);
	}
	public long getUpdatedRate(){
		setRate();
		return this.planet.getRate();
	}
	public long getHLevel(){
		return this.H.getHLevel();
	}
	public long getHeLevel(){
		return this.He.getHeLevel();
	}
	public long getCLevel(){
		return this.C.getCLevel();
	}
	public long getNLevel(){
		return this.N.getNLevel();
	}
	public long getOLevel(){
		return this.O.getOLevel();
	}
}
//classes for the elements 
class Hydrogen{//level max. 99
	private long HLevel;
	public Hydrogen(){}
	public long calcLevel(long upgrade){
		return upgrade%100;
	}
	public void setHLevel(long upgrade){
			this.HLevel = upgrade;
			
	}
	public long getHLevel(){
		return this.HLevel;
	}
}
class Helium{//level max. 99
	private long HeLevel;
	public Helium(){}
	public long calcLevel(long upgrade){
		return (upgrade%10000)/100;
	}
	public void setHeLevel(long upgrade){
		this.HeLevel = upgrade;
	}
	public long getHeLevel(){
		return this.HeLevel;
	}
}

class Carbon{//level max. 99
	private long CLevel;
	public Carbon(){}
	public long calcLevel(long upgrade){
		return (upgrade%1000000)/10000;
	}
	public void setCLevel(long upgrade){
		this.CLevel = upgrade;
	}
	public long getCLevel(){
		return this.CLevel;
	}
}
class Nitrogen{//level max. 99
	private long NLevel;
	public Nitrogen(){}
	public long calcLevel(long upgrade){
		return (upgrade%100000000)/1000000;
	}
	public void setNLevel(long upgrade){
		this.NLevel = upgrade;
	}
	public long getNLevel(){
		return this.NLevel;
	}
}
class Oxygen{//level max. 9
	private long OLevel;
	public Oxygen(){}
	public long calcLevel(long upgrade){
		return (upgrade%1000000000)/100000000;
	}
	public void setOLevel(long upgrade){
		this.OLevel = upgrade;
	}
	public long getOLevel(){
		return this.OLevel;
	}
}

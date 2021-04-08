/**
 * Main class to increment the upgrades and checking the amount of lights
 * 
 * @author 2016311221 YoungHoon Lim & 2016313746 JaeHun Yu
 * 
 */
package planet;

import gui.Main;

public class Upgrade {

	private Main planet;
	private int whichUpgrade;

	public Upgrade(Main planet){
		this.planet = planet;
	}
	//현재 업그레이드 레벨 확인하고 그에 맞는 금액 설정 후 맞는지 아닌지 확인, 맞으면 차감
	public boolean chkEnoughLight(int code){
		Element ele = new Element(this.planet);
		long temp;
		this.whichUpgrade = code;
		switch(this.whichUpgrade){
		case 1:
			temp = ele.H.calcLevel(this.planet.getUpgrade());
			if(this.planet.getLight() < (1+(10*temp*Math.sqrt(2)))){
				return false;
			}
			else {
				this.planet.setLight((long)(this.planet.getLight() - (1+(10*temp*Math.sqrt(2)))));
				return true;
			}
		case 2:
			temp = ele.He.calcLevel(this.planet.getUpgrade());
			if(this.planet.getLight() < (100+(40*temp*Math.sqrt(2)))){
				return false;
			}
			else {
				this.planet.setLight((long)(this.planet.getLight() - (100+(40*temp*Math.sqrt(2)))));
				return true;
			}
		case 4:
			temp = ele.C.calcLevel(this.planet.getUpgrade());
			if(this.planet.getLight() < (1000+(80*temp*Math.sqrt(2)))){
				return false;
			}
			else {
				this.planet.setLight((long)(this.planet.getLight() - (1000+(80*temp*Math.sqrt(2)))));
				return true;
			}
		case 7:
			temp = ele.N.calcLevel(this.planet.getUpgrade());
			if(this.planet.getLight() < (10000+(120*temp*Math.sqrt(2)))){
				return false;
			}
			else {
				this.planet.setLight((long)(this.planet.getLight() - (10000+(120*temp*Math.sqrt(2)))));
				return true;
			}
		case 11:
			temp = ele.O.calcLevel(this.planet.getUpgrade());
			if(this.planet.getLight() < (100000+(300*temp*Math.sqrt(2)))){
				return false;
			}
			else {
				this.planet.setLight((long)(this.planet.getLight() - (100000+(300*temp*Math.sqrt(2)))));
				return true;
			}
			
		case 100:
			temp = this.planet.getGravity();
			if(this.planet.getLight() < (1000000+(30000*temp*Math.sqrt(2)))){
				return false;
			}
			else {
				this.planet.setLight((long)(this.planet.getLight() - (1000000+(30000*temp*Math.sqrt(2)))));
				return true;
			}
		default: 
			return false;
		}
	}
	//upgrade elements
	public long HUpgrade(){
		this.planet.setUpgrade(this.planet.getUpgrade()+1);
		Element ele = new Element(this.planet);
		ele.H.setHLevel(ele.H.calcLevel(this.planet.getUpgrade()));
		return ele.getUpdatedRate();
	}
	public long HeUpgrade(){
		this.planet.setUpgrade(this.planet.getUpgrade()+100);
		Element ele = new Element(this.planet);
		ele.He.setHeLevel(ele.He.calcLevel(this.planet.getUpgrade()));
		return ele.getUpdatedRate();
	}
	public long CUpgrade(){
		this.planet.setUpgrade(this.planet.getUpgrade()+10000);
		Element ele = new Element(this.planet);
		ele.C.setCLevel(ele.C.calcLevel(this.planet.getUpgrade()));
		return ele.getUpdatedRate();
	}
	public long NUpgrade(){
		this.planet.setUpgrade(this.planet.getUpgrade()+1000000);
		Element ele = new Element(this.planet);
		ele.N.setNLevel(ele.N.calcLevel(this.planet.getUpgrade()));
		return ele.getUpdatedRate();
	}
	public long OUpgrade(){
		this.planet.setUpgrade(this.planet.getUpgrade()+100000000);
		Element ele = new Element(this.planet);
		ele.O.setOLevel(ele.O.calcLevel(this.planet.getUpgrade()));
		return ele.getUpdatedRate();
	}
	public void GUpgrade(){
		this.planet.setGravity(this.planet.getGravity()+1);
	}

}

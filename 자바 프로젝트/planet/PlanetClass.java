package planet;

public class PlanetClass {
	int light = 1;
	int weight = 0;
	int level = 0;
	int rate = 1;
	int upgrade = 0;
	public static void main(String[] args){
		while(true){
			if(weight > 1000*rate){
				level++;
				rate++;
			}
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){

			}
			light++;
		}
	}
}

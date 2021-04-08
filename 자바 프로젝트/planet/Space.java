package Planet;

public class Space extends Thread{
	private Thread t;

	public void run(){
		try{
			light = light + 10;
		}catch(InterruptedException e){

		}
	}
	public void start(){

		if(t==null){
			t = new Thread();
			t.start();
		}
	}
}
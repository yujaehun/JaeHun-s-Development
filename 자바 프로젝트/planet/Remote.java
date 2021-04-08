package planet;

public class Remote extends Thread{
	private Thread t;

	public void run(){
		try{
			while(true){
				light++;
			}
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

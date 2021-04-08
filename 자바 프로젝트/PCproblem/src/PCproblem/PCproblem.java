package PCproblem;

public class PCproblem {
	 private boolean flag = false;
	 private int cnt=0;
	 private int size = 0;
	 public synchronized void Consume(int i){
		while (size <= 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		size --;
		System.out.println("Consumed: "+i);
		flag = false;
		notify();
	 }
	 public synchronized void Produce(int val){
		 while (size > 4) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		 size ++;
		 System.out.println("Produced: "+val);
		 flag = true;
		 notify();
	 }
}

class Consumer extends Thread{
	private PCproblem pro;
	private int num;
	
	public Consumer(PCproblem p, int num){
		pro = p;
		this.num = num;
	}
	public void run(){
		int val = 0;
		for(int i = 0; i<10;i++){
			pro.Consume(i);
		}
	}
}

class Producer extends Thread{
	private PCproblem pro;
	private int num;
	
	public Producer(PCproblem p, int num){
		pro = p;
		this.num = num;
	}
	public void run(){
		for(int i = 0; i<10;i++){
			pro.Produce(i);
			try{
				Thread.sleep(300);
			}catch(InterruptedException e){			
			}
		}
	}
}
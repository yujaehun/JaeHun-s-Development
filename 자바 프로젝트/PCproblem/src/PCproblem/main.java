package PCproblem;

public class main {
	public static void main(String args[]){
		PCproblem b = new PCproblem();
		Producer p = new Producer(b,1);
		Consumer c = new Consumer(b,1);
		p.start();
		c.start();
	}
}

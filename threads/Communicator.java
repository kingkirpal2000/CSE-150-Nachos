package nachos.threads;
import nachos.machine.*;


public class communicator{

    //Allocate a communicator 
	public communicator{
		mutex = new lock();
		listenerReady = new Condition(mutex);
		speakerReady = new Condition(mutex);
		ready = new Condition(mutex);
		x = null; //integer
		//listener = 0;
		//speaker = 0;
	}

	public void speak(int word){
		mutex.acquire();
		while(word != null){
			speakerReady.sleep();
		}
		x = word;
		listenReady.wake();
		ready.sleep();
		mutex.release();

		/*speaker++;
		while(speaker==1){
			speakerReady.sleep();
		}
		while(listener==0){
			listenerReady.wake();
		}
		this.r = word; 
		mutex.release();*/
	}

	public int listen(){
		mutex.acquire();
		int r;
		while (x ==null){
			listenerReady.sleep();
		}
		r = x.intValue();
		x = null;
		speakerReady.wake();
		ready.wake();
		mutex.release();
		return r;

		/*listener++:
		if(listen == 1){
			listenerReady.sleep();
		}
		if(speaker == 0){
			speakerReady.wake();
		} 
		listener--;
		mutex.release();
		return this.r;*/
	}

	private int x;
	private Lock mutex;
	private Condition speakReady;
    private Condition listenReady; 
    private Condition ready;
    //private int listener;
    //private int speaker;

	public static class test implements Runnable(){
		int x;
		Communicator communicate = new Communicator();
		test(int x){
			this.x = x;
		}

		public void run(){
			if(x>0){
				for(int i =0; i<x.length(), i++){
					System.out.println("Communicator "+x+ " speaks");
					communicate.speak(i);
					System.out.println("Communicator "+x+ " listens")
					int h = communicate.listen();
				}
			}
		}
		Random ran = new Random();
		Communicator com = new Communicator();

	}

	public static void testing() {
		new test(0).run();
		KThread thread1 = new KThread(new test(1));
		KThread thread2 = new KThread(new test(2));
		thread1.fork();
    	thread2.fork();
    	thread1.join();
    	thread2.join();
}
	


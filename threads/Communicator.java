package nachos.threads;
import nachos.machine.*;


public class communicator{
	private int r;
	private Lock mutex;
	private Condition speakReady;
    private Condition listenReady; 
    private int listener;
    private int speaker;

    //Allocate a communicator 
	public communicator{
		mutex = new lock();
		listenerReady = new Condition(lock);
		speakerReady = new Condition(lock);
		listener = 0;
		speaker = 0;
	}

	public void speak(int word){
		mutex.acquire();
		speaker++;
		while(speaker==1){
			speakerReady.sleep();
		}
		while(listener==0){
			listenerReady.wake();
		}
		this.r = word;
		mutex.release();
	}

	public int listen(){
		mutex.acquire();
		listener++:
		if(listen == 1){
			listenerReady.sleep();
		}
		if(speaker == 0){
			speakerReady.wake();
		} 
		listener--;
		mutex.release();
		return this.r;
	}
}
	


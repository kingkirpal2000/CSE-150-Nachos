package nachos.threads;

import nachos.machine.*;

/**
 * A <i>communicator</i> allows threads to synchronously exchange 32-bit
 * messages. Multiple threads can be waiting to <i>speak</i>,
 * and multiple threads can be waiting to <i>listen</i>. But there should never
 * be a time when both a speaker and a listener are waiting, because the two
 * threads can be paired off at this point.
 */
public class Communicator {
    /**
     * Allocate a new communicator.
     */
    private Lock mutex;
    private Condition listenerReady;
    private Condition speakerReady;
    private Condition ready;
    private int x ;

    public Communicator() {
        mutex = new Lock();
        listenerReady = new Condition(mutex);
        speakerReady = new Condition(mutex);
        ready = new Condition(mutex);
        x = 0;
    }

    /**
     * Wait for a thread to listen through this communicator, and then transfer
     * <i>word</i> to the listener.
     *
     * <p>
     * Does not return until this thread is paired up with a listening thread.
     * Exactly one listener should receive <i>word</i>.
     *
     * @param	word	the integer to transfer.
     */
    public void speak(int word) {
        mutex.acquire();
        while(word != 0){
            speakerReady.sleep();
        }
        x = word;
        listenerReady.notify();
        ready.sleep();
        mutex.release();
    }

    /**
     * Wait for a thread to speak through this communicator, and then return
     * the <i>word</i> that thread passed to <tt>speak()</tt>.
     *
     * @return	the integer transferred.
     */
    public int listen() {
        mutex.acquire();
        int r = 0;
        while (x == 0){
            listenerReady.sleep();
        }
        r = x;
        x = 0;
        speakerReady.notify();
        ready.notify();
        mutex.release();
        return r;
    }

/*    public static class test implements Runnable(){
        int x;

        test(int x){
            this.x = x;
        }

        public void run(){
            if(x>0){
                for(int i =0; i<x.length(); i++){
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
*/
    public static void testing(){
        Communicator communicate = new Communicator();

        KThread thread1 = new KThread(new Runnable(){
            public void run(){
                System.out.println("Listening");
                communicate.listen();
            }
        });
        KThread thread2 = new KThread(new Runnable(){
            public void run(){
                System.out.println("Speaking");
                communicate.speak(2);
            }
        });
        thread1.fork();
        thread2.fork();
        thread1.join();
        thread2.join();
}
}

	


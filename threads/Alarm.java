package nachos.threads;

import java.util.Comparator;
import java.util.TreeSet;

import nachos.machine.*;

class comp implements Comparator<KThread>{  
    public int compare(KThread s1, KThread s2) {  
    	long w1;
    	long w2;
    	w1 = s1.getWakeTime();
    	w2 = s2.getWakeTime();
        //return w1.compareTo(w2);
    	if(w1==w2) return 0;
    	else if (w1<w2) return -1;
    	else return 1;
    }   
}  

/**
 * Uses the hardware timer to provide preemption, and to allow threads to sleep
 * until a certain time.
 */
public class Alarm {
		
    /**
     * Allocate a new Alarm. Set the machine's timer interrupt handler to this
     * alarm's callback.
     *
     * <p><b>Note</b>: Nachos will not function correctly with more than one
     * alarm.
     */
    public Alarm() {
 
	    sleepQueue = new TreeSet<KThread>(new comp());
		Machine.timer().setInterruptHandler(new Runnable() {
			public void run() { timerInterrupt(); }
		    });
    }

    /**
     * The timer interrupt handler. This is called by the machine's timer
     * periodically (approximately every 500 clock ticks). Causes the current
     * thread to yield, forcing a context switch if there is another thread
     * that should be run.
     */
    public void timerInterrupt() {
		while(!sleepQueue.isEmpty() && sleepQueue.first().getWakeTime() < Machine.timer().getTime()) {
			sleepQueue.pollFirst().ready();
		}
    }

    /**
     * Put the current thread to sleep for at least <i>x</i> ticks,
     * waking it up in the timer interrupt handler. The thread must be
     * woken up (placed in the scheduler ready set) during the first timer
     * interrupt where
     *
     * <p><blockquote>
     * (current time) >= (WaitUntil called time)+(x)
     * </blockquote>
     *
     * @param	x	the minimum number of clock ticks to wait.
     *
     * @see	nachos.machine.Timer#getTime()
     */
    public void waitUntil(long x) {
	// for now, cheat just to get something working (busy waiting is bad)
	    boolean intStatus = Machine.interrupt().disable();
		KThread.currentThread().setWakeTime(Machine.timer().getTime() + x);
		sleepQueue.add(KThread.currentThread());
		KThread.currentThread().sleep();
		//KThread.sleep();
		Machine.interrupt().restore(intStatus);
	    }
 
//    public static void alarmTest1() {
//    	int durations[] = {1000, 10*1000, 100*1000};
//    	long t0, t1;
//
//    	for (int d : durations) {
//    	    t0 = Machine.timer().getTime();
//    	    ThreadedKernel.alarm.waitUntil (d);
//    	    t1 = Machine.timer().getTime();
//    	    System.out.println ("alarmTest1: waited for " + (t1 - t0) + " ticks");
//    	}
//    }

        // Implement more test methods here ...

    TreeSet<KThread> sleepQueue;
}

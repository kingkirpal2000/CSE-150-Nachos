package nachos.threads;

import nachos.machine.*;
import java.util.LinkedList;

/**
 * An implementation of condition variables that disables interrupt()s for
 * synchronization.
 *
 * <p>
 * You must implement this.
 *
 * @see	nachos.threads.Condition
 */
public class Condition2 
{
    /**
     * Allocate a new condition variable.
     *
     * @param	conditionLock	the lock associated with this condition
     *				variable. The current thread must hold this
     *				lock whenever it uses <tt>sleep()</tt>,
     *				<tt>wake()</tt>, or <tt>wakeAll()</tt>.
     */
    private Lock conditionLock;
    private LinkedList waitQueue;
    private int counter;
    private ThreadQueue readyQueue = ThreadedKernel.scheduler.newThreadQueue(false);
    
    
    public Condition2(Lock conditionLock) 
    {
    	this.conditionLock = conditionLock;
    	LinkedList waitQueue = new LinkedList();
    }

    /**
     * Atomically release the associated lock and go to sleep on this condition
     * variable until another thread wakes it using <tt>wake()</tt>. The
     * current 	thread must hold the associated lock. The thread will
     * automatically reacquire the lock before <tt>sleep()</tt> returns.
     */
    
    public void sleep()
    {
    	Lib.assertTrue(conditionLock.isHeldByCurrentThread());
    	int counter = 0;
    	waitQueue.add(counter);
    	conditionLock.release();
    	
    	boolean ifDisable = Machine.interrupt().disable();
    	if(counter == 0)
    	{
    		readyQueue.waitForAccess(KThread.currentThread());
    		KThread.sleep();
    	}
    	else
    	{
    		counter--;
    	}
    	
    	Machine.interrupt().restore(ifDisable);
    	conditionLock.acquire();
    }

    /**
     * Wake up at most one thread sleeping on this condition variable. The
     * current thread must hold the associated lock.
     */
    public void wake() 
    {
    	Lib.assertTrue(conditionLock.isHeldByCurrentThread());
    	boolean ifDisable = Machine.interrupt().disable();
    	
    	if(!waitQueue.isEmpty())
    	{
    		waitQueue.removeFirst();
    		KThread thread = readyQueue.nextThread();
    		
    		if(thread != null)
    		{
    			thread.ready();
    		}
    		else
    		{
    			counter++;
    		}
    	}
    	Machine.interrupt().restore(ifDisable);
    }

    /**
     * Wake up all threads sleeping on this condition variable. The current
     * thread must hold the associated lock.
     */
    public void wakeAll() 
    {
    	Lib.assertTrue(conditionLock.isHeldByCurrentThread());
    	while(!waitQueue.isEmpty())
    	{
    		wake();
    	}
    }
 

}

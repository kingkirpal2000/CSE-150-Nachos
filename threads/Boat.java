package nachos.threads;

import nachos.ag.BoatGrader;


public class Boat {
	static BoatGrader bg;
	static boolean not_done;
	static boolean boat_is_on_oahu;
	static Lock lock;
	static int children_on_boat;
	
	// your code here
	static int TotalChildren;
	static int TotalAdults;
	static int ChildrenOnOahu;
	static int AdultsOnOahu;
	static int ChildrenOnMolokai; 
	static int AdultsOnMolokai; 
	//static boolean BoatOnOahu; 
	static Condition Children_On_Oahu;
	static Condition Children_On_Molokai;
	static Condition Adults_On_Oahu;
	static Condition Adults_On_Molokai;
	
	
	public static void selfTest() {
		BoatGrader b = new BoatGrader();

		System.out.println("\n ***Testing Boats with only 2 children***");
		begin(0, 2, b);

//	System.out.println("\n ***Testing Boats with only 2 children, 1 adult***");
//	begin(1, 2, b);

//	System.out.println("\n ***Testing Boats with 3 children, 3 adult***");
//	begin(1, 3, b);

	} 


	public static void begin(int adults, int children, BoatGrader b) {
		// Store the externally generated autograder in a class
		// variable to be accessible by children.
		bg = b;

		// Instantiate global variables here
		not_done = true;
		boat_is_on_oahu = true;
		lock = new Lock();
		children_on_boat = 0;
		
		// your code here
		TotalChildren = children;
		ChildrenOnOahu = TotalChildren;
		TotalAdults = adults;
		AdultsOnOahu = TotalAdults;
		ChildrenOnMolokai = 0;
		AdultsOnMolokai = 0;
		//BoatOnOahu = true;
		Children_On_Oahu = new Condition(lock);
		Children_On_Molokai = new Condition(lock);
		Adults_On_Oahu = new Condition(lock);
		Adults_On_Molokai = new Condition(lock);
		
		// Create threads here. See section 3.4 of the Nachos for Java
		// Walkthrough linked from the projects page.

//		Runnable r = new Runnable() {
//		    public void run() {
//	                SampleItinerary();
//	            }
//	        };
//	        KThread t = new KThread(r);
//	        t.setName("Sample Boat Thread");
//	        t.fork();


		// Define runnable object for child thread.
		Runnable r_child = new Runnable() {
			public void run() {
				ChildItinerary();
			}
		}; // r_child Runnable()

		// Define runnable object for adult thread.
		Runnable r_adult = new Runnable() {
			public void run() {
				AdultItinerary();
			}
		}; // r_adult Runnable()

		// Spawn all adult threads.
		for (int i = 0; i < adults; i++) {
			new KThread(r_adult).setName("Adult " + Integer.toString(i + 1)).fork();
		} // after this for loop, all adult threads are spawned and sleeping

		// Spawn all child threads.
		for (int i = 0; i < children; i++) {
			new KThread(r_child).setName("Child " + Integer.toString(i + 1)).fork();
		} // after this for loop, all child threads are spawned and start running

		// hold main thread while solutions calls are made to the BoatGrader
		while (not_done)
			KThread.yield();
		// while loop ends when last children and all adults are on Molokai

	} 


	static void AdultItinerary() {
		
		/*
		 * This is where you should put your solutions. Make calls to the BoatGrader to
		 * show that it is synchronized. For example: bg.AdultRowToMolokai(); indicates
		 * that an adult has rowed the boat across to Molokai
		 */
		
		// adult threads can only operate with the lock atomically
		lock.acquire();

		// while there are still adults not asleep on Molokai
		while (not_done) {
			
			//your code here
			Adults_On_Molokai.sleep();
			Adults_On_Oahu.wakeAll();
			// check see if children still need it
//			ChildItinerary();
//			bg.AdultRideToMolokai();
//			AdultsOnOahu = AdultsOnOahu - 1;
//			AdultsOnMolokai = AdultsOnMolokai + 1;

			} // after while, boat is on Oahu and children do not need it.

			// row adult self to Molokai and wake one child up so it can bring the
			// boat back to Oahu for another adult or last children
			bg.AdultRowToMolokai();
			
			//your code here 
			Adults_On_Oahu.sleep();
			Adults_On_Molokai.wakeAll();
			AdultsOnOahu = AdultsOnOahu - 1;
			AdultsOnMolokai = AdultsOnMolokai + 1;
			//BoatOnOahu = false;
			Children_On_Molokai.wake();
			//
			
			boat_is_on_oahu = false;
			
			//your code here
			bg.ChildRideToOahu();
			Children_On_Molokai.sleep();
			ChildrenOnOahu = ChildrenOnOahu + 1;
			ChildrenOnMolokai = ChildrenOnMolokai - 1;
			//BoatOnOahu = true;
			Children_On_Oahu.sleep();
			
		} // while not done and adult still need to get to Molokai

	


	static void ChildItinerary() {
		// child threads can only operate with the lock atomically
		lock.acquire();

		// while there are still adults and children not on Molokai
		while (not_done) {

			// if the boat is not on Oahu
			while (!boat_is_on_oahu) {

				// your code here
				Children_On_Molokai.wake();
				bg.ChildRowToOahu();
				Children_On_Oahu.wakeAll();
				Children_On_Molokai.sleep();
				ChildrenOnMolokai = ChildrenOnMolokai - 1;
				ChildrenOnOahu = ChildrenOnOahu + 1;
				boat_is_on_oahu = true;
				
				
			} 

			// if this child will be the first into the boat, it will be a
			// passenger and wait for a rower
			if (children_on_boat == 0) {

				// increment boat counter and wake sleeping children that could
				// ferry us, and sleep on Molokai
				children_on_boat++;
				
				// your code here
				Children_On_Oahu.wakeAll();
				Children_On_Molokai.sleep();
				
				
				
				
//				bg.ChildRowToMolokai();
//    			bg.ChildRideToMolokai();
//    			ChildrenOnOahu = ChildrenOnOahu - 2;
//    			ChildrenOnMolokai = ChildrenOnMolokai + 2;
//    			BoatOnOahu = false;
    			
    			//
    			

				// this child is woken up on Molokai by an adult to ferry boat
				// for other adults on Oahu or last children on Oahu
				bg.ChildRowToOahu();
				boat_is_on_oahu = true;
				
				//your code here
				Children_On_Molokai.wake();
				ChildrenOnMolokai = ChildrenOnMolokai - 1;
				ChildrenOnOahu = ChildrenOnOahu + 1;
				//
				children_on_boat = 0;
				
				//your code here
				
//				bg.ChildRowToMolokai();
//    			bg.ChildRideToMolokai();
//    			ChildrenOnOahu = ChildrenOnOahu - 2;
//    			ChildrenOnMolokai = ChildrenOnMolokai + 2;
//    			//BoatOnOahu = false;
    			//
			}

			// else this child should be a rower if there is a child in the boat
			else if (children_on_boat == 1) {

				// two children always bring the boat back from Oahu and check
				// if they are done before returning to Oahu for an adult
				children_on_boat++;
				bg.ChildRowToMolokai();
				bg.ChildRideToMolokai();
				boat_is_on_oahu = false;
				
				
				//your code here
				ChildrenOnOahu = ChildrenOnOahu - 2;
    			ChildrenOnMolokai = ChildrenOnMolokai + 2;
    			//
    			
				// check if we are all done
				//if (checking condition) {

					// set terminal bool to false to end all loops and return
					not_done = false;
					return;
				} // boat terminates after this if statement is executed

				// else we are not done so we need to send one back to Oahu
				else {
					bg.ChildRowToOahu();
					
					//your code here
					Children_On_Molokai.wake();
					ChildrenOnMolokai = ChildrenOnMolokai - 1;
					ChildrenOnOahu = ChildrenOnOahu + 1;
					//
					
					children_on_boat = 0;
					boat_is_on_oahu = true;
					
					//your code here
					bg.ChildRowToMolokai();
	    			bg.ChildRideToMolokai();
	    			ChildrenOnOahu = ChildrenOnOahu - 2;
	    			ChildrenOnMolokai = ChildrenOnMolokai + 2;
	    			boat_is_on_oahu = false;
	    			//
	    			
				} 
			} 
		} 
//    static void SampleItinerary()
//    {
//	// Please note that this isn't a valid solution (you can't fit
//	// all of them on the boat). Please also note that you may not
//	// have a single thread calculate a solution and then just play
//	// it back at the autograder -- you will be caught.
//	System.out.println("\n ***Everyone piles on the boat and goes to Molokai***");
//	bg.AdultRowToMolokai();
//	bg.ChildRideToMolokai();
//	bg.AdultRideToMolokai();
//	bg.ChildRideToMolokai();
//    }

}

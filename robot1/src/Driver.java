/**
 * 
 * The Driver class containing the main method to control all the behaviours.
 * It will use bluetooth to connect with PC and print all the maps on the output screen.
 * RobotMonitor and MapUpdate are two daemons and updated by several seconds.
 * Behaviours are developed independently and arranged into a hierarchy
 * ForwardBehavior is low level, AvoidBehavior is middle level, CornerBehavior is high level
 * and AdjustBehavior is higher level. 
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 1.0
 *
 */
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Driver {

	private final static int updateTime = 500;

	/**
	 * Main method to start and run two daemons.
	 * 
	 * 
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RConsole.open();
		// counter class is used to count the steps of turns and travel
		Counter c = new Counter();

		// Standard robot - other classes can call it with static methods

		new StandardRobot();

		// Creating an instance of robotmonitor called rm

		RobotMonitor rm = new RobotMonitor(updateTime);

		// Starting the thread for the RobotMonitor

		rm.start();

		// Creating an instance of MapUpdate called mu Update the map for
		// several time c to compute the probability More steps by Counter, less
		// for pose

		MapUpdate mu = new MapUpdate(updateTime, c);

		// Starting the thread for the MapUpdate

		mu.start();

		// Four different corners of the arena

		Corner c1 = new Corner(0, 0);
		Corner c2 = new Corner(140, 0);
		Corner c3 = new Corner(0, -100);
		Corner c4 = new Corner(140, -100);

		// Array of Corners

		Corner[] cArray = { c1, c2, c3, c4 };

		// Creating an Instance of CornerStore called store

		CornerStore store = new CornerStore(cArray);

		// Creating Instances of our Behaviours

		Behavior b1 = new ForwardBehavior(c);
		Behavior b2 = new AvoidBehavior(store, c);
		Behavior b3 = new CornerBehavior(store, c);
		Behavior b4 = new AdjustBehavior(c);

		// Levels of behaviour methods from lowest to highest in array

		Behavior[] bArray = { b1, b2, b3, b4 };

		// The arbtitrator is passed the array of behaviours

		Arbitrator arb = new Arbitrator(bArray);

		// Starting the arbitrator thread

		arb.start();

		RConsole.close();
	}
}

/**
 * 
 * The Driver class containing the main method and senderThread which is a 
 * daemon and runs in the background. The WayPoints are set here as well as the 
 * array of WayPoints, which gives the order of WayPoints and how the robot will
 * navigate them. 
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 2.0
 *
 */
import java.util.ArrayList;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class Driver {

	// connection with doctor
	private static NXTConnection conn;
	// use bluetooth
	public static BTComunication sender;
	// deamon runs in background
	private static Thread senderThread;

	/**
	 * This calls the necessary methods from other classes to achieve mapping
	 * and navigation
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		System.out.println("Waiting");
		conn = Bluetooth.waitForConnection();
		System.out.println("Connected");
		sender = new BTComunication(conn);
		senderThread = new Thread(sender);
		// Set thread to be deamon
		senderThread.setDaemon(true);
		// Starting thread
		senderThread.start();

		while (BTComunication.flag.equals("Not_Start")) {
		}

		// Class initialises sensors and motors
		new StandardRobot();

		// Setups a map instance and callibrates it
		Map current = new Map(sender);
		current.setWalls();
		current.gridMap[1][1] = 1;
		// Prints out current map
		current.sendVictimAndMap();

		// Setting order of Waypoints robot must travel to in array list
		ArrayList<WayPoint> list = new ArrayList<WayPoint>();

		// Setting WayPoints for navigation around the arena
		for (int i = 1; i <= 5; i++) {
			for (int j = 1; j <= 7; j++) {
				WayPoint w = new WayPoint(j, i);
				list.add(w);
			}
			if (i == 5) {
				break;
			}
			i++;
			for (int j = 7; j >= 1; j--) {
				WayPoint w = new WayPoint(j, i);
				list.add(w);
			}
		}

		// Creating an instance of WayPointStore called store
		WayPointStore store = new WayPointStore(list, current);

		// Creating an instance of WayPointStore called store
		NavigationWayPoint nwp = new NavigationWayPoint(store, current);

		// Calls
		nwp.navigation();
	}

}

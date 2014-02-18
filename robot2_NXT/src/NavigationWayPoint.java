/**
 * 
 * The NavigationWayPoint class is to check if a waypoint has already been
 * passed over by the robot if so * then no need to check again so robot moves
 * on to next waypoint.
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 2.0
 * 
 */

public class NavigationWayPoint {
	// create instance for storing way points
	private WayPointStore store;
	// create instance for Map
	private Map current;

	/**
	 * Constructor for passing parameters.
	 * 
	 * @param store
	 *            storing way points
	 * @param current
	 *            Map
	 */
	public NavigationWayPoint(WayPointStore store, Map current) {
		this.store = store;
		this.current = current;
	}

	/**
	 * Navigate to next way point if the previous way point has been detected
	 * before.
	 */
	public void navigation() {
		// always true until break
		while (true) {
			// check if it is last way point
			if (store.nextWayPoint()) {
				try {
					current.findRoute(store.getX(), store.getY(), current);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// set flag to "Stop"
				Map.flag = "Stop";
				// print final Map
				current.sendVictimAndMap();

				// wait for sending
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			}
		}
	}
}
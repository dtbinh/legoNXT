/**
 * 
 * The WayPointStore class is used to check the WayPoints set against the
 * location of the robot in the map to determine if the next WayPoint is
 * accurate. An Index is utilized for this.
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 2.0
 * 
 */
import java.util.ArrayList;

public class WayPointStore {

	// use array to store the way point
	private ArrayList<WayPoint> list;
	// first index is 0
	private int index = 0;
	// create instance of Map
	private Map current;

	/**
	 * Constructor for passing parameters.
	 * 
	 * @param wArray
	 * @param current
	 */
	public WayPointStore(ArrayList<WayPoint> list, Map current) {
		this.list = list;
		this.current = current;
	}

	/**
	 * If the index is not equal the length of the WayPoint array. If the
	 * current gridmap location with the x and y coordinate of the waypoint is
	 * not equal to 0 return true. If index equal the length of the WayPoint
	 * array return false.
	 * 
	 * @return false or true
	 */
	public boolean nextWayPoint() {
		if (index != list.size()) {
			// check if it has been detected before
			if (current.gridMap[list.get(index).getX()][list.get(index).getY()] != 0) {
				index++;
			}
		}
		// check index is last one
		if (index == list.size()) {
			return false;
		}
		return true;
	}

	/**
	 * Get the X value from array.
	 * 
	 * @return int value
	 */
	public int getX() {
		return list.get(index).getX();
	}

	/**
	 * Get the Y value from array.
	 * 
	 * @return int value
	 */
	public int getY() {
		return list.get(index).getY();
	}
}

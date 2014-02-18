/**
 * 
 * Helper class to but coordinate objects into a queue.
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 2.0
 * 
 */
public class Pair {
	// x & y
	private int first;
	private int second;

	/**
	 * Takes the two passed int parameters and constructs an pair object with
	 * them
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 */
	public Pair(int x, int y) {
		first = x;
		second = y;
	}

	/**
	 * Returns the first value within the pair object
	 */
	public int getFirst() {
		return first;
	}

	/**
	 * Returns the second value within the pair object
	 */
	public int getSecond() {
		return second;
	}
}

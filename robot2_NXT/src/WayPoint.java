/**
 * 
 * The WayPoint class defines a WayPoint with X and Y values.
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 2.0
 * 
 */
public class WayPoint {
	private int x;
	private int y;

	/**
	 * Way point with X and Y values.
	 * 
	 * @param x
	 * @param y
	 */
	public WayPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Return the X value.
	 * 
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Return the Y value.
	 * 
	 * @return y
	 */
	public int getY() {
		return y;
	}
}

/**
 * 
 * The Corner class defines a corner with x and y values. PassNumber is
 * instantiated as n=0 and is incremented or decremented and returned with the
 * given methods.
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 1.0
 * 
 */

public class Corner {
	private double x;
	private double y;
	private int n = 0;

	/**
	 * Corner Values with constructors
	 * 
	 * @param x
	 * @param y
	 */
	public Corner(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Defines a corner using the following calculation
	 * 
	 * @param a
	 * @param b
	 * @return calculation result
	 */
	public boolean isCorner(float a, float b) {
		return (Math.abs(Math.abs(x) - Math.abs(a)) <= 5 && Math.abs(Math
				.abs(y) - Math.abs(b)) <= 5);
	}

	/**
	 * Increase pass number.
	 */
	public void increasePassNumber() {
		n++;
	}

	/**
	 * Decrease pass number.
	 */
	public void decreasePassNumber() {
		n--;
	}

	/**
	 * Return the pass number.
	 * 
	 * @return PassNumber
	 */
	public int getPassNumber() {
		return n;
	}
}

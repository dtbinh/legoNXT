/**
 * 
 * The Counter class is used to count the steps of the robots turns and travel.
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 1.0
 * 
 */
public class Counter {

	// Initialized at 0
	private int turn = 0;
	private int travel = 0;

	/**
	 * Turn and Count are assigned to 1 and using getTotalStep we can get the
	 * sum of the turn and travel steps
	 */
	public Counter() {
	}

	/**
	 * Turn count for each turn 90 or -90
	 */
	public void countTurn() {
		turn += 1;
	}

	/**
	 * Travel count for each travel
	 */
	public void countTravel() {
		travel += 1;
	}

	/**
	 * 
	 * @return integer number for steps
	 */
	public int getTotalStep() {
		return (turn + travel);
	}
}

/**
 * 
 * The CornerBehavior class controlls the second highest behaviour in our 
 * hierarchy of behaviours and utilizes the classes CornerStore and Corner to 
 * behave correctly when a corner has been detected. 
 * 
 *  
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 1.0
 *
 */
import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;

public class CornerBehavior implements Behavior {

	public static boolean suppressed;
	private CornerStore store;
	private Counter counter;

	/**
	 * Contains constructors for CornerStore Class and Counter Class
	 * 
	 * @param store
	 *            storing corners
	 * @param counter
	 *            counting number of passes
	 */
	public CornerBehavior(CornerStore store, Counter counter) {
		this.store = store;
		this.counter = counter;
	}

	/**
	 * Takes control of robot and returns values very fast
	 * 
	 * @return using store to state this is a corner and retrieves the x and y
	 *         values of this corner.
	 */
	public boolean takeControl() {
		return store.isAnyCorner(StandardRobot.opp.getPose().getX(),
				StandardRobot.opp.getPose().getY());
	}

	/**
	 * Immediately terminates code running in the action() method
	 */
	public void suppress() {
		suppressed = true;
	}

	/**
	 * This method begins performing tasks once the AvoidBehavior becomes
	 * active. After the PassNumber is set using the store, we use a thread
	 * sleep and in the case that an exception occurs the call stack will be
	 * printed. In the case the range of the ultrasonic sensor is less than 35
	 * depending on the flag value taken from the CornerStore being equal to 0
	 * or not the robot will rotate left or otherwise right and this turn
	 * movement will be counted by our counter.
	 */
	public void action() {
		LCD.drawString("Corner Behavior!", 0, 7);
		store.setPassNumber();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (StandardRobot.us.getRange() < 35) {
			if (store.getFlag() == 0) {
				StandardRobot.pilot.rotate(-90);
				counter.countTurn();
			} else {
				StandardRobot.pilot.rotate(90);
				counter.countTurn();
			}
		}

		// Calibrate the turn movement of the robot to fit the build of the
		// robot for a more accurate turn.

		if (((int) StandardRobot.opp.getPose().getHeading() >= -91 && (int) StandardRobot.opp
				.getPose().getHeading() <= -89)
				|| ((int) StandardRobot.opp.getPose().getHeading() >= 89 && (int) StandardRobot.opp
						.getPose().getHeading() <= 91)) {

			// Get the PassNumber for the Units the robot has moved before the
			// robot commences it's movement up and down the arena

			int i = store.getPassNumber();

			// while the range of the ultrasonic sensor is greater than 20 and
			// the PassNumber is greater or at least equal to 1 we update our
			// Map using the UpdateWithoutObject and UpdateWithObject methods
			// from the MapUpdate Class, furthermore travel movement is counted
			// and the PassNumber decremented.

			while (StandardRobot.us.getRange() > 20 && i >= 1) {
				MapUpdate.updateWithoutObject(0);
				MapUpdate.updateWithoutObject(StandardRobot.us.getRange() - 1);
				MapUpdate.updateWithObject(StandardRobot.us.getRange());

				StandardRobot.pilot.setTravelSpeed(12);
				StandardRobot.pilot.travel(20);
				counter.countTravel();
				i--;
			}

			// If the range of the ultrasonic sensor is less than 25 The Map
			// will be updated otherwise the robot will stop, It will turn left
			// or right dependant on the flag value.

			if (StandardRobot.us.getRange() < 25) {
				MapUpdate.updateWithoutObject(0);
				MapUpdate.updateWithObject(StandardRobot.us.getRange());
			} else {
				StandardRobot.pilot.stop();

				if (store.getFlag() == 0) {
					StandardRobot.pilot.rotate(-90);
					counter.countTurn();
				} else {
					StandardRobot.pilot.rotate(90);
					counter.countTurn();
				}
			}
		} else {
			MapUpdate.updateWithoutObject(0);
			MapUpdate.updateWithoutObject(StandardRobot.us.getRange() - 1);
			MapUpdate.updateWithObject(StandardRobot.us.getRange());

			StandardRobot.pilot.setTravelSpeed(12);
			StandardRobot.pilot.travel(20);
			counter.countTravel();
		}
	}
}
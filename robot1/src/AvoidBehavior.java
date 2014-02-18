/**
 * 
 * The AvoidBehavior class controlls the second lowest behaviour in our 
 * hierarchy of behaviours
 * It is designed to recognize objects or corners and avoid them. 
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 1.0
 *
 */
import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;

public class AvoidBehavior implements Behavior {
	public static boolean suppressed;
	private CornerStore store;
	private Counter counter;

	/**
	 * contains constructors for CornerStore Class and Counter Class
	 * 
	 * @param store
	 *            storing corners / objects
	 * @param counter
	 *            counting steps of turns and movement
	 */
	public AvoidBehavior(CornerStore store, Counter counter) {
		this.store = store;
		this.counter = counter;
	}

	/**
	 * Takes control of robot
	 * 
	 * @return if the Touch sensor has been set off or feeds back range less
	 *         than 25
	 */
	public boolean takeControl() {
		return (StandardRobot.ts.isPressed() || (StandardRobot.us.getRange() < 25));
	}

	/**
	 * Immediately terminates code running in the action() method
	 */
	public void suppress() {
		suppressed = true;
	}

	/**
	 * This method begins performing statically accessed methods for avoidance
	 * once the AvoidBehavior becomes active. If the Touch Sensor is set off the
	 * travel speed is adjusted and the robot will back away from the obstacle,
	 * furthermore the counter counts the travel steps. If the Flag retained
	 * from the CornerStore equals 0 the turn direction will be changed (90 =
	 * right turn or -90 = left turn), this turn movement is also counted by the
	 * counter.
	 */
	public void action() {
		LCD.drawString("Avoid Behavior!", 0, 7);

		MapUpdate.updateWithoutObject(0);
		MapUpdate.updateWithObject(StandardRobot.us.getRange());

		if (StandardRobot.ts.isPressed()) {
			StandardRobot.pilot.setTravelSpeed(7);
			StandardRobot.pilot.travel(-5);
			counter.countTravel();

			if (store.getFlag() == 0) {
				StandardRobot.pilot.rotate(-90);
				counter.countTurn();
			} else {
				StandardRobot.pilot.rotate(90);
				counter.countTurn();
			}
		} else {

			if (store.getFlag() == 0) {
				StandardRobot.pilot.rotate(-90);
				counter.countTurn();
			} else {
				StandardRobot.pilot.rotate(90);
				counter.countTurn();
			}
		}
	}
}
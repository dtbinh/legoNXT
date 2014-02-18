/**
 * 
 * The AdjustBehavior class corrects errors using two different color tapes for 
 * length and width in order to adjust our X and Y values, when the color sensor
 * passes and recognizes the tape line 
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 1.0
 *
 */
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.robotics.navigation.Pose;
import lejos.robotics.subsumption.Behavior;

public class AdjustBehavior implements Behavior {

	// Will be true or false

	public static boolean suppressed;
	private Counter c;

	// Variables only visible to objects of same class, there exists only one
	// instance of these, which can only be assigned once
	private static final int LENGTH_VALUE = 60;
	private static final int WIDTH_VALUE = -40;
	private static final int LENGTH_COLOR = 0; // Red
	private static final int WIDTH_COLOR = 1; // Green

	public AdjustBehavior(Counter c) {
		this.c = c;
	}

	/**
	 * If the color sensor picks up the length or width color in the arena
	 * 
	 * @return true otherwise false
	 */
	public boolean takeControl() {
		if (StandardRobot.cs.getColorID() == LENGTH_COLOR
				|| StandardRobot.cs.getColorID() == WIDTH_COLOR) {
			return true;
		}
		return false;
	}

	/**
	 * Immediately terminates code running in the action() method
	 */
	public void suppress() {
		suppressed = true;
	}

	/**
	 * This method begins performing tasks once the AdjustBehavior becomes
	 * active If the color sensor recognizes the color red set representing the
	 * length of the Arena then the Pose X and Y values of the robot will be
	 * adjusted with the set LENGTH VALUE 60 as well as the direction of the
	 * robot. (y is negative and x is positive in the arena layout) Otherwise
	 * the Pose will be adjusted using the WIDTH_Value -40 representing the
	 * width of the arena.
	 */
	public void action() {
		LCD.drawString("Adjust Behavior!", 0, 6);

		if (StandardRobot.cs.getColorID() == LENGTH_COLOR) {
			Pose p = new Pose(LENGTH_VALUE, StandardRobot.opp.getPose().getY(),
					StandardRobot.opp.getPose().getHeading());
			StandardRobot.opp.setPose(p);
		} else {
			Pose p = new Pose(StandardRobot.opp.getPose().getX(), WIDTH_VALUE,
					StandardRobot.opp.getPose().getHeading());
			StandardRobot.opp.setPose(p);
		}
		// catch exception
		Sound.twoBeeps();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StandardRobot.pilot.travel(20);
		c.countTravel();
	}
}

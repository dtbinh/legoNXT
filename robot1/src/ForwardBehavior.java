/**
 * 
 * The ForwardBehavior class controlls the behaviour lowest in our 
 * hierarchy of behaviours. It makes the robot travel around the arena and 
 * update the map by utilizing the Ultrasonic Sensor. 
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 1.0
 *
 */
import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;

public class ForwardBehavior implements Behavior {

	public static boolean suppressed;
	private Counter c;

	/**
	 * Contains constructor for Counter Class
	 * 
	 * @param c
	 *            to count steps of travel
	 */
	public ForwardBehavior(Counter c) {
		this.c = c;
	}

	/**
	 * utilizing if cPose less than STOPTIMES from MapUpdate Class
	 * 
	 * @return false if equals 1, if cPose is less than STOPTIMES return true
	 */
	public boolean takeControl() {
		if (MapUpdate.stop() == 1) {
			return false;
		}
		return true;
	}

	/**
	 * Immediately terminates code running in the action() method
	 */
	public void suppress() {
		suppressed = true;
	}

	/**
	 * This method begins performing tasks once the AvoidBehavior becomes
	 * active. while not suppressed the the map will be updated. The robot will
	 * travel at the set speed and distance up the length of the map and turn
	 * right every 20 travel units get a range reading using the ultrasonic
	 * sensor turn left travel a further 20 travel units and repeat. It will
	 * travel around the arena whilst turning away from the wall and to scan the
	 * arena.
	 */
	public void action() {
		suppressed = false;
		while (!suppressed) {
			LCD.drawString("Forward Behavior!", 0, 7);

			MapUpdate.updateWithoutObject(0);
			MapUpdate.updateWithoutObject(StandardRobot.us.getRange() - 1);
			MapUpdate.updateWithObject(StandardRobot.us.getRange());

			if (((int) StandardRobot.opp.getPose().getHeading() >= -1 && (int) StandardRobot.opp
					.getPose().getHeading() <= 1)
					|| ((int) Math
							.abs(StandardRobot.opp.getPose().getHeading()) <= 181 && (int) Math
							.abs(StandardRobot.opp.getPose().getHeading()) >= 179)) {
				StandardRobot.pilot.rotate(-90);
				MapUpdate.updateWithoutObject(StandardRobot.us.getRange() - 1);
				MapUpdate.updateWithObject(StandardRobot.us.getRange());
				StandardRobot.pilot.rotate(90);
			}

			StandardRobot.pilot.setTravelSpeed(12);
			StandardRobot.pilot.travel(20);
			c.countTravel();
		}
	}
}
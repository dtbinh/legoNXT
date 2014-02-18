/**
 * 
 * The RobotMonitor class continually monitors motors and sensors using a thread 
 * and utilizes the LCD panel to display their status.
 * The thread is called a deamon because it runs as a background process.
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 1.0
 *
 */
import java.lang.Thread;
import lejos.nxt.LCD;

public class RobotMonitor extends Thread {

	private static int delay;

	/**
	 * Runs in background
	 * 
	 * @param d
	 */
	public RobotMonitor(int d) {
		this.setDaemon(true); // daemon flag
		delay = d;
	}

	/**
	 * Displays sensors and Inputs on the LCD screen as well as the X and Y
	 * values for Pose
	 */
	public void run() {
		while (true) {
			LCD.clear();
			LCD.drawString("Color = " + StandardRobot.cs.getColorID(), 0, 0);
			LCD.drawString("Ultra = " + StandardRobot.us.getRange(), 0, 1);
			LCD.drawString("Touch = " + StandardRobot.ts.isPressed(), 0, 2);
			LCD.drawString("X = " + StandardRobot.opp.getPose().getX(), 0, 3);
			LCD.drawString("Y = " + StandardRobot.opp.getPose().getY(), 0, 4);
			LCD.drawString(
					"Head = " + StandardRobot.opp.getPose().getHeading(), 0, 5);

			float x = StandardRobot.opp.getPose().getX();
			float y = StandardRobot.opp.getPose().getY();
			if ((int) x / 25 <= 5 && (int) y / -20 <= 4 && (int) x / 25 >= 0
					&& (int) y / -20 >= 0) {
				LCD.drawString("Point = (" + (int) x / 25 + ", " + (int) y
						/ (-20) + ")", 0, 6);
			} else {
				LCD.drawString("Out of map!", 0, 6);
			}
			try {
				sleep(delay);
			} // exception catching
			catch (Exception e) {
				;
			} // exception handling
		}
	}
}
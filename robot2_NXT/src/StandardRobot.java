/**
 * 
 * The StandardRobot class is defined, which can be reused every time we need the 
 * standard configuration robot.
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 2.0
 *
 */
import lejos.nxt.addon.ColorHTSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;

public class StandardRobot {
        // create different controls for robot
	public static ColorHTSensor cs;
	public static TouchSensor ts;
	public static UltrasonicSensor us;
	public static DifferentialPilot pilot;
	public static OdometryPoseProvider opp;

	/**
	 * Instantiate Sensors and pilot values with wheel diameter, axel width,
	 * left and right motor.
	 */
	public StandardRobot() {
		// Instantiate sensors
		us = new UltrasonicSensor(SensorPort.S1);
		cs = new ColorHTSensor(SensorPort.S2);
		ts = new TouchSensor(SensorPort.S4);
		// Instantiate pilot
		pilot = new DifferentialPilot(3.25, 19.8, Motor.A, Motor.B);

		opp = new OdometryPoseProvider(pilot);
	}
}

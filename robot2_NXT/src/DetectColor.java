/**
 * 
 * The DetectColor class will detect and distinguish between RED, BLUE and Green
 * using the color sensor.
 * Once a color is detected the robot will sound the appropriate number of beeps 
 * for that color and return a number identifying the color, which will be
 * displayed in the map.
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 2.0
 *
 */
import lejos.nxt.Sound;

public class DetectColor {
	// Color detectin dependent on lighting and environment.
	private final static int RED = 0;
	private final static int BLUE = 2;
	private final static int GREEN = 3;

	/**
	 * Constructor for color detect.
	 */
	public DetectColor() {
	}

	/**
	 * RED = Robot will beep 3 times and return value 9 BLUE = Robot will beep
	 * twice and return value 8 GREEN = Robot will beep once and return value 7
	 * If no color is detected during the robots travel then the return value
	 * will be 1 and also displayed us such in the map.
	 * 
	 * @return either 9, 8, 7 or 1
	 */
	public static int detect() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block.
			e.printStackTrace();
		}
		// Detect Red - Beep 3 times - return 9
		if (StandardRobot.cs.getColorID() == RED) {
			Sound.twoBeeps();
			Sound.beep();
			return 9;
		// Detect Blue - Beep twice - return 8
		} else if (StandardRobot.cs.getColorID() == BLUE) {
			Sound.twoBeeps();
			return 8;
		// Detect Blue - Beep once - return 7
		} else if (StandardRobot.cs.getColorID() == GREEN) {
			Sound.beep();
			return 7;
		} else {
			return 1;
		}

	}
}

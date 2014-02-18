/**
 * 
 * The MapUpdate class builds a 6 by 5 grid on the screen of the real world arena.
 * The thread is called a deamon because it runs as a background process.
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 1.0
 *
 */
import java.lang.Thread;
import lejos.nxt.comm.RConsole;

public class MapUpdate extends Thread {

	private final static int STOPTIMES = 1;
	private static int delay;
	private Counter counter;
	private static int[][] cPose;
	private static int[][] mPose;
	private static int[][] cSensor;
	private static int[][] mSensor;

	/**
	 * Contains constructor for Counter Class
	 * 
	 * @param d
	 * @param counter
	 */
	public MapUpdate(int d, Counter counter) {
		this.setDaemon(true); // daemon flag
		this.counter = counter;
		delay = d;

		cPose = new int[6][5];
		mPose = new int[6][5];

		cSensor = new int[6][5];
		mSensor = new int[6][5];

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				cPose[i][j] = 0;
			}
		}

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				mPose[i][j] = 0;
			}
		}

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				cSensor[i][j] = 0;
			}
		}

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				mSensor[i][j] = 0;
			}
		}
	}

	/**
	 * Print as the format.
	 */
	public void run() {
		while (true) {
			for (int j = 0; j < 5; j++) {
				for (int i = 0; i < 5; i++) {
					RConsole.print("("
							+ getPercent(mPose[i][j], cPose[i][j])
							+ ", "
							+ getPercent(mSensor[i][j], cSensor[i][j])
							+ ", "
							+ combinePoseAndSensor(mPose[i][j], mSensor[i][j],
									cPose[i][j], cSensor[i][j]) + ")" + "\t");
				}
				RConsole.println("("
						+ getPercent(mPose[5][j], cPose[5][j])
						+ ", "
						+ getPercent(mSensor[5][j], cSensor[5][j])
						+ ", "
						+ combinePoseAndSensor(mPose[5][j], mSensor[5][j],
								cPose[5][j], cSensor[5][j]) + ")");
				RConsole.println("");
			}
			RConsole.println("Key: (Pose (%), Ultrasonic Sensor (%), Combine (%))");
			RConsole.println("====================================================================================");

			try {
				sleep(delay);
			} // exception catching
			catch (Exception e) {
				;
			} // exception handling
		}
	}

	/**
	 * Translate m and c into percent number
	 * 
	 * @param m
	 *            object times
	 * @param c
	 *            count times
	 * @return return the percent number
	 */
	private int getPercent(int m, int c) {
		if (c == 0) {
			return -1;
		} else {
			return (m * 100) / c;
		}

	}

	/**
	 * Combine the pose and sensor counts
	 * 
	 * @param mp
	 *            object times by pose
	 * @param ms
	 *            count times by sensor
	 * @param cp
	 *            object times by pose
	 * @param cs
	 *            count times by sensor
	 * @return
	 */
	private int combinePoseAndSensor(int mp, int ms, int cp, int cs) {
		float weight = (float) (10.0 / counter.getTotalStep());
		if (getPercent(mp + ms, cp + cs) == -1 || counter.getTotalStep() == 0) {
			return -1;
		} else {
			return getPercent((int) (weight * mp + ms),
					(int) (weight * cp + cs));
		}
	}

	/**
	 * Update if there is not object
	 * 
	 * @param range
	 *            sensor range
	 */
	public static void updateWithoutObject(float range) {
		float x;
		float y;

		if ((int) range == 0) {
			x = StandardRobot.opp.getPose().getX();
			y = -StandardRobot.opp.getPose().getY();

			if ((int) x / 25 <= 5 && (int) y / 20 <= 4 && (int) x / 25 >= 0
					&& (int) y / 20 >= 0) {
				cPose[(int) x / 25][(int) y / 20] += 1;
			}
		} else {

			// depend on the direction
			for (int i = 1; i <= range / 20; i++) {
				switch ((int) StandardRobot.opp.getPose().getHeading()) {
				case 0:
				case -1:
				case 1:
					x = StandardRobot.opp.getPose().getX() + i * 20;
					y = -StandardRobot.opp.getPose().getY();
					break;
				case 90:
				case 89:
				case 91:
					x = StandardRobot.opp.getPose().getX();
					y = -(StandardRobot.opp.getPose().getY() + i * 20);
					break;
				case 180:
				case 179:
				case 181:
					x = StandardRobot.opp.getPose().getX() - i * 20;
					y = -StandardRobot.opp.getPose().getY();
					break;
				case -180:
				case -179:
				case -181:
					x = StandardRobot.opp.getPose().getX() - range;
					y = -StandardRobot.opp.getPose().getY();
					break;
				default:
					x = StandardRobot.opp.getPose().getX();
					y = -(StandardRobot.opp.getPose().getY() - i * 20);
					break;
				}

				if ((int) x / 25 <= 5 && (int) y / 20 <= 4 && (int) x / 25 >= 0
						&& (int) y / 20 >= 0) {
					cSensor[(int) x / 25][(int) y / 20] += 1;
				}
			}
		}
	}

	/**
	 * Update if there is a object
	 * 
	 * @param range
	 *            sensor range
	 */
	public static void updateWithObject(float range) {
		float x;
		float y;

		// depend on the direction
		switch ((int) StandardRobot.opp.getPose().getHeading()) {
		case 0:
		case -1:
		case 1:
			x = StandardRobot.opp.getPose().getX() + range;
			y = -StandardRobot.opp.getPose().getY();
			break;
		case 90:
		case 89:
		case 91:
			x = StandardRobot.opp.getPose().getX();
			y = -(StandardRobot.opp.getPose().getY() + range);
			break;
		case 180:
		case 179:
		case 181:
			x = StandardRobot.opp.getPose().getX() - range;
			y = -StandardRobot.opp.getPose().getY();
			break;
		case -180:
		case -179:
		case -181:
			x = StandardRobot.opp.getPose().getX() - range;
			y = -StandardRobot.opp.getPose().getY();
			break;
		default:
			x = StandardRobot.opp.getPose().getX();
			y = -(StandardRobot.opp.getPose().getY() - range);
			break;
		}

		if ((int) x / 25 <= 5 && (int) y / 20 <= 4 && (int) x / 25 >= 0
				&& (int) y / 20 >= 0) {
			cSensor[(int) x / 25][(int) y / 20] += 1;
			mSensor[(int) x / 25][(int) y / 20] += 1;
		}
	}

	/**
	 * Until test once and stop.
	 * 
	 * @return 1 and stop all the things
	 */
	public static int stop() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				if (cPose[i][j] < STOPTIMES) {
					return 0;
				}
			}
		}

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				if (cSensor[i][j] < STOPTIMES) {
					return 0;
				}
			}
		}

		return 1;
	}
}

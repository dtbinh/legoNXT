/**
 * 
 * RobotLogic is set to store all the methods to help robot to run and turning.
 * When finding the robot, it will call the method gotoPoint for letting it to 
 * go that point with avoiding object. If meeting object, it will return to 
 * previous grid.
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 2.0
 * 
 */
public class RobotLogic {

	/**
	 * Constructor.
	 */
	public RobotLogic() {

	}

	/**
	 * This method moves the robot in 5 steps by taking on the passed and diving
	 * it by five, if it hits and object it will reverse back and return
	 * feedback representing this else it will return sucess feedback. Also
	 * combined in this method is a colour detect class method so more grid can
	 * be tested instead of a single point.
	 * 
	 * @param distance
	 *            int value of cm to be traveled
	 */
	public int forwards(int distance) {
		int feedback = 1;
		int counter = distance / 5;
		int i;
		for (i = 0; i < counter; i++) {
			// touch the object, then set to 0
			if (StandardRobot.ts.isPressed()) {
				feedback = 0;
				break;
			}
			// set the speed slow
			StandardRobot.pilot.setTravelSpeed(12);
			StandardRobot.pilot.travel(5);

			if (feedback == 1) {
				// detect color
				feedback = DetectColor.detect();
			}

		}

		if (feedback == 0) {
			for (int j = 0; j < i; j++) {
				// change to different speed for back
				StandardRobot.pilot.setTravelSpeed(7);
				StandardRobot.pilot.travel(-5);
			}
		}
		return feedback;
	}

	// Following methods depending on the current direction will turn to face
	// the called method and update robots recorded direction

	// DIRECTION ALIGNMENT
	/**
	 * This method will check the instance direction and turn in most optimal
	 * way to face 0 degrees from the map instance. It will also update the map
	 * instance direction with this value.
	 * 
	 * @param current
	 *            the "current" map instance
	 */
	public void turnNorth(Map current) {
		// System.out.println("Facing North");
		if (current.direction == 270) {
			turn(90);
			current.direction = 0;
		}
		while (current.direction != 0) {
			turn(-90);
			current.direction -= 90;
		}

	}

	/**
	 * This method will check the instance direction and turn in most optimal
	 * way to face 90 degrees from the map instance. It will also update the map
	 * instance direction with this value.
	 * 
	 * @param current
	 *            the "current" map instance
	 */
	public void turnEast(Map current) {
		// System.out.println("Facing East");
		if (current.direction == 0) {
			turn(90);
			current.direction = 90;
		}

		while (current.direction != 90) {
			turn(-90);
			current.direction -= 90;
		}

	}

	/**
	 * This method will check the instance direction and turn in most optimal
	 * way to face 180 degrees from the map instance. It will also update the
	 * map instance direction with this value.
	 * 
	 * @param current
	 *            the "current" map instance
	 */
	public void turnSouth(Map current) {
		// System.out.println("Facing South");
		if (current.direction == 270) {
			turn(-90);
			current.direction = 180;
		}
		while (current.direction != 180) {
			turn(90);
			current.direction += 90;
		}

	}

	/**
	 * This method will check the instance direction and turn in most optimal
	 * way to face 270 degrees from the map instance. It will also update the
	 * map instance direction with this value.
	 * 
	 * @param current
	 *            the "current" map instance
	 */
	public void turnWest(Map current) {
		// System.out.println("Facing West");
		if (current.direction == 0) {
			turn(-90);
			current.direction = 270;
		}
		while (current.direction != 270) {
			turn(90);
			current.direction += 90;
		}

	}
	
	/**
	 * Simply takes the pass value and calls the StandardPilot class to carry it
	 * out. This make the robot rotate using the differential pilot class.
	 * 
	 * @param angle
	 *            the degrees it will turn
	 */
	public void turn(int angle) {
		// Take in a parameter and turns robot accordingly
		StandardRobot.pilot.rotate(angle);
	}

	/**
	 * This scans using the ultrasonic sensor at a passed on range and return an
	 * value on wether it detected something. This is done using the
	 * StandardPilot class.
	 * 
	 * @param range
	 *            the range it will scan using the ultrasonic sensor in cm
	 */
	public int scanRange(int range) {
		// Scanner method that takes in the range the scan and give conditional
		// feedback
		if (StandardRobot.us.getRange() < range) {
			return 2;
		}
		return 1;
	}

	/**
	 * This method will call the other class methods in reponse to the passed on
	 * parameters in order to reach the destination. It will compare the passed
	 * on values with the current position and determine which way to go, it
	 * will check it the call a face* method first, then will check if it's
	 * clear then it will attempt to travel there. If something prevents this
	 * from being possible such as an obstacle it will return a value
	 * representing this else it will return a success feedback value. This
	 * method will update the map according to occuring events thoughout the
	 * attempt such as colour, object and visted values. The return value 1 is
	 * success and -1 for failure.
	 * 
	 * @param x
	 * @param y
	 * @param current
	 * @throws Exception
	 */
	public int gotoPoint(int x, int y, Map current) throws Exception {
		// This method will heading to the adajacent grid by using the above methods
		// with the passed on parameters
		int feedback;
		int count = 0;
		// While the given x coord or y coord are not equal to the current
		// position...
		while (current.posx != x || current.posy != y) {
			// For avoidance and breaking loop count increment
			count += 1;

			// If x coord objective is greater current coord
			if (x > current.posx) {
				// Face direction and check adajacent grid & update any value of
				// map
				turnEast(current);
				// Scan the adajacent grid by the gridwidth or length with
				// offset
				feedback = scanRange(current.gridwidth + 5);
				current.appendMap(current.posx + 1, current.posy, feedback);
				// If adajacent grid is clear move to it and update current
				// position
				if (current.gridMap[current.posx + 1][current.posy] != 2) {
					int back = forwards(current.gridwidth);
					if (back != 0) {
						Thread.sleep(300);
						// Increment position coordinate
						current.posx += 1;
						current.gridMap[current.posx][current.posy] = back;
						count = 0;
						// send the message
						current.sendVictimAndMap();
						return 1;
					} else {
						// otherwise set to 2
						current.gridMap[current.posx + 1][current.posy] = 2;
						current.sendVictimAndMap();
						return -1;
					}
				} else {
					// only for printing map
					current.sendVictimAndMap();
					return -1;

				}
			} else if (x < current.posx) {
				// If x coord objective is smaller current coord & adajacent
				// grid
				turnWest(current);
				feedback = scanRange(current.gridwidth);
				current.appendMap(current.posx - 1, current.posy, feedback);
				if (current.gridMap[current.posx - 1][current.posy] != 2) {
					int back = forwards(current.gridwidth);
					if (back != 0) {
						Thread.sleep(300);
						current.posx -= 1;
						count = 0;
						// detect color
						current.gridMap[current.posx][current.posy] = back;
						// send the message
						current.sendVictimAndMap();
						return 1;
					} else {
						// otherwise set to 2
						current.gridMap[current.posx - 1][current.posy] = 2;
						current.sendVictimAndMap();
						return -1;
					}
				} else {
					// only for printing map
					current.sendVictimAndMap();
					return -1;
				}
			} else if (y > current.posy) {
				// If y coord objective is greater current coord & adajacent
				// grid is not blocked
				turnSouth(current);
				feedback = scanRange(current.gridheight);
				current.appendMap(current.posx, current.posy + 1, feedback);
				if (current.gridMap[current.posx][current.posy + 1] != 2) {
					int back = forwards(current.gridwidth);
					if (back != 0) {
						Thread.sleep(300);
						current.posy += 1;
						count = 0;
						// detect color
						current.gridMap[current.posx][current.posy] = back;
						// send the message
						current.sendVictimAndMap();
						return 1;
					} else {
						// otherwise set to 2
						current.gridMap[current.posx][current.posy + 1] = 2;
						current.sendVictimAndMap();
						return -1;
					}
				} else {
					// only for printing map
					current.sendVictimAndMap();
					return -1;

				}
			} else if (y < current.posy) {
				// If x coord objective is smaller current coord & adajacent
				// grid is not blocked
				turnNorth(current);
				feedback = scanRange(current.gridheight);
				current.appendMap(current.posx, current.posy - 1, feedback);
				if (current.gridMap[current.posx][current.posy - 1] != 2) {
					int back = forwards(current.gridwidth);
					if (back != 0) {
						Thread.sleep(300);
						current.posy -= 1;
						count = 0;
						// detect color
						current.gridMap[current.posx][current.posy] = back;
						// set a message
						current.sendVictimAndMap();
						return 1;
					} else {
						// otherwise set to 2
						current.gridMap[current.posx][current.posy - 1] = 2;
						current.sendVictimAndMap();
						return -1;
					}
				} else {
					// only for printing map
					current.sendVictimAndMap();
					return -1;
				}
			} else if (x == current.posx && y == current.posy) {
				return 1;
			}

			Thread.sleep(200);

			// Break loop if objective is covered by box
			if (current.gridMap[x][y] == 2 && count > 50) {
				// RConsole.println("EXIT TRIGGER|Destination unreachable");
				Thread.sleep(200);
				break;
			}
		}

		return -1;
	}
}

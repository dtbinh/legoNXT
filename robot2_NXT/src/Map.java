/**
 * 
 * This map class is set to produce the map by 9*7 with wall around.
 * Moreover, there is method to find the route to one goal.
 * The find route method implements a shortest route algorthim that will 
 * plan a route around obstacles.
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 2.0
 * 
 */
import java.util.Stack;

public class Map {
	// communication for robot and doctor
	private BTComunication sender;
	// flag is set to stop or continue
	public static String flag = "Continue";
	// x and y
	private final int xGridcells = 9;// 7
	private final int yGridcells = 7;// 5
	public int gridMap[][] = new int[xGridcells][yGridcells];
	// Secondary array for copying map
	private int shortestPath[][] = new int[xGridcells][yGridcells];
	// Robot Position
	public int posx = 1;
	public int posy = 1;
	public int direction = 90;
	// Grid
	public int gridheight = 20;
	public int gridwidth = 25;
	/**
	 * Methods takes the instance value of BTSend class and encapsulates it within itself
	 * via static variable.
	 * 
	 * @param sender BTSend instance
	 */
	public Map(BTComunication sender) {
		this.sender = sender;
	}

	/**
	 * A method that sets the boundary values of the 2d Map Array
	 * to the value 2. Usefull Init method when instance of Map
	 * has been created.
	 */
	public void setWalls() {
		// Sets up walls into the array
		for (int i = 0; i < xGridcells; i++) {
			gridMap[i][0] = 2;
			gridMap[i][yGridcells - 1] = 2;
		}
		for (int i = 0; i < yGridcells; i++) {
			gridMap[0][i] = 2;
			gridMap[xGridcells - 1][i] = 2;
		}
	}

	
	/**
	 * Takes in feedback into the 2d array, 0 for not been there, 1 to have been
	 * there and 2 for impassable object(grid), can take any value as feedback
	 * as long .
	 */
	public void appendMap(int x, int y, int feedback) {

		gridMap[x][y] = feedback;

	}

	/**
	 * This method simply updates the static variables within the instance for
	 * the robot x and y coordinate and the direction it is facing.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param angle Direction in degrees
	 */
	public void appendPosition(int x, int y, int angle) {
		// Updates robot position
		posx = x;
		posy = y;
		direction = angle;
	}
	/**
	 * This methods takes the maps current instance variables and puts the map into a string format
	 * which is then used by the BTSend instance.
	 */
	public void sendVictimAndMap() {

		String message = checkVictim();

		message += "\n=============================";
		message += "\n" + "Current Position: [" + posx + "," + posy + "] , "
				+ direction + "\n";
		message += "=\n";
		for (int j = 0; j < yGridcells; j++) {
			message += " -----------------------------------------\n";
			for (int i = 0; i < xGridcells; i++) {
				message += " | ";
				message += " " + gridMap[i][j];

			}
			message += "\n";
		}
		message += "Number label:\n0-Undetect\t1-Travel\t2-Object\n7-Green\t8-Blue\t9-Red\n";
		message += "=============================\n";
		sender.write(message);
	}

	/**
	 * Method checks wether the current coordinate positions have an colour value from the
	 * 2D gridmap array and checks if flag is set to "stop".
	 */
	private String checkVictim() {
		if (flag.equals("Stop")) {
			return "99\n99\n99\n";
		}
		if (gridMap[posx][posy] == 9 || gridMap[posx][posy] == 8
				|| gridMap[posx][posy] == 7) {
			return gridMap[posx][posy] + "\n" + posx + "\n" + posy + "\n";
		}
		return "-\n-\n-\n";
	}
	
	/**
	 *The find route method implements a shortest route algorthim that will plan a route around obstacles
	 *until the destination is occupied by the a number representing the turns it will take to get there.
	 *First it copies the 2D array containing the map and sets all existing values to 0 expect walls which
	 *are set to 99.
	 *It does the from putting + 1 in all adjacent grids with the exeception of grids containing numbers
	 *less then it's own or value 99(representing wall), it then repeats this with a loop until the
	 *destination grid is occupied with a number or the loop breaks with n?cycles (gridlength x gridheight)
	 *meaning destination was imposible to reach.
	 *
	 *EG [1,1] Starting Position , [3,1] Destination Position
	 *
	 *X:   0     1     2     3     5
	 *Y -------------------------------
 	 *0 |  99 |  99 |  99 |  99 |  99 |
 	 *  -------------------------------
 	 *1 |  99 |  0  |  99 |  4  |  99 |
 	 *  -------------------------------
 	 *2 |  99 |  1  |  2  |  3  |  99 |  
 	 *  -------------------------------
 	 *3 |  99 |  2  |  3  |  4  |  99 | 
 	 *  -------------------------------
 	 *4 |  99 |  99 |  99 |  99 |  99 | 
 	 *  -------------------------------
	 * 
	 * Once this is it will then back track from the destination position encapsulating the x and y coord
	 * into a Pair object and adding it to the stack.
	 * 
	 * EG Stack that would be made from the above
	 * -----------------------------------
 	 * | [1,2] | [2,2] |  [3,2] |  [3,1] |
 	 * -----------------------------------
 	 * 
 	 * It will then using the RobotLogic Goto method to reach the destination for each stack element removing
 	 * them as they have been reached. If the object is detected and interferes with the route it will empty
 	 * the stack and call itself until the destination is reached or imposible to get to.
 	 * 
 	 * 
	 * @param destx X coordinate
	 * @param desty Y coordinate
	 * @param current
	 * @throws Exception
	 */
	// Find the optimum route
	public void findRoute(int destx, int desty, Map current) throws Exception {

		// Copys gridmap into shortestPath array
		for (int i = 0; i < xGridcells; i++) {
			for (int j = 0; j < yGridcells; j++) {
				shortestPath[i][j] = gridMap[i][j];
			}
		}
		// Inits helper variables and instances
		int route = 0;
		int tempx = posx;
		int tempy = posy;
		int moves = 1;
		int trigger = 0;

		Stack<Pair> pathStore = new Stack<Pair>();

		// Loop
		while (route == 0) {
			// Sets walls & objects grids to value 99 as to not be affects by
			// algorithm in this scenario, value would have to be bigger the
			// greater the size of the map
			for (int i = 0; i < xGridcells; i++) {
				for (int j = 0; j < yGridcells; j++) {
					if (shortestPath[i][j] == 2) {
						shortestPath[i][j] = 99;
					}
					if (shortestPath[i][j] == 1) {
						shortestPath[i][j] = -1;
					}
					if (shortestPath[i][j] == 0) {
						shortestPath[i][j] = -1;
					}
					// color
					if (shortestPath[i][j] == 9 || shortestPath[i][j] == 8
							|| shortestPath[i][j] == 7) {
						shortestPath[i][j] = -1;
					}
				}
			}

			shortestPath[posx][posy] = 0;

			// Sets the initial adajacent grids around position to one
			if (shortestPath[tempx + 1][tempy] != 99) {
				shortestPath[tempx + 1][tempy] = 1;
			}
			if (shortestPath[tempx - 1][tempy] != 99) {
				shortestPath[tempx - 1][tempy] = 1;
			}
			if (shortestPath[tempx][tempy + 1] != 99) {
				shortestPath[tempx][tempy + 1] = 1;
			}
			if (shortestPath[tempx][tempy - 1] != 99) {
				shortestPath[tempx][tempy - 1] = 1;
			}

			int exitcount = 0;
			moves += 1;
			
			// While grid destination is not written over
			while (shortestPath[destx][desty] == -1) {

				// Route setup
				// 0 1 2 3 4... till destination grid is filled
				// 1 2 3 99 5
				// 2 3 4 5 6
				for (moves = 1; moves < (xGridcells * yGridcells); moves++) {
					for (int i = 1; i < xGridcells; i++) {
						for (int j = 1; j < yGridcells; j++) {
							if (shortestPath[i][j] == moves - 1) {
								if (shortestPath[i + 1][j] != 99) {
									if (shortestPath[i + 1][j] == -1
											&& shortestPath[i + 1][j] != shortestPath[posx][posy]) {
										shortestPath[i + 1][j] = moves;
									}
								}
								if (shortestPath[i - 1][j] != 99) {
									if (shortestPath[i - 1][j] == -1
											&& shortestPath[i - 1][j] != shortestPath[posx][posy]) {
										shortestPath[i - 1][j] = moves;
									}

								}
								if (shortestPath[i][j + 1] != 99) {
									if (shortestPath[i][j + 1] == -1
											&& shortestPath[i][j + 1] != shortestPath[posx][posy]) {
										shortestPath[i][j + 1] = moves;
									}
								}
								if (shortestPath[i][j - 1] != 99) {
									if (shortestPath[i][j - 1] == -1
											&& shortestPath[i][j - 1] != shortestPath[posx][posy]) {
										shortestPath[i][j - 1] = moves;
									}
								}
							}
						}
					}
				}
				// Destination cannot be reached, exits loops
				if (exitcount > (xGridcells * yGridcells)) {
					trigger = 1;
					break;
				}
			}
			
			// Breaks outer loop
			if (trigger == 1) {
				// RConsole.println("Impossible to reach");
				break;
			}
			
			route = 1;
			// Puts destination into queue
			Pair coord = new Pair(destx, desty);
			pathStore.push(coord);
			tempx = destx;
			tempy = desty;

			int totalmoves = shortestPath[destx][desty];
			// Checks shortestPath starting around destination and backtracks to
			// current position adding coordinates to the queue
			for (moves = shortestPath[destx][desty]; moves > 0; moves--) {
				if (shortestPath[tempx + 1][tempy] == (moves - 1)) {
					tempx += 1;
					coord = new Pair(tempx, tempy);
					pathStore.push(coord);
				} else if (shortestPath[tempx - 1][tempy] == (moves - 1)) {
					tempx -= 1;
					coord = new Pair(tempx, tempy);
					pathStore.push(coord);
				} else if (shortestPath[tempx][tempy + 1] == (moves - 1)) {
					tempy += 1;
					coord = new Pair(tempx, tempy);
					pathStore.push(coord);
				} else if (shortestPath[tempx][tempy - 1] == (moves - 1)) {
					tempy -= 1;
					coord = new Pair(tempx, tempy);
					pathStore.push(coord);
				}
			}

			// Will use the robot logic movement commands to attempt to reach
			// destination, it will break and clear queue if unexpected object
			// interferes and then reassess by calling itself again
			RobotLogic go = new RobotLogic();
			int confirmation = 1;
			coord = (Pair) pathStore.pop();
			for (; 0 < totalmoves; totalmoves--) {
				coord = (Pair) pathStore.pop();
				// RConsole.println("Expected Moves: " + totalmoves);
				// RConsole.println("GOTO: [" + coord.getFirst() + "," +
				// coord.getSecond() + "]");
				System.out.println("GOTO: [" + coord.getFirst() + ","
						+ coord.getSecond() + "]");
				int x = coord.getFirst();
				int y = coord.getSecond();
				confirmation = go.gotoPoint(x, y, current);
				if (confirmation == -1) {
					pathStore.clear();
					if (gridMap[destx][desty] == 2) {
						// RConsole.println("Impossible to reach");
						break;
					} else {
						// RConsole.println("Find Alternative Route");
						current.findRoute(destx, desty, current);
					}

				}
				if (gridMap[posx][posy] == gridMap[destx][desty]) {
					// RConsole.println("Destination reached");
					break;
				}

			}

		}

	}

}

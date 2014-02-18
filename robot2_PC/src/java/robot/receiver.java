/**
 * 
 * This is a extending standard class for facilitating the implementation
 * of user-defined internal actions:
 * 		1) output the map 
 * 		2) return each victim and corresponding location
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 2.0
 * 
 */
// package for connecting class
package robot;

//controls unifier
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
//AgentSpeak syntax
import jason.asSyntax.StringTerm;
import jason.asSyntax.StringTermImpl;
import jason.asSyntax.Term;

public class receiver extends DefaultInternalAction {
	// communication for creating
	private Communication comm;

	/**
	 * Constructor to get communication.
	 */
	public receiver() {
		comm = Communication.getComm();
	}

	/**
	 * Implement the method in the standard class for agent doctor to call.
	 * Executes the internal action. It should return a Boolean or an Iterator.
	 * A true boolean return means that the IA was successfully executed.
	 * 
	 * @param ts
	 *            TransitionSystem
	 * @param un
	 *            Unifier
	 * @param args
	 *            Term array
	 * @return true or false
	 * @throws Exception
	 */
	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args)
			throws Exception {

		// print a message to confirm action (appear in the debug window)
		ts.getAg().getLogger()
				.info("Executing internal action 'robot.receiver'.");

		// temporary arry to store the messages divided into two parts:
		// 1) color, location_X, location_Y
		// 2) map
		String temp[];

		// do while loop to divide into two parts
		// output the map until there is a color (Red, Blue, Green)
		do {
			// split by newline "\n"
			temp = comm.read().split("\n");
			// the first three values are color number, location_X and
			// location_Y
			for (int i = 3; i < temp.length; i++) {
				System.out.println(temp[i]);
			}
			System.out.println("");
		} while (temp[0].equals("-"));

		// first message is color number
		String message1 = temp[0];
		// second message is location of X and Y
		String message2 = "[" + temp[1] + "," + temp[2] + "]";

		// set two messages for parameters
		StringTerm result1 = new StringTermImpl(message1);
		StringTerm result2 = new StringTermImpl(message2);

		// unify result1 and result2 with two messages
		un.unifies(result1, args[0]);
		un.unifies(result2, args[1]);

		// everything ok, so return true
		return true;
	}
}
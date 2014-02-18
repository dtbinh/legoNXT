/**
 * 
 * This is a extending standard class for facilitating the implementation
 * of user-defined internal actions:
 * 		send message to start the robot
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
import jason.asSyntax.Term;

public class send extends DefaultInternalAction {
	// communication for creating
	private Communication comm;

	/**
	 * Constructor to get communication.
	 */
	public send() {
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
		ts.getAg().getLogger().info("Executing internal action 'robot.send'.");

		// loop through each argument e.g. if robot.send(Hello, Robot)
		// the arguments will be Hello and Robot.
		// in our doctor, we only use message "start" to wake up the robot
		for (Term t : args) {
			// use the static comms class to send the message to the robot
			comm.write(t.toString());
		}

		// everything ok, so return true
		return true;
	}
}
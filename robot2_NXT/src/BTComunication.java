/**
 * 
 * The BTCommunication class contains method to send and receive BT commands 
 * and replies (BT = Bluetooth) between the Robot and the PC.
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 2.0
 *
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Queue;

import lejos.nxt.Sound;
import lejos.nxt.comm.NXTConnection;

public class BTComunication implements Runnable {
	// Queue to store strings
	private Queue<String> q;
	// Output stream to store output
	private DataOutputStream out;
	// input stream to store input
	private DataInputStream in;

	// Flag to start
	public static String flag = "Not_Start";

	/**
	 * Initializes a new instance of the Queue<String> class that is empty and
	 * has a initial default capacity.
	 * 
	 * Creates instances for the implementation of an output and input stream
	 * over Bluetooth.
	 * 
	 * 
	 * @param conn
	 */
	public BTComunication(NXTConnection conn) {
		q = new Queue<String>();
		out = conn.openDataOutputStream();
		in = conn.openDataInputStream();
	}

	/**
	 * A flag is used to initiate the start of reading in an encoded string.
	 * 
	 */
	public void run() {

		if (!flag.equals("Start")) {
			try {
				// Reads in a string that has been encoded using a
				// modified UTF-8 format.
				flag = in.readUTF();
				System.out.println("Start Now");
				Sound.twoBeeps();
				Thread.sleep(2000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			while (true) {
				// While queue is empty, thread object is
				// temporarily paused to allow other threads to execute.
				while (q.empty())
					Thread.yield();
				
				 // Writes a string to the underlying output stream using Java
				 // modified UTF-8 encoding in a machine-independent manner.
				 
				out.writeUTF((String) q.pop());

				// Flushes this output stream and forces any
				// buffered output bytes to be written out.
				out.flush();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Write a message into queue.
	 * 
	 * @param message
	 *            from Map.java
	 */
	public void write(String message) {
		q.push(message);
	}
}
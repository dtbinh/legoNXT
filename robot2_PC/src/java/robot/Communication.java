/**
 * 
 * This is a PC LeJOS for communication with robot by bluetooth. There is a 
 * queue to store the message from the robot. The rule is first-in first out.
 * Then, the doctor is set to read the information of victim stored in queue.
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 2.0
 * 
 */
// package for connecting class
package robot;

// communication and queue
import lejos.pc.comm.*;
import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Communication implements Runnable {
	// NXT connection for connecting with robot
	private NXTConnector conn;
	// input stream to get information from robot
	private DataInputStream dis;
	// output stream to send information to robot
	private DataOutputStream dos;
	// it is set for storing each message
	private LinkedBlockingQueue<String> q;
	// communication for creating
	private static Communication comm;
	// thread for running many times
	private static Thread commThread;

	/**
	 * Constructor with parameters. It will connect with robot by bluetooth. Two
	 * cases: 1) connect successfully 2) fail to connect
	 * 
	 * @param nxtName
	 *            robot name set before
	 * @param nxtBTAddress
	 *            robot address set before
	 */
	public Communication(String nxtName, String nxtBTAddress) {
		// due to message is String type, set queue to store String
		q = new LinkedBlockingQueue<String>();

		// create instance of NXTConnector
		conn = new NXTConnector();

		// judge if connect successfully
		// true--success
		// false--fail
		boolean connected = conn.connectTo(nxtName, nxtBTAddress, 2);

		// when failing, it will output sentence and exit
		if (!connected) {
			System.err.println("Failed to connect!");
			System.exit(1);
		}

		// otherwise, it will connect successfully
		System.out.println("Connected to " + nxtName);

		// get the input stream and connect with robot
		dis = new DataInputStream(conn.getInputStream());
		dos = new DataOutputStream(conn.getOutputStream());

	}

	/**
	 * Input the robot information: name and address. Create thread to run for
	 * receiver and send.
	 * 
	 * @return communication instance
	 */
	public static synchronized Communication getComm() {
		// if communication has been set before,
		// we can use it directly
		if (comm == null) {
			// each time, we choose different robot,
			// we should set it again
			comm = new Communication("Ironhide", "00:16:53:11:14:AD");
			commThread = new Thread(comm);
			commThread.start();
		}
		return comm;
	}

	/**
	 * Implement run method of Runnable, repeat to run many times.
	 */
	public void run() {
		try {
			// always run and get message and store into queue
			// wait for 100
			while (true) {
				q.put(dis.readUTF());
				Thread.sleep(100);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Until there is message in the queue, it will poll out and return.
	 * 
	 * @return message in the queue
	 */
	public String read() {
		// size is zero, we should wait
		while (q.size() == 0) {
			Thread.yield();
		}
		return q.poll();
	}

	/**
	 * Send a message to robot.
	 * 
	 * @param message
	 * @throws Exception
	 */
	public void write(String message) throws Exception {
		dos.writeUTF(message);
		// flush means sending immediately
		dos.flush();
		Thread.sleep(100);
	}
}
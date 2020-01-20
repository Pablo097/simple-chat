package pkgClients;
import pkgUtils.SocketUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ChatClient extends NetworkClient {
	private String username;
	private LinkedBlockingQueue<String> frameInQueue = null;
//	private LinkedBlockingQueue<String> frameOutQueue = null;
	private final int POLL_DELAY = 200;	// Milliseconds

	public ChatClient(String host, int port, String username) {
		super(host, port);
		this.username = username;
	}

	@Override
	protected void handleConnection(Socket client) throws IOException {
		PrintWriter out = SocketUtils.getWriter(client);
		BufferedReader in = SocketUtils.getReader(client);

		print("INFO Client Thread: Connected to " + getHost() + " on port " + getPort());
		// Send Username to server
		out.println("Username " + username);
		// Receive confirmation
		String message = in.readLine();
		print(message);
		if (message.equalsIgnoreCase("ACCEPTED")) {
			boolean end = false;
			while (!end) {	// Run until disconnected
				try {
					if (frameInQueue != null) {    // Interface need to initialize queue
						while ((message = frameInQueue.poll(POLL_DELAY, TimeUnit.MILLISECONDS)) != null) {
							// There are messages from the interface
							print("INFO Client Thread: Received from GUI: " + message);
							out.println(message);
							if (message.equalsIgnoreCase("DISCONNECT")) {
								end = true;
							}
						}
					} else {
						TimeUnit.MILLISECONDS.sleep(POLL_DELAY);
					}
					while (in.ready()) {	// There are messages from server
						message = in.readLine();
						print(message);		// Send command to interface
						if (message.equalsIgnoreCase("REFUSED")) {
							end = true;
						}
					}
				} catch (InterruptedException ie) {
					end = true;
					print("INFO Client Thread: Client interrupted");
				}
			}
			print("INFO Client Thread: Client ended");
		}
	}

	// Prints commands to console initially
	// It is meant to be override for printing commands elsewhere
	@Override
	protected void print(String s) {
//		if (frameOutQueue != null) {
//			try {
//				frameOutQueue.put(s);
//			} catch (InterruptedException e) {
//				System.err.println("InterruptedException at putting String to frameOutQueue in ChatClient");
//			}
//		} else {
			System.out.println(s);
//		}
	}

	// Sets the queue GUI messages will arrive from
	public void setFrameInQueue(LinkedBlockingQueue<String> queue) {
		frameInQueue = queue;
	}

	// Sets the queue commands will be sent to
//	public void setFrameOutQueue(LinkedBlockingQueue<String> queue) {
//		this.frameOutQueue = queue;
//	}
}
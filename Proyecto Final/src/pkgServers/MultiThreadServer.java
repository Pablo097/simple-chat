package pkgServers;

import pkgFrames.ServerFrame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Build a server on specified port. 
 * The server will accept a connection and 
 * then pass the connection to a connection handler,
 * then put the connection handler in a queue of tasks 
 * to be executed in separate threads.
 */
public abstract class MultiThreadServer extends Thread {
	private int port;

	public MultiThreadServer(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}
	
	/** Monitor a port for connections. 
	 * Each time one is established, 
	 * pass resulting Socket to connection handler,
	 * which will execute it in background thread.
	 */
	public void listen() {
		ExecutorService tasks =	Executors.newCachedThreadPool();
		try (ServerSocket listener = new ServerSocket(port)) {
			print("Server listening on port " + port);
			listener.setSoTimeout(500);
			Socket socket;
			while(!isInterrupted()){ // Run until interrupted
				try {
					socket = listener.accept();
					// Class whose run handleConnection method
					tasks.execute(new ConnectionHandlerRunnable(socket, this));
				} catch (SocketTimeoutException ste) {}
			}
			System.out.println("Server interrupted. Interrupting threads");
			tasks.shutdownNow();
		} catch (IOException ioe) {
			System.err.println("IOException: "+ioe);
			ioe.printStackTrace();
		}
	}
	/** This is the method that provides the behavior to the server,
	 *  since it determines what is done with the resulting socket.
	 *  Override this method in servers you write.
	*/
	public abstract void handleConnection(Socket connection)
			throws IOException;

	protected abstract void print(String s);

	public abstract void setFrame(ServerFrame frame);
}

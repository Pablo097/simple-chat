package pkgServers;
import pkgChatUtils.ChatTools;
import pkgFrames.ServerFrame;
import pkgUtils.SocketUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class ChatServer extends MultiThreadServer {
	private ServerFrame frame = null;
	private ChatTools chatTools;
	private final int POLL_DELAY = 200;	// Milliseconds

	public ChatServer(int port) {
		super(port);
		chatTools = new ChatTools();
	}

	public void run() {
		listen();
	}

	@Override
	public void handleConnection(Socket socket) throws IOException {
		PrintWriter out = SocketUtils.getWriter(socket);
		BufferedReader in = SocketUtils.getReader(socket);

		//System.out.println("Connection Local Port: " + socket.getLocalPort());
		print("Host " + socket.getInetAddress().getHostName());
		print("Connection Remote Port: " + socket.getPort());

		// First we need the username
		String message	= in.readLine();
		String[] tokens = message.split(" ");
		if (tokens[0].equalsIgnoreCase("Username") && tokens.length > 1) {
			String username = tokens[1];
			// Create a queue for receiving messages from other clients
			ConcurrentLinkedQueue<String> messagesQueue = new ConcurrentLinkedQueue<>();
			// Add this client to the list of active clients
			if (chatTools.addNewUser(username, messagesQueue)) {
				out.println("ACCEPTED");
				print(Thread.currentThread().getName() + ": User " + username + " connected");
				boolean end = false;
				while (!end) {	// Run until client ends
					try {
						while ((message = messagesQueue.poll()) != null) {
							// There are messages from another clients. Format "USERNAME <username> MESSAGE <message>"
							print(Thread.currentThread().getName() + ": Received: " + message);
							String[] split = message.split(" ", 4);
							if (split[0].equals("USERNAME") && split[2].equals("MESSAGE")) {
								out.println("MESSAGE " + split[1] + " " + split[3]);
								print(Thread.currentThread().getName() + ": Message sent");
							}
						}
						while (in.ready()) {
							message = in.readLine();	// Message from client
							print(Thread.currentThread().getName() + ": Received from client: " + message);
							String[] split = message.split(" ", 2);
							switch(split[0]) {
								case "MESSAGE":		// Format "<username> <message>"
									String[] aux = split[1].split(" ", 2);
									String usernameToSend = aux[0];
									String messageToSend = aux[1];
									// Intra-server command format is "USERNAME <username> MESSAGE <message>"
									StringBuilder formattedCommand = new StringBuilder().append("USERNAME ");
									formattedCommand.append(username).append(" MESSAGE ").append(messageToSend);
									if (chatTools.sendMessage(usernameToSend, formattedCommand.toString())) {
										print(Thread.currentThread().getName() + ": Message sent to user " + usernameToSend);
										out.println("INFO Message sent");
									} else {
										print(Thread.currentThread().getName() + ": Failed to send message to user " + usernameToSend);
										out.println("DELIVER_FAILED " + usernameToSend);
									}
									break;
								case "GET_USERS":	// Need an updated list of active users' usernames
									StringBuilder sb = new StringBuilder();
									sb.append("ACTIVE_USERNAMES");
									for (String user: chatTools.getUsernames()) {
										sb.append(" ").append(user);
									}
									out.println(sb.toString());
									print(Thread.currentThread().getName() + ": Updated list of usernames sent to " + username);
									break;
								case "DISCONNECT":	// Client disconnected
									end = true;
									chatTools.removeUser(username);
									print(Thread.currentThread().getName() + ": User " + username + " disconnected");
									break;
								case "NOT_IMPLEMENTED":
									// Case to implement
									break;
								default:
									print(Thread.currentThread().getName() + ": Unknown command");
							}
						}
						// There is no need for polling so quickly and, therefore, it can be interrupted
						TimeUnit.MILLISECONDS.sleep(POLL_DELAY);
					} catch (InterruptedException e) {
						end = true;
						chatTools.removeUser(username);
						out.println("REFUSED Server interrupted");
						print(Thread.currentThread().getName() + ": Server interrupted");
					}
					if (socket.isClosed() && !end) {
						end = true;
						chatTools.removeUser(username);
						print(Thread.currentThread().getName() + ": Connection lost with user " + username + ". Disconnecting");
					}
				}
			} else {
				print(Thread.currentThread().getName() + ": New user addition failure. Closing connection");
				out.println("REFUSED Username already connected");
			}
		} else {
			print(Thread.currentThread().getName() + ": Username not received. Closing connection");
			out.println("REFUSED Username not found");
		}
		socket.close();
	}

	protected void print(String s) {
		if (frame!=null) {
			frame.appendTextArea(s);
		} else {
			System.out.println(s);
		}
	}

	public void setFrame(ServerFrame frame) {
		this.frame = frame;
	}

}

package pkgControl;

import pkgChatUtils.Sound;
import pkgClients.ChatClient;
import pkgFrames.ChatClientFrame;
import pkgFrames.IndividualChatPanel;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatClientWorker extends SwingWorker<Void, String> {
	private ChatClientFrame frame;
	private MyChatClient client;

	private class MyChatClient extends ChatClient {
		MyChatClient(String host, int port, String username) {
			super(host, port, username);
		}

		@Override
		protected void print(String s) {
			publish(s);	// Now the commands are processed in the process method
		}
	}

	public ChatClientWorker(ChatClientFrame frame, String username, String host, int port, LinkedBlockingQueue<String> queue) {
		this.frame = frame;
		this.client = new MyChatClient(host, port, username);
		this.client.setFrameInQueue(queue);
	}

	@Override
	protected Void doInBackground() throws Exception {
		client.connect();
		return null;
	}

	@Override
	protected void process(List<String> chunks) {
		String username;
		IndividualChatPanel chatPanel;
		// Process commands from client
		for (String command: chunks) {
			String[] tokens = command.split(" ", 2);
			switch (tokens[0]) {
				case "ACCEPTED":
					frame.enableHub();
					break;
				case "REFUSED":		// Format: "REFUSED <cause>"
					frame.disableHub();
					if (tokens.length > 1) frame.appendLogText("REFUSED: " + tokens[1]);
					break;
				case "INFO":		// Format: "INFO <info_message>"
					frame.appendLogText(tokens[1]);
					break;
				case "ACTIVE_USERNAMES":	// Format: "ACTIVE_USERNAMES <username1> <username2> (...) <last_username>"
					if (tokens.length > 1) {	// It should be this way, but in case it's not
						String[] usernames = tokens[1].split(" ");
						DefaultListModel<String> usernamesList = new DefaultListModel<>();
						for (String u: usernames) {
							if (!u.equals(frame.getUsername())) {
								usernamesList.addElement(u);
							}
						}
						frame.updateActiveUsersList(usernamesList);
					}
					break;
				case "MESSAGE":		// Format: "MESSAGE <username_from> <message>"
					String[] aux = tokens[1].split(" ", 2);
					username = aux[0];
					String message = aux[1];
					chatPanel = frame.getChatPanel(username);
					if (message.charAt(0) == '*') {		// Control message
						chatPanel.writeInfoText(message);
					} else {	// Message from user
						chatPanel.writeOtherMessage(message);
					}
					// Lights on the new notification icon if not in the chat tab
					if (!frame.getChatTabTitle().equals(username)) {
						frame.setNotificationIconVisibleForUsername(username, true);
						new Sound().start();
					}
					frame.appendLogText("Message received from user " + username);
					break;
				case "DELIVER_FAILED":		// Format: "DELIVER_FAILED <username_to_send>"
					username = tokens[1];
					chatPanel = frame.getChatPanel(username);
					chatPanel.writeInfoText("* Message could not be delivered *");
					break;
				default:
					frame.appendLogText("Unknown message format received: " + command);
					break;
			}
		}
	}

	// Primera implementacion, la dejo por si me sirve de algo
//	@Override
//	protected Void doInBackground() throws Exception {
//		// Set client queue the worker will receive commands from
//		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
//		client.setFrameOutQueue(queue);
//		Thread clientThread = new Thread(() -> client.connect());
//		// Poll the queue until its interrupted
//		boolean end = false;
//		String command;
//		while(!end) {
//			try {
//				command = queue.poll(5, TimeUnit.SECONDS);
//				if (command != null) {
//					publish(command);
//				}
//			} catch (InterruptedException ie) {
//				end = true;
//			}
//		}
//		return null;
//	}
}


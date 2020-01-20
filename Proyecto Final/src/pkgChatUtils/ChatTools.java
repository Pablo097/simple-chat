package pkgChatUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChatTools {
	private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> activeUsers;

	public ChatTools() {
		activeUsers = new ConcurrentHashMap<>();
	}

	public boolean sendMessage(String username, String message) {
		boolean done = false;
		Queue<String> queue = getUserQueue(username);
		if (queue != null) {
			queue.add(message);
			done = true;
		}
		return done;
	}

	public boolean addNewUser(String username, ConcurrentLinkedQueue<String> queue) {
		if (activeUsers.containsKey(username)) {
			return false;
		} else {
			activeUsers.put(username, queue);
			return true;
		}
	}

	public void removeUser(String username) {
		activeUsers.remove(username);
	}

	private Queue<String> getUserQueue(String username) {
		return activeUsers.get(username);
	}

	public int getNumberOfUsers() {
		return activeUsers.size();
	}

	public ArrayList<String> getUsernames() {
		return new ArrayList<>(Collections.list(activeUsers.keys()));
	}
}

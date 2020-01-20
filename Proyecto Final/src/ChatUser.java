import pkgFrames.ClientInitializer;
import pkgFrames.ServerFrame;
import pkgServers.ChatServer;
import pkgUtils.WindowUtilities;

import java.awt.*;

// Main for users which are not running the chat server
public class ChatUser {

	public static void main(String[] args) {
		String hostname = "83.38.175.102";
		int chatPort = 9000;

		WindowUtilities.setNativeLookAndFeel();
		ClientInitializer clientInitializer = new ClientInitializer(null, chatPort, true);
	}
}

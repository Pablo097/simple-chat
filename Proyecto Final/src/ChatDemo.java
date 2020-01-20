import pkgFrames.ClientInitializer;
import pkgFrames.ServerFrame;
import pkgServers.ChatServer;
import pkgUtils.WindowUtilities;

import java.awt.*;

/*
	----- THE MAIN FOR THE REAL PROJECT IS THIS -------
 */
// Main for creating a chat server and as much chat clients as necessary
public class ChatDemo {

	public static void main(String[] args) {
		String hostname = "localhost";
		int chatPort = 9000;

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		WindowUtilities.setNativeLookAndFeel();

		ChatServer chatServer = new ChatServer(chatPort);
		ServerFrame serverFrame = new ServerFrame("Chat Server", chatServer,width*13/20, height*2/10);
		chatServer.start();

		ClientInitializer clientInitializer = new ClientInitializer(hostname, chatPort, false);
//		ChatClientFrame client1 = new ChatClientFrame("Pablo", hostname, chatPort, width*1/20, height*2/10);
//		ChatClientFrame client2 = new ChatClientFrame("Carlos", hostname, chatPort, width*4/20, height*2/10);
//		ChatClientFrame client3 = new ChatClientFrame("Rocio", hostname, chatPort, width*7/20, height*2/10);
//		ChatClientFrame client4 = new ChatClientFrame("Lorena", hostname, chatPort, width*10/20, height*2/10);
	}
}

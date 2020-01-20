import pkgFrames.SumClientFrame;
import pkgFrames.ServerFrame;
import pkgServers.SumServer;
import pkgUtils.WindowUtilities;

import java.awt.*;

// Main for creating a sum server and two clients
public class SumDemo {

	public static void main(String[] args) {
		String hostname = "localhost";
		int sumPort = 9000;

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		WindowUtilities.setNativeLookAndFeel();

		SumClientFrame client1 = new SumClientFrame("Cliente 1", hostname, sumPort, width*2/10, height*2/10);
		SumClientFrame client2 = new SumClientFrame("Cliente 2", hostname, sumPort, width*2/10, height*6/10);

		SumServer sumServer = new SumServer(sumPort);
		ServerFrame serverFrame = new ServerFrame("Servidor suma", sumServer,width*6/10, height*3/10);
		sumServer.start();
	}
}

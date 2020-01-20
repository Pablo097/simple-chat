package pkgControl;
import pkgFrames.*;
import pkgServers.MultiThreadServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerController implements ActionListener {
	ServerFrame frame;
	MultiThreadServer server;

	public ServerController(ServerFrame frame, MultiThreadServer server) {
		this.frame = frame;
		this.server = server;
		this.server.setFrame(frame);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand().toUpperCase()) {
			case "CLEAR":
				frame.clearTextArea();
				break;
			case "EXIT":
				server.interrupt();
				System.exit(0);
				break;
			default:
				throw new RuntimeException("Unknown command");
		}
	}
}

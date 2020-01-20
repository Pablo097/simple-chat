package pkgServers;
import pkgFrames.ServerFrame;
import pkgUtils.SocketUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SumServer extends MultiThreadServer {
	private ServerFrame frame = null;

	public SumServer(int port) {
		super(port);
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

		String line;
		double num1, num2;
		try {
			line = in.readLine();
			num1 = Double.parseDouble(line);
			line = in.readLine();
			num2 = Double.parseDouble(line);
			print("Numbers received: " + num1 + ", " + num2);
			out.println(num1+num2);
		} catch (NumberFormatException e) {
			print("Number format error");
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

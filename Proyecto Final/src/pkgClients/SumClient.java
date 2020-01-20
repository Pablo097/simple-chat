package pkgClients;
import pkgFrames.SumClientFrame;
import pkgUtils.SocketUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SumClient extends NetworkClient {
    private double num1, num2;
    private double result;
    SumClientFrame frame = null;

    public SumClient(String host, int port, double num1, double num2) {
        super(host, port);
        this.num1 = num1;
        this.num2 = num2;
    }

    @Override
    protected void handleConnection(Socket client) throws IOException {
        PrintWriter out = SocketUtils.getWriter(client);
        BufferedReader in = SocketUtils.getReader(client);

        print("Connected to " + getHost() + " on port " + getPort());
        // Send numbers
        out.println(num1);
        out.println(num2);
        // Receive sum
        String message;
        do {
            message = in.readLine();
        } while ((message == null) || (message.length() == 0));
        try {
            print("Received: " + message);
            result = Double.parseDouble(message);
        } catch (NumberFormatException e) {
            print("Number format error");
        }
    }

    public double getResult() {
        return result;
    }

    @Override
    protected void print(String s) {
        if (frame!=null) {
            frame.appendTextArea(s);
        } else {
            System.out.println(s);
        }
    }

    public void setFrame(SumClientFrame frame) {
        this.frame = frame;
    }
}
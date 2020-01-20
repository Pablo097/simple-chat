package pkgClients;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class NetworkClient {
    private String host;
    private int port;

    public String getHost() {
        return(host);
    }

    public int getPort() {
        return(port);
    }

    public NetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        try(Socket client = new Socket(host, port)) {
            handleConnection(client);
        } catch(UnknownHostException e) {
            print("INFO Unknown host: " + host);
            // e.printStackTrace();
        } catch(IOException ioe) {
            print("INFO IOException: " + ioe);
            // ioe.printStackTrace();
        }
    }

    protected abstract void handleConnection(Socket client) throws IOException;

    protected abstract void print(String s);
}

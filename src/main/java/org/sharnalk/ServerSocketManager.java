package org.sharnalk;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketManager {
    private final ServerSocket serverSocket;
    public ServerSocketManager(int port) throws IOException{
        serverSocket = new ServerSocket(port);
    }
    public Socket waitClientRequest() throws IOException {
        return serverSocket.accept();
    }
    public void shutDown() throws IOException{
        serverSocket.close();
    }
}

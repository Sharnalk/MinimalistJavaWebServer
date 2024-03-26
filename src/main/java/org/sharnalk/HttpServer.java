package org.sharnalk;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class HttpServer extends Thread {
    /**
     * A simple HTTP server that listens on a specified port and IP address.
     * It handles incoming connections with a thread pool, allowing for concurrent request processing.
     *
     * @param port        Port to listen on, with 0 for dynamic allocation.
     * @param inetAddress IP address to bind to, or null/"0.0.0.0" for all interfaces.
     * @throws IOException if binding to the port fails or during server operation.
     */
    public static void HttpServer(int port, InetAddress inetAddress) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(1); // Adjustable thread pool size
        int backlog = 50; // Backlog size, adjustable for different request queue capacities
        try(ServerSocket serverSocket = new ServerSocket(port, backlog, inetAddress)){
            logStartServer(serverSocket);
            while (true){
                Socket client = serverSocket.accept();
                executor.submit(new ClientHandler(client));
            }
        }
    }

    /**
     * Logs server start information, including the listening address and port.
     *
     * @param serverSocket ServerSocket instance for access to address and port data.
     */
    private static void logStartServer(ServerSocket serverSocket){
        String address = serverSocket.getInetAddress().getHostAddress().equals("0.0.0.0") ? "/localhost" : String.valueOf(serverSocket.getInetAddress());
        System.out.println("Server listening on http:/"+ address + ":" + serverSocket.getLocalPort() +"\r\n");
    }
}

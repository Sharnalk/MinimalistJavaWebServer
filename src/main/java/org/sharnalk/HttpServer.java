package org.sharnalk;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class HttpServer extends Thread {
    /**
     * Initializes and runs an HTTP server on a specified port.
     * This server listens for incoming client connections and handles them using a fixed-size thread pool.
     *
     * @param port The port number on which the server will listen. Valid range is 1024 to 65535 for non-privileged services.
     * @throws IOException If the server cannot bind to the specified port or if an I/O error occurs during server operation.
     *
     * Usage example:
     * {@code
     *   HttpServer.HttpServer(8080); // Starts the server on port 8080
     * }
     *
     * Note: The size of the thread pool determines how many concurrent requests the server can handle. It can be adjusted
     * according to the server's capacity and expected load. The default size is set to handle a single request at a time.
     */
    public static void HttpServer(int port) throws IOException {
        System.out.println("Server listening on http://localhost:" + port +"\r\n");
        ExecutorService executor = Executors.newFixedThreadPool(1); //Thread pool size can be adjusted here
        try(ServerSocket serverSocket = new ServerSocket(port)){
            while (true){
                Socket client = serverSocket.accept();
                executor.submit(new ClientHandler(client));
            }
        }
    }
}

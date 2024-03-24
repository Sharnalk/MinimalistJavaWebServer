package org.sharnalk;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer extends Thread {
    /**
     * HttpServer
     * This class start the server on a specific @port
     * then we listen for the request by accepting them with the Socket
     * We create a ThreadPool to treat nThreads simultaneous, you can change this value depend on your server capacity
     *
     */
    public static void HttpServer(int port) throws IOException {
        System.out.println("Server listening on port " + port);
        //Here you can change your ThreadPool
        ExecutorService executor = Executors.newFixedThreadPool(1);
        try(ServerSocket serverSocket = new ServerSocket(port)){
            while (true){
                Socket client = serverSocket.accept();
                executor.submit(new ClientHandler(client));
            }
        }
    }


}

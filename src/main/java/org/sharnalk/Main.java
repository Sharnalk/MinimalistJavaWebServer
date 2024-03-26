package org.sharnalk;


import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = new ArgumentsParser(args).parseCommandLine();
        HttpServer.HttpServer(port);
    }
}
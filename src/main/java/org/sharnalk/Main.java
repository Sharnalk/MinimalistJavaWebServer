package org.sharnalk;


import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        Map.Entry<Integer, InetAddress> addressEntry = new ArgumentsParser(args).parseCommandLine();
        HttpServer.HttpServer(addressEntry.getKey(), addressEntry.getValue());
    }
}
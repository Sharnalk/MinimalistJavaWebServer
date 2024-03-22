package org.sharnalk;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class BaseServer {
    /**
     * Base Server Class.
     * This class initializes a ServerSocket to listen for connections on a specified port.
     * Upon accepting a connection from a client (Socket), the server reads the HTTP request,
     * determines the requested resource, and responds accordingly.
     *
     * The URL format is expected to be http://host:port/resource-path.
     * If the requested resource exists within the src/main/resources directory, the server
     * sends the resource content with a 200 OK response. Otherwise, a 404 Not Found response is sent.
     */
    public static String response404 = "HTTP/1.1 404 Not Found\r\n\r\n";
    public static String response200 = "HTTP/1.1 200 OK\r\n\r\n";
    public static void HttpServer(int port) throws IOException {

        try(ServerSocket serverSocket = new ServerSocket(port)) {

            while (true){

                try(Socket client = serverSocket.accept()){

                    BufferedReader clientRequest = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    OutputStream clientOutput = client.getOutputStream();

                    String clientRequestString = clientRequest.readLine();
                    if (clientRequestString == null || clientRequestString.isBlank()) continue;

                    String requestHeader = clientRequestString.toString().split("\n")[0];
                    String ressource = requestHeader.split(" ")[1];

                    String filePath = "src/main/resources" + ((ressource.equals("/")) ? "/default.html" : ressource);
                    File file = new File(filePath);
                    if (!file.exists()){
                        String fileNotFoundPath = "src/main/resources/404NotFound.html";
                        httpStaticResponse(fileNotFoundPath,response404,clientOutput);
                    }else{
                        httpStaticResponse(filePath,response200,clientOutput);
                    }
                    clientOutput.close();
                }
            }
        }
    }

    /**
     * Provide a basic template for httpResponses
     * @param filePath
     * @param response
     * @param clientOutput
     * @throws IOException
     */
    private static void httpStaticResponse(String filePath, String response, OutputStream clientOutput) throws IOException {
        try(FileInputStream file = new FileInputStream(filePath)){
            clientOutput.write(response.getBytes(StandardCharsets.UTF_8));
            clientOutput.write(file.readAllBytes());
            clientOutput.flush();
        };
    };
}

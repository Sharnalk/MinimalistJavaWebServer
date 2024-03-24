package org.sharnalk;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable{
    /**
     * ClientHandler Class.
     * This class get a Socket in parameter to build the request
     * Upon accepting a connection from a client (Socket), the server reads the HTTP request,
     * determines the requested resource, and responds accordingly.
     *
     * The URL format is expected to be http://host:port/resource-path.
     * If the requested resource exists within the src/main/resources directory, the server
     * sends the resource content with a 200 OK response. Otherwise, a 404 Not Found response is sent.
     */
    public static String response404 = "HTTP/1.1 404 Not Found\r\n\r\n";
    public static String response200 = "HTTP/1.1 200 OK\r\n\r\n";
    private final Socket clientSocket;
    private String ressource = "src/main/resources";
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
            try{
                BufferedReader clientRequest = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream clientOutput = clientSocket.getOutputStream();

                //We get the route from the URL
                String requestLine = clientRequest.readLine();
                String firstLine = requestLine.toString().split("\n")[0];
                String route = firstLine.split(" ")[1];
                System.out.println(firstLine);

                //Here you can specify your File Server
                String filePath = ressource + ((route.equals("/")) ? "/default.html" : route);
                File file = new File(filePath);
                if (!file.exists()){
                    String fileNotFoundPath = ressource + "/404NotFound.html";
                    httpStaticResponse(fileNotFoundPath,response404,clientOutput);
                }else{
                    httpStaticResponse(filePath,response200,clientOutput);
                }
                clientOutput.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
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

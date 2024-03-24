package org.sharnalk;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

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

    //Here you can specify the main directory you want to search in the navigator
    private static String ressource = "src/main/resources";
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
            try(BufferedReader clientRequest = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));){
                OutputStream clientOutput = clientSocket.getOutputStream();

                String[] request = requestHandler(clientRequest);
                loggingHandler(request);
                String route = request[1];

                httpStaticResponse(route,clientOutput);
                clientOutput.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    /**
     * Provide a basic template for httpResponses
     * @param clientOutput
     * @throws IOException
     */
    private static void httpStaticResponse(String route, OutputStream clientOutput) throws IOException {
        String filePath = ressource + (route.equals("/") ? "/default.html" : route);
        File file = new File(filePath);
        if (!file.exists()){
            filePath = ressource + "/404NotFound.html";
            clientOutput.write(response404.getBytes(StandardCharsets.UTF_8));
        }else {
            clientOutput.write(response200.getBytes(StandardCharsets.UTF_8));
        }
        try(FileInputStream fileInputStream = new FileInputStream(filePath)){
            clientOutput.write(fileInputStream.readAllBytes());
            clientOutput.flush();
        }
    }

    /**
     *
     * @param clientRequest
     * @return
     * @throws IOException
     */
    private static String[] requestHandler(BufferedReader clientRequest) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line = clientRequest.readLine();
        while (!line.isBlank()){
            sb.append(line+ " ");
            line = clientRequest.readLine();
        }
        System.out.println(sb);
        String[] request = sb.toString().split(" ");
        return request;
    }

    private static void loggingHandler(String[] request) throws IOException {
        String header = Arrays.toString(Arrays.copyOfRange(request,0,3)).replace(",", " ");
        String URL = request[4];
        System.out.println("URL : "+URL + " - - [" + LocalTime.now().format(timeFormatter)+"] " + header);
    }
}

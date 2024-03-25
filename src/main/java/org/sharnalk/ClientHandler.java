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
     * sends the content of the resource with a 200 OK response. Otherwise, a 404 Not Found response is sent.
     *
     * This class implements Runnable to allow its execution in a separate thread,
     * enabling the server to handle multiple client connections concurrently.
     */
    private static String response404 = "HTTP/1.1 404 Not Found\r\n\r\n";
    private static String response200 = "HTTP/1.1 200 OK\r\n\r\n";
    private static String ressource = "src/main/resources";
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


    /**
     * Processes the client's request, generates, and sends the response.
     * This method is called when the dedicated thread for this ClientHandler starts.
     */
    @Override
    public void run() {
            try(BufferedReader clientRequest = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));){
                OutputStream clientOutput = clientSocket.getOutputStream();

                String[] request = parseHttpRequest(clientRequest);
                String resourcePath = request[1];
                sendHttpResponse(resourcePath,clientOutput);
                logRequest(request);
                clientOutput.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    /**
     * Generates and sends the HTTP response based on the requested route.
     *
     * @param route        The route requested by the HTTP request.
     * @param clientOutput The output stream to the client to send the response.
     * @throws IOException If an I/O error occurs while sending the response.
     */
    private static void sendHttpResponse(String route, OutputStream clientOutput) throws IOException {
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
     * Reads and parses the HTTP request from the client.
     *
     * @param clientRequest The input stream from the client socket.
     * @return An array of Strings containing elements of the first line of the HTTP request.
     * @throws IOException If an I/O error occurs while reading the request.
     */
    private static String[] parseHttpRequest(BufferedReader clientRequest) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line = clientRequest.readLine();
        while (!line.isBlank()){
            sb.append(line+ " ");
            line = clientRequest.readLine();
        }
        String[] request = sb.toString().split(" ");
        return request;
    }

    /**
     * Logs the HTTP request to the console.
     *
     * @param request The parsed HTTP request.
     * @throws IOException If an error occurs during logging.
     */
    private static void logRequest(String[] request) throws IOException {
        String requestHeader = String.join(" ", Arrays.copyOfRange(request, 0, 3));
        String URL = request[4];
        System.out.println(URL + " - - [" + LocalTime.now().format(timeFormatter)+"] " + requestHeader);
    }
}

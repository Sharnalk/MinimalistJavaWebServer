package org.sharnalk;

import java.io.*;
import java.net.MalformedURLException;
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
    public static final String RESPONSE_404 = "HTTP/1.1 404 Not Found";
    public static final String RESPONSE_200 = "HTTP/1.1 200 OK";
    private static final String DEFAULT_RESOURCE_PATH = "src/main/resources";
    private static final String DEFAULT_PAGE = "/default.html";
    private static final String NOT_FOUND_PAGE = "/404NotFound.html";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

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
                String serverResponse = sendHttpResponse(resourcePath,clientOutput);
                logRequest(request, serverResponse);
                clientOutput.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    /**
     * Parses the initial line of an HTTP request received from the client.
     *
     * This method reads the request from the client and extracts the initial request line,
     * which contains the HTTP method, requested path, and HTTP version. It throws an
     * {@link MalformedURLException} if the request line is malformed or empty, ensuring that
     * subsequent processing is based on valid request syntax.
     *
     * @param clientRequest The input stream from the client socket, wrapped in a BufferedReader.
     * @return An array of Strings containing the components of the request line.
     * @throws IOException If an I/O error occurs while reading from the input stream.
     * @throws MalformedURLException If the request line is malformed or empty.
     */
    public static String[] parseHttpRequest(BufferedReader clientRequest) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line = clientRequest.readLine();
        if (line.isBlank() || line == "") {
            throw new MalformedURLException("Malformed Request");
        }
        while (!line.isBlank()){
            sb.append(line + " ");
            line = clientRequest.readLine();
        }

        return sb.toString().split(" ");
    }

    /**
     * Generates and sends an HTTP response based on the requested resource path.
     *
     * This method attempts to locate the requested resource within the server's resource
     * directory. If the resource exists, it sends a 200 OK response with the resource content.
     * Otherwise, it sends a 404 Not Found response. The actual response, including the status
     * line and content, is written to the client's output stream.
     *
     * @param route The requested resource path extracted from the HTTP request.
     * @param clientOutput The output stream to the client, used to send the response.
     * @throws IOException If an I/O error occurs while reading the resource or writing the response.
     */
    private static String sendHttpResponse(String route, OutputStream clientOutput) throws IOException {
        String filePath = DEFAULT_RESOURCE_PATH + (route.equals("/") ? DEFAULT_PAGE : route);
        String serverResponse = "";
        File file = new File(filePath);
        if (!file.exists()){
            filePath = DEFAULT_RESOURCE_PATH + NOT_FOUND_PAGE;
            serverResponse = RESPONSE_404;
        }else {
            serverResponse = RESPONSE_200;
        }

        clientOutput.write(serverResponse.getBytes(StandardCharsets.UTF_8));
        try(FileInputStream fileInputStream = new FileInputStream(filePath)){
            clientOutput.write(fileInputStream.readAllBytes());
            clientOutput.flush();
        }
        return serverResponse;
    }

    /**
     * Logs the HTTP request and response status to the console.
     *
     * This method constructs a log entry for the HTTP request using the request method, path, and
     * the server's response status. The log entry includes a timestamp and is printed to the console.
     *
     * @param request An array of Strings containing the components of the request line.
     * @param serverResponse The HTTP status line of the server's response.
     * @throws IOException If an error occurs during logging. Note: This exception is not expected
     *                     to be thrown as the method currently does not perform I/O operations
     *                     that could result in an IOException.
     */
    public static void logRequest(String[] request, String serverResponse) throws IOException {
        String requestHeader = String.join(" ", Arrays.copyOfRange(request, 0, 2));
        String URL = request[4];
        System.out.println(URL + " - - [" + LocalTime.now().format(TIME_FORMATTER)+"] " + requestHeader +" " + serverResponse);
    }
}

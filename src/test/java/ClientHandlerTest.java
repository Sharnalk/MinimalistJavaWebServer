import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sharnalk.ClientHandler;

import java.io.*;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class ClientHandlerTest {
    String httpInput = "GET / HTTP/1.1 \r\n\r\n";
    String[] httpInputArray = {"GET", "/", "HTTP/1.1", "Host:", "localhost:00000"};
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp(){
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    @AfterEach
    public void tearDown(){
        System.setOut(standardOut);
    }
    @Test
    public void parseHttpRequestShouldReturnCorrectParts() throws Exception{
        BufferedReader reader = new BufferedReader(new StringReader(httpInput));

        String[] result = ClientHandler.parseHttpRequest(reader);

        assertArrayEquals(new String[]{"GET", "/", "HTTP/1.1"}, result);
    }

    @Test
    public void parseBadHttpRequestShouldReturnException(){
        String httpInput = "\r\n";
        BufferedReader reader = new BufferedReader(new StringReader(httpInput));

        Exception exception = assertThrows(MalformedURLException.class, () ->{
            ClientHandler.parseHttpRequest(reader);
        } );
        String expectedMessage = "Malformed Request";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void logRequest_ShouldPrintCorrectFormat() throws IOException {
    ClientHandler.logRequest(httpInputArray,ClientHandler.RESPONSE_200);
    String expectedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    String expectedOutput = httpInputArray[4] + " - - [" + expectedDate + "] GET / " + ClientHandler.RESPONSE_200;

    assertTrue(outputStreamCaptor.toString().trim().contains(expectedOutput));
    }


}

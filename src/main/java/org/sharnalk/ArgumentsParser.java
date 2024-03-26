package org.sharnalk;

import org.apache.commons.cli.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.AbstractMap;
import java.util.Map;

/**
 * Parses command-line arguments for the application.
 * This class is designed to interpret command-line options and provide configured values,
 * such as the server port, for application use.
 */
public class ArgumentsParser {
    private final String[] args;

    /**
     * Constructs an ArgumentsParser with the command-line arguments.
     *
     * @param args The command-line arguments passed to the application.
     */
    public ArgumentsParser(String[] args) {
        this.args = args;
    }

    /**
     * Parses command-line arguments for the application to extract server configuration such as port and host.
     * This class utilizes Apache Commons CLI to interpret command-line options and provides the extracted
     * configuration values, like server port and host, for application use.
     *
     * If a port is specified using the '-p' or '--port' option, that value is used; otherwise, a default
     * value of 0 indicates dynamic port allocation. Similarly, if a host is specified using the '-h' or
     * '--host' option, that value is used; otherwise, it defaults to "0.0.0.0", indicating the server
     * should listen on all interfaces.
     *
     * In case of parsing failure due to invalid arguments or if the specified host is unknown, the application
     * terminates with an error message.
     */
    public Map.Entry<Integer, InetAddress> parseCommandLine() throws UnknownHostException {
        Options options = new Options();
        options.addOption("p", "port", true, "Port for the server");
        options.addOption("h", "host", true, "Host for the server");

        CommandLineParser parser = new DefaultParser();
        Integer port = 0; // Default to 0, indicating dynamic port allocation if not specified.
        InetAddress inetAddress = InetAddress.getByName("0.0.0.0"); // Default to null, indicating dynamic inetAddress allocation if not specified.
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("port")) {
                port = Integer.parseInt(cmd.getOptionValue("port"));
                if (port < 0 || port > 65535){
                    throw new IllegalArgumentException("Port number out of valid range (0-65535): " + port);
                }
                System.out.println("Port selected: " + port);
            }
            System.out.println("Port selected: " + (cmd.hasOption("port") ? port : "using dynamic port allocation"));

            if (cmd.hasOption("host")){
                inetAddress = InetAddress.getByName(cmd.getOptionValue("host"));
            }
            System.out.println("Host selected: " + (cmd.hasOption("host") ? inetAddress : "using dynamic host allocation"));

        } catch (ParseException | UnknownHostException e) {
            throw new RuntimeException("Error parsing command-line arguments: " + e.getMessage());
        }
        return new AbstractMap.SimpleEntry<>(port, inetAddress);
    }
}

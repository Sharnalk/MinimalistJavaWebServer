package org.sharnalk;

import org.apache.commons.cli.*;

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
     * Parses the command-line arguments and extracts configuration values,
     * such as the server port. If a port is specified using the '-p' or '--port'
     * option, that value is used. Otherwise, a default value of 0 is returned
     * to signify that no specific port was requested, allowing for dynamic port allocation.
     *
     * Prints the selected port to the console. If parsing fails due to invalid
     * arguments or other issues, the application will terminate with an error message.
     *
     * @return The port specified by the command-line arguments, or 0 if none was specified.
     */
    public int parseCommandLine() {
        Options options = new Options();
        options.addOption("p", "port", true, "Port for the server");

        CommandLineParser parser = new DefaultParser();
        int port = 0; // Default to 0, indicating dynamic port allocation if not specified.
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("port")) {
                port = Integer.parseInt(cmd.getOptionValue("port"));
                System.out.println("Port selected: " + port);
            } else {
                System.out.println("No port argument specified, using dynamic port allocation: " + port);
            }
        } catch (ParseException e) {
            System.err.println("Error parsing command-line arguments: " + e.getMessage());
            System.exit(1);
        }
        return port;
    }
}

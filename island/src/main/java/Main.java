import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import island.IslandGenerator;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Main {
    /**
     * The {@code ParentLogger} that will be used to assist in debug.
     */
    private static final ParentLogger logger = new ParentLogger();

    public static void main(String[] args) throws IOException {

        logger.trace("Extracting command line arguments");

        //Setting up the initial variables
        Map<String, String> cmdArguments = parseCmdArguments(args);
        String input = cmdArguments.get("input");
        String mesh_name = cmdArguments.get("output");
        String mode = cmdArguments.get("mode");
        String lakesString = cmdArguments.get("lakes");
        String aquifier = cmdArguments.get("aquifier");

        int lakes = Integer.parseInt(lakesString);

        // Getting width and height for the canvas
        Structs.Mesh aMesh = new MeshFactory().read(input);
        double max_x = Double.MIN_VALUE;
        double max_y = Double.MIN_VALUE;
        for (Structs.Vertex v: aMesh.getVerticesList()) {
            max_x = (Double.compare(max_x, v.getX()) < 0? v.getX(): max_x);
            max_y = (Double.compare(max_y, v.getY()) < 0? v.getY(): max_y);
        }

        IslandGenerator generator = new IslandGenerator(aMesh, max_x, max_y);
        aMesh = generator.generate(mode, lakes, aquifier);

        MeshFactory factory = new MeshFactory();
        factory.write(aMesh, mesh_name);
    }

    /**
     * This is the method used to parse command line arguments and return
     * a mapping of command line arguments to their value. It takes in an array of {@code String} as inputs.
     * @param args An array of String arguments.
     * @return {@code Map<String, String>}
     */
    private static Map<String, String> parseCmdArguments(String[] args) {
        logger.trace("Creating cmd options");
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        Map<String, String> cmdArguments = new HashMap<>();

        Option input = Option.builder("i")
                .longOpt("input")
                .argName("inputFile")
                .hasArg(true)
                .desc("Enter the file path of the input mesh you want to visualize")
                .build();

        Option output = Option.builder("o")
                .longOpt("output")
                .argName("outputFile")
                .hasArg(true)
                .desc("Enter the file name of the output file. It will be written in the current directory")
                .build();

        Option mode = Option.builder("mode")
                .argName("islandMode")
                .hasArg(true)
                .desc("Enter what mode the island should be generated in")
                .build();

        Option lakes = Option.builder("lakes")
                .argName("lakes")
                .hasArg(true)
                .desc("Enter the max number of lakes you want to generate")
                .build();

        Option aquifier = Option.builder("aquifier")
                .argName("aquifier")
                .hasArg(true)
                .desc("Enter the number of aquifiers you want to generate")
                .build();

        options.addOption(input);
        options.addOption(output);
        options.addOption(mode);
        options.addOption(lakes);
        options.addOption(aquifier);
        logger.trace("Possible options added to options list");

        try {
            CommandLine cmd = parser.parse(options, args);

            //Checking for input file
            if (cmd.hasOption("i")) {

                File inputFile = new File(cmd.getOptionValue("i"));

                //Check if the input entered is a directory, and if it exists
                if (inputFile.isDirectory()) {
                    throw new ParseException("Entered an invalid file path. Currently entered a directory");
                }
                else if (! inputFile.exists()){
                    throw new ParseException("File does not exist");
                }

                //If it is not a directory and exists, then map it to cmdArguments
                else {
                    cmdArguments.put("input", cmd.getOptionValue("input"));
                }
            }

            //If there is no input, throw exception
            else {
                throw new ParseException("Please enter an input file path");
            }

            logger.trace("Input file exists");

            //Checking for valid output name
            if (cmd.hasOption("o")) {

                String outputArg = cmd.getOptionValue("o");
                Paths.get(cmd.getOptionValue("o"));
                logger.trace("Output path is valid");

                if (outputArg.length() == 0) { throw new ParseException("Please enter valid output file name"); }

                cmdArguments.put("output", cmd.getOptionValue("output"));
            }
            else {
                throw new ParseException("Please enter a output file name");
            }

            logger.trace("Checking for island mode");
            if (cmd.hasOption("mode")) {
                String modeValue = cmd.getOptionValue("mode");

                if (Objects.equals(modeValue, "lagoon")) {
                    cmdArguments.put("mode", modeValue);
                }
                else {
                    throw new ParseException("Invalid island mode, please enter 'Lagoon'");
                }
            }

            else {
                throw new ParseException("Please enter an island mode");
            }

            logger.trace("Checking for number of lakes to implement");
            if (cmd.hasOption("lakes")) {
                String lakeValue = cmd.getOptionValue("lakes");

                int lakeInt = Integer.parseInt(lakeValue);

                if (lakeInt < 0) {
                    throw new ParseException("Invalid number of lakes entered, please enter more than 0 lakes");
                }

                cmdArguments.put("lakes", lakeValue);
            }
            else {
                logger.trace("No number of lakes given, assuming default of 0");
                cmdArguments.put("lakes", "0");
            }

            logger.trace("Checking for number of aquifiers to implement");
            if(cmd.hasOption("aquifier")){
                int aquifierValue = Integer.parseInt(cmd.getOptionValue("Aquifier"));
                
                if(aquifierValue < 0){
                    throw new ParseException("Invalid number of aquifiers entered, please enter more than 0 aquifiers");
                }
                cmdArguments.put("aquifier", cmd.getOptionValue("Aquifier"));
            }
            else{
                logger.trace("No number of aquifiers given, assuming default of 0");
                cmdArguments.put("aquifier", "0");
            }

        }

        //If the parsing fails, print out why and how to use the program
        catch (ParseException | InvalidPathException | NullPointerException | NumberFormatException exp) {
            logger.error("Parsing failed. Reason: " + exp.getMessage());
            formatter.printHelp("java -jar island.jar -[input path] -[output name] -[island mode] --[lakes | optional]", options);
            throw new RuntimeException(exp.getMessage());
        }

        //Return the mapping of command line arguments
        return cmdArguments;
    }
}

import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.visualizer.GraphicRenderer;
import ca.mcmaster.cas.se2aa4.a2.visualizer.MeshDump;
import ca.mcmaster.cas.se2aa4.a2.visualizer.SVGCanvas;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.*;

/**
 *  This class is the main  a Mesh data type given to it.
 *  It utilizes the information stored within the mesh to display vertices, segments, and polygons.
 *  This class utilizes the PropertyExtractor class to retrieve the properties of each component of the mesh.
 * @author Ahren, Mike, Simon
 * @version February 2023
 */
public class Main {

    /**
     * The {@code ParentLogger} that will be used to assist in debug.
     */
    private static final ParentLogger logger = new ParentLogger();

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

        Option input = Option.builder("input")
                .argName("inputFile")
                .hasArg(true)
                .desc("Enter the file path of the input mesh you want to visualize")
                .build();

        Option output = Option.builder("output")
                .argName("outputFile")
                .hasArg(true)
                .desc("Enter the file name of the output file. It will be written in the current directory")
                .build();

        Option debugMode = new Option("X", false, "Visualize the mesh in debug mode");

        options.addOption(input);
        options.addOption(output);
        options.addOption(debugMode);
        logger.trace("Possible options added to options list");

        try {
            CommandLine cmd = parser.parse(options, args);

            //Checking for input file
            if (cmd.hasOption("input")) {

                File inputFile = new File(cmd.getOptionValue("input"));

                //Check if the input entered is a directory, and if it exists
                if (inputFile.isDirectory()) {
                    throw new ParseException("Entered a valid file path. Currently entered a directory");
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
            if (cmd.hasOption("output")) {

                Paths.get(cmd.getOptionValue("output"));
                logger.trace("Output path is valid");

                cmdArguments.put("output", cmd.getOptionValue("output"));
            }
            else {
                throw new ParseException("Please enter a output file name");
            }

            logger.trace("Checking for visualizer mode");
            if (cmd.hasOption("X")) {
                cmdArguments.put("mode", "debug");
                logger.trace("Debug mode activated");
            }
        }

        //If the parsing fails, print out why and how to use the program
        catch (ParseException | InvalidPathException | NullPointerException exp) {
            logger.error("Parsing failed. Reason: " + exp.getMessage());
            formatter.printHelp("java -jar visualizer.jar -[input path] -[output file] -[debug mode | optional]", options);
            System.exit(1);
        }

        //Return the mapping of command line arguments
        return cmdArguments;
    }

    /**
     * This is the method main where the program begins. It will take in the command line arguments,
     * calculate the max size of the canvas, and render the information of the mesh onto a canvas.
     * Then it will write the information into a SVG file and dump the mesh information onto the terminal.
     */
    public static void main(String[] args) throws IOException {

        logger.trace("Extracting command line arguments");

        //Setting up the initial variables
        Map<String, String> cmdArguments = parseCmdArguments(args);
        String input = cmdArguments.get("input");
        String output = cmdArguments.get("output");

        boolean debugMode = cmdArguments.containsKey("mode");

        // Getting width and height for the canvas
        Structs.Mesh aMesh = new MeshFactory().read(input);
        double max_x = Double.MIN_VALUE;
        double max_y = Double.MIN_VALUE;
        for (Structs.Vertex v: aMesh.getVerticesList()) {
            max_x = (Double.compare(max_x, v.getX()) < 0? v.getX(): max_x);
            max_y = (Double.compare(max_y, v.getY()) < 0? v.getY(): max_y);
        }
        // Creating the Canvas to draw the mesh
        Graphics2D canvas = SVGCanvas.build((int) Math.ceil(max_x), (int) Math.ceil(max_y));
        logger.trace("Canvas size is: " + (int) Math.ceil(max_x) + (int) Math.ceil(max_y));

        GraphicRenderer renderer = new GraphicRenderer(debugMode);
        // Painting the mesh on the canvas
        logger.trace("Rendering the mesh");
        renderer.render(aMesh, canvas);

        // Dump the mesh to stdout
        MeshDump dumper = new MeshDump();
        dumper.dump(aMesh);

        // Storing the result in an SVG file
        logger.trace("Writing to SVG file");
        SVGCanvas.write(canvas, output);
    }
}

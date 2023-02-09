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
import java.util.Objects;

import org.apache.commons.cli.*;

public class Main {

    private static Map<String, String> parseCmdArguments(String[] args) {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        Map<String, String> cmdArguments = new HashMap<String, String>();

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

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("input")) {
                File inputFile = new File(cmd.getOptionValue("input"));
                if (inputFile.isDirectory()) {
                    throw new ParseException("Entered a valid file path. Currently entered a directory");
                }
                else if (! inputFile.exists()){
                    throw new ParseException("File does not exist");
                }
                else {
                    cmdArguments.put("input", cmd.getOptionValue("input"));
                }
            }
            else {
                throw new ParseException("Please enter an input file path");
            }

            if (cmd.hasOption("output")) {
                Paths.get(cmd.getOptionValue("output"));
                cmdArguments.put("output", cmd.getOptionValue("output"));
            }
            else {
                throw new ParseException("Please enter a output file name");
            }

            if (cmd.hasOption("mode")) {
                if (Objects.equals(cmd.getOptionValue("mode"), "X")) {
                    cmdArguments.put("mode", "debug");
                }
            }
        }

        catch (ParseException | InvalidPathException | NullPointerException exp) {
            System.out.println("Parsing failed. Reason: " + exp.getMessage());
            formatter.printHelp("java -jar visualizer.jar -[input path] -[output file] -[debug mode | optional]", options);
            System.exit(1);
        }

        return cmdArguments;
    }
    public static void main(String[] args) throws IOException {
        // Extracting command line parameters
        Map<String, String> cmdArguments = parseCmdArguments(args);
        String input = cmdArguments.get("input");
        String output = cmdArguments.get("output");

        boolean debug = cmdArguments.containsKey("mode");

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
        GraphicRenderer renderer = new GraphicRenderer();
        // Painting the mesh on the canvas
        renderer.render(aMesh, canvas, debug);
        // Storing the result in an SVG file
        SVGCanvas.write(canvas, output);
        // Dump the mesh to stdout
        MeshDump dumper = new MeshDump();
        dumper.dump(aMesh);
    }
}

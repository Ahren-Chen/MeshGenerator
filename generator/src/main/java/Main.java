import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.generator.Generator;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final ParentLogger logger = new ParentLogger();
    private static final String defaultPolygonNum = "50";
    private static final String defaultMode = "gridMesh";
    private static final String defaultRelaxationLevel = "10";
    public static void main(String[] args) throws Exception {
        try {
            Map<String, String> cmdArgs = parseCmdArguments(args);

            String mesh_name = cmdArgs.get("name");
            String mode = cmdArgs.get("mesh");
            String numOfPolygons = cmdArgs.get("polygonNum");
            String relaxationLevel = cmdArgs.get("relaxationLevel");

            int numOfPolygonsInt = Integer.parseInt(numOfPolygons);
            int relaxationLevelInt = Integer.parseInt(relaxationLevel);

            Generator generator = new Generator();
            Mesh myMesh = generator.generate(mode, numOfPolygonsInt, relaxationLevelInt);

            MeshFactory factory = new MeshFactory();
            factory.write(myMesh, mesh_name);
        }
        catch (NoSuchAlgorithmException ex) {
            logger.error(ex.getMessage());
        }
    }

    private static Map<String, String> parseCmdArguments(String[] args) {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        Map<String, String> cmdArguments = new HashMap<>();

        Option output = Option.builder("output")
                .argName("outputFile")
                .hasArg(true)
                .desc("Enter the output file name you want to store the mesh in")
                .build();

        Option polygonNum = Option.builder("polygonNum")
                .argName("polygons")
                .hasArg(true)
                .desc("Enter the number of polygons you want on the mesh")
                .build();

        Option relaxationLevel = Option.builder("relaxationLevel")
                .argName("relaxation")
                .hasArg(true)
                .desc("Enter the relaxation level you want for the polygons")
                .build();

        Option help = new Option("h", "-help", false, "Prints the avaliable options" +
                " and how to interact with the program through the command line");

        Option gridMesh = new Option("gridMesh", false, "Creates a mesh in grid mode");
        Option randomMesh = new Option("randomMesh", false, "Creates an irregular mesh");

        OptionGroup meshMode = new OptionGroup();
        meshMode.addOption(gridMesh);
        meshMode.addOption(randomMesh);

        options.addOption(output);
        options.addOption(polygonNum);
        options.addOption(relaxationLevel);
        options.addOption(help);
        options.addOptionGroup(meshMode);

        logger.trace("Adding possible options to options list");

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("output")) {
                Paths.get(cmd.getOptionValue("output"));
                logger.trace("Output path is valid");

                cmdArguments.put("name", cmd.getOptionValue("output"));
            }
            else {
                throw new ParseException("Please enter a valid output file name");
            }

            if (cmd.hasOption("polygonNum")) {
                String polygonNumValue = cmd.getOptionValue("polygonNum");

                //To test for number format exception
                int polygonNumInt = Integer.parseInt(polygonNumValue);

                if (polygonNumInt <= 0) {
                    throw new ParseException("Invalid number of polygons entered, please enter a number bigger than 0");
                }

                cmdArguments.put("polygonNum", polygonNumValue);
            }
            else {
                cmdArguments.put("polygonNum", defaultPolygonNum);
            }

            if (cmd.hasOption("relaxationLevel")) {
                String relaxationValue = cmd.getOptionValue("relaxationLevel");

                //To test for number format exception
                int relaxationValueInt = Integer.parseInt(relaxationValue);

                if (relaxationValueInt <= 0) {
                    throw new ParseException("Invalid relaxation level entered, please enter a number bigger than 0");
                }

                cmdArguments.put("relaxationLevel", relaxationValue);
            }
            else {
                cmdArguments.put("relaxationLevel", defaultRelaxationLevel);
            }

            if (cmd.hasOption("h")) {
                formatter.printHelp("java -jar generator.jar sample.mesh,randomMesh", options);
                System.exit(0);
            }

            if (cmd.hasOption("randomMesh")) {
                cmdArguments.put("mesh", "randomMesh");
            }

            else {
                cmdArguments.put("mesh", defaultMode);
            }
        }

        catch (NumberFormatException | ParseException | InvalidPathException | NullPointerException exp) {
            logger.error("Parsing failed. Reason: " + exp.getMessage());
            formatter.printHelp("java -jar generator.jar sample.mesh,randomMesh", options);
            System.exit(1);
        }

        return cmdArguments;
    }

}
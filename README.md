# Assignment A2: Mesh Generator

  - Author #1 [xum97@mcmaster.ca]
  - Author #2 [chens356@mcmaster.ca]
  - Author #3 [chena125@mcmaster.ca]

### Installation instructions

This product is handled by Maven, as a multi-module project. We assume here that you have cloned the project in a directory named `A2`

To install the different tooling on your computer, simply run: (Assuming you are already in the 'A2' directory)

```
mosser@azrael mvn install
```

After installation, you'll find an application named `generator.jar` in the `generator` directory, and a file named `visualizer.jar` in the `visualizer` one. 

### Generator

To run the generator, go to the `generator` directory, and use `java -jar` to run the product. The product takes at least one argument, the name of the file where the generated mesh will be stored as binary.
## Mandatory arguments:
* output <insert a file name to output the mesh onto>

## Optional Arguments:
* Adding the "-h" or "-help" argument will display what options you can run the generator in
* [-gridMesh || -randomMesh || TetrakisSquare] Whichever of these options is given is the mesh the generator will generate. (Default is gridMesh)
* -polygonNum <insert number of polygons you want to generate (will be ignored if gridMesh type is chosen)
* -relaxationLevel <insert how smooth you want the irregular mesh is> (will be ignored if gridMesh type is chosen)
* -vThickness <insert how thick you want vertices to be generated as a double type> (will default to 3 is none is given)
* -segThickness <insert how thick you want segments to be generated as a double type> (will default to 3 is none is given)
* -width <insert integer of how big you want the mesh> (will default to 500)
* -height <insert integer of how big you want the mesh> (will default to 500)

## Example:

```
mosser@azrael cd generator 
mosser@azrael java -jar generator.jar -output sample.mesh -randomMesh -polygonNum 50 -relaxationLevel 5 -vThickness 2.7 -segThickness 10 -height 700 -width 700
mosser@azrael ls -lh sample.mesh
-rw-r--r--  1 mosser  staff    29K 29 Jan 10:52 sample.mesh
mosser@azrael cd ../
```

### Island

To generate an island type from an existing mesh, go to `island` directory, and use `java -jar` to run the product. The product
takes an input mesh file and an output name for the resulting generated mesh. The product also takes in the `mode` of the island.

## Possible modes:
- lagoon

## Example:

```
mosser@azrael cd island 
mosser@azrael java -jar island.jar -o island.mesh -i ../generator/sample.mesh -mode lagoon
mosser@azrael ls -lh island.mesh
-rw-r--r--  1 mosser  staff    29K 29 Jan 10:52 island.mesh
mosser@azrael cd ../
```

### Visualizer

To visualize an existing mesh, go the `visualizer` directory, and use `java -jar` to run the product. The product take two arguments (so far): the file containing the mesh, and the name of the file to store the visualization (as an SVG image).

```
mosser@azrael cd visualizer 
mosser@azrael java -jar visualizer.jar -input ../generator/sample.mesh -output sample.svg

... (lots of debug information printed to stdout) ...

mosser@azrael ls -lh sample.svg
-rw-r--r--  1 mosser  staff    56K 29 Jan 10:53 sample.svg
mosser@azrael cd ../
```

To visualize a mesh in debug mode, go to the `visualizer` directory, and use `java jar` to run the product. The product will take 3 arguments: the input file containing the mesh, the name of the output file, and the debug option -X

```
mosser@azrael cd visualizer 
mosser@azrael java -jar visualizer.jar -input ../generator/sample.mesh -output sample.svg -X

... (lots of debug information printed to stdout) ...

mosser@azrael ls -lh sample.svg
-rw-r--r--  1 mosser  staff    56K 29 Jan 10:53 sample.svg
mosser@azrael cd ../
```

To visualize the SVG file:

  - Open it with a web browser
  - Convert it into something else with tool slike `rsvg-convert`

## How to contribute to the project

When you develop features and enrich the product, remember that you have first reinstall maven (as in `mvn install`) it so that the `jar` file is re-generated by maven.

## Possible Properties
Vertices: (Will assume default configurations if property is not given, or incorrect format)
- Color (Default: Black): 'rgba_color', 'R,G,B,A' (R, G, B, A are valid float numbers from 0-1. A is the alpha value (transparency), where 1 is visible and 0 is transparent)
- Centroid (Default: False): 'centroid', 'boolean' (Give True/False based on whether the vertex is a centroid)
- Thickness (Default: 3): 'thickness', 'int' (Give integer that is bigger than 0, describes the thickness of the vertex)

Segments: (Will assume default configurations if property is not given, or incorrect format)
- Color (Default: Black): 'rgba_color', 'R,G,B,A' (R, G, B, A are valid float numbers from 0-1. A is the alpha value (transparency), where 1 is visible and 0 is transparent)
- Thickness (Default: 3): 'thickness', 'int' (Give integer that is bigger than 0, describes the thickness of the segment)

Polygons: (Will assume default configurations if property is not given, or incorrect format)
- Color (Default: Black): 'rgba_color', 'R,G,B,A' (R, G, B, A are valid float numbers from 0-1. A is the alpha value (transparency), where 1 is visible and 0 is transparent)

## Backlog
see logbook.md
### Definition of Done

-- Successfully completes the feature goal with no bugs. It is also covered with tests. --

### Product Backlog
* Status:
    * Pending (P), Started (S), Blocked (B), Done (D), Needs Testing (T)

| Id | Feature title | Who? | Start | End | Status |
|:--:|---------------|------|-------|-----|--------|
| F1 | Draw segments between vertices to visualize the squares | Ahren, Mike, Mingyang | 02/04/23 | 02/04/23 | D |
| F2 | Remove duplicate vertices and segments | Shike(Mike) | 02/05/23 | 02/--/23 | S |
| F3 | Have each square be a polygon | Mingyang  | 02/--/23 | 02/--/23 | D |
| F4 | Have each polygon have a centroid |Mingyang | 02/--/23 | 02/--/23 | D |
| F5 | Give vertices, segments, and polygons a transparency property | Ahren | 02/06/23 | 02/19/23 | D |
| F6 | Visualize thickness property to vertices, segments, and polygons | Ahren | 02/06/23 | 02/19/23 | D |
| F7 | Visualize centroid identifier for vertices | Ahren | 02/06/23 | 02/19/23 | D |
| F8 | Implement Apache Commons CLI in visualizer to get debug option | Ahren | 02/08/23 | 02/12/23 | D |
| F9 | Implement color change in segments | Ahren | 02/08/23 | 02/19/23 | D |
| F10 | give each polygons their neighbor polygon list  | Mingyang | 02/08/23 | 02/--/23 | D |
| F11 | Implement light gray relationship line between neighbouring centroids in debug mode | Ahren | 02/14/23 | 02/--/23 | D |
| F12 | -- | -- | -- | -- | -- |
| F13 | Draw square vertices | Shike | 02/12/23 | 02/19/23 | D |
| F14 | generate segments between vertices to make squares | Shike | 02/12/23 | 02/19/23 | D |
| F15 | add color to vertices| Shike | 02/12/23 | 02/19/23 | D |
| F16 | add color to Segments | Shike | 02/12/23 | 02/19/23 | D |
| F17 | implement conversion from Vertex to Structs.Vertex | Shike | 02/20/23 | 02/22/23 | D |
| F18 | implement conversion from Segment to Structs.Segment| Shike | 02/20/23 | 02/22/23 | D |
| F19 | implement conversion from Polygon to Structs.Polygon| Shike | 02/20/23 | 02/22/23 | D |
| F20 | vertices registered in polygon for grid style mesh generation| 02/20/23 | 02/22/23 | D |
| F21 | Make a command line to let the user to choose generator random mesh or grid mesh | Mingyang Xu | 02/23/23 | 02//23 | S |
| F22 | check if segments are able make a polygon or not | Mingyang Xu | 02/11/23 | 02/19/23 | D |
| F23 | caculate the center point for an input polygon | Mingyang Xu | 02/11/23 | 02/18/23 | D |
| F24 | compare methods for Vertex, Polygon, Segment | Mingyang Xu | 02/22/23 | 02/23/23 | D |
| F25 | Implemented VoronoiDiagramBuilder to help create polygons | Ahren | 02/26/23 | 02/26/23 | D |
| F26 | Implemented Convexhull and DelaunayTriangulationBuilder to set neighbouring polygons | Ahren | 02/26/23 | 02/26/23| D |
| F27 | Created random mesh based on features 25 and 26 | Ahren | 02/26/23 | 02/26/23 | D |
| F28 | wrap all segments into proper polygon | Shike | 02/26/23 | 02/26/23 | D |
| F29 | Assign the correct ID (index in 1D IO List) in Vertex, Segment and Polygon | Shike | 02/26/23 | 02/26/23 | D |
| F30 | wrap all segments into proper polygon | Shike | 02/26/23 | 02/26/23 | D |
| F31 | seperate TetrakiSquare from gridMesh | Shike | 02/26/23 | 02/26/23 | D |
| F32 | realize 2D to 1D List<Vertex> conversion | Shike | 02/26/23 | 02/26/23 | D |
| F33 | realize 2D Array Vertex to 1D List<Vertex> conversion | Shike | 02/26/23 | 02/26/23 | D |
| F34 | Segregate all shapes into different inputs and give options for each | Ahren | 03/06/23 | 03/06/23 | D |
| F35 | Integrate the ocean tile and have every tile as an ocean tile for now | Ahren | 03/06/23 | 03/--/23 | T |
| F36 | Integrate the land tile inside a circle in the center of the mesh | Ahren | 03/15/23 | 03/--/23 | T |
| F37 | Have a beach tile for land tiles that are touching an ocean tile | Ahren | 03/18/23 | 03/--/23 | T |
| F38 | Implement a maximum number of lakes based on the input and have lakes affect the precipitation of tiles around it | Ahren | 03/20/23 | 03/--/23 | S |
| F39 | Implement Aquafiers by getting input from the Apache Commons CLI | -- | 03/--/23 | 03/--/23 | P |
| F40 | Implement Volcano elevation (a form of altitude from input) (high center) | -- | 03/--/23 | 03/--/23 | P |
| F41 | Implement Arctic elevation (a form of altitude from input) (flat land) | -- | 03/--/23 | 03/--/23 | P |
| F42 | Implement Canon elevation (a form of altitude from input) (low center, high borders) | -- | 03/--/23 | 03/--/23 | P |
| F43 | Implement Rivers without merging thickness (based on input) | -- | 03/--/23 | 03/--/23 | P |
| F44 | Implement Rivers that can merge to become thicker | -- | 03/--/23 | 03/--/23 | P |
| F45 | Implement temperature as an attribute of each tile | -- | 03/--/23 | 03/--/23 | P |
| F46 | Create a ‘slow’ soil profile and have it affect the precipitation of all tiles | -- | 03/--/23 | 03/--/23 | P |
| F47 | Create a ‘medium’ soil profile and have it affect the precipitation of all tiles | -- | 03/--/23 | 03/--/23 | P |
| F48 | Create a ‘fast’ soil profile and have it affect the precipitation of all tiles | -- | 03/--/23 | 03/--/23 | P |
| F49 | Implement the Arctic region (Whittaker diagram) and have it affect the temperature & precipitation | -- | 03/--/23 | 03/--/23 | P |
| F50 | Implement Desert region (Whittaker diagram) and have it affect the temperature & precipitation | -- | 03/--/23 | 03/--/23 | P |
| F51 | Implement Forest region (Whittaker diagram) and have it affect the temperature & precipitation | -- | 03/--/23 | 03/--/23 | P |
| F52 | Implement Grassland region (Whittaker diagram) and have it affect the temperature & precipitation | -- | 03/--/23 | 03/--/23 | P |
| F53 | Have a ‘land tile’ change into different biomes depending on the tile temperature and precipitation | Ahren | 03/20/23 | 03/--/23 | S |
| F54 | Calculate the seed of the mesh and generate any mesh using a previously given seed | Ahren | 03/20/23 | 03/--/23 | S |
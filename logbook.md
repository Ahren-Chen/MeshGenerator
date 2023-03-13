Feb 2 2023:
Ahren: 
* Started step 1, to create segments for squares on the vertices
* Finished the segment generation
* Finished average color generation
* Feature is technically done, but there is too much debt, so it needs fixing
  * Currently, all vertices are stored in a list that does not check for duplicates
  * All segments are referring to vertex indexes instead of the vertex.
  * Code is messy and hard to understand
  * Needs to be optimized, but do not know whether we can edit the vertex since the prof wants us to use the IO

Feb 6 2023:
Ahren: 
* Updated README with new features to implement this week.
* Fixed the visualizer code to look more readable, and added a couple more methods to get a thickness value from a vertex, segment, or polygon
* Gave thickness value to segments and display it based on basic stroke.

Feb 6 2023:
Shike Chen:
* updated vertices and vertex
* implement loop to assign Segments, and its property to vertices

Feb 7 2023:
Mingyang Xu:

* Finished the method to convert index form 1D to 2D and 2D to 1D.
* Make a method to check if segments can make a polygon or not.
* Finished if the segments is able to make a polygon then add all the segments' index to the polygon class.

Feb 8 2023:
Shike Chen:
* Reduce Size of Vertices
* resolve merge conflict

Feb 8 2023:
Ahren:
* Experimented with creating some tests for the visualizer class
* Implemented Apache Commons CLI to integrate user input inside the visualizer class for debug option.
  * Have not yet done anything with the debug option
* Implemented checking for centroid option during debug program
* Implemented alpha value for vertices

Feb 8 2023:
Mingyang Xu:
* Implemented adding the polygon neighbor list for each polygon method.
* Improved the checking_polygon function to less parmater.
* Implemented the method for calculating centroid.

* finished the method to convert index form 1D to 2D and 2D to 1D.
* make a method to check if segments can make a polygon or not.
* finished if the segments is able to make a polygon then add all the segments' index to the polygon class.
* still need to fix the index for neighbor polygon part.

Feb 10 2023:
Ahren:
* Fixed a bug where visuals were not displaying due to wrong alpha value
* The generator still produces an invalid input and the visualizer will not write to svg file

Feb 12 2023:
Ahren:
* Alpha value is now a part of the color that is given as part of the original RGB property instead of a new property
* Added try and catch when trying to scan for valid RGBA colors.
* Installed Loggers into the visualizer.

Feb 13 2023:
Ahren:
* Updated README by refining additional properties that each vertex or segment can have.
* Changed how I extract the color from the list of properties
  * Implemented a new PropertyExtractor class that will take all properties from the list and have them as methods
* Implemented that segments connect neighbouring centroids in debug mode based on centroid property.

Feb 14 2023:
Ahren:
* Created an abstract class for property extractor
* Preparing to implement neighbouring polygon method in graphic renderer
* Needs to test that method, but it should be complete

Feb 14 2023:
Mingyang Xu
* fix the merge conflict and improve the code a bit.

Feb 16 2023: 
Shike Chen
* reducing vertice length
* assigning properties to its proper index

Frb 17 2023:
Shike Chen
* added run.txt use chmod u+x to make it a script then run the file

Feb 18 2023: 
Mingyang Xu
* create polygon class move all the method about polygon in that class and lists all member variables.

Ahren:
* Refactored code a little bit in the visualizer and added more trace statements.
* Wrote tests for the PropertyExtractor and refactored the property extractor to account for more errors.

Feb 19:
Shike Chen
* Vertex and Segments generated properly with step 2

Feb 20:
Shike Chen
* register vertices into polygon -- failed
* modify structure, implement OOP style

Feb 21 2023:
Shike Chen:
* Created a Vertex class as part of the new Mesh ADT and created an interface for converters
* Implemented a VertexConverter class based on the converter interface where it will convert to mesh acceptable
* refractor Generator into OOP style implementation
* create Polygon, Segment basics
* create Extractor, Converter
* Implemented grid style mesh conversion in generator


Feb 21 2023: Mingyang Xu
* Modify the input of the segment generation method.
* Changed the color of segment, vertex, and polygon member variables.
* Simplify the generation method of polygon.
* Generate polygon from polygon_ The method of neighbor and center point calculation has changed from the index based on the segmentation list to the specific segment object.
* still need to make the same changes to other methods.
 
* Feb 21 2023:
  Shike Chen:
* Implemented conversion betwen Vertex to structs.vertex
* assigned proper segments to polygon
* finished conversion between polygon and Structs.polygon

* Feb 22 2023:
  Shike Chen
* polygon constructor done in minimal to support Generator
* generator functions properly without adding segments to Structs.polygon and add neighbors

* Feb 23 2023
* gridMesh Done with no bug

Ahren:
* Helped write the generate method of the Polygon class

Feb 23 2023: Mingyang Xu
* created UML diagram almost done still need to find the relationship between each class.
* finshed basic polygon class and basic segment class.
* debug the calculated_center method in polygon class.

Feb 25 2023:
Shike
* implemented randomMesh generator
* implemented comparable interfaces for Vertex, Segment and Polygon

Feb 26 2023:
Ahren:
* Figured out how the voronoi diagram builder worked
* Figured out how the DelaunayTriangulationBuilder
* Figured out how convex hull works
* Implemented voronoi diagram builder into a generate method
* Used DelaunayTriangulationBuilder to calculate the neighbours of the generated polygon
* Used convex hull to ensure the consecutive invariant holds
* Refactored how classes interact with each other and where specific methods should be
* Helped generate the random mesh
* Assisted in using Apache Commons CLI to get user input in the generator

Feb 26 2023:
Shike:
* debugged problems in gridMesh, randomMesh
* seperated TetrakisSquare from grishMesh
* resolve merge conflict
* fixed ID problem in gridMesh

March 6 2023:
Ahren:
* Set up the technical subproject for the island project
* Updated README to reflect what we have on our feature plan
* Finished 34 by creating the technical structure to begin creating the lagoon and separating it from the other possible shapes
* Also integrated the necessary inputs
* Started to work on feature 35 by integrating the ocean tile

March 13 2023:
Ahren:
* Almost finished F35 by making every tile an OceanTile and visualizing it as all blue for now. Need to write unit tests in regards to that
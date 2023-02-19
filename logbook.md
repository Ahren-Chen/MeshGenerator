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
* added run.txt use chmod u+x to make it a script then run the file

Feb 18 2023: 
Mingyang Xu
* create polygon class move all the method about polygon in that class and lists all member variables.

Ahren:
* Refactored code a little bit in the visualizer and added more trace statements.
* Wrote tests for the PropertyExtractor and refactored the property extractor to account for more errors.
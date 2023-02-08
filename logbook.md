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
* Gave thickness value to segments

Feb 7 2023:
Mingyang Xu:
* make a method to check if segments can make a polygon or not.
* finished if the segments is able to make a polygon then add all the segments' index to the polygon class.
* still need to fix the index for neighbor polygon part.

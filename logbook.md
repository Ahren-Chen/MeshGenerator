Feb 2 2023:
* Started step 1, to create segments for squares on the vertices
* Finished the segment generation - Ahren
* Finished average color generation - Ahren
* Feature is technically done, but there is too much debt, so it needs fixing
  * Currently, all vertices are stored in a list that does not check for duplicates
  * All segments are referring to vertex indexes instead of the vertex.
  * Code is messy and hard to understand
  * Needs to be optimized, but do not know whether we can edit the vertex since the prof wants us to use the IO
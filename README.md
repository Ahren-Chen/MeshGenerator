# Assignment A2: Mesh Generator

  - Author #1 [xum97@mcmaster.ca]
  - Author #2 [chens356@mcmaster.ca]
  - Author #3 [chena125@mcmaster.ca]

## How to run the product

_This section needs to be edited to reflect how the user can interact with thefeature released in your project_

### Installation instructions

This product is handled by Maven, as a multi-module project. We assume here that you have cloned the project in a directory named `A2`

To install the different tooling on your computer, simply run: (Assuming you are already in the 'A2' directory)

```
mosser@azrael mvn install
```

After installation, you'll find an application named `generator.jar` in the `generator` directory, and a file named `visualizer.jar` in the `visualizer` one. 

### Generator

To run the generator, go to the `generator` directory, and use `java -jar` to run the product. The product takes one single argument (so far), the name of the file where the generated mesh will be stored as binary.

```
mosser@azrael cd generator 
mosser@azrael java -jar generator.jar sample.mesh
mosser@azrael ls -lh sample.mesh
-rw-r--r--  1 mosser  staff    29K 29 Jan 10:52 sample.mesh
mosser@azrael cd ../
```

### Visualizer

To visualize an existing mesh, go the the `visualizer` directory, and use `java -jar` to run the product. The product take two arguments (so far): the file containing the mesh, and the name of the file to store the visualization (as an SVG image).

```
mosser@azrael cd visualizer 
mosser@azrael java -jar visualizer.jar ../generator/sample.mesh sample.svg

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

## Backlog
see logbook.md
### Definition of Done


-- Successfully completes the feature goal with no bugs. It is also covered with tests. --

### Product Backlog
* Status:
    * Pending (P), Started (S), Blocked (B), Done (D), Needs Testing (T)

| Id | Feature title | Who? | Start | End | Status |
|:--:|---------------|------|-------|-----|--------|
| F1 | Draw segments between vertices to visualize the squares | Ahren, Mike, Simon | 02/04/23 | 02/04/23 | D |
| F2 | Remove duplicate vertices and segments | Mike | 02/05/23 | 02/--/23 | S |
| F3 | Have each square be a polygon | Simon | 02/--/23 | 02/--/23 | P |
| F4 | Have each polygon have a centroid | Simon | 02/--/23 | 02/--/23 | P |
| F5 | Give vertices, segments, and polygons a transparency property | Ahren | 02/06/23 | 02/--/23 | S |
| F6 | Give thickness property to vertices, segments, and polygons | Ahren | 02/06/23 | 02/07/23 | T |
| F7 | Give centroid identifier for vertices | Ahren | 02/06/23 | 02/--/23 | S |
| F8 | Implement Apache Commons CLI in visualizer to get debug option | Ahren | 02/08/23 | 02/--/23 | S |
package island.Interfaces;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public interface ShapeGen {
    Structs.Mesh generate(Structs.Mesh mesh, double width, double height, int lakes, int seed, int aquifier);
}

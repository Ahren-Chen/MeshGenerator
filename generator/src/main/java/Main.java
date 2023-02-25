import ca.mcmaster.cas.se2aa4.a2.generator.Generator;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws IOException ,Exception {
        try {
            String mesh_name = args[0].split(",")[0];
            String mesh_type = args[0].split(",")[1];
            Generator generator = new Generator();
            Mesh myMesh = generator.generate(mesh_type);
            MeshFactory factory = new MeshFactory();
            factory.write(myMesh, mesh_name);
        }
        catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
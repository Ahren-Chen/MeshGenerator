import ca.mcmaster.cas.se2aa4.a2.generator.DotGen;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws IOException {
        try {
            DotGen generator = new DotGen();
            Mesh myMesh = generator.generate();
            MeshFactory factory = new MeshFactory();
            factory.write(myMesh, args[0]);
        }
        catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
    }

}

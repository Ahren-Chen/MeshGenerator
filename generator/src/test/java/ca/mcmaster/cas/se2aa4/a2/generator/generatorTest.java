package ca.mcmaster.cas.se2aa4.a2.generator;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;


public class generatorTest {

    @Test
    public void meshIsNotNull() throws Exception{
        try {
            Generator generator = new Generator();
            Structs.Mesh aMesh = generator.generate("gridMesh");
            assertNotNull(aMesh);
        }
        catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
    }

}

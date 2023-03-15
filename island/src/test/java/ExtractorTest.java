import Logging.ParentLogger;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import island.PropertyExtractor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExtractorTest {
    private static final ParentLogger logger = new ParentLogger();

    private static final List<Property> propertiesList = new ArrayList<>();

    private PropertyExtractor properties;

    @BeforeAll
    public static void initExtractor() {
        logger.info("\n Initializing Extractor testing \n");
    }

    @BeforeEach
    public void creatingPropertyList() {
        logger.info("Starting new PropertyExtractor test");
        propertiesList.clear();
    }

    @Test
    public void emptyProperties() {
        properties = new PropertyExtractor(propertiesList);

        assertEquals(properties.color(), Color.BLACK);
        assertFalse(properties.isCentroid());
        assertEquals(properties.thickness(), PropertyExtractor.defaultThickness);
    }

    @Test
    public void incorrectPropertyKeys() {
        Property badColor = Property.newBuilder().setKey("rgb_color").setValue("0.1,0.5,0.2,1").build();
        Property badThickness = Property.newBuilder().setKey("thicknes").setValue("4").build();
        Property badCentroid = Property.newBuilder().setKey("centroids").setKey("true").build();

        propertiesList.add(badColor);
        propertiesList.add(badThickness);
        propertiesList.add(badCentroid);

        properties = new PropertyExtractor(propertiesList);

        assertEquals(properties.color(), Color.BLACK);
        assertFalse(properties.isCentroid());
        assertEquals(properties.thickness(), PropertyExtractor.defaultThickness);
    }

    @Test
    public void validColorTest() {
        Property color = Property.newBuilder().setKey("rgba_color").setValue("0.4,0.7,0.5,1").build();
        propertiesList.add(color);

        properties = new PropertyExtractor(propertiesList);
        assertEquals(new Color(0.4f, 0.7f, 0.5f, 1f), properties.color());
    }

    @Test
    public void outOfRangeColorTest() {
        Property color = Property.newBuilder().setKey("rgba_color").setValue("1.1,-0.7, 500").build();
        propertiesList.add(color);

        properties = new PropertyExtractor(propertiesList);
        assertEquals(Color.BLACK, properties.color());
    }

    @Test
    public void invalidColorTest() {
        Property color = Property.newBuilder().setKey("rgba_color").setValue("test").build();
        propertiesList.add(color);

        properties = new PropertyExtractor(propertiesList);
        assertEquals(Color.BLACK, properties.color());
    }

    @Test
    public void centroidTestTrue() {
        Property propertyTrue = Property.newBuilder().setKey("centroid").setValue("true").build();
        propertiesList.add(propertyTrue);

        properties = new PropertyExtractor(propertiesList);
        assertTrue(properties.isCentroid());
    }

    @Test
    public void centroidTestFalse() {
        Property propertyFalse = Property.newBuilder().setKey("centroid").setValue("True").build();
        propertiesList.add(propertyFalse);

        properties = new PropertyExtractor(propertiesList);
        assertFalse(properties.isCentroid());
    }

    @Test
    public void validThicknessTest() {
        Property thickness = Property.newBuilder().setKey("thickness").setValue("4").build();
        propertiesList.add(thickness);

        properties = new PropertyExtractor(propertiesList);
        assertEquals(4, properties.thickness());
    }

    @Test
    public void negativeThicknessTest() {
        Property thickness = Property.newBuilder().setKey("thickness").setValue("-23").build();
        propertiesList.add(thickness);

        properties = new PropertyExtractor(propertiesList);
        assertEquals(PropertyExtractor.defaultThickness, properties.thickness());
    }

    @Test
    public void zeroThicknessTest() {
        Property thickness = Property.newBuilder().setKey("thickness").setValue("0").build();
        propertiesList.add(thickness);

        properties = new PropertyExtractor(propertiesList);
        assertEquals(PropertyExtractor.defaultThickness, properties.thickness());
    }

    @AfterAll
    public static void TestDoneExtractor() {logger.info("\n Finished testing Extractor \n");}
}

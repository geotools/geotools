/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.data.csv.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.geotools.data.csv.CSVFileState;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;

public class CSVSpecifiedWKTStrategyTest {

    @Test
    public void testBuildFeatureType() {
        String input = CSVTestStrategySupport.buildInputString("quux,morx\n");
        CSVFileState fileState = new CSVFileState(input, "foo");
        CSVStrategy strategy = new CSVSpecifiedWKTStrategy(fileState, "quux");
        SimpleFeatureType featureType = strategy.getFeatureType();
        assertEquals("Invalid attribute count", 2, featureType.getAttributeCount());
        GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
        assertEquals("Invalid geometry attribute name", "quux", geometryDescriptor.getLocalName());
    }

    @Test
    public void testCreateFeature() throws IOException {
        String input =
                CSVTestStrategySupport.buildInputString(
                        "fleem,zoo,morx", "foo,POINT(3.14 1.59),car");
        CSVFileState fileState = new CSVFileState(input, "bar");
        CSVStrategy strategy = new CSVSpecifiedWKTStrategy(fileState, "zoo");
        SimpleFeatureType featureType = strategy.getFeatureType();
        assertEquals("Invalid attribute count", 3, featureType.getAttributeCount());
        CSVTestStrategySupport.verifyType(featureType.getDescriptor("fleem"), String.class);
        CSVTestStrategySupport.verifyType(featureType.getDescriptor("zoo"), Geometry.class);
        CSVTestStrategySupport.verifyType(featureType.getDescriptor("morx"), String.class);
        CSVIterator iterator = strategy.iterator();
        SimpleFeature feature = iterator.next();
        assertEquals("Invalid feature property", "foo", feature.getAttribute("fleem"));
        assertEquals("Invalid feature property", "car", feature.getAttribute("morx"));
        assertNotNull("Expected geometry", feature.getDefaultGeometry());
        Point point = (Point) feature.getAttribute("zoo");
        Coordinate coordinate = point.getCoordinate();
        assertEquals("Invalid x coordinate", coordinate.x, 3.14, 0.1);
        assertEquals("Invalid y coordinate", coordinate.y, 1.59, 0.1);
    }

    public void testCreateFeatureBadGeometry() throws IOException {
        String input = CSVTestStrategySupport.buildInputString("fleem,morx", "foo,bar");
        CSVFileState fileState = new CSVFileState(input, "blub");
        CSVStrategy strategy = new CSVSpecifiedWKTStrategy(fileState, "fleem");
        SimpleFeatureType featureType = strategy.getFeatureType();
        assertEquals("Invalid attribute count", 2, featureType.getAttributeCount());
        CSVTestStrategySupport.verifyType(featureType.getDescriptor("fleem"), Geometry.class);
        CSVTestStrategySupport.verifyType(featureType.getDescriptor("morx"), String.class);
        CSVIterator iterator = strategy.iterator();
        SimpleFeature feature = iterator.next();
        assertEquals("Invalid feature property", "bar", feature.getAttribute("morx"));
        assertNull("Unexpected geometry", feature.getAttribute("fleem"));
    }

    @Test
    public void testCreateSchema() throws IOException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setCRS(DefaultGeographicCRS.WGS84);
        builder.setName("testCreateSchema");
        builder.add("the_geom", Point.class);
        builder.add("id", Integer.class);
        builder.add("int_field", Integer.class);
        builder.add("string_field", String.class);
        SimpleFeatureType featureType = builder.buildFeatureType();

        File csvFile = File.createTempFile("testCreateSchema", ".csv");
        CSVFileState csvFileState = new CSVFileState(csvFile);
        CSVStrategy strategy = new CSVSpecifiedWKTStrategy(csvFileState, "the_geom_wkt");
        strategy.createSchema(featureType);

        assertEquals(
                "Stragegy does not have provided feature type",
                featureType,
                strategy.getFeatureType());
        List<String> content = Files.readAllLines(csvFile.toPath());
        assertEquals("the_geom_wkt,id,int_field,string_field", content.get(0));
        csvFile.delete();
    }
}

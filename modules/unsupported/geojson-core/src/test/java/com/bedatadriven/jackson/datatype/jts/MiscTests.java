package com.bedatadriven.jackson.datatype.jts;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.data.geojson.GeoJSONWriter;
import org.geotools.feature.SchemaException;
import org.junit.Test;

public class MiscTests {

    @Test
    public void testGEOT7027() throws SchemaException, IOException {
        double[] coord = {1000000, 1000000};
        String featureDef = "1=POINT(" + coord[0] + " " + coord[1] + ")";
        SimpleFeatureType schema = DataUtilities.createType("test", "p:Point:srid=27700");
        SimpleFeature feature = DataUtilities.createFeature(schema, featureDef);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GeoJSONWriter writer = new GeoJSONWriter(out)) {
            // writing the feature using its current CRS
            writer.setEncodeFeatureCollectionCRS(true);
            writer.write(feature);
            String featureJson = new String(out.toByteArray(), StandardCharsets.UTF_8);
            assertTrue("Coordinates should not be formatted", featureJson.contains("1000000"));
        }
    }
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.jackson.datatype.geoparquet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.geotools.jackson.datatype.projjson.model.CoordinateReferenceSystem;
import org.geotools.jackson.datatype.projjson.model.GeographicCRS;
import org.geotools.jackson.datatype.projjson.model.Identifier;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import tools.jackson.databind.ObjectMapper;

/** Test cases for the GeoParquet Jackson module. */
public class GeoParquetModuleTest {

    @Test
    public void testDeserializeMetadata() throws IOException {
        String json = "{"
                + "\"version\": \"1.1.0\","
                + "\"primary_column\": \"geometry\","
                + "\"columns\": {"
                + "  \"geometry\": {"
                + "    \"encoding\": \"WKB\","
                + "    \"geometry_types\": [\"Point\", \"MultiPoint\"],"
                + "    \"bbox\": [1.0, 2.0, 3.0, 4.0]"
                + "  }"
                + "}"
                + "}";

        ObjectMapper mapper = GeoParquetModule.createObjectMapper();
        GeoParquetMetadata metadata = mapper.readValue(json, GeoParquetMetadata.class);

        assertNotNull(metadata);
        assertTrue(metadata instanceof GeoParquetMetadataV1_1_0);
        assertEquals("1.1.0", metadata.getVersion());
        assertEquals("geometry", metadata.getPrimaryColumn());

        Map<String, Geometry> columns = metadata.getColumns();
        assertNotNull(columns);
        assertEquals(1, columns.size());

        Geometry geom = columns.get("geometry");
        assertNotNull(geom);
        assertEquals("WKB", geom.getEncoding());

        List<String> types = geom.getGeometryTypes();
        assertNotNull(types);
        assertEquals(2, types.size());
        assertTrue(types.contains("Point"));
        assertTrue(types.contains("MultiPoint"));

        Envelope envelope = geom.bounds();
        assertNotNull(envelope);
        assertEquals(1.0, envelope.getMinX(), 0.0001);
        assertEquals(2.0, envelope.getMinY(), 0.0001);
        assertEquals(3.0, envelope.getMaxX(), 0.0001);
        assertEquals(4.0, envelope.getMaxY(), 0.0001);
    }

    @Test
    public void testDeserializeCRS() throws IOException {
        String json = "{"
                + "\"version\": \"1.1.0\","
                + "\"primary_column\": \"geometry\","
                + "\"columns\": {"
                + "  \"geometry\": {"
                + "    \"encoding\": \"WKB\","
                + "    \"geometry_types\": [\"Point\"],"
                + "    \"crs\": {"
                + "      \"type\": \"GeographicCRS\","
                + "      \"name\": \"WGS 84\","
                + "      \"datum\": {"
                + "        \"type\": \"GeodeticReferenceFrame\","
                + "        \"name\": \"World Geodetic System 1984\","
                + "        \"ellipsoid\": {"
                + "          \"name\": \"WGS 84\","
                + "          \"semi_major_axis\": 6378137,"
                + "          \"inverse_flattening\": 298.257223563"
                + "        }"
                + "      },"
                + "      \"coordinate_system\": {"
                + "        \"type\": \"ellipsoidal\","
                + "        \"axes\": ["
                + "          {"
                + "            \"name\": \"Geodetic latitude\","
                + "            \"abbreviation\": \"Lat\","
                + "            \"direction\": \"north\","
                + "            \"unit\": \"degree\""
                + "          },"
                + "          {"
                + "            \"name\": \"Geodetic longitude\","
                + "            \"abbreviation\": \"Lon\","
                + "            \"direction\": \"east\","
                + "            \"unit\": \"degree\""
                + "          }"
                + "        ]"
                + "      }"
                + "    }"
                + "  }"
                + "}"
                + "}";

        ObjectMapper mapper = GeoParquetModule.createObjectMapper();
        GeoParquetMetadata metadata = mapper.readValue(json, GeoParquetMetadata.class);

        assertNotNull(metadata);
        Geometry geom = metadata.getColumns().get("geometry");
        assertNotNull(geom);

        CoordinateReferenceSystem crs = geom.getCrs();
        assertNotNull(crs);

        // The CRS is now already a PROJJSON model object
        CoordinateReferenceSystem projCrs = crs;

        assertNotNull(projCrs);
        assertTrue(projCrs instanceof GeographicCRS);
        assertEquals("WGS 84", projCrs.getName());
    }

    @Test
    public void testParseExampleMetadata() throws IOException {
        // Read the example metadata file
        String resourcePath = "example_metadata_1.1.json";
        String json;
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            assertNotNull("Resource not found: " + resourcePath, is);
            json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }

        // Parse the outer wrapper (the "geo" key in Parquet metadata)
        ObjectMapper mapper = GeoParquetModule.createObjectMapper();
        @SuppressWarnings("unchecked")
        Map<String, Object> geoWrapper = mapper.readValue(json, Map.class);

        // Extract the GeoParquet metadata from the "geo" field
        Object geoObj = geoWrapper.get("geo");
        assertNotNull("Missing 'geo' field in example metadata", geoObj);

        // Convert the geo object to a GeoParquetMetadata instance
        String geoJson = mapper.writeValueAsString(geoObj);
        GeoParquetMetadata metadata = GeoParquetMetadata.readValue(geoJson);

        // Verify basic structure
        assertNotNull(metadata);
        assertTrue(metadata instanceof GeoParquetMetadataV1_1_0);
        assertEquals("1.1.0", metadata.getVersion());
        assertEquals("geometry", metadata.getPrimaryColumn());

        // Verify geometry column
        Map<String, Geometry> columns = metadata.getColumns();
        assertNotNull(columns);
        assertEquals(1, columns.size());

        Geometry geom = columns.get("geometry");
        assertNotNull(geom);
        assertEquals("WKB", geom.getEncoding());
        assertEquals("planar", geom.getEdges());

        List<String> types = geom.getGeometryTypes();
        assertNotNull(types);
        assertEquals(2, types.size());
        assertTrue(types.contains("Polygon"));
        assertTrue(types.contains("MultiPolygon"));

        // Verify bounding box
        List<Double> bbox = geom.getBbox();
        assertNotNull(bbox);
        assertEquals(4, bbox.size());
        assertEquals(-180.0, bbox.get(0), 0.0001);
        assertEquals(-90.0, bbox.get(1), 0.0001);
        assertEquals(180.0, bbox.get(2), 0.0001);
        assertEquals(83.6451, bbox.get(3), 0.0001);

        // Verify covering
        Covering covering = geom.getCovering();
        assertNotNull(covering);
        BboxCovering bboxCovering = covering.getBbox();
        assertNotNull(bboxCovering);

        List<String> xmin = bboxCovering.getXmin();
        assertEquals(2, xmin.size());
        assertEquals("bbox", xmin.get(0));
        assertEquals("xmin", xmin.get(1));

        // Verify CRS
        CoordinateReferenceSystem crs = geom.getCrs();

        // The CRS is now already a PROJJSON model object
        assertNotNull(crs);
        assertTrue(crs instanceof GeographicCRS);
        assertEquals("WGS 84 (CRS84)", crs.getName());

        // Get the ID information
        Identifier id = crs.getId();
        assertNotNull(id);
        assertEquals("OGC", id.getAuthority());
        assertEquals("CRS84", id.getCode());
    }

    @Test
    public void testParseExampleMetadataV120Dev() throws IOException {
        // Read the example metadata file
        String resourcePath = "example_metadata_1.2.0-dev.json";
        String json;
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            assertNotNull("Resource not found: " + resourcePath, is);
            json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }

        // Parse the outer wrapper (the "geo" key in Parquet metadata)
        ObjectMapper mapper = GeoParquetModule.createObjectMapper();
        @SuppressWarnings("unchecked")
        Map<String, Object> geoWrapper = mapper.readValue(json, Map.class);

        // Extract the GeoParquet metadata from the "geo" field
        Object geoObj = geoWrapper.get("geo");
        assertNotNull("Missing 'geo' field in example metadata", geoObj);

        // Convert the geo object to a GeoParquetMetadata instance
        String geoJson = mapper.writeValueAsString(geoObj);
        GeoParquetMetadata metadata = GeoParquetMetadata.readValue(geoJson);

        // Verify basic structure
        assertNotNull(metadata);
        assertTrue(metadata instanceof GeoParquetMetadataV1_2_0Dev);
        assertEquals("1.2.0-dev", metadata.getVersion());
        assertEquals("geometry", metadata.getPrimaryColumn());

        // Verify geometry columns
        Map<String, Geometry> columns = metadata.getColumns();
        assertNotNull(columns);
        assertEquals(2, columns.size());

        // Test main geometry column
        Geometry geom = columns.get("geometry");
        assertNotNull(geom);
        assertEquals("WKB", geom.getEncoding());
        assertEquals("spherical", geom.getEdges());
        assertEquals("counterclockwise", geom.getOrientation());
        assertEquals(2022.5, geom.getEpoch(), 0.0001);

        List<String> types = geom.getGeometryTypes();
        assertNotNull(types);
        assertEquals(3, types.size());
        assertTrue(types.contains("Polygon Z"));
        assertTrue(types.contains("MultiPolygon Z"));
        assertTrue(types.contains("GeometryCollection"));

        // Verify 3D bounding box
        List<Double> bbox = geom.getBbox();
        assertNotNull(bbox);
        assertEquals(6, bbox.size()); // 3D bbox
        assertEquals(-180.0, bbox.get(0), 0.0001); // minX
        assertEquals(-90.0, bbox.get(1), 0.0001); // minY
        assertEquals(0.0, bbox.get(2), 0.0001); // minZ
        assertEquals(180.0, bbox.get(3), 0.0001); // maxX
        assertEquals(83.6451, bbox.get(4), 0.0001); // maxY
        assertEquals(100.0, bbox.get(5), 0.0001); // maxZ

        // Verify enhanced covering with Z dimension
        Covering covering = geom.getCovering();
        assertNotNull(covering);
        BboxCovering bboxCovering = covering.getBbox();
        assertNotNull(bboxCovering);

        // Verify X, Y, and Z covering components
        List<String> xmin = bboxCovering.getXmin();
        assertEquals(2, xmin.size());
        assertEquals("bbox", xmin.get(0));
        assertEquals("xmin", xmin.get(1));

        // Check for Z dimension covering
        List<String> zmin = bboxCovering.getZmin();
        assertNotNull(zmin);
        assertEquals(2, zmin.size());
        assertEquals("bbox", zmin.get(0));
        assertEquals("zmin", zmin.get(1));

        // Verify 3D CRS
        CoordinateReferenceSystem crs = geom.getCrs();

        // The CRS is now already a PROJJSON model object
        assertNotNull(crs);
        assertTrue(crs instanceof GeographicCRS);
        assertEquals("WGS 84 height (CRS84h)", crs.getName());

        // Get the ID information
        Identifier id = crs.getId();
        assertNotNull(id);
        assertEquals("OGC", id.getAuthority());
        assertEquals("CRS84h", id.getCode());

        // Verify coordinate system has 3 axes
        assertTrue(crs instanceof GeographicCRS);
        GeographicCRS geoCrs = (GeographicCRS) crs;
        assertNotNull(geoCrs.getCoordinateSystem());
        assertNotNull(geoCrs.getCoordinateSystem().getAxes());
        assertEquals(3, geoCrs.getCoordinateSystem().getAxes().size());

        // Verify second column (points)
        Geometry points = columns.get("points");
        assertNotNull(points);
        assertEquals("point", points.getEncoding());
        assertEquals("spherical", points.getEdges());

        // Verify null CRS in points column
        assertNull(points.getCrs());

        List<String> pointTypes = points.getGeometryTypes();
        assertEquals(2, pointTypes.size());
        assertTrue(pointTypes.contains("Point"));
        assertTrue(pointTypes.contains("Point Z"));
    }
}

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
package org.geotools.jackson.datatype.projjson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.jackson.datatype.projjson.model.BoundCRS;
import org.geotools.jackson.datatype.projjson.model.CompoundCRS;
import org.geotools.jackson.datatype.projjson.model.CoordinateReferenceSystem;
import org.geotools.jackson.datatype.projjson.model.Ellipsoid;
import org.geotools.jackson.datatype.projjson.model.GeodeticReferenceFrame;
import org.geotools.jackson.datatype.projjson.model.GeographicCRS;
import org.geotools.jackson.datatype.projjson.model.ProjectedCRS;
import org.geotools.jackson.datatype.projjson.model.Transformation;
import org.junit.Before;
import org.junit.Test;

/** Test cases for the PROJJSON module. */
public class ProjJSONModuleTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new ProjJSONModule());
    }

    @Test
    public void testDeserializeWGS84() throws Exception {
        String json = "{\n"
                + "  \"$schema\": \"https://proj.org/schemas/v0.7/projjson.schema.json\",\n"
                + "  \"type\": \"GeographicCRS\",\n"
                + "  \"name\": \"WGS 84\",\n"
                + "  \"datum\": {\n"
                + "    \"type\": \"GeodeticReferenceFrame\",\n"
                + "    \"name\": \"World Geodetic System 1984\",\n"
                + "    \"ellipsoid\": {\n"
                + "      \"name\": \"WGS 84\",\n"
                + "      \"semi_major_axis\": 6378137,\n"
                + "      \"inverse_flattening\": 298.257223563\n"
                + "    }\n"
                + "  },\n"
                + "  \"coordinate_system\": {\n"
                + "    \"type\": \"ellipsoidal\",\n"
                + "    \"axes\": [\n"
                + "      {\n"
                + "        \"name\": \"Geodetic latitude\",\n"
                + "        \"abbreviation\": \"Lat\",\n"
                + "        \"direction\": \"north\",\n"
                + "        \"unit\": \"degree\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"Geodetic longitude\",\n"
                + "        \"abbreviation\": \"Lon\",\n"
                + "        \"direction\": \"east\",\n"
                + "        \"unit\": \"degree\"\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  \"id\": {\n"
                + "    \"authority\": \"EPSG\",\n"
                + "    \"code\": 4326\n"
                + "  }\n"
                + "}";

        CoordinateReferenceSystem crs = mapper.readValue(json, CoordinateReferenceSystem.class);

        // Verify the parsed object
        assertNotNull(crs);
        assertTrue(crs instanceof GeographicCRS);
        GeographicCRS geoCrs = (GeographicCRS) crs;

        assertEquals("GeographicCRS", geoCrs.getType());
        assertEquals("WGS 84", geoCrs.getName());

        // Check datum
        GeodeticReferenceFrame datum = geoCrs.getDatum();
        assertNotNull(datum);
        assertEquals("World Geodetic System 1984", datum.getName());

        // Check ellipsoid
        Ellipsoid ellipsoid = datum.getEllipsoid();
        assertNotNull(ellipsoid);
        assertEquals("WGS 84", ellipsoid.getName());
        assertEquals(6378137, ellipsoid.getSemiMajorAxis(), 0.0001);
        assertEquals(298.257223563, ellipsoid.getInverseFlattening(), 0.0000001);
    }

    @Test
    public void testHelperParseCRS() throws Exception {
        String json = "{\n"
                + "  \"$schema\": \"https://proj.org/schemas/v0.7/projjson.schema.json\",\n"
                + "  \"type\": \"GeographicCRS\",\n"
                + "  \"name\": \"WGS 84\",\n"
                + "  \"datum\": {\n"
                + "    \"type\": \"GeodeticReferenceFrame\",\n"
                + "    \"name\": \"World Geodetic System 1984\",\n"
                + "    \"ellipsoid\": {\n"
                + "      \"name\": \"WGS 84\",\n"
                + "      \"semi_major_axis\": 6378137,\n"
                + "      \"inverse_flattening\": 298.257223563\n"
                + "    }\n"
                + "  },\n"
                + "  \"coordinate_system\": {\n"
                + "    \"type\": \"ellipsoidal\",\n"
                + "    \"axes\": [\n"
                + "      {\n"
                + "        \"name\": \"Geodetic latitude\",\n"
                + "        \"abbreviation\": \"Lat\",\n"
                + "        \"direction\": \"north\",\n"
                + "        \"unit\": \"degree\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"Geodetic longitude\",\n"
                + "        \"abbreviation\": \"Lon\",\n"
                + "        \"direction\": \"east\",\n"
                + "        \"unit\": \"degree\"\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  \"id\": {\n"
                + "    \"authority\": \"EPSG\",\n"
                + "    \"code\": 4326\n"
                + "  }\n"
                + "}";

        org.geotools.api.referencing.crs.CoordinateReferenceSystem crs = ProjJSONHelper.parseCRS(json);

        assertNotNull(crs);
        ReferenceIdentifier name = crs.getName();
        assertEquals("WGS84(DD)", name.getCode());
    }

    /**
     * Helper method to read a resource file from the classpath.
     *
     * @param resourcePath Path to resource relative to classpath
     * @return The contents as a string
     * @throws IOException If the resource can't be found or read
     */
    private String readResource(String resourcePath) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Test
    public void testParseGeographicCRSFromFile() throws Exception {
        String json = readResource("geographic_crs.json");

        CoordinateReferenceSystem crs = mapper.readValue(json, CoordinateReferenceSystem.class);
        assertNotNull(crs);
        assertTrue(crs instanceof GeographicCRS);
        GeographicCRS geoCrs = (GeographicCRS) crs;

        assertEquals("GeographicCRS", geoCrs.getType());
        assertEquals("NAD83(2011)", geoCrs.getName());

        // Check the scope and area
        assertEquals("Horizontal component of 3D system.", geoCrs.getScope());
        assertTrue(geoCrs.getArea().startsWith("Puerto Rico"));

        // Check bbox format - in the file it's a map, not a list
        Object bbox = geoCrs.getBbox();
        assertNotNull(bbox);
    }

    @Test
    public void testParseBoundCRSFromFile() throws Exception {
        String json = readResource("bound_crs.json");

        CoordinateReferenceSystem crs = mapper.readValue(json, CoordinateReferenceSystem.class);
        assertNotNull(crs);
        assertTrue(crs instanceof BoundCRS);
        BoundCRS boundCrs = (BoundCRS) crs;

        assertEquals("BoundCRS", boundCrs.getType());

        // Check source CRS
        CoordinateReferenceSystem sourceCrs = boundCrs.getSourceCrs();
        assertNotNull(sourceCrs);
        assertTrue(sourceCrs instanceof GeographicCRS);
        assertEquals("ETRS89", sourceCrs.getName());

        // Check target CRS
        CoordinateReferenceSystem targetCrs = boundCrs.getTargetCrs();
        assertNotNull(targetCrs);
        assertTrue(targetCrs instanceof GeographicCRS);
        assertEquals("WGS 84", targetCrs.getName());

        // Check transformation
        Transformation transformation = boundCrs.getTransformation();
        assertNotNull(transformation);
        assertEquals("Transformation from unknown to WGS84", transformation.getName());
    }

    @Test
    public void testParseProjectedCRSFromFile() throws Exception {
        String json = readResource("projected_crs.json");

        CoordinateReferenceSystem crs = mapper.readValue(json, CoordinateReferenceSystem.class);
        assertNotNull(crs);
        assertTrue(crs instanceof ProjectedCRS);
        ProjectedCRS projCrs = (ProjectedCRS) crs;

        assertEquals("ProjectedCRS", projCrs.getType());
        assertEquals("WGS 84 / UTM zone 31N", projCrs.getName());

        // Check base CRS
        CoordinateReferenceSystem baseCrs = projCrs.getBaseCrs();
        assertNotNull(baseCrs);
        assertTrue(baseCrs instanceof GeographicCRS);
        assertEquals("WGS 84", baseCrs.getName());

        // Check conversion
        assertNotNull(projCrs.getConversion());
        assertEquals("UTM zone 31N", projCrs.getConversion().getName());

        // Check parameters
        assertNotNull(projCrs.getConversion().getParameters());
        assertTrue(projCrs.getConversion().getParameters().size() > 0);
    }

    @Test
    public void testParseCompoundCRSFromFile() throws Exception {
        String json = readResource("compound_crs.json");

        CoordinateReferenceSystem crs = mapper.readValue(json, CoordinateReferenceSystem.class);
        assertNotNull(crs);
        assertTrue(crs instanceof CompoundCRS);
        CompoundCRS compoundCrs = (CompoundCRS) crs;

        assertEquals("CompoundCRS", compoundCrs.getType());
        assertEquals("WGS 84 + EGM2008 height", compoundCrs.getName());

        // Check components
        List<CoordinateReferenceSystem> components = compoundCrs.getComponents();
        assertNotNull(components);
        assertEquals(2, components.size());

        // First component should be a geographic CRS
        assertTrue(components.get(0) instanceof GeographicCRS);
        assertEquals("WGS 84", components.get(0).getName());
    }

    @Test
    public void testParseTransformationFromFile() throws Exception {
        String json = readResource("transformation.json");

        CoordinateReferenceSystem crs = mapper.readValue(json, CoordinateReferenceSystem.class);
        assertNotNull(crs);
        assertTrue(crs instanceof Transformation);
        Transformation transform = (Transformation) crs;

        assertEquals("Transformation", transform.getType());
        assertEquals("NAD27 to NAD83 (8)", transform.getName());

        // Check source and target CRS
        assertNotNull(transform.getSourceCrs());
        assertNotNull(transform.getTargetCrs());
        assertEquals("NAD27", transform.getSourceCrs().getName());
        assertEquals("NAD83", transform.getTargetCrs().getName());

        // Check parameters
        assertNotNull(transform.getParameters());
        assertTrue(transform.getParameters().size() > 0);
        assertEquals("0.5", transform.getAccuracy());
    }
}

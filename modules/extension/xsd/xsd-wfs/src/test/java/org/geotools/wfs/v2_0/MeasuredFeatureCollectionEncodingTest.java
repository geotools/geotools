/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v2_0;

import static org.junit.Assert.assertEquals;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.WfsFactory;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.xsd.Encoder;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Encodes a whole feature collection, the way a WFS response is built, and checks that the declared srsDimension
 * matches the ordinates written for measured geometries. Both encoder paths are exercised, since they answer that
 * question from different call sites.
 */
public class MeasuredFeatureCollectionEncodingTest {

    private static final GeometryFactory GF = new GeometryFactory();

    private MemoryDataStore store;

    @Before
    public void setUp() throws Exception {
        store = new MemoryDataStore();
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("lines");
        tb.setNamespaceURI("http://geotools.org");
        tb.add("geometry", LineString.class, 4269);
        store.createSchema(tb.buildFeatureType());
    }

    /** XYM, two coordinates, measures -1.5 and -2.5. */
    private static LineString lineXYM() {
        return GF.createLineString(new LiteCoordinateSequence(new double[] {0, 1, -1.5, 3, 4, -2.5}, 3, 1));
    }

    /** XYZM, two coordinates, heights 10 and 15, measures -1.5 and -2.5. */
    private static LineString lineXYZM() {
        return GF.createLineString(new LiteCoordinateSequence(new double[] {0, 1, 10, -1.5, 3, 4, 15, -2.5}, 4, 1));
    }

    @Test
    public void testFastPathXYMMeasuresOff() throws Exception {
        assertCollection(true, false, lineXYM(), 2, "0 1 3 4");
    }

    @Test
    public void testFastPathXYMMeasuresOn() throws Exception {
        assertCollection(true, true, lineXYM(), 3, "0 1 -1.5 3 4 -2.5");
    }

    @Test
    public void testFastPathXYZMMeasuresOff() throws Exception {
        assertCollection(true, false, lineXYZM(), 3, "0 1 10 3 4 15");
    }

    @Test
    public void testFastPathXYZMMeasuresOn() throws Exception {
        assertCollection(true, true, lineXYZM(), 4, "0 1 10 -1.5 3 4 15 -2.5");
    }

    @Test
    public void testFullEncoderXYMMeasuresOff() throws Exception {
        assertCollection(false, false, lineXYM(), 2, "0 1 3 4");
    }

    @Test
    public void testFullEncoderXYMMeasuresOn() throws Exception {
        assertCollection(false, true, lineXYM(), 3, "0 1 -1.5 3 4 -2.5");
    }

    @Test
    public void testFullEncoderXYZMMeasuresOff() throws Exception {
        assertCollection(false, false, lineXYZM(), 3, "0 1 10 3 4 15");
    }

    @Test
    public void testFullEncoderXYZMMeasuresOn() throws Exception {
        assertCollection(false, true, lineXYZM(), 4, "0 1 10 -1.5 3 4 15 -2.5");
    }

    /**
     * Encodes the geometry as a one feature collection and asserts the declaration and the ordinates together: the two
     * are computed at different call sites, and the bug is that they disagree.
     */
    @SuppressWarnings("unchecked")
    private void assertCollection(
            boolean optimized, boolean encodeMeasures, LineString line, int expectedDimension, String expectedPosList)
            throws Exception {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(store.getSchema("lines"));
        b.add(line);
        store.addFeature(b.buildFeature("line.1"));

        FeatureCollectionType fc = WfsFactory.eINSTANCE.createFeatureCollectionType();
        FeatureCollection features = store.getFeatureSource("lines").getFeatures();
        fc.getFeature().add(features);

        WFSConfiguration configuration = new WFSConfiguration();
        configuration
                .getDependency(org.geotools.gml3.v3_2.GMLConfiguration.class)
                .setEncodeMeasures(encodeMeasures);
        if (optimized) {
            configuration.getProperties().add(GMLConfiguration.OPTIMIZED_ENCODING);
        }
        Encoder encoder = new Encoder(configuration);
        encoder.getNamespaces().declarePrefix("geotools", "http://geotools.org");

        Document dom = encoder.encodeAsDOM(fc, WFS.FeatureCollection);
        Element lineString =
                (Element) dom.getElementsByTagName("gml:LineString").item(0);
        String posList =
                dom.getElementsByTagName("gml:posList").item(0).getTextContent().trim();

        assertEquals(expectedPosList, posList);
        assertEquals(String.valueOf(expectedDimension), lineString.getAttribute("srsDimension"));
        assertEquals(expectedDimension * line.getNumPoints(), posList.split("\\s+").length);
    }
}

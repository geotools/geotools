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
package org.geotools.gml3.simple;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;

import javax.xml.namespace.QName;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.gml3.GML;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.gml3.bindings.GML3MockData;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.impl.PackedCoordinateSequence;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Checks that the declared srsDimension and the ordinates actually written agree, on the full encoder and on the fast
 * path, for GML 3.1 and GML 3.2, with and without measure encoding.
 */
public final class MeasuredGeometryDimensionTest extends GeometryEncoderTestSupport {

    private static final GeometryFactory GF = new GeometryFactory();

    /** XYM, two coordinates, measures -1.5 and -2.5. */
    private static LineString lineXYM() {
        LiteCoordinateSequence cs = new LiteCoordinateSequence(new double[] {0, 1, -1.5, 3, 4, -2.5}, 3, 1);
        LineString line = GF.createLineString(cs);
        line.setUserData(GML3MockData.crs());
        return line;
    }

    /** XYZM, two coordinates, heights 10 and 15, measures -1.5 and -2.5. */
    private static LineString lineXYZM() {
        LiteCoordinateSequence cs = new LiteCoordinateSequence(new double[] {0, 1, 10, -1.5, 3, 4, 15, -2.5}, 4, 1);
        LineString line = GF.createLineString(cs);
        line.setUserData(GML3MockData.crs());
        return line;
    }

    /** Same values as {@link #lineXYZM()} in a sequence implementation the dimension heuristic cannot inspect. */
    private static LineString linePackedXYZM() {
        CoordinateSequence cs =
                new PackedCoordinateSequence.Double(new double[] {0, 1, 10, -1.5, 3, 4, 15, -2.5}, 4, 1);
        LineString line = GF.createLineString(cs);
        line.setUserData(GML3MockData.crs());
        return line;
    }

    @Test
    public void testFullEncoderXYMMeasuresOff() throws Exception {
        assertFullEncoder(new GMLConfiguration(), GML.LineString, lineXYM(), false, "0 1 3 4");
    }

    @Test
    public void testFullEncoderXYMMeasuresOn() throws Exception {
        assertFullEncoder(new GMLConfiguration(), GML.LineString, lineXYM(), true, "0 1 -1.5 3 4 -2.5");
    }

    @Test
    public void testFullEncoderXYZMMeasuresOff() throws Exception {
        assertFullEncoder(new GMLConfiguration(), GML.LineString, lineXYZM(), false, "0 1 10 3 4 15");
    }

    @Test
    public void testFullEncoderXYZMMeasuresOn() throws Exception {
        assertFullEncoder(new GMLConfiguration(), GML.LineString, lineXYZM(), true, "0 1 10 -1.5 3 4 15 -2.5");
    }

    @Test
    public void testFullEncoder32XYMMeasuresOff() throws Exception {
        assertFullEncoder(
                new org.geotools.gml3.v3_2.GMLConfiguration(),
                org.geotools.gml3.v3_2.GML.LineString,
                lineXYM(),
                false,
                "0 1 3 4");
    }

    @Test
    public void testFullEncoder32XYZMMeasuresOn() throws Exception {
        assertFullEncoder(
                new org.geotools.gml3.v3_2.GMLConfiguration(),
                org.geotools.gml3.v3_2.GML.LineString,
                lineXYZM(),
                true,
                "0 1 10 -1.5 3 4 15 -2.5");
    }

    /** The measure has to survive a sequence the dimension heuristic cannot look inside, declaration included. */
    @Test
    public void testFullEncoderPackedXYZMMeasuresOn() throws Exception {
        assertFullEncoder(new GMLConfiguration(), GML.LineString, linePackedXYZM(), true, "0 1 10 -1.5 3 4 15 -2.5");
    }

    @Test
    public void testFullEncoderPackedXYZMMeasuresOff() throws Exception {
        assertFullEncoder(new GMLConfiguration(), GML.LineString, linePackedXYZM(), false, "0 1 10 3 4 15");
    }

    @Test
    public void testFastPathPackedXYZMMeasuresOn() throws Exception {
        assertFastPath(linePackedXYZM(), true, "0 1 10 -1.5 3 4 15 -2.5");
    }

    @Test
    public void testFastPathXYMMeasuresOff() throws Exception {
        assertFastPath(lineXYM(), false, "0 1 3 4");
    }

    @Test
    public void testFastPath32XYMMeasuresOff() throws Exception {
        assertFastPath32(lineXYM(), false, "0 1 3 4");
    }

    @Test
    public void testFastPath32XYZMMeasuresOn() throws Exception {
        assertFastPath32(lineXYZM(), true, "0 1 10 -1.5 3 4 15 -2.5");
    }

    @Test
    public void testFastPathXYMMeasuresOn() throws Exception {
        assertFastPath(lineXYM(), true, "0 1 -1.5 3 4 -2.5");
    }

    @Test
    public void testFastPathXYZMMeasuresOff() throws Exception {
        assertFastPath(lineXYZM(), false, "0 1 10 3 4 15");
    }

    @Test
    public void testFastPathXYZMMeasuresOn() throws Exception {
        assertFastPath(lineXYZM(), true, "0 1 10 -1.5 3 4 15 -2.5");
    }

    /** Encodes through the binding based encoder and checks declaration and posList against each other. */
    private void assertFullEncoder(
            Configuration configuration, QName element, LineString line, boolean encodeMeasures, String expectedPosList)
            throws Exception {
        setEncodeMeasures(configuration, encodeMeasures);
        Document dom = new Encoder(configuration).encodeAsDOM(line, element);
        Element root = dom.getDocumentElement();
        String posList = root.getElementsByTagNameNS(element.getNamespaceURI(), "posList")
                .item(0)
                .getTextContent()
                .trim();
        assertEquals(expectedPosList, posList);
        assertDeclarationMatches(root.getAttribute("srsDimension"), posList, line);
    }

    /**
     * Encodes through the fast path and checks the ordinates written against the dimension the same configuration
     * declares, since the fast path writes the declaration from a separate call site.
     */
    private void assertFastPath(LineString line, boolean encodeMeasures, String expectedPosList) throws Exception {
        // the encoder's own configuration, the one the declaration is computed from
        assertFastPath(gtEncoder.getConfiguration(), GML.NAMESPACE, line, encodeMeasures, expectedPosList);
    }

    /** Same fast path, GML 3.2 namespace and configuration: the 3.2 setter mirrors the flag on its own. */
    private void assertFastPath32(LineString line, boolean encodeMeasures, String expectedPosList) throws Exception {
        Configuration configuration = new org.geotools.gml3.v3_2.GMLConfiguration();
        gtEncoder = new Encoder(configuration);
        // an Encoder declares its prefixes only while encoding, and GMLWriter names posList from that mapping,
        // so without this the fast path would write the GML 3.1 namespace it falls back to
        gtEncoder.getNamespaces().declarePrefix("gml", org.geotools.gml3.v3_2.GML.NAMESPACE);
        assertFastPath(configuration, org.geotools.gml3.v3_2.GML.NAMESPACE, line, encodeMeasures, expectedPosList);
    }

    private void assertFastPath(
            Configuration configuration,
            String namespace,
            LineString line,
            boolean encodeMeasures,
            String expectedPosList)
            throws Exception {
        setEncodeMeasures(configuration, encodeMeasures);
        LineStringEncoder encoder = new LineStringEncoder(gtEncoder, "gml", namespace);
        AttributesImpl atts = srsAttributes(line, namespace);
        Document dom = encode(encoder, line, encodeMeasures, "line", 6, false, false, namespace, atts);
        Element root = dom.getDocumentElement();
        String posList = root.getElementsByTagNameNS(namespace, "posList")
                .item(0)
                .getTextContent()
                .trim();
        assertEquals(expectedPosList, posList);
        assertDeclarationMatches(root.getAttribute("srsDimension"), posList, line);
    }

    /**
     * The srsDimension attribute the feature collection delegate would attach to this geometry, built by the delegate
     * itself so the fast path is checked on emitted GML rather than on a utility call.
     */
    private AttributesImpl srsAttributes(LineString line, String namespace) {
        // the delegate reads the gml prefix off the encoder, which declares it only while encoding
        gtEncoder.getNamespaces().declarePrefix("gml", namespace);
        AttributesImpl atts = new AttributesImpl();
        Integer dimension = GML2EncodingUtils.getGeometryDimension(line, gtEncoder.getConfiguration());
        new GML3FeatureCollectionEncoderDelegate.GML3Delegate(gtEncoder).setGeometryDimensionAttribute(atts, dimension);
        return atts;
    }

    private void setEncodeMeasures(Configuration configuration, boolean encodeMeasures) {
        org.geotools.gml2.GMLConfiguration.setEncodeMeasures(configuration, encodeMeasures);
    }

    /**
     * The bug being guarded: the declared dimension has to be the ordinates written per coordinate. The coordinate
     * count is pinned against the source geometry as well, so fewer coordinates carrying more ordinates each cannot
     * balance out into a passing assertion.
     */
    private void assertDeclarationMatches(String srsDimension, String posList, LineString line) {
        int written = posList.split("\\s+").length;
        int declared = Integer.parseInt(srsDimension);
        assertThat("no ordinates declared", declared, greaterThan(0));
        assertEquals("ordinates written are not a whole number of coordinates", 0, written % declared);
        assertEquals("coordinate count differs from the source geometry", line.getNumPoints(), written / declared);
    }
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v1_1;

import org.w3c.dom.Document;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.geotools.gml3.GML;


public class BinarySpatialOpTypeBindingTest extends FilterTestSupport {
    public void testDistanceBufferType() {
        assertEquals(DistanceBufferOperator.class, binding(OGC.DistanceBufferType).getType());
    }

    public void testBeyondType() {
        assertEquals(Beyond.class, binding(OGC.Beyond).getType());
    }

    public void testBeyondParse() throws Exception {
        FilterMockData.beyond(document, document);

        Beyond beyond = (Beyond) parse();

        assertNotNull(beyond.getExpression1());
        assertNotNull(beyond.getExpression2());
        assertEquals(1.0, beyond.getDistance(), 0.1);
        assertEquals("m",beyond.getDistanceUnits());
    }

    public void testBeyondEncode() throws Exception {
        Document dom = encode(FilterMockData.beyond(), OGC.Beyond);

        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart()).getLength());
        assertEquals(1, dom.getElementsByTagNameNS(OGC.NAMESPACE, "Distance").getLength());
        assertEquals("1.0",
            dom.getElementsByTagNameNS(OGC.NAMESPACE, "Distance").item(0).getFirstChild()
               .getNodeValue());
    }

    public void testDWithinType() {
        assertEquals(DWithin.class, binding(OGC.DWithin).getType());
    }

    public void testDWithinParse() throws Exception {
        FilterMockData.dwithin(document, document);

        DWithin dwithin = (DWithin) parse();

        assertNotNull(dwithin.getExpression1());
        assertNotNull(dwithin.getExpression2());
        assertEquals(1.0, dwithin.getDistance(), 0.1);
        assertEquals("m", dwithin.getDistanceUnits());
    }

    public void testDWithinEncode() throws Exception {
        Document dom = encode(FilterMockData.beyond(), OGC.DWithin);

        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart()).getLength());
        assertEquals(1, dom.getElementsByTagNameNS(OGC.NAMESPACE, "Distance").getLength());
        assertEquals("1.0",
            dom.getElementsByTagNameNS(OGC.NAMESPACE, "Distance").item(0).getFirstChild()
               .getNodeValue());
    }

    public void testBinarySpatialOpType() {
        assertEquals(BinarySpatialOperator.class, binding(OGC.BinarySpatialOpType).getType());
    }

    public void testContainsType() {
        assertEquals(Contains.class, binding(OGC.Contains).getType());
    }

    public void testContainsParse() throws Exception {
        FilterMockData.contains(document, document);

        Contains contains = (Contains) parse();

        assertNotNull(contains.getExpression1());
        assertNotNull(contains.getExpression2());
    }

    public void testContainsEncode() throws Exception {
        Document dom = encode(FilterMockData.contains(), OGC.Contains);

        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart()).getLength());
    }

    public void testCrossesType() {
        assertEquals(Crosses.class, binding(OGC.Crosses).getType());
    }

    public void testCrossesParse() throws Exception {
        FilterMockData.crosses(document, document);

        Crosses crosses = (Crosses) parse();

        assertNotNull(crosses.getExpression1());
        assertNotNull(crosses.getExpression2());
    }

    public void testCrossesEncode() throws Exception {
        Document dom = encode(FilterMockData.crosses(), OGC.Crosses);

        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart()).getLength());
    }

    public void testDisjointType() {
        assertEquals(Disjoint.class, binding(OGC.Disjoint).getType());
    }

    public void testDisjointParse() throws Exception {
        FilterMockData.disjoint(document, document);

        Disjoint disjoint = (Disjoint) parse();

        assertNotNull(disjoint.getExpression1());
        assertNotNull(disjoint.getExpression2());
    }

    public void testDisjointEncode() throws Exception {
        Document dom = encode(FilterMockData.disjoint(), OGC.Disjoint);

        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart()).getLength());
    }

    public void testEqualsType() {
        assertEquals(Equals.class, binding(OGC.Equals).getType());
    }

    public void testEqualsParse() throws Exception {
        FilterMockData.equals(document, document);

        Equals equals = (Equals) parse();

        assertNotNull(equals.getExpression1());
        assertNotNull(equals.getExpression2());
    }

    public void testEqualsEncode() throws Exception {
        Document dom = encode(FilterMockData.equals(), OGC.Equals);

        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart()).getLength());
    }

    public void testIntersectsType() {
        assertEquals(Intersects.class, binding(OGC.Intersects).getType());
    }

    public void testIntersectsParse() throws Exception {
        FilterMockData.intersects(document, document);

        Intersects intersects = (Intersects) parse();

        assertNotNull(intersects.getExpression1());
        assertNotNull(intersects.getExpression2());
    }

    public void testIntersectsEncode() throws Exception {
        Document dom = encode(FilterMockData.intersects(), OGC.Intersects);

        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart()).getLength());
    }

    public void testOverlapsType() {
        assertEquals(Overlaps.class, binding(OGC.Overlaps).getType());
    }

    public void testOverlapsParse() throws Exception {
        FilterMockData.overlaps(document, document);

        Overlaps overlaps = (Overlaps) parse();

        assertNotNull(overlaps.getExpression1());
        assertNotNull(overlaps.getExpression2());
    }

    public void testOverlapsEncode() throws Exception {
        Document dom = encode(FilterMockData.overlaps(), OGC.Overlaps);

        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart()).getLength());
    }

    public void testTouchesType() {
        assertEquals(Touches.class, binding(OGC.Touches).getType());
    }

    public void testTouchesParse() throws Exception {
        FilterMockData.touches(document, document);

        Touches touches = (Touches) parse();

        assertNotNull(touches.getExpression1());
        assertNotNull(touches.getExpression2());
    }

    public void testTouchesEncode() throws Exception {
        Document dom = encode(FilterMockData.touches(), OGC.Touches);

        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart()).getLength());
    }

    public void testWithinType() {
        assertEquals(Within.class, binding(OGC.Within).getType());
    }

    public void testWithinParse() throws Exception {
        FilterMockData.within(document, document);

        Within within = (Within) parse();

        assertNotNull(within.getExpression1());
        assertNotNull(within.getExpression2());
    }

    public void testWithinEncode() throws Exception {
        Document dom = encode(FilterMockData.within(), OGC.Within);

        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart()).getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart()).getLength());
    }
}

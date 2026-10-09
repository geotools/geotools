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
package org.geotools.gml3.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.api.geometry.Position;
import org.geotools.geometry.Position1D;
import org.geotools.geometry.Position2D;
import org.geotools.gml3.GML;
import org.geotools.gml3.GML3TestSupport;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xsd.Encoder;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateXYM;
import org.locationtech.jts.geom.CoordinateXYZM;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.w3c.dom.Document;

public class DirectPositionTypeBindingTest extends GML3TestSupport {
    @Test
    public void test1D() throws Exception {
        GML3MockData.element(GML.pos, document, document);
        document.getDocumentElement().appendChild(document.createTextNode("1.0"));

        Position pos = (Position) parse();

        assertNotNull(pos);
        assertTrue(pos instanceof Position1D);

        assertEquals(pos.getOrdinate(0), 1.0, 0);
    }

    @Test
    public void test2D() throws Exception {
        GML3MockData.element(GML.pos, document, document);
        document.getDocumentElement().appendChild(document.createTextNode("1.0 2.0"));

        Position pos = (Position) parse();

        assertNotNull(pos);
        assertTrue(pos instanceof Position2D);

        assertEquals(pos.getOrdinate(0), 1.0, 0);
        assertEquals(pos.getOrdinate(1), 2.0, 0);
    }

    @Test
    public void testEncode2D() throws Exception {
        Point point = GML3MockData.pointLite2D();
        CoordinateSequence seq = point.getCoordinateSequence();
        Document doc = encode(seq, GML.pos);
        checkPosOrdinates(doc, 2);
    }

    @Test
    public void testEncode3D() throws Exception {
        Point point = GML3MockData.pointLite3D();
        CoordinateSequence seq = point.getCoordinateSequence();
        Document doc = encode(seq, GML.pos);
        checkPosOrdinates(doc, 3);
    }

    /** XYM, measures off: the M value is not a height, so the position is a plain 2D one. */
    @Test
    public void testEncodeXYM() throws Exception {
        checkPosOrdinates(encodeMeasured(xym(), false), 2);
    }

    /** XYM, measures on: the measure takes the third slot, the same one posList writes it in. */
    @Test
    public void testEncodeXYMMeasuresEncoded() throws Exception {
        checkPosOrdinates(encodeMeasured(xym(), true), 3);
    }

    /** XYZM, measures off: the height stays, the measure goes. */
    @Test
    public void testEncodeZM() throws Exception {
        checkPosOrdinates(encodeMeasured(xyzm(), false), 3);
    }

    /** XYZM, measures on: four ordinates, measure last. */
    @Test
    public void testEncodeZMMeasuresEncoded() throws Exception {
        checkPosOrdinates(encodeMeasured(xyzm(), true), 4);
    }

    private Document encodeMeasured(CoordinateSequence seq, boolean encodeMeasures) throws Exception {
        GMLConfiguration configuration = new GMLConfiguration();
        configuration.setEncodeMeasures(encodeMeasures);
        return new Encoder(configuration).encodeAsDOM(seq, GML.pos);
    }

    private static CoordinateSequence xym() {
        return new GeometryFactory().createPoint(new CoordinateXYM(1, 1, 4)).getCoordinateSequence();
    }

    private static CoordinateSequence xyzm() {
        return new GeometryFactory().createPoint(new CoordinateXYZM(1, 1, 2, 4)).getCoordinateSequence();
    }
}

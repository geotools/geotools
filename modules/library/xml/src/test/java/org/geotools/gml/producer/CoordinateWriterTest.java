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
package org.geotools.gml.producer;

import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * We need to ensure that the CoordinateWriter can output Z ordinates (if they are actually around).
 *
 * <p>This test case added as part of fixing
 *
 * @author Jody
 */
public class CoordinateWriterTest {

    /** Test normal 2D output */
    @Test
    public void test2DCoordSeq() throws Exception {
        CoordinateSequence coords = new CoordinateArraySequence(coords2D(new int[] {1, 1, 4, 4, 0, 4, 1, 1}));

        CoordinateWriter writer = new CoordinateWriter(4);
        CoordinateHandler output = new CoordinateHandler();

        output.startDocument();
        writer.writeCoordinates(coords, output);
        output.endDocument();

        Assert.assertEquals("<coordinates>1,1 4,4 0,4 1,1</coordinates>", output.received);
    }

    /** Test normal 2D output */
    @Test
    public void test2D() throws Exception {
        Coordinate[] coords = coords2D(new int[] {1, 1, 4, 4, 0, 4, 1, 1});
        Assert.assertNotNull(coords);
        Assert.assertEquals(4, coords.length);

        CoordinateWriter writer = new CoordinateWriter(4);
        CoordinateHandler output = new CoordinateHandler();

        output.startDocument();
        writer.writeCoordinates(new CoordinateArraySequence(coords), output);
        output.endDocument();

        Assert.assertEquals("<coordinates>1,1 4,4 0,4 1,1</coordinates>", output.received);
        // System.out.println(output.received);
    }

    @Test
    public void test2DWithDummyZ() throws Exception {
        Coordinate[] coords = coords2D(new int[] {1, 1, 4, 4, 0, 4, 1, 1});
        Assert.assertNotNull(coords);
        Assert.assertEquals(4, coords.length);

        final boolean useDummyZ = true;
        final double zValue = 0.0;
        CoordinateWriter writer = new CoordinateWriter(4, false, true, " ", ",", useDummyZ, zValue);
        CoordinateHandler output = new CoordinateHandler();

        output.startDocument();
        writer.writeCoordinates(new CoordinateArraySequence(coords), output);
        output.endDocument();

        // System.out.println(output.received);
        Assert.assertEquals("<coordinates>1,1,0 4,4,0 0,4,0 1,1,0</coordinates>", output.received);
    }

    @Test
    public void test2DWithDummyZCoordSeq() throws Exception {
        CoordinateSequence coords = new CoordinateArraySequence(coords2D(new int[] {1, 1, 4, 4, 0, 4, 1, 1}));

        final boolean useDummyZ = true;
        final double zValue = 0.0;
        CoordinateWriter writer = new CoordinateWriter(4, false, true, " ", ",", useDummyZ, zValue);
        CoordinateHandler output = new CoordinateHandler();

        output.startDocument();
        writer.writeCoordinates(coords, output);
        output.endDocument();

        Assert.assertEquals("<coordinates>1,1,0 4,4,0 0,4,0 1,1,0</coordinates>", output.received);
    }

    @Test
    public void test3D() throws Exception {
        Coordinate[] coords = coords3D(new int[] {1, 1, 3, 4, 4, 2, 0, 4, 2, 1, 1, 3});
        Assert.assertNotNull(coords);
        Assert.assertEquals(4, coords.length);

        CoordinateWriter writer = new CoordinateWriter(4, false, true, " ", ",", true, 0.0, 3);
        CoordinateHandler output = new CoordinateHandler();

        output.startDocument();
        writer.writeCoordinates(new CoordinateArraySequence(coords), output);
        output.endDocument();

        Assert.assertEquals("<coordinates>1,1,3 4,4,2 0,4,2 1,1,3</coordinates>", output.received);
        // System.out.println(output.received);
    }

    @Test
    public void test3DCoordSeq() throws Exception {
        CoordinateSequence coords =
                new CoordinateArraySequence(coords3D(new int[] {1, 1, 3, 4, 4, 2, 0, 4, 2, 1, 1, 3}));

        CoordinateWriter writer = new CoordinateWriter(4, false, true, " ", ",", true, 0.0, 3);
        CoordinateHandler output = new CoordinateHandler();

        output.startDocument();
        writer.writeCoordinates(coords, output);
        output.endDocument();

        // System.out.println(output.received);
        Assert.assertEquals("<coordinates>1,1,3 4,4,2 0,4,2 1,1,3</coordinates>", output.received);
    }

    @Test
    public void test3DCoordSeqEvenWithWrongDimension() throws Exception {
        CoordinateSequence coords =
                new CoordinateArraySequence(coords3D(new int[] {1, 1, 3, 4, 4, 2, 0, 4, 2, 1, 1, 3}));

        CoordinateWriter writer = new CoordinateWriter(4, false, true, " ", ",", true, 0.0, 2);
        CoordinateHandler output = new CoordinateHandler();

        output.startDocument();
        writer.writeCoordinates(coords, output);
        output.endDocument();

        Assert.assertEquals("<coordinates>1,1,3 4,4,2 0,4,2 1,1,3</coordinates>", output.received);
    }

    @Test
    public void testFormatting() throws Exception {
        CoordinateSequence coords =
                new CoordinateArraySequence(coords3DDouble(new double[] {1, 1, 3, 4.5, 4, 2, 0, 4, 2, 1, 1, 3.582}));

        CoordinateWriter writer = new CoordinateWriter(2, true, true, " ", ",", true, 0.0, 2);
        CoordinateHandler output = new CoordinateHandler();

        output.startDocument();
        writer.writeCoordinates(coords, output);
        output.endDocument();

        Assert.assertEquals(
                "<coordinates>1.00,1.00,3.00 4.50,4.00,2.00 0.00,4.00,2.00 1.00,1.00,3" + ".58</coordinates>",
                output.received);
    }

    static class CoordinateHandler implements ContentHandler {
        StringBuffer buffer;
        String received;

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            buffer.append(new String(ch, start, length));
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            buffer.append("</");
            buffer.append(localName);
            buffer.append(">");
        }

        @Override
        public void endDocument() throws SAXException {
            received = buffer.toString();
        }

        @Override
        public void endPrefixMapping(String prefix) throws SAXException {}

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}

        @Override
        public void processingInstruction(String target, String data) throws SAXException {}

        @Override
        public void setDocumentLocator(Locator locator) {}

        @Override
        public void skippedEntity(String name) throws SAXException {}

        @Override
        public void startDocument() throws SAXException {
            buffer = new StringBuffer();
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
            buffer.append("<");
            buffer.append(localName);
            buffer.append(">");
        }

        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {}
    }

    Coordinate[] coords2D(int[] array) {
        Coordinate[] coords = new Coordinate[array.length / 2];
        for (int i = 0; i < coords.length; i++) {
            int offset = i * 2;
            coords[i] = new Coordinate(array[offset + 0], array[offset + 1]);
        }
        return coords;
    }

    Coordinate[] coords3D(int[] array) {
        Coordinate[] coords = new Coordinate[array.length / 3];
        for (int i = 0; i < coords.length; i++) {
            int offset = i * 3;
            coords[i] = new Coordinate(array[offset + 0], array[offset + 1], array[offset + 2]);
        }
        return coords;
    }

    Coordinate[] coords3DDouble(double[] array) {
        Coordinate[] coords = new Coordinate[array.length / 3];
        for (int i = 0; i < coords.length; i++) {
            int offset = i * 3;
            coords[i] = new Coordinate(array[offset + 0], array[offset + 1], array[offset + 2]);
        }
        return coords;
    }
}

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

import junit.framework.TestCase;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

/**
 * We need to ensure that the CoordianteWriter can output Z ordinates
 * (if they are actually around).
 * <p>
 * This test case added as part of fixing 
 * 
 * @author Jody
 *
 *
 * @source $URL$
 */
public class CoordinateWriterTest extends TestCase {
    
    /**
     * Test normal 2D output 
     * @throws Exception
     */
    public void test2DCoordSeq() throws Exception {
        CoordinateSequence coords = new CoordinateArraySequence(coords2D( new int[]{1,1, 4,4, 0,4, 1,1 }));

        CoordinateWriter writer = new CoordinateWriter(4);
        CoordinateHandler output = new CoordinateHandler();

        output.startDocument();
        writer.writeCoordinates( coords, output);
        output.endDocument();

        assertEquals("<coordinates>1,1 4,4 0,4 1,1</coordinates>", output.received );
    }
    
    /**
	 * Test normal 2D output 
	 * @throws Exception
	 */
	public void test2D() throws Exception {
		Coordinate[] coords = coords2D( new int[]{1,1, 4,4, 0,4, 1,1 });
		assertNotNull( coords );
		assertEquals( 4, coords.length );

		CoordinateWriter writer = new CoordinateWriter(4);
		CoordinateHandler output = new CoordinateHandler();

		output.startDocument();
		writer.writeCoordinates( coords, output);
		output.endDocument();

		assertEquals("<coordinates>1,1 4,4 0,4 1,1</coordinates>", output.received );
		System.out.println( output.received );
	}

	public void test2DWithDummyZ() throws Exception {
		Coordinate[] coords = coords2D( new int[]{1,1, 4,4, 0,4, 1,1 });
		assertNotNull( coords );
		assertEquals( 4, coords.length );

		final boolean useDummyZ = true;
		final double zValue = 0.0;
		CoordinateWriter writer = new CoordinateWriter(4," ", ",", useDummyZ, zValue);
		CoordinateHandler output = new CoordinateHandler();

		output.startDocument();
		writer.writeCoordinates( coords, output);
		output.endDocument();

        System.out.println( output.received );
		assertEquals("<coordinates>1,1,0 4,4,0 0,4,0 1,1,0</coordinates>", output.received );
	}

    public void test2DWithDummyZCoordSeq() throws Exception {
        CoordinateSequence coords = new CoordinateArraySequence(coords2D( new int[]{1,1, 4,4, 0,4, 1,1 }));

        final boolean useDummyZ = true;
        final double zValue = 0.0;
        CoordinateWriter writer = new CoordinateWriter(4," ", ",", useDummyZ, zValue);
        CoordinateHandler output = new CoordinateHandler();

        output.startDocument();
        writer.writeCoordinates( coords, output);
        output.endDocument();

        assertEquals("<coordinates>1,1,0 4,4,0 0,4,0 1,1,0</coordinates>", output.received );
    }

    public void test3D() throws Exception {
		Coordinate[] coords = coords3D( new int[]{1,1,3, 4,4,2, 0,4,2, 1,1,3 });
		assertNotNull( coords );
		assertEquals( 4, coords.length );

		CoordinateWriter writer = new CoordinateWriter(4," ", ",", true, 0.0, 3 );
		CoordinateHandler output = new CoordinateHandler();

		output.startDocument();
		writer.writeCoordinates( coords, output);
		output.endDocument();

		assertEquals("<coordinates>1,1,3 4,4,2 0,4,2 1,1,3</coordinates>", output.received );
		System.out.println( output.received );
	}
	
    public void test3DCoordSeq() throws Exception {
        CoordinateSequence coords = new CoordinateArraySequence(coords3D( new int[]{1,1,3, 4,4,2, 0,4,2, 1,1,3 }));

        CoordinateWriter writer = new CoordinateWriter(4," ", ",", true, 0.0, 3 );
        CoordinateHandler output = new CoordinateHandler();

        output.startDocument();
        writer.writeCoordinates( coords, output);
        output.endDocument();

        System.out.println( output.received );
        assertEquals("<coordinates>1,1,3 4,4,2 0,4,2 1,1,3</coordinates>", output.received );
    }

    class CoordinateHandler implements ContentHandler {
		StringBuffer buffer;
		String received;
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			buffer.append( new String(ch, start, length ) );
		}
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			buffer.append("</");
			buffer.append( localName );
			buffer.append(">");
		}
		public void endDocument() throws SAXException {			
			received = buffer.toString();
		}

		public void endPrefixMapping(String prefix) throws SAXException {
		}

		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
		}

		public void processingInstruction(String target, String data)
				throws SAXException {
		}

		public void setDocumentLocator(Locator locator) {
		}

		public void skippedEntity(String name) throws SAXException {
		}

		public void startDocument() throws SAXException {
			 buffer = new StringBuffer();
		}

		public void startElement(String uri, String localName, String name,
				Attributes atts) throws SAXException {
			buffer.append("<");
			buffer.append( localName );
			buffer.append(">");
		}

		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
		}		
	};
	
	Coordinate[] coords2D( int array[] ){
		Coordinate coords[] = new Coordinate[ array.length / 2 ];
		for( int i=0; i<coords.length; i++ ){
			int offset = i*2;
			coords[i]= new Coordinate( array[offset+0], array[offset+1]);		
		}
		return coords;		
	}
	Coordinate[] coords3D( int array[] ){
		Coordinate coords[] = new Coordinate[ array.length / 3 ];
		for( int i=0; i<coords.length; i++  ){
			int offset = i*3;
			coords[i]= new Coordinate( array[offset+0], array[offset+1], array[offset+2]);		
		}
		return coords;		
	}
}

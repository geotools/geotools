/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

import junit.framework.TestCase;

import org.geotools.TestData;
import org.geotools.xml.schema.Schema;


/**
 * <p>
 * DOCUMENT ME!
 * </p>
 * @
 *
 * @author dzwiers www.refractions.net
 * @source $URL$
 */
public class SchemaParser2Test extends TestCase {
    //	public void testMail(){
    //		runit("","test/mails.xsd");
    //	}
    public void testWFS() throws URISyntaxException {
        runit(new URI("http://www.opengis.net/wfs"), "xml/wfs/WFS-basic.xsd");
    }

    public void testGMLFeature() throws URISyntaxException {
        runit(new URI("http://www.opengis.net/gml"), "xml/gml/feature.xsd");
    }

    public void testGMLGeometry() throws URISyntaxException {
        runit(new URI("http://www.opengis.net/gml"), "xml/gml/geometry.xsd");
    }

    public void testGMLXLinks() throws URISyntaxException {
        runit(new URI("http://www.w3.org/1999/xlink"), "xml/gml/xlinks.xsd");
    }

    private void runit(URI targetNS, String path) {
        Schema s = null;

        try {
            File f = TestData.copy(this,path);
            s = SchemaFactory.getInstance(targetNS, f.toURI(),
                    Level.INFO);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }

        assertNotNull("Schema missing", s);
        System.out.println(s);

        Schema s2 = null;
        s2 = SchemaFactory.getInstance(targetNS);
        assertTrue("Should be the same", s == s2);
    }
}

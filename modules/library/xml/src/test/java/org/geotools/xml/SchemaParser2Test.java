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
import org.geotools.TestData;
import org.geotools.xml.schema.Schema;
import org.junit.Assert;
import org.junit.Test;

/** @author dzwiers www.refractions.net */
public class SchemaParser2Test {
    //	public void testMail(){
    //		runit("","test/mails.xsd");
    //	}
    @Test
    public void testWFS() throws URISyntaxException {
        runit(new URI("http://www.opengis.net/wfs"), "xml/wfs/WFS-basic.xsd");
    }

    @Test
    public void testGMLFeature() throws URISyntaxException {
        runit(new URI("http://www.opengis.net/gml"), "xml/gml/feature.xsd");
    }

    @Test
    public void testGMLGeometry() throws URISyntaxException {
        runit(new URI("http://www.opengis.net/gml"), "xml/gml/geometry.xsd");
    }

    @Test
    public void testGMLXLinks() throws URISyntaxException {
        runit(new URI("http://www.w3.org/1999/xlink"), "xml/gml/xlinks.xsd");
    }

    private void runit(URI targetNS, String path) {
        Schema s = null;

        try {
            File f = TestData.copy(this, path);
            s = SchemaFactory.getInstance(targetNS, f.toURI(), Level.INFO);
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail(e.toString());
        }

        Assert.assertNotNull("Schema missing", s);
        // System.out.println(s);

        Schema s2 = SchemaFactory.getInstance(targetNS);
        Assert.assertSame("Should be the same", s, s2);
    }
}

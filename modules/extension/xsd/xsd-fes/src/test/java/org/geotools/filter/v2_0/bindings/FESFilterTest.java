/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v2_0.bindings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import javax.xml.parsers.DocumentBuilderFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.v2_0.FESTestSupport;
import org.geotools.xsd.Encoder;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsLike;
import org.w3c.dom.Document;

/**
 * Tests for GEOT-4697
 *
 * @author "Mauro Bartolomeoli - mauro.bartolomeoli@geo-solutions.it"
 */
public class FESFilterTest extends FESTestSupport {

    public void testEncodePropertyIsLike() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyIsLike filter = ff.like(ff.property("name"), "%test%");
        org.geotools.filter.v2_0.FESConfiguration configuration =
                new org.geotools.filter.v2_0.FESConfiguration();
        Encoder encoder = new Encoder(configuration);
        encoder.encode(filter, org.geotools.filter.v2_0.FES.Filter, os);

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);

        Document doc =
                docFactory.newDocumentBuilder().parse(new ByteArrayInputStream(os.toByteArray()));

        assertEquals(1, doc.getElementsByTagName("fes:PropertyIsLike").getLength());
        assertEquals(1, doc.getElementsByTagName("fes:ValueReference").getLength());
        assertEquals(1, doc.getElementsByTagName("fes:Literal").getLength());
    }

    public void testEncodeTemporal() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Filter filter = ff.after(ff.property("date"), ff.literal(new Date()));
        org.geotools.filter.v2_0.FESConfiguration configuration =
                new org.geotools.filter.v2_0.FESConfiguration();
        Encoder encoder = new Encoder(configuration);
        encoder.encode(filter, org.geotools.filter.v2_0.FES.Filter, os);

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);

        Document doc =
                docFactory.newDocumentBuilder().parse(new ByteArrayInputStream(os.toByteArray()));

        assertEquals(1, doc.getElementsByTagName("fes:After").getLength());
        assertEquals(1, doc.getElementsByTagName("fes:ValueReference").getLength());
        assertEquals(1, doc.getElementsByTagName("fes:Literal").getLength());
    }
}

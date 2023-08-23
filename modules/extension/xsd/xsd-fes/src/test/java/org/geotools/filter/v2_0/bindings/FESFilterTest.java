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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import javax.xml.parsers.DocumentBuilderFactory;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.v2_0.FESTestSupport;
import org.geotools.xsd.Encoder;
import org.junit.Test;
import org.w3c.dom.Document;

public class FESFilterTest extends FESTestSupport {
    @Test
    public void testEncodePropertyIsLike() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
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

    @Test
    public void testEncodeTemporal() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
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

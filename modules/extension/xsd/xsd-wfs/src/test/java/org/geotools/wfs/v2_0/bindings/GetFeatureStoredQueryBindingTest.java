/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.wfs.v2_0.bindings;

import java.io.StringWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.opengis.wfs20.GetFeatureType;
import net.opengis.wfs20.ParameterType;
import net.opengis.wfs20.StoredQueryType;
import net.opengis.wfs20.Wfs20Factory;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.wfs.v2_0.WFSTestSupport;
import org.w3c.dom.Document;

public class GetFeatureStoredQueryBindingTest extends WFSTestSupport {

    public void testEncodeGetFeatureWithStoredQueryId() throws Exception {
        Wfs20Factory factory = Wfs20Factory.eINSTANCE;

        GetFeatureType request = factory.createGetFeatureType();

        StoredQueryType storedQuery = factory.createStoredQueryType();
        storedQuery.setId("fmi::radar::composite::rr24h");

        ParameterType param1 = factory.createParameterType();

        param1.setName("bbox");
        param1.setValue("21,61,22,62,epsg::4326");

        storedQuery.getParameter().add(param1);

        request.getAbstractQueryExpression().add(storedQuery);

        Document doc = encode(request, WFS.GetFeature);

        DOMSource domSource = new DOMSource(doc);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);

        String asString = writer.toString();
        assertTrue(asString.contains("id=\"fmi::radar::composite::rr24h\""));
        assertTrue(asString.contains(">21,61,22,62,epsg::4326<"));
    }
}

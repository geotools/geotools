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
package org.geotools.gml2;

import java.net.URI;
import org.geotools.gml2.bindings.GML2ParsingUtils;
import org.geotools.xsd.impl.AttributeImpl;
import org.geotools.xsd.impl.NodeImpl;
import org.junit.Assert;
import org.junit.Test;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;

public class GML2ParsingUtilsTest {
    @Test
    public void testCRS() throws Exception {
        NodeImpl node = new NodeImpl(null);

        AttributeImpl att = new AttributeImpl(null);
        att.setName("srsName");

        NodeImpl attNode = new NodeImpl(att);
        attNode.setValue(new URI("EPSG:4326"));
        node.addAttribute(attNode);

        CoordinateReferenceSystem crs = GML2ParsingUtils.crs(node);
        Assert.assertNotNull(crs);

        attNode.setValue(new URI("http://www.opengis.net/gml/srs/epsg.xml#4326"));
        crs = GML2ParsingUtils.crs(node);
        Assert.assertNotNull(crs);
    }
}

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

import org.geotools.feature.AttributeImpl;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.gml3.GML3TestSupport;
import org.geotools.xlink.XLINK;
import org.opengis.feature.type.AttributeType;

/**
 * Test ReferenceTypeBinding.
 *
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class ReferenceTypeBindingTest extends GML3TestSupport {

    /** Make sure no xlink:href is created if id is not set. */
    public void testXlinkHrefNull() throws Exception {
        AttributeType attType =
                new AttributeTypeImpl(
                        new NameImpl("someAttributeType"),
                        String.class,
                        false,
                        false,
                        null,
                        null,
                        null);
        AttributeImpl node = new AttributeImpl(null, attType, null);
        ReferenceTypeBinding binding = new ReferenceTypeBinding();
        Object xlink = binding.getProperty(node, XLINK.HREF);
        assertNull(xlink);
    }

    /** Test xlink:href returns gml:id. */
    public void testXlinkHref() throws Exception {
        AttributeType attType =
                new AttributeTypeImpl(
                        new NameImpl("someAttributeType"),
                        String.class,
                        false,
                        false,
                        null,
                        null,
                        null);
        AttributeImpl node = new AttributeImpl(null, attType, null);
        final String ID = "something";
        node.getUserData().put("gml:id", ID);
        ReferenceTypeBinding binding = new ReferenceTypeBinding();
        Object xlink = binding.getProperty(node, XLINK.HREF);
        assertNotNull(xlink);
        assertEquals("#" + ID, xlink.toString());
    }
}

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
package org.geotools.sld.bindings;

import org.geotools.styling.FeatureTypeStyle;

public class SLDFeatureTypeStyleBindingTest extends SLDTestSupport {
    public void testType() throws Exception {
        assertEquals(FeatureTypeStyle.class, new SLDFeatureTypeStyleBinding(null).getType());
    }

    public void test() throws Exception {
        SLDMockData.featureTypeStyle(document, document);

        FeatureTypeStyle fts = (FeatureTypeStyle) parse();
        assertNotNull(fts);

        assertEquals("theName", fts.getName());
        assertEquals("theAbstract", fts.getDescription().getAbstract().toString());
        assertEquals("theTitle", fts.getDescription().getTitle().toString());
        assertEquals("theFeatureTypeName", fts.featureTypeNames().iterator().next().getLocalPart());

        assertEquals(2, fts.semanticTypeIdentifiers().size());
        assertEquals(2, fts.rules().size());
    }
}

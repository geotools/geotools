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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.gml3.GML;
import org.geotools.gml3.GML3TestSupport;
import org.geotools.xsd.Configuration;
import org.junit.Test;
import org.w3c.dom.Element;

public class AbstractFeatureCollectionTypeBindingTest extends GML3TestSupport {

    @Override
    protected Map<String, String> getNamespaces() {
        final Map<String, String> namespaces = super.getNamespaces();
        namespaces.put("test", TEST.NAMESPACE);
        return namespaces;
    }

    @Override
    protected Configuration createConfiguration() {
        return new TestConfiguration();
    }

    @Test
    public void testFeatureMember() throws Exception {
        Element featureCollection = GML3MockData.element(TEST.TestFeatureCollection, document, document);

        Element featureMember = GML3MockData.element(GML.featureMember, document, featureCollection);

        Element feature = GML3MockData.feature(document, featureMember);
        feature.setAttributeNS(GML.NAMESPACE, "id", "fid.1");

        featureMember = GML3MockData.element(GML.featureMember, document, featureCollection);

        feature = GML3MockData.feature(document, featureMember);
        feature.setAttributeNS(GML.NAMESPACE, "id", "fid.2");

        SimpleFeatureCollection fc = (SimpleFeatureCollection) parse();
        assertNotNull(fc);

        assertEquals(2, fc.size());

        try (SimpleFeatureIterator i = fc.features()) {
            SimpleFeature f = i.next();
            assertEquals("fid.1", f.getID());

            f = i.next();
            assertEquals("fid.2", f.getID());
        }
    }

    @Test
    public void testFeatureMembers() throws Exception {
        Element featureCollection = GML3MockData.element(TEST.TestFeatureCollection, document, document);

        Element featureMember = GML3MockData.element(GML.featureMembers, document, featureCollection);

        Element feature = GML3MockData.feature(document, featureMember);
        feature.setAttributeNS(GML.NAMESPACE, "id", "fid.1");

        feature = GML3MockData.feature(document, featureMember);
        feature.setAttributeNS(GML.NAMESPACE, "id", "fid.2");

        SimpleFeatureCollection fc = (SimpleFeatureCollection) parse();
        assertNotNull(fc);

        assertEquals(2, fc.size());

        try (SimpleFeatureIterator i = fc.features()) {
            SimpleFeature f = i.next();
            assertEquals("fid.1", f.getID());

            f = i.next();
            assertEquals("fid.2", f.getID());
        }
    }
}

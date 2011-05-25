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

package org.geotools.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.complex.DataAccessRegistry;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.Types;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.spatial.BBOX;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * This is to test bounding box query that previously didn't work for app-schema.
 * 
 * @author Rini Angreani, Curtin University of Technology
 *
 * @source $URL$
 */
public class BBoxTest {
    private static FilterFactoryImpl ff;

    private static DataAccess<FeatureType, Feature> dataAccess;

    private static FeatureSource<FeatureType, Feature> fSource;

    @BeforeClass
    public static void setUp() throws Exception {

        final String GSML_URI = "urn:cgi:xmlns:CGI:GeoSciML:2.0";
        /**
         * Set up filter factory
         */
        NamespaceSupport namespaces = new NamespaceSupport();
        namespaces.declarePrefix("gsml", GSML_URI);
        namespaces.declarePrefix("gml", "http://www.opengis.net/gml");
        ff = new FilterFactoryImplNamespaceAware(namespaces);

        /**
         * Load data access
         */
        final Name FEATURE_TYPE = Types.typeName(GSML_URI, "MappedFeature");
        final String schemaBase = "/test-data/";
        Map<String, Serializable> dsParams = new HashMap<String, Serializable>();
        dsParams.put("dbtype", "app-schema");
        URL url = BBoxTest.class.getResource(schemaBase + "MappedFeatureAsOccurrence.xml");
        assertNotNull(url);
        dsParams.put("url", url.toExternalForm());
        dataAccess = DataAccessFinder.getDataStore(dsParams);

        fSource = (FeatureSource<FeatureType, Feature>) dataAccess.getFeatureSource(FEATURE_TYPE);
    }

    @AfterClass
    public static void tearDown() {
        DataAccessRegistry.unregisterAll();
    }

    @Test
    /*
     * Test BBox function with the property name specified.
     */
    public void testBBoxWithPropertyName() throws Exception {
        // property name exists and is a geometry attribute
        BBOX filter = ff.bbox(ff.property("gsml:shape"), -1.1, 52.5, -1.1, 52.6, null);
        FeatureCollection<FeatureType, Feature> features = fSource.getFeatures(filter);
        assertEquals(2, size(features));
        Iterator<Feature> iterator = features.iterator();
        Feature f = iterator.next();
        assertEquals(f.getIdentifier().toString(), "mf1");
        f = iterator.next();
        assertEquals(f.getIdentifier().toString(), "mf3");

        // prove that it would fail when property name is not a geometry attribute
        filter = ff.bbox(ff.property("gml:name[1]"), -1.2, 52.5, -1.1, 52.6, null);
        features = fSource.getFeatures(filter);
        assertEquals(0, size(features));
    }

    @Test
    /*
     * Test Bbox function with no property name specified. The default geometry should be used.
     */
    public void testBBoxWithNoPropertyName() throws IOException {
        // default geometry exists
        // in theory, when property name is not specified, the filter should apply to all the
        // geometry attributes,
        // but it wouldn't work until the bug in GeometryFilterImpl is fixed
        // and our test data only have 1 geometry, so it doesn't test multiple geometries case
        BBOX filter = ff.bbox(ff.property(""), -1.1, 52.5, -1.1, 52.6, null);
        FeatureCollection<FeatureType, Feature> features = fSource.getFeatures(filter);
        assertEquals(2, size(features));
        Iterator<Feature> iterator = features.iterator();
        Feature f = iterator.next();
        assertEquals(f.getIdentifier().toString(), "mf1");
        f = iterator.next();
        assertEquals(f.getIdentifier().toString(), "mf3");
    }

    private int size(FeatureCollection<FeatureType, Feature> features) {
        int size = 0;
        for (Iterator i = features.iterator(); i.hasNext(); i.next()) {
            size++;
        }
        return size;
    }
}

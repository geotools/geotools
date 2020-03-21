/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.appschema.filter.FilterFactoryImplNamespaceAware;
import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.test.AppSchemaTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * This tests property selection using nested x-paths, combined with feature selection
 *
 * @author Niels Charlier (Curtin University of Technology)
 */
public class PropSelectionTest extends AppSchemaTestSupport {

    private static final String schemaBase = "/test-data/";

    static final String GSMLNS = "urn:cgi:xmlns:CGI:GeoSciML:2.0";

    static final String GMLNS = "http://www.opengis.net/gml";

    static final Name GEOLOGIC_UNIT = Types.typeName(GSMLNS, "GeologicUnit");

    static final Name MAPPED_FEATURE = Types.typeName(GSMLNS, "MappedFeature");

    private FeatureSource<FeatureType, Feature> mfSource;

    /** namespace aware filter factory * */
    private FilterFactory2 ff;

    @Before
    public void setUp() throws Exception {
        /** Set up filter factory */
        NamespaceSupport namespaces = new NamespaceSupport();
        namespaces.declarePrefix("gsml", GSMLNS);
        namespaces.declarePrefix("gml", GMLNS);
        ff = new FilterFactoryImplNamespaceAware(namespaces);

        /** Load mapped feature data access */
        Map<String, Serializable> dsParams = new HashMap<String, Serializable>();
        URL url = PropSelectionTest.class.getResource(schemaBase + "MappedFeaturePropertyfile.xml");
        assertNotNull(url);

        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        DataAccess<FeatureType, Feature> mfDataAccess = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(mfDataAccess);

        /** Load geologic unit data access */
        url = PropSelectionTest.class.getResource(schemaBase + "GeologicUnit.xml");
        assertNotNull(url);

        dsParams = new HashMap<String, Serializable>();
        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        DataAccess<FeatureType, Feature> guDataAccess = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(guDataAccess);

        mfSource = mfDataAccess.getFeatureSource(MAPPED_FEATURE);
    }

    /** Testing Property Name Selection */
    @Test
    public void testPropertyNameSelection() throws IOException {

        PropertyName propertyName1 =
                ff.property("gsml:specification/gsml:GeologicUnit/gml:description");
        PropertyName propertyName2 =
                ff.property("gsml:specification/gsml:GeologicUnit/gsml:occurrence");

        List<PropertyName> properties = new ArrayList<PropertyName>();
        properties.add(propertyName1);
        Query query = new Query();
        query.setProperties(properties);

        FeatureCollection<FeatureType, Feature> mfCollection = mfSource.getFeatures(query);

        FeatureIterator iterator = mfCollection.features();

        int i = 0;
        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            assertNotNull(propertyName1.evaluate(feature));
            assertNull(propertyName2.evaluate(feature));
            i++;
        }
        assertEquals(4, i);

        properties = new ArrayList<PropertyName>();
        properties.add(propertyName2);
        query.setProperties(properties);

        mfCollection = mfSource.getFeatures(query);

        iterator = mfCollection.features();

        i = 0;
        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            assertNotNull(propertyName2.evaluate(feature));
            assertNull(propertyName1.evaluate(feature));
            i++;
        }
        assertEquals(4, i);
    }
}

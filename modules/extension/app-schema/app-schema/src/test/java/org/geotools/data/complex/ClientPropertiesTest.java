/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory2;
import org.geotools.api.filter.expression.PropertyName;
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
import org.xml.sax.helpers.NamespaceSupport;

public class ClientPropertiesTest extends AppSchemaTestSupport {

    private static final String schemaBase = "/test-data/";

    static final String GSMLNS = "urn:cgi:xmlns:CGI:GeoSciML:2.0";

    static final String GMLNS = "http://www.opengis.net/gml";

    static final Name MAPPED_FEATURE = Types.typeName(GSMLNS, "MappedFeature");

    private FeatureSource<FeatureType, Feature> mfSource;

    private FilterFactory2 ff;

    @Before
    public void setUp() throws Exception {
        /** Set up filter factory */
        NamespaceSupport namespaces = new NamespaceSupport();
        namespaces.declarePrefix("gsml", GSMLNS);
        namespaces.declarePrefix("gml", GMLNS);
        ff = new FilterFactoryImplNamespaceAware(namespaces);

        /** Load mapped feature data access */
        Map<String, Serializable> dsParams = new HashMap<>();
        URL url =
                PropSelectionTest.class.getResource(
                        schemaBase + "MappedFeatureClientProperties.xml");
        assertNotNull(url);

        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        DataAccess<FeatureType, Feature> mfDataAccess = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(mfDataAccess);

        /** Load geologic unit data access */
        url = PropSelectionTest.class.getResource(schemaBase + "GeologicUnit.xml");
        assertNotNull(url);

        dsParams = new HashMap<>();
        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        DataAccess<FeatureType, Feature> guDataAccess = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(guDataAccess);

        mfSource = mfDataAccess.getFeatureSource(MAPPED_FEATURE);
    }

    /** Testing Property Name Selection */
    @Test
    public void testClientPropertyOnRootElement() throws IOException {

        PropertyName clientProp1 = ff.property("@codeSpace");
        PropertyName clientProp2 = ff.property("@staticProp");

        Query query = new Query();
        query.setProperties(Query.ALL_PROPERTIES);
        query.setFilter(Filter.INCLUDE);
        PropertyName namePn = ff.property("gml:name");
        FeatureCollection<FeatureType, Feature> mfCollection = mfSource.getFeatures(query);

        try (FeatureIterator iterator = mfCollection.features()) {
            int featureCount = 0;
            while (iterator.hasNext()) {
                Feature feature = iterator.next();
                String name = namePn.evaluate(feature, String.class);
                assertEquals(
                        "urn:x-test:classifierScheme:TestAuthority:" + name,
                        clientProp1.evaluate(feature));
                assertEquals("static_property", clientProp2.evaluate(feature));
                featureCount++;
            }
            assertEquals(5, featureCount);
        }
    }
}

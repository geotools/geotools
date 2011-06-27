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

package org.geotools.data.ws;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.complex.AppSchemaDataAccess;
import org.geotools.data.complex.DataAccessRegistry;
import org.geotools.data.complex.FeatureChainingTest;
import org.geotools.data.complex.XmlDataStoreTest;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.Types;
import org.geotools.filter.FilterFactoryImplNamespaceAware;

import org.junit.BeforeClass;

import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * This is to test the integration of a data access (which does not necessarily have to be an
 * app-schema data access) that produces complex features of a certain XML form as an input to an
 * app-schema data access of a different XML form. A new app-schema data access would be created to
 * remap the non-app-schema data access into the output XML form. Then the features can chain or be
 * chained as per normal. See FeatureChainingTest.java to see feature chaining in action.
 * 
 * @author Rini Angreani, Curtin University of Technology
 *
 *
 * @source $URL$
 */
public class AppSchemaWithBackendDataAccessIntegrationTest extends TestCase {

    static FilterFactory ff;

    private final int MAX_FEATURES = 5;

    private Name typeName;

    private static final String schemaBase = "/test-data/";

    /**
     * Remapped Geologic Unit data access in GSML form
     */
    private static AppSchemaDataAccess newGuDataAccess;

    /**
     * Create the input data access containing complex features of MO form.
     */
    @BeforeClass
    protected void setUp() throws Exception {
        super.setUp();
        setFilterFactory();
        typeName = Types.typeName("GeologicUnit1");
        URL url = XmlDataStoreTest.class.getResource(schemaBase
                + "GuDaBackend.xml");

        assertNotNull(url);

        Map<String, Serializable> dsParams = new HashMap<String, Serializable>();
        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        newGuDataAccess = (AppSchemaDataAccess) DataAccessFinder.getDataStore(dsParams);
        assertNotNull(newGuDataAccess);
    }

    protected void tearDown() throws Exception {
        DataAccessRegistry.unregisterAndDisposeAll();
        super.tearDown();
    }

    public void testSimplePropertyFilter() throws Exception {
        final Filter filter = ff.equals(ff.property("gml:name/@codeSpace"), ff
                .literal("gsv:NameSpace"));
        runAppSchemaBackendTests(filter);
    }

    public void testComplexPropertyFilter() throws Exception {
        final Filter filter = ff.equals(ff
                .property("gsml:observationMethod/gsml:CGI_TermValue/gsml:value"), ff
                .literal("CONSTANT"));
        runAppSchemaBackendTests(filter);
    }

    private void runAppSchemaBackendTests(Filter filter) throws Exception {
        final Name GEOLOGIC_UNIT1 = Types.typeName("GeologicUnit1");
        FeatureSource<FeatureType, Feature> guFeatureSource = newGuDataAccess
                .getFeatureSourceByName(GEOLOGIC_UNIT1);
        assertNotNull(guFeatureSource);
        List<Feature> results = new ArrayList<Feature>();

        FeatureCollection<FeatureType, Feature> features = getFeatures(MAX_FEATURES, filter);
        FeatureIterator<Feature> it = features.features();
        for (; it.hasNext();) {
            results.add((Feature) it.next());
        }
        it.close();
    }

    private void setFilterFactory() {
        NamespaceSupport namespaces = new NamespaceSupport();
        namespaces.declarePrefix("gsml", FeatureChainingTest.GSMLNS);
        namespaces.declarePrefix("gml", FeatureChainingTest.GMLNS);
        ff = new FilterFactoryImplNamespaceAware(namespaces);
    }

    private FeatureCollection<FeatureType, Feature> getFeatures(final int maxFeatures,
            Filter inputFilter) throws Exception {
        FeatureSource<FeatureType, Feature> fSource = newGuDataAccess
                .getFeatureSourceByName(typeName);
        FeatureCollection<FeatureType, Feature> features = fSource.getFeatures(namedQuery(
                inputFilter, new Integer(maxFeatures)));
        return features;
    }

    private Query namedQuery(Filter filter, int count) throws Exception {
        return new Query("GeologicUnit", new URI(FeatureChainingTest.GSMLNS), filter, count, new String[] {}, "test");
    }
}

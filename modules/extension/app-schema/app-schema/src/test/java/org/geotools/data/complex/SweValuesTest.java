/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015 - 2016, Open Source Geospatial Foundation (OSGeo)
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
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.appschema.filter.FilterFactoryImplNamespaceAware;
import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.util.ComplexFeatureConstants;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Test checking the correct encoding of the swe:values element from the OGC SWE Common schema.
 *
 * @author Stefano Costa, GeoSolutions
 */
public class SweValuesTest {

    public static final String OM_NS = "http://www.opengis.net/om/2.0";

    public static final String SWE_NS = "http://www.opengis.net/swe/2.0";

    public static final String GML_NS = "http://www.opengis.net/gml/3.2";

    public static final String XLINK_NS = "http://www.w3.org/1999/xlink";

    public static final Name OBSERVATION_TYPE = Types.typeName(OM_NS, "OM_ObservationType");

    public static final Name OBSERVATION_FEATURE = Types.typeName(OM_NS, "OM_Observation");

    public static final String SWE_VALUES_MAPPING = "/test-data/sweValuesAsList.xml";

    private static FilterFactory2 ff;

    private static FeatureSource<FeatureType, Feature> obsSource;

    /** Generated observation features */
    private static FeatureCollection<FeatureType, Feature> obsFeatures;

    private NamespaceSupport namespaces = new NamespaceSupport();

    public SweValuesTest() {
        namespaces.declarePrefix("om", OM_NS);
        namespaces.declarePrefix("swe", SWE_NS);
        namespaces.declarePrefix("gml", GML_NS);
        namespaces.declarePrefix("xlink", XLINK_NS);
        ff = new FilterFactoryImplNamespaceAware(namespaces);
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        loadDataAccesses();
    }

    /** Load all the data accesses. */
    private static void loadDataAccesses() throws Exception {
        /** Load observation data access */
        Map dsParams = new HashMap();
        URL url = SweValuesTest.class.getResource(SWE_VALUES_MAPPING);
        assertNotNull(url);

        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        DataAccess<FeatureType, Feature> omsoDataAccess = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(omsoDataAccess);

        FeatureType observationFeatureType = omsoDataAccess.getSchema(OBSERVATION_FEATURE);
        assertNotNull(observationFeatureType);

        obsSource = (FeatureSource) omsoDataAccess.getFeatureSource(OBSERVATION_FEATURE);
        obsFeatures = (FeatureCollection) obsSource.getFeatures();
        assertEquals(2, size(obsFeatures));
    }

    static int size(FeatureCollection<FeatureType, Feature> features) {
        int size = 0;
        FeatureIterator<Feature> iterator = features.features();
        while (iterator.hasNext()) {
            iterator.next();
            size++;
        }
        iterator.close();
        return size;
    }

    @Test
    public void testSweValues() {
        Map<String, String> expected = new HashMap<String, String>();
        expected.put(
                "ID1.2",
                "missing missing 8.9 7.9 14.2 15.4 18.1 19.1 21.7 20.8 19.6 14.9 10.8 8.8 8.5 10.4");
        expected.put(
                "ID2.2",
                "16.2 17.1 22.0 25.1 23.9 22.8 17.0 10.2 9.2 7.1 12.3 12.9 17.2 23.6 21.6 21.9 17.6 14.0 9.3 3.8");

        FeatureIterator<? extends Feature> featIt = obsFeatures.features();
        while (featIt.hasNext()) {
            Feature f = featIt.next();
            PropertyName pf = ff.property("om:result/swe:DataArray/swe:values", namespaces);
            Object sweValues = pf.evaluate(f);
            assertNotNull(sweValues);
            assertTrue(sweValues instanceof ComplexAttribute);
            ComplexAttribute sweValuesAttr = (ComplexAttribute) sweValues;
            assertEquals(
                    expected.get(f.getIdentifier().getID()),
                    sweValuesAttr.getProperty(ComplexFeatureConstants.SIMPLE_CONTENT).getValue());
        }
    }
}

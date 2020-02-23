/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.data.complex.SweValuesTest.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.appschema.filter.FilterFactoryImplNamespaceAware;
import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Checks that gml:id attribute can be retrieved also when it is mapped as a regular <code>
 * &lt;ClientProperty&gt;</code> rather than an identifier (using <code>&lt;idExpression&gt;</code>
 * ).
 *
 * @author Stefano Costa, GeoSolutions
 */
public class ClientPropertyIdentifierTest {

    private FilterFactory2 ff;

    private NamespaceSupport namespaces = new NamespaceSupport();

    private FeatureSource obsSource;

    public ClientPropertyIdentifierTest() {
        namespaces.declarePrefix("om", OM_NS);
        namespaces.declarePrefix("swe", SWE_NS);
        namespaces.declarePrefix("gml", GML_NS);
        namespaces.declarePrefix("xlink", XLINK_NS);
        ff = new FilterFactoryImplNamespaceAware(namespaces);
    }

    /** Load all the data accesses. */
    @Before
    public void loadDataAccess() throws Exception {
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
        assertNotNull(obsSource);
        FeatureCollection obsFeatures = (FeatureCollection) obsSource.getFeatures();
        assertEquals(2, size(obsFeatures));
    }

    @Test
    public void testRetrieveTimeInstantGmlId() throws IOException {
        FilterFactory2 ff = new FilterFactoryImplNamespaceAware(namespaces);
        PropertyName gmlIdProperty = ff.property("om:resultTime/gml:TimeInstant/@gml:id");
        try (FeatureIterator featureIt = obsSource.getFeatures().features()) {
            Feature f = featureIt.next();
            String gmlId = (String) gmlIdProperty.evaluate(f);
            assertTrue(gmlId != null && !gmlId.trim().isEmpty());
        }
    }
}

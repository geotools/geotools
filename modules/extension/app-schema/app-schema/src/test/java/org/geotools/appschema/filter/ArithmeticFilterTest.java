/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.appschema.filter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.test.AppSchemaTestSupport;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

public class ArithmeticFilterTest extends AppSchemaTestSupport {

    private static FilterFactoryImpl ff;

    private static DataAccess<FeatureType, Feature> dataAccess;

    private static FeatureSource<FeatureType, Feature> fSource;

    @BeforeClass
    public static void onetimeSetUp() throws Exception {

        final String GSML_URI = "urn:cgi:xmlns:CGI:GeoSciML:2.0";
        /** Set up filter factory */
        NamespaceSupport namespaces = new NamespaceSupport();
        namespaces.declarePrefix("gsml", GSML_URI);
        namespaces.declarePrefix("gml", "http://www.opengis.net/gml");
        ff = new FilterFactoryImplNamespaceAware(namespaces);

        /** Load data access */
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

    @Test
    public void testWithArithmeticOperator() throws IOException {

        // evaluating first feature with Location accuracy value = 200

        // 200 x 2 = 400
        Filter arithmeticMultiplyFilter =
                ff.equals(
                        ff.multiply(
                                ff.property(
                                        "gsml:positionalAccuracy/gsml:CGI_NumericValue/gsml:principalValue"),
                                ff.literal(2)),
                        ff.literal(400));

        // 200 / 2 = 100
        Filter arithmeticDivideFilter =
                ff.equals(
                        ff.divide(
                                ff.property(
                                        "gsml:positionalAccuracy/gsml:CGI_NumericValue/gsml:principalValue"),
                                ff.literal(2)),
                        ff.literal(100));

        // 200 + 100 = 300
        Filter arithmeticAdditionFilter =
                ff.equals(
                        ff.add(
                                ff.property(
                                        "gsml:positionalAccuracy/gsml:CGI_NumericValue/gsml:principalValue"),
                                ff.literal(100)),
                        ff.literal(300));

        // 200 - 100 = 100
        Filter arithmeticSubtractionFilter =
                ff.equals(
                        ff.subtract(
                                ff.property(
                                        "gsml:positionalAccuracy/gsml:CGI_NumericValue/gsml:principalValue"),
                                ff.literal(100)),
                        ff.literal(100));

        FeatureCollection<FeatureType, Feature> features = fSource.getFeatures();

        PropertyName positionalAccuracy =
                ff.property("gsml:positionalAccuracy/gsml:CGI_NumericValue/gsml:principalValue");

        FeatureIterator<Feature> iterator = features.features();
        try {

            Feature f = iterator.next();
            Property val = (Property) positionalAccuracy.evaluate(f);
            System.out.println(val.getValue());
            // try all filters
            assertTrue(arithmeticMultiplyFilter.evaluate(f));
            assertTrue(arithmeticDivideFilter.evaluate(f));
            assertTrue(arithmeticAdditionFilter.evaluate(f));
            assertTrue(arithmeticSubtractionFilter.evaluate(f));

        } finally {
            iterator.close();
        }
    }
}

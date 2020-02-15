/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.geotools.test.AppSchemaTestSupport;
import org.geotools.util.Converters;
import org.geotools.util.URLs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * This is the test for vocabulary functions used in mapping file, ie. CategorizeFunction,
 * RecodeFunction and InterpolateFunction. This also tests the new VocabFunction, using a properties
 * file as the lookup table.
 *
 * @author Jody Garnett (GeoServer)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class VocabFunctionsTest extends AppSchemaTestSupport {
    private DataAccess<FeatureType, Feature> dataAccess;

    private FeatureCollection<FeatureType, Feature> exCollection;

    /** namespace aware filter factory * */
    private FilterFactory ff;

    @Before
    public void setUp() throws Exception {
        /** Set up filter factory */
        NamespaceSupport namespaces = new NamespaceSupport();
        namespaces.declarePrefix("ex", "http://example.com");
        namespaces.declarePrefix("gml", "http://www.opengis.net/gml");
        ff = new FilterFactoryImplNamespaceAware(namespaces);

        /** Load data access */
        final Name EXAMPLE_TYPE = Types.typeName("http://example.com", "FirstParentFeature");
        final String schemaBase = "/test-data/";
        Map dsParams = new HashMap();
        dsParams.put("dbtype", "app-schema");
        URL url = getClass().getResource(schemaBase + "VocabFunctionsTest.xml");
        assertNotNull(url);
        dsParams.put("url", url.toExternalForm());
        dataAccess = DataAccessFinder.getDataStore(dsParams);

        FeatureSource<FeatureType, Feature> fSource =
                (FeatureSource) dataAccess.getFeatureSource(EXAMPLE_TYPE);
        exCollection = (FeatureCollection<FeatureType, Feature>) fSource.getFeatures();

        assertEquals(3, size(exCollection));
    }

    @After
    public void tearDown() {
        dataAccess.dispose();
    }

    /** Test RecodeFunction */
    @Test
    public void testRecodeFunction() throws IOException {
        final Map<String, String> VALUE_MAP =
                new HashMap<String, String>() {
                    {
                        put("sc.1", "a");
                        put("sc.2", "b");
                        put("sc.3", "c");
                    }
                };
        FeatureIterator<Feature> features = exCollection.features();
        try {
            while (features.hasNext()) {
                Feature feature = features.next();
                String fId = feature.getIdentifier().getID();
                String recodedName = VALUE_MAP.get(fId);
                // gml[3]: <OCQL>Recode(STRING, 'string_one', 'a', 'string_two', 'b',
                // 'string_three',
                // 'c')</OCQL>
                ComplexAttribute complexAttribute =
                        (ComplexAttribute) ff.property("gml:name[3]").evaluate(feature);
                String value =
                        Converters.convert(
                                GML3EncodingUtils.getSimpleContent(complexAttribute), String.class);
                assertEquals(recodedName, value);
            }
        } finally {
            features.close();
        }
    }

    private int size(FeatureCollection<FeatureType, Feature> features) {
        // return features.size(); // JG: why are you not doing this?
        int size = 0;
        FeatureIterator<Feature> i = features.features();
        try {
            for (; i.hasNext(); i.next()) {
                size++;
            }
        } finally {
            i.close();
        }
        return size;
    }

    /** Test CategorizeFunction */
    @Test
    public void testCategorizeFunction() {
        final Map<String, String> VALUE_MAP =
                new HashMap<String, String>() {
                    {
                        put("sc.1", "missing value");
                        put("sc.2", "a valid value");
                        put("sc.3", "a valid value");
                    }
                };
        FeatureIterator<Feature> features = exCollection.features();
        try {
            while (features.hasNext()) {
                Feature feature = features.next();
                String fId = feature.getIdentifier().getID();
                Property attribute = feature.getProperty("someAttribute");
                // <OCQL>Categorize(getID(), 'missing value', 2, 'a valid value')</OCQL>
                assertEquals(attribute.getValue(), VALUE_MAP.get(fId));
            }
        } finally {
            features.close();
        }
    }

    /** Test the VocabFunction making use of a sample mapping provided by Alastair. */
    @Test
    public void testVocabFunction() {
        URL file = getClass().getResource("/test-data/minoc_lithology_mapping.properties");
        assertNotNull(file);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Function function =
                ff.function(
                        "Vocab", ff.literal("1LIST"), ff.literal(URLs.urlToFile(file).getPath()));

        Object value = function.evaluate(null);
        assertEquals(
                "urn:cgi:classifier:CGI:SimpleLithology:2008:calcareous_carbonate_sedimentary_rock",
                value);
    }

    @Test
    public void testNoVocabFunction() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Function function = ff.function("Vocab", ff.literal("a"), ff.literal("urn:1234"));

        try {
            function.evaluate(null);
            fail("Should not be able to get this far");
        } catch (Exception expected) {

        }
    }

    /** Test VocabFunction in a mapping file */
    @Test
    public void testVocabFunctionInMappingFile() {
        final Map<String, String> VALUE_MAP =
                new HashMap<String, String>() {
                    {
                        put("sc.1", "urn:cgi:classifier:CGI:SimpleLithology:2008:gravel");
                        put("sc.2", "urn:cgi:classifier:CGI:SimpleLithology:2008:diamictite");
                        put("sc.3", "urn:cgi:classifier:CGI:SimpleLithology:2008:sediment");
                    }
                };
        FeatureIterator<Feature> features = exCollection.features();
        try {
            while (features.hasNext()) {
                Feature feature = features.next();
                String fId = feature.getIdentifier().getID();
                // gml[2]: <OCQL>Vocab(URN_ID,
                // 'src/test/java/org/geotools/filter/test-data/minoc_lithology_mapping.properties')</OCQL>
                ComplexAttribute complexAttribute =
                        (ComplexAttribute) ff.property("gml:name[2]").evaluate(feature);
                String value =
                        Converters.convert(
                                GML3EncodingUtils.getSimpleContent(complexAttribute), String.class);
                assertEquals(VALUE_MAP.get(fId), value);
            }
        } finally {
            features.close();
        }
    }

    /**
     * Test VocabFunction in a mapping file with the <code>${config.parent}</code> interpolation
     * property.
     */
    @Test
    public void testVocabFunctionInMappingFileWithConfigParent() {
        @SuppressWarnings("serial")
        final Map<String, String> expectedValues =
                new HashMap<String, String>() {
                    {
                        put("sc.1", "urn:cgi:classifier:CGI:SimpleLithology:2008:gravel");
                        put("sc.2", "urn:cgi:classifier:CGI:SimpleLithology:2008:diamictite");
                        put("sc.3", "urn:cgi:classifier:CGI:SimpleLithology:2008:sediment");
                    }
                };
        FeatureIterator<Feature> features = exCollection.features();
        try {
            while (features.hasNext()) {
                Feature feature = features.next();
                String fid = feature.getIdentifier().getID();
                ComplexAttribute complexAttribute =
                        (ComplexAttribute) ff.property("gml:name[4]").evaluate(feature);
                String value =
                        Converters.convert(
                                GML3EncodingUtils.getSimpleContent(complexAttribute), String.class);
                assertEquals(expectedValues.get(fid), value);
            }
        } finally {
            features.close();
        }
    }
}

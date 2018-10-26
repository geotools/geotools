/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.geotools.data.DataAccess;
import org.geotools.data.complex.config.Types;
import org.geotools.data.util.FeatureStreams;
import org.geotools.test.AppSchemaTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * FeatureTypeMapping Indexed use case instancing tests
 *
 * @author Fernando Miño - Geosolutions
 */
public class FeatureTypeMappingIndexTest extends AppSchemaTestSupport {

    private static final String schemaBase = "/test-data/index/";
    private static final String NSURI = "http://www.stations.org/1.0";
    static final Name mappedTypeName = Types.typeName(null, "stationsIndexed");

    private AppSchemaDataAccessFactory factory;
    private Map params;
    DataAccess<FeatureType, Feature> dataStore;
    MappingFeatureSource mappedSource;

    @Before
    public void setUp() throws Exception {
        factory = new AppSchemaDataAccessFactory();
        params = new HashMap();
        params.put("dbtype", "app-schema");
        URL resource = getClass().getResource(schemaBase + "stationsIndexed.xml");
        if (resource == null) {
            fail("Can't find resouce " + schemaBase + "stationsIndexed.xml");
        }
        params.put("url", resource);
        dataStore = factory.createDataStore(params);
        mappedSource = (MappingFeatureSource) dataStore.getFeatureSource(mappedTypeName);
    }

    @After
    public void tearDown() throws Exception {
        dataStore.dispose();
        factory = null;
        params = null;
    }

    @Test
    public void testIndexesNames() throws Exception {
        List<AttributeMapping> atts = mappedSource.getMapping().getAttributeMappings();
        // check ID index
        assertTrue(atts.stream().anyMatch(att -> "ID".equals(att.getIndexField())));
        // check NAME index
        assertTrue(atts.stream().anyMatch(att -> "NAME".equals(att.getIndexField())));
    }

    @Test
    public void testIndexesSources() throws Exception {
        try (Stream<SimpleFeature> fstream =
                FeatureStreams.toFeatureStream(
                        mappedSource.getMapping().getIndexSource().getFeatures())) {
            assertTrue(fstream.anyMatch(f -> f.getIdentifier().getID().equals("st.1")));
        }
    }

    @Test
    public void testSourcesFeatures() throws Exception {
        try (Stream<Feature> fstream = FeatureStreams.toFeatureStream(mappedSource.getFeatures())) {
            List<Feature> flist = fstream.collect(Collectors.toList());
            assertTrue(flist.size() == 11);
            assertTrue(flist.get(2).getIdentifier().getID().equals("st.3"));
        }
    }
}

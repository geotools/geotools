/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
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

import com.vividsolutions.jts.util.Stopwatch;
import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.Types;
import org.geotools.filter.FilterFactoryImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import java.net.URL;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Eric Sword
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/app-schema/app-schema/src/test/java/org/geotools/data/complex/PolymorphicChainingTest.java $
 */
public class PolymorphicChainingTest {
    static final String EX_NS = "urn:example:xmlns:ArtifactML:1.0";
    
    static final Name ARTIFACT = Types.typeName(EX_NS, "Artifact");

    static FilterFactory ff = new FilterFactoryImpl(null);

    private static final String schemaBase = "/test-data/";

    private static FeatureSource<FeatureType, Feature> artifactSource;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Stopwatch sw = new Stopwatch();
        sw.start();
        loadDataAccesses();
        sw.stop();
        System.out.println("Set up time: " + sw.getTimeString());
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        DataAccessRegistry.unregisterAll();
    }

    /**
     * Basic test to make sure everything got loaded correctly
     *
     * @throws Exception
     */
    @Test
    public void testSimpleFilter() throws Exception {
        Expression property = ff.property("ex:seqId");
        Filter filter = ff.equals(property, ff.literal(101));
        FeatureCollection<FeatureType, Feature> filteredResults = artifactSource.getFeatures(filter);
        List<Feature> retVal = getFeatures(filteredResults);
        assertEquals(1, retVal.size());

        Feature feature = retVal.get(0);
        assertId("a.101", feature);
    }

    /**
     * Test filtering attributes on nested features.
     *
     * @throws Exception
     */
    @Test
    public void testMultiMappedFilter() throws Exception {
        Expression property = ff.property("ex:attributes/ex:Attribute/ex:key");
        Filter filter = ff.equals(property, ff.literal("stringKey1"));
        FeatureCollection<FeatureType, Feature> filteredResults = artifactSource.getFeatures(filter);
        List<Feature> retVal = getFeatures(filteredResults);
        assertEquals(0, retVal.size());
        
        property = ff.property("ex:attributes/ex:StringAttribute/ex:key");
        filter = ff.equals(property, ff.literal("stringKey1"));
        filteredResults = artifactSource.getFeatures(filter);
        retVal = getFeatures(filteredResults);
        assertEquals(2, retVal.size());
        
        Feature feature = retVal.get(0);
        assertId("a.101", feature);
        feature = retVal.get(1);
        assertId("a.102", feature);
        
        property = ff.property("ex:attributes/ex:GeoAttribute/ex:key");
        filter = ff.equals(property, ff.literal("stringKey1"));
        filteredResults = artifactSource.getFeatures(filter);
        retVal = getFeatures(filteredResults);
        assertEquals(0, retVal.size());
        
//        property = ff.property("ex:attributes/ex:key");
//        filter = ff.equals(property, ff.literal("stringKey1"));
//        filteredResults = artifactSource.getFeatures(filter);
//        retVal = getFeatures(filteredResults);
//        assertEquals(0, retVal.size());
    }

    protected static void assertId(String expected, Feature f) {
        String actual = f.getIdentifier().toString();
        assertEquals("Incorrect id: " + actual, expected, actual);
    }

    /**
     * Load all the data accesses.
     * 
     * @return
     * @throws Exception
     */
    private static void loadDataAccesses() throws Exception {
        /**
         * Load mapped feature data access
         */
        Map dsParams = new HashMap();
        URL url = PolymorphicChainingTest.class.getResource(schemaBase
                + "artifact_mapping.xml");
        assertNotNull(url);

        dsParams.put("dbtype", "app-schema");
        dsParams.put("url", url.toExternalForm());
        DataAccess<FeatureType, Feature> mfDataAccess = DataAccessFinder.getDataStore(dsParams);
        assertNotNull(mfDataAccess);

        FeatureType mappedFeatureType = mfDataAccess.getSchema(ARTIFACT);
        assertNotNull(mappedFeatureType);

        artifactSource = (FeatureSource) mfDataAccess.getFeatureSource(ARTIFACT);
    }

    protected List<Feature> getFeatures(FeatureCollection<FeatureType, Feature> features) {
        FeatureIterator<Feature> iterator = features.features();
        List<Feature> retVal = new ArrayList<Feature>();
        while (iterator.hasNext()) {
            retVal.add(iterator.next());
        }
        iterator.close();
        return retVal;
    }
}

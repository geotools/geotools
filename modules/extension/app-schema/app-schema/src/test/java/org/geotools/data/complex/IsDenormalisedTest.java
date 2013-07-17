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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.geotools.data.Query;
import java.util.logging.Logger;
import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.test.AppSchemaTestSupport;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * Tests for the <isDenormalised> mapping option.  Normalised datasources have the option of
 * supplying a maxFeatures limit which can be applied during SQL where clause generation but 
 * denormalised data sources MUST scan the entire table to ensure complete features are generated.
 * 
 * This class tests that any supplied maxFeatures limits are processed or disregarded correctly 
 * according to this setting
 * 
 * @author Geoff Williams
 */
public class IsDenormalisedTest extends AppSchemaTestSupport {

    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(IsDenormalisedTest.class.getPackage().getName());

    private static final String TEST_DATA = "/test-data/isDenormalised/";

    /**
     * GeoSciML 3.0 Core namespace.
     */
    private static final String GSML = "urn:cgi:xmlns:CGI:GeoSciML-Core:3.0.0";

    /**
     * gsml:MappedFeature, the type under test.
     */
    private static final Name MAPPED_FEATURE = new NameImpl(GSML, "MappedFeature");


    private int[] getMaxFeatures(String mappingFile, int queryMaxFeatures) throws Exception {


        int dataMaxFeatures = 0;
        int requestMaxFeatures = 0;

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("dbtype", "app-schema");
        URL url = getClass().getResource(TEST_DATA + mappingFile);
        Assert.assertNotNull(url);
        params.put("url", url.toExternalForm());
        DataAccess<FeatureType, Feature> dataAccess = null;
        try {
            dataAccess = DataAccessFinder.getDataStore(params);
            Assert.assertNotNull(dataAccess);
            FeatureType mappedFeatureType = dataAccess.getSchema(MAPPED_FEATURE);
            Assert.assertNotNull(mappedFeatureType);
            FeatureSource<FeatureType, Feature> source = dataAccess
                    .getFeatureSource(MAPPED_FEATURE);
            FeatureCollection<FeatureType, Feature> features = source.getFeatures();

            MappingFeatureCollection mfc = (MappingFeatureCollection) features;

            // set the requested filter limit
            Query query = mfc.getQuery();
            query.setMaxFeatures(queryMaxFeatures);

            // run the query...
            AbstractMappingFeatureIterator iterator = (AbstractMappingFeatureIterator) features.features();

            // see what the values are after runnign the search
            dataMaxFeatures = iterator.getDataMaxFeatures();
            requestMaxFeatures = iterator.getRequestMaxFeatures();

           


        } finally {
            if (dataAccess != null) {
                dataAccess.dispose();
            }
        }
        return new int[] {requestMaxFeatures, dataMaxFeatures};
    }

    /**
     * should return all records in .properties file -- maxFeatures should be ignored
     * @throws Exception 
     */
    @Test
    public void isDenormalisedTrue() throws Exception {
        int maxFeatures = 1;
        int[] res = getMaxFeatures("isDenormalised_true.xml", maxFeatures);
        Assert.assertEquals("requestMaxFeatures should have been preserved", maxFeatures, res[0]);
        Assert.assertEquals("dataMaxFeatures should have been reset to Integer.MAX_VALUE", Integer.MAX_VALUE, res[1]);
    }

    /**
     * maxFeatures limit should be applied
     * @throws Exception
     */
    @Test
    public void isDenormalisedFalse() throws Exception {
        int maxFeatures = 1;
        int res[] = getMaxFeatures("isDenormalised_false.xml",1);
        Assert.assertEquals("requestMaxFeatures should have been preserved", maxFeatures, res[0]);
        Assert.assertEquals("dataMaxFeatures should have been preserved", maxFeatures, res[1]);
    }

}

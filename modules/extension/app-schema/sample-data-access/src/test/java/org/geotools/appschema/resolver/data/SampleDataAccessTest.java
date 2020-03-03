/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.appschema.resolver.data;

import junit.framework.TestCase;
import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * Test for {@link org.geotools.appschema.resolver.data.SampleDataAccess}.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @version $Id$
 * @since 2.6
 */
public class SampleDataAccessTest extends TestCase {

    /**
     * Test that {@link org.geotools.appschema.resolver.data.SampleDataAccess} can be used to obtain
     * two features.
     */
    public static void testDataAccess() throws Exception {
        DataAccess<FeatureType, Feature> dataAccess =
                DataAccessFinder.getDataStore(SampleDataAccessFactory.PARAMS);
        FeatureSource<FeatureType, Feature> featureSource =
                dataAccess.getFeatureSource(SampleDataAccessData.MAPPEDFEATURE_TYPE_NAME);
        FeatureCollection<FeatureType, Feature> featureCollection = featureSource.getFeatures();
        int count = 0;
        for (FeatureIterator<Feature> iterator = featureCollection.features();
                iterator.hasNext();
                iterator.next()) {
            count++;
        }
        assertEquals(2, count);
    }
}

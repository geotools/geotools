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

package org.geotools.data;

import java.util.Iterator;

import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.SampleDataAccessData;
import org.geotools.data.SampleDataAccessFactory;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

import junit.framework.TestCase;

/**
 * Test for {@link SampleDataAccess}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 * @source $URL$
 * @since 2.6
 */
public class SampleDataAccessTest extends TestCase {

    /**
     * Test that {@link SampleDataAccess} can be used to obtain two features.
     * 
     * @throws Exception
     */
    public static void testDataAccess() throws Exception {
        DataAccess<FeatureType, Feature> dataAccess = DataAccessFinder
                .getDataStore(SampleDataAccessFactory.PARAMS);
        FeatureSource<FeatureType, Feature> featureSource = dataAccess
                .getFeatureSource(SampleDataAccessData.MAPPEDFEATURE_TYPE_NAME);
        FeatureCollection<FeatureType, Feature> featureCollection = featureSource.getFeatures();
        int count = 0;
        for (Iterator<Feature> iterator = featureCollection.iterator(); iterator.hasNext(); iterator
                .next()) {
            count++;
        }
        assertEquals(2, count);
    }

}

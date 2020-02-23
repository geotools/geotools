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
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * Test for {@link org.geotools.appschema.resolver.data.SampleDataAccessFactory}.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @version $Id$
 * @since 2.6
 */
public class SampleDataAccessFactoryTest extends TestCase {

    /**
     * Test that {@link org.geotools.data.DataAccessFinder} can find {@link
     * org.geotools.appschema.resolver.data.SampleDataAccessFactory} and use it to create a {@link
     * org.geotools.appschema.resolver.data.SampleDataAccess}.
     */
    public static void testFindSamplesDataAccessFactory() throws Exception {
        DataAccess<FeatureType, Feature> dataAccess =
                DataAccessFinder.getDataStore(SampleDataAccessFactory.PARAMS);
        assertNotNull(dataAccess);
        assertEquals(SampleDataAccess.class, dataAccess.getClass());
    }
}

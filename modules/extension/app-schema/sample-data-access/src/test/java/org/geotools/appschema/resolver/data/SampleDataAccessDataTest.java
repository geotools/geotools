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

import java.util.List;
import org.geotools.api.feature.Feature;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the test data created by {@link org.geotools.appschema.resolver.data.SampleDataAccessData}.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @version $Id$
 * @since 2.6
 */
public class SampleDataAccessDataTest {

    /** Test that two features are created. */
    @Test
    public void testData() {
        List<Feature> features = SampleDataAccessData.createMappedFeatures();
        Assert.assertEquals(2, features.size());
    }
}

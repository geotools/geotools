/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectormosaic;

import static org.hamcrest.MatcherAssert.assertThat;

import org.geotools.feature.NameImpl;
import org.hamcrest.Matchers;
import org.junit.Test;

public class VectorMosaicStoreTest extends VectorMosaicTest {

    @Test
    public void testGetFeatureSource() throws Exception {
        assertThat(
                MOSAIC_STORE.getFeatureSource(new NameImpl(MOSAIC_TYPE_NAME)),
                Matchers.instanceOf(VectorMosaicFeatureSource.class));
    }
}

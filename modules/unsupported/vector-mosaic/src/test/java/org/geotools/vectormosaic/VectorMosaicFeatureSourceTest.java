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

import static org.junit.Assert.assertEquals;

import org.geotools.api.filter.Filter;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;

public class VectorMosaicFeatureSourceTest extends VectorMosaicTest {

    @Test
    public void testGetCount() throws Exception {
        FeatureSource featureSource = MOSAIC_STORE.getFeatureSource(MOSAIC_TYPE_NAME);
        assertEquals(-1, featureSource.getCount(Query.ALL));
    }

    @Test
    public void testGetBoundsReturnsAll() throws Exception {
        FeatureSource featureSource = MOSAIC_STORE.getFeatureSource(MOSAIC_TYPE_NAME);
        Query query = new Query(MOSAIC_TYPE_NAME);
        query.setFilter(Filter.INCLUDE);
        assertEquals(
                new ReferencedEnvelope(
                        -87.93455924124713,
                        -76.66065404625304,
                        33.75260500029857,
                        39.65010808794791,
                        DefaultGeographicCRS.WGS84),
                featureSource.getBounds(query));
    }

    @Test
    public void testGetBounds() throws Exception {
        FeatureSource featureSource = MOSAIC_STORE.getFeatureSource(MOSAIC_TYPE_NAME);
        Query query = new Query(MOSAIC_TYPE_NAME);
        Filter qequals = FF.equals(FF.property("queryables"), FF.literal("time"));
        query.setFilter(qequals);
        assertEquals(
                new ReferencedEnvelope(
                        -81.8210129839034,
                        -76.66065404625304,
                        36.65259600815406,
                        39.65010808794791,
                        DefaultGeographicCRS.WGS84),
                featureSource.getBounds(query));
    }

    @Test
    public void testPassedInSPI() throws Exception {
        FeatureSource featureSource = MOSAIC_STORE_WITH_SPI.getFeatureSource(MOSAIC_TYPE_NAME);
        Query query = new Query(MOSAIC_TYPE_NAME);
        Filter qequals = FF.equals(FF.property("queryables"), FF.literal("time"));
        query.setFilter(qequals);
        assertEquals(
                new ReferencedEnvelope(
                        -81.8210129839034,
                        -76.66065404625304,
                        36.65259600815406,
                        39.65010808794791,
                        DefaultGeographicCRS.WGS84),
                featureSource.getBounds(query));
    }
}

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
package org.geotools.appschema.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.TestFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/** @author Fernando Miño, Geosolutions */
public class IndexQueryUtilsTest {

    @Test
    public void testCheckAllPropertiesIndexedXpath() {
        try (TestFeatureSource fsource =
                new TestFeatureSource(
                        "/test-data/index/",
                        "stationsIndexed.xml",
                        "http://www.stations.org/1.0",
                        "stationsIndexed")) {
            assertTrue(
                    IndexQueryUtils.checkAllPropertiesIndexed(
                            IndexQueryUtils.getAttributesOnFilter(totallyIndexedFilterXpath()),
                            fsource.getMappedSource().getMapping()));
            assertFalse(
                    IndexQueryUtils.checkAllPropertiesIndexed(
                            IndexQueryUtils.getAttributesOnFilter(partialIndexedFilterXpath()),
                            fsource.getMappedSource().getMapping()));
        }
    }

    @Test
    public void testEqualsXpath() {
        try (TestFeatureSource fsource =
                new TestFeatureSource(
                        "/test-data/index/",
                        "stationsIndexed.xml",
                        "http://www.stations.org/1.0",
                        "stationsIndexed")) {
            AttributeMapping attMap =
                    fsource.getMappedSource()
                            .getMapping()
                            .getAttributeMapping("st:Station/st:name");
            assertNotNull(attMap);
        }
    }

    private Filter totallyIndexedFilter() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Filter filter =
                ff.and(
                        ff.equals(ff.property("ID"), ff.literal("st.1")),
                        ff.like(ff.property("NAME"), "*fer*"));
        return filter;
    }

    private Filter partialIndexedFilter() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Filter filter =
                ff.and(
                        ff.equals(ff.property("ID"), ff.literal("st.1")),
                        ff.like(ff.property("LOCATION_NAME"), "*fer*"));
        return filter;
    }

    private Filter totallyIndexedFilterXpath() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Filter filter =
                ff.and(
                        ff.equals(ff.property("st:Station"), ff.literal("st.1")),
                        ff.like(ff.property("st:Station/st:name"), "*fer*"));
        return filter;
    }

    private Filter partialIndexedFilterXpath() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Filter filter =
                ff.and(
                        ff.equals(ff.property("st:Station"), ff.literal("st.1")),
                        ff.like(ff.property("st:Station/st:location/st:name"), "*fer*"));
        return filter;
    }
}

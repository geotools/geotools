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
package org.geotools.geopkg;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStore;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;

public class GeoPkgDialectTest {

    @Test
    public void testMultiBBOX() throws Exception {
        File lakes0 = new File("./src/test/resources/org/geotools/geopkg/lakes_srs_0.gpkg");
        Map<String, File> params = Collections.singletonMap("database", lakes0);
        JDBCDataStore store = new GeoPkgDataStoreFactory().createDataStore(params);
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        BBOX bboxDefault = ff.bbox("", 0, 0, 10, 10, null);
        BBOX bboxExplicit = ff.bbox("geom", 5, 5, 15, 15, null);
        And and = ff.and(bboxDefault, bboxExplicit);

        // should run an equivalent bbox direclty on the index, and the and in memory
        Filter[] split = store.getSQLDialect().splitFilter(and, store.getSchema("lakes_null"));
        assertThat(split[0], CoreMatchers.instanceOf(BBOX.class));
        assertEquals(and, split[1]);
        BBOX sqlbox = (BBOX) split[0];
        assertEquals(new ReferencedEnvelope(5, 10, 5, 10, null), sqlbox.getBounds());
    }
}

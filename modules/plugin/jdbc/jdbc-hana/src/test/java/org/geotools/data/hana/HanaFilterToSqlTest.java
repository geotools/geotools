/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import static org.junit.Assert.assertEquals;

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

/** @author Stefan Uhrig, SAP SE */
public class HanaFilterToSqlTest {

    @Test
    public void testDWithinFilterWithUnitEscaping() throws Exception {
        HanaFilterToSQL encoder = new HanaFilterToSQL(null, true, null);
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        GeometryFactory gf = new GeometryFactory();

        Coordinate coordinate = new Coordinate();
        DWithin dwithin = ff.dwithin(ff.property("GEOM"), ff.literal(gf.createPoint(coordinate)), 10.0, "'FOO");
        String encoded = encoder.encodeToString(dwithin);
        assertEquals("WHERE GEOM.ST_WithinDistance(?, 10.0, '''FOO') = 1", encoded);
    }
}

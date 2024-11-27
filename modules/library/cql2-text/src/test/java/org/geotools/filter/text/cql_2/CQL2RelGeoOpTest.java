/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.cql_2;

import org.geootols.filter.text.cql_2.CQL2;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.Test;

/** CQL2 does not support DWITHIN/BEYOND operators (not even in a separate conformance class) */
public class CQL2RelGeoOpTest {

    @Test(expected = CQLException.class)
    public void dwithin() throws CQLException {
        CQL2.toFilter("S_DWITHIN(ATTR1, POINT(1 2), 10, kilometers)");
    }

    @Test(expected = CQLException.class)
    public void beyond() throws CQLException {
        CQL2.toFilter("BEYOND(ATTR1, POINT(1.0 2.0), 10.0, kilometers)");
    }
}

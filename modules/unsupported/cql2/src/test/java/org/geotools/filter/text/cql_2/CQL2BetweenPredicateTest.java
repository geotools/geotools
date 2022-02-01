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

import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQLBetweenPredicateTest;
import org.opengis.filter.Filter;

/** Same as ECQL test case, can be merged back as-is. */
public class CQL2BetweenPredicateTest extends ECQLBetweenPredicateTest {

    @Override
    protected Filter parseFilter(String samplePredicate) throws CQLException {
        return CompilerUtil.parseFilter(this.language, samplePredicate);
    }
}

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
import org.geotools.filter.text.ecql.ECQLBooleanValueExpressionTest;
import org.opengis.filter.Filter;

/** No changes here, base examples are also valid CQL2 */
public class CQL2BooleanValueExpressionTest extends ECQLBooleanValueExpressionTest {

    @Override
    protected Filter parseFilter(String filter) throws CQLException {
        return CQL2.toFilter(filter);
    }
}

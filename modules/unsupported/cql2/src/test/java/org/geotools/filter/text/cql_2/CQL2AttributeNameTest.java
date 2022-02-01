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
import org.geotools.filter.text.cql2.CQLAttributeNameTest;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.expression.PropertyName;

/**
 * This test required no changes, could be folded in the CQL family one. Findings:
 *
 * <ul>
 *   <li>ECQL allowed the column already
 *   <li>The period is hopefully not going be a problem, as long as it's encoded back as a dot and
 *       not a /.
 *   <li>Interpretation of the period as a separator might be problematic though, CQL2 has no notion
 *       of nesting AFAIK (research is needed to confirm/deny).
 * </ul>
 */
public class CQL2AttributeNameTest extends CQLAttributeNameTest {

    @Override
    protected PropertyName parsePropertyName(String attSample) throws CQLException {
        return (PropertyName) CQL2.toExpression(attSample);
    }
}

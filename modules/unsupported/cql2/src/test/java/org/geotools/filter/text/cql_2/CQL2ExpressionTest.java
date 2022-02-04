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
import org.geotools.filter.text.cql2.CQLExpressionTest;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;

/** CQL2 allows $ to be part of an un-quoted identifier, which CQL/ECQL did not */
public class CQL2ExpressionTest extends CQLExpressionTest {

    @Test
    public void attributeNameOtherChars() throws CQLException {
        Expression expression = parseExpression("one:two.three$four_five");
        Assert.assertNotNull(expression);
        Assert.assertTrue(expression instanceof PropertyName);
        Assert.assertEquals(
                "one:two/three$four_five", ((PropertyName) expression).getPropertyName());
    }

    @Override
    protected Expression parseExpression(String cql) throws CQLException {
        return CQL2.toExpression(cql);
    }

    @Override
    protected Filter parseFilter(String cql) throws CQLException {
        return CQL2.toFilter(cql);
    }
}

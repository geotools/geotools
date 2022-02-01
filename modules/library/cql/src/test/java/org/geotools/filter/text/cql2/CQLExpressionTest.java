/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.cql2;

import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.Language;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;

/**
 * Expression Test
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
public class CQLExpressionTest {

    protected final Language language;

    public CQLExpressionTest() {

        this(Language.CQL);
    }

    protected CQLExpressionTest(final Language language) {

        assert language != null : "language cannot be null value";

        this.language = language;
    }

    @Test
    public void attributeName() throws CQLException {
        Expression expression = parseExpression("attName");
        Assert.assertNotNull(expression);
        Assert.assertTrue(expression instanceof PropertyName);
        Assert.assertEquals("attName", ((PropertyName) expression).getPropertyName());
    }

    protected Expression parseExpression(String cql) throws CQLException {
        return CompilerUtil.parseExpression(language, cql);
    }

    /** Bad identifier */
    @Test(expected = CQLException.class)
    public void badPropertyName() throws CQLException {

        String cqlExpression = "1A=2";
        parseFilter(cqlExpression);
    }

    protected Filter parseFilter(String cql) throws CQLException {
        return CompilerUtil.parseFilter(language, cql);
    }

    @Test
    public void add() throws CQLException {
        Expression expression = parseExpression("a + b + x.y.z");
        Assert.assertNotNull(expression);
        Assert.assertTrue(expression instanceof Add);

        Add add = (Add) expression;
        Expression e1 = add.getExpression1();
        Expression e2 = add.getExpression2();

        Assert.assertTrue(e1 instanceof Add);
        Assert.assertTrue(e2 instanceof PropertyName);
        Assert.assertEquals("x/y/z", ((PropertyName) e2).getPropertyName());
    }

    @Test(expected = CQLException.class)
    public final void testGetSyntaxError() throws CQLException {
        final String malformedExp = "12 / ] + 4";
        parseExpression(malformedExp);
    }

    @Test
    public void testCombinedOperations() throws Exception {
        final String combinedOperations = "4 / (1 + 3) + 5 * (10 - 8)";
        Expression exp = parseExpression(combinedOperations);
        assertExpectedExpressionsStructure(exp);

        // Get the Expression as CQL text
        String cql = CQL.toCQL(exp);

        // parse back again the text to Expression, to validate operations are respected.
        // Without the visitWithbrackets fix, the operators precedences get
        // screwed up so that the result was
        // 4 / 1 + 3 + 5 * 10 - 8
        exp = parseExpression(cql);
        assertExpectedExpressionsStructure(exp);
    }

    private void assertExpectedExpressionsStructure(Expression exp) {
        // This is the originating expression
        // [4 / (1 + 3)] + [5 * (10 - 8)]

        Assert.assertTrue(exp instanceof Add);
        Add add = (Add) exp;

        // [4 / (1 + 3)]
        Expression exp1 = add.getExpression1();
        Assert.assertTrue(exp1 instanceof Divide);
        Divide divide = (Divide) exp1;
        //      (1 + 3)
        Expression exp3 = divide.getExpression2();
        Assert.assertTrue(exp3 instanceof Add);

        //                 [5 * (10 - 8)]
        Expression exp2 = add.getExpression2();
        Assert.assertTrue(exp2 instanceof Multiply);
        Multiply multiply = (Multiply) exp2;

        //                      (10 - 8)
        Expression exp4 = multiply.getExpression2();
        Assert.assertTrue(exp4 instanceof Subtract);
    }
}

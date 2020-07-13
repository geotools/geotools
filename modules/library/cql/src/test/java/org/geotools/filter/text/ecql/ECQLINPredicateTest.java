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

package org.geotools.filter.text.ecql;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;

/**
 * Test for IN Predicate
 *
 * <pre>
 * &lt in predicate &gt         ::= &lt attribute-name &gt [  "NOT"  ]  "IN"  &lt in predicate value &gt
 * &lt in predicate value &gt   ::= "(" &lt in value list &gt ")"
 * &lt in value list &gt        ::= &lt expression &gt  {, &lt expression &gt}
 * </pre>
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
public class ECQLINPredicateTest {

    /** sample: length IN (4100001) */
    @Test
    public void oneIntegerLiteralInList() throws CQLException {

        List<String> intList = new LinkedList<String>();
        intList.add("4100001");
        String propName = "length";
        final String txtPredicate = makeInPredicate(propName, intList);

        Filter filter = ECQL.toFilter(txtPredicate);

        commonAssertForInPredicate(filter);

        assertFilterHasProperty((Or) filter, propName);
    }

    /** sample: length IN (4100001,4100002, 4100003 ) */
    @Test
    public void manyIntegerLiteralInList() throws CQLException {

        List<String> intList = new LinkedList<String>();
        String v1 = "4100001";
        intList.add(v1);
        String v2 = "4100002";
        intList.add(v2);
        String v3 = "4100003";
        intList.add(v3);
        String propName = "length";
        final String txtPredicate = makeInPredicate(propName, intList);

        Filter filter = ECQL.toFilter(txtPredicate);

        commonAssertForInPredicate(filter);

        assertFilterHasProperty((Or) filter, propName);
    }

    /** sample: length IN (4100001,4100002, 4100003 ) */
    @Test
    public void manyStringLiteralInList() throws CQLException {

        List<String> stringList = new LinkedList<String>();
        String v1 = "one";
        stringList.add(v1);
        String v2 = "two";
        stringList.add(v2);
        String v3 = "three";
        stringList.add(v3);
        String propName = "name";
        final String txtPredicate = makeInPredicateUsingString(propName, stringList);

        Filter filter = ECQL.toFilter(txtPredicate);

        commonAssertForInPredicate(filter);

        assertFilterHasProperty((Or) filter, propName);
    }

    /** Sample: length in ((1+2), 3-4, [5*6]) */
    @Test
    public void binaryExpression() throws CQLException {

        List<String> mathExptList = new LinkedList<String>();
        mathExptList.add("(1+2)");
        mathExptList.add("3-4");
        mathExptList.add("[5*6]");
        String propName = "length";
        final String txtPredicate = makeInPredicate(propName, mathExptList);

        Filter filter = ECQL.toFilter(txtPredicate);

        commonAssertForInPredicate(filter);

        assertFilterHasProperty((Or) filter, propName);
    }

    /** sample: huc_8 IN (abs(-1),area(the_geom) ) */
    @Test
    public void functions() throws CQLException {

        List<String> intList = new LinkedList<String>();
        intList.add("abs(-1)");
        intList.add("area(the_geom)");
        String propName = "length";
        final String txtPredicate = makeInPredicate(propName, intList);

        Filter filter = ECQL.toFilter(txtPredicate);

        commonAssertForInPredicate(filter);

        assertFilterHasProperty((Or) filter, propName);
    }

    /**
     * Test the IN Predicate with list of integers
     *
     * @param propName property name
     * @param exprList list of integer values
     */
    private void commonAssertForInPredicate(Filter filter) throws CQLException {

        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof Or);

        Or filterId = (Or) filter;
        List<Filter> filterList = filterId.getChildren();
        Assert.assertTrue(
                "one or more expressions in Or filter was expected", filterList.size() >= 1);
    }
    /**
     * This is successful if each PropertyIsEqual filter has on the left hand the same property
     * name.
     */
    private void assertFilterHasProperty(final Or filter, final String expectedName) {

        List<Filter> filterlist = filter.getChildren();

        for (Filter f : filterlist) {

            PropertyIsEqualTo eq = (PropertyIsEqualTo) f;
            Expression expr = eq.getExpression1();

            Assert.assertTrue(expr instanceof PropertyName);
            PropertyName actualName = (PropertyName) expr;

            Assert.assertEquals(expectedName, actualName.toString());
        }
    }

    /**
     * Makes an in predicate using the property name and the list of expressions
     *
     * @return an In Predicate
     */
    private String makeInPredicate(final String propName, final List<String> exprList) {

        StringBuffer txtExprList = new StringBuffer();
        Iterator<String> iterator = exprList.iterator();
        while (iterator.hasNext()) {
            String literal = iterator.next();
            txtExprList.append(literal);
            if (iterator.hasNext()) {
                txtExprList.append(",");
            }
        }
        String txtPredicate = propName + " IN (" + txtExprList + ")";
        return txtPredicate;
    }

    /**
     * Makes an in predicate using the property name and the list of expressions
     *
     * @return an In Predicate
     */
    private String makeInPredicateUsingString(final String propName, final List<String> exprList) {

        StringBuffer txtExprList = new StringBuffer();
        Iterator<String> iterator = exprList.iterator();
        while (iterator.hasNext()) {
            String literal = iterator.next();
            txtExprList.append("'").append(literal).append("'");
            if (iterator.hasNext()) {
                txtExprList.append(",");
            }
        }
        String txtPredicate = propName + " IN (" + txtExprList + ")";
        return txtPredicate;
    }
}

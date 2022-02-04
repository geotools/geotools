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

import java.util.LinkedList;
import java.util.List;
import org.geootols.filter.text.cql_2.CQL2;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQLINPredicateTest;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.Or;

/** Same as ECQL it seems */
public class CQL2InPredicateTest extends ECQLINPredicateTest {

    @Override
    protected Filter parseFilter(String txtPredicate) throws CQLException {
        return CQL2.toFilter(txtPredicate);
    }

    /** Overridden as base test used square brackets */
    @Override
    @Test
    public void binaryExpression() throws CQLException {

        List<String> mathExptList = new LinkedList<>();
        mathExptList.add("(1+2)");
        mathExptList.add("3-4");
        mathExptList.add("(5*6)");
        String propName = "length";
        final String txtPredicate = makeInPredicate(propName, mathExptList);

        Filter filter = parseFilter(txtPredicate);

        commonAssertForInPredicate(filter);

        assertFilterHasProperty((Or) filter, propName);
    }
}

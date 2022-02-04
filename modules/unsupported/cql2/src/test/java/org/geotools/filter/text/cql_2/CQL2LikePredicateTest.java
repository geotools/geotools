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
import org.geotools.filter.LikeFilterImpl;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQLLikePredicateTest;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.Not;

/**
 * Like works the same as in (E)CQL, but ILIKE does not exist, replaced by the CASEI function. TODO:
 * add support for it, in all comparisons)
 */
public class CQL2LikePredicateTest extends ECQLLikePredicateTest {

    @Override
    protected Filter parseFilter(String likeFilter) throws CQLException {
        return CQL2.toFilter(likeFilter);
    }

    // TODO: use CASEI
    @Override
    @Test(expected = CQLException.class)
    public void ilikePredicate() throws Exception {
        // iLike
        Filter resultFilter = parseFilter("ATTR1 ILIKE 'abc%'");

        Assert.assertNotNull("Filter expected", resultFilter);

        Assert.assertTrue(resultFilter instanceof LikeFilterImpl);
        LikeFilterImpl likeFilter = (LikeFilterImpl) resultFilter;

        Assert.assertFalse(likeFilter.isMatchingCase());
    }

    // TODO: use CASEI
    @Override
    @Test(expected = CQLException.class)
    public void notilikePredicate() throws Exception {

        // not iLike
        Filter resultFilter = parseFilter("not ATTR1 ILIKE 'abc%'");

        Not notFilter = (Not) resultFilter;

        LikeFilterImpl likeFilter = (LikeFilterImpl) notFilter.getFilter();

        Assert.assertFalse(likeFilter.isMatchingCase());
    }
}

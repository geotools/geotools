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

/**
 * No such a thing as an ID Predicate in CQL2, checks the grammar parts have been removed correctly
 */
public class CQL2IDPredicateTest {

    /** Sample: IN( 'states.1', 'states.2', 'states.3' </pre> */
    @Test(expected = CQLException.class)
    public void filterIdWithListOfIdValues() throws Exception {

        final String strId1 = "states.1";
        final String strId2 = "states.2";
        final String strId3 = "states.3";
        CQL2.toFilter("IN ('" + strId1 + "','" + strId2 + "', '" + strId3 + "')");
    }

    @Test(expected = CQLException.class)
    public void notFilterId() throws Exception {

        final String strId1 = "states.1";
        final String strId2 = "states.2";
        final String strId3 = "states.3";

        CQL2.toFilter("NOT IN ('" + strId1 + "','" + strId2 + "', '" + strId3 + "')");
    }

    @Test(expected = CQLException.class)
    public void idUsingIntegerValues() throws Exception {

        final String strId1 = "1";
        final String strId2 = "2";
        final String strId3 = "3";
        CQL2.toFilter("IN (" + strId1 + "," + strId2 + ", " + strId3 + ")");
    }

    @Test(expected = CQLException.class)
    public void IdUsingDateValues() throws Exception {

        final String date1 = "2010-01-01";
        final String date2 = "2010-02-02";
        final String date3 = "2010-03-03";
        CQL2.toFilter("IN ('" + date1 + "','" + date2 + "', '" + date3 + "')");
    }

    @Test(expected = CQLException.class)
    public void IdUsingTimeStamp() throws Exception {

        final String timeStamp1 = "2010-01-01 00:01:01";
        final String timeStamp2 = "2010-02-02 00:01:01";
        final String timeStamp3 = "2010-03-03 00:01:01";
        CQL2.toFilter("IN ('" + timeStamp1 + "','" + timeStamp2 + "', '" + timeStamp3 + "')");
    }

    @Test(expected = CQLException.class)
    public void filterIdSimple() throws Exception {
        CQL2.toFilter("IN states.1");
    }
}

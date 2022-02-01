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

import static org.junit.Assert.fail;

import org.geootols.filter.text.cql_2.CQL2;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cql2.CQLExistenceTest;
import org.geotools.filter.text.cql2.FilterCQLSample;
import org.junit.Test;
import org.opengis.filter.Filter;

/**
 * CQL2 has no "EXIST" operator (uh oh, so we cannot encode back a PropertyExists filter. See also
 * https://github.com/opengeospatial/ogcapi-features/issues/335
 */
public class CQL2ExistenceTest extends CQLExistenceTest {

    @Override
    @Test
    public void attributeDoesNotExist() throws CQLException {
        ensureFail(FilterCQLSample.ATTRIBUTE_NAME_DOES_NOT_EXIST);
    }

    @Override
    @Test
    public void attributeExist() throws Exception {
        ensureFail(FilterCQLSample.ATTRIBUTE_NAME_EXISTS);
    }

    @Override
    protected Filter parseFilter(String cql) throws CQLException {
        return CQL2.toFilter(cql);
    }

    private void ensureFail(String filter) {
        try {
            Filter parsed = parseFilter(filter);
            fail("Filter should not have been parsed: " + filter + " but got instead: " + parsed);
        } catch (CQLException e) {
            // expected failure
        }
    }
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.validate;

import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.ysld.parse.Util;
import org.yaml.snakeyaml.events.ScalarEvent;

/** Validator for Filters */
public class FilterValidator extends ScalarValidator {

    /** {@inheritDoc} */
    @Override
    protected String validate(String value, ScalarEvent evt, YsldValidateContext context) {
        try {
            ECQL.toFilter(Util.removeExpressionBrackets(value));
            return null;
        } catch (CQLException e) {
            return e.getSyntaxError();
        }
    }
}

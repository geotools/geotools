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

import org.yaml.snakeyaml.events.ScalarEvent;

/**
 * Validator for Scale
 *
 * <p>This Validator is stateful, do not re-use it.
 *
 * @author Kevin Smith, Boundless
 */
public class ScaleValidator extends RangeValidator<Double> {

    @Override
    Double parse(String s) throws IllegalArgumentException {
        return Double.parseDouble(s);
    }

    @Override
    protected void validateParsed(Double parsed, ScalarEvent evt, YsldValidateContext context) {
        if (parsed.compareTo(0d) < 0) {
            context.error("scale denominator must be non-negative", evt.getStartMark());
        } else if (parsed.isNaN()) {
            context.error("scale denominator is not a number", evt.getStartMark());
        }
    }
}

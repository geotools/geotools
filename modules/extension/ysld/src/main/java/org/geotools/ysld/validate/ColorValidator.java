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

import java.awt.*;
import org.geotools.ysld.parse.Util;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.yaml.snakeyaml.events.ScalarEvent;

/** Validator for Colors */
public class ColorValidator extends ScalarValidator {

    /** {@inheritDoc} */
    @Override
    protected String validate(String value, ScalarEvent evt, YsldValidateContext context) {
        try {
            Expression expr = Util.color(value, context.factory);
            if (expr instanceof Literal) {
                Color col = expr.evaluate(null, Color.class);
                if (col == null) {
                    return "Invalid color, must be one of: '#RRGGBB', rgb(r,g,b), or expression";
                }
            }
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}

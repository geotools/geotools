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

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.events.ScalarEvent;

/**
 * Top-level validator that checks the expression type and delegates to the appropriate validator
 */
public class RootValidator extends YsldValidateHandler {

    // Colour Expressions
    static Pattern COLOR = Pattern.compile("(?:fill|stroke)-color");

    // Name expressions
    static Pattern NAME =
            Pattern.compile("shape|font-(?:family|style|weight)|stroke(?:linecap|linejoin)");

    // General expressions
    static Pattern EXPRESSION =
            Pattern.compile(
                    "stroke-(?:width|opacity|linejoin|linecap|dashoffset)|offset|shape|gamma|geometry|label|font-(?:family|size|style|weight)|size|rotation|gap|initial-gap|radius|opacity");

    @Override
    public void scalar(ScalarEvent evt, YsldValidateContext context) {
        String key = evt.getValue();

        Matcher m = COLOR.matcher(key);
        if (m.matches()) {
            context.push(new ColorValidator());
        } else if ("filter".equals(key)) {
            context.push(new FilterValidator());
        } else if (NAME.matcher(key).matches()) {
            context.push(new ExpressionValidator());
        } else if (EXPRESSION.matcher(key).matches()) {
            context.push(new ExpressionValidator());
        } else if ("grid".equals(key)) {
            context.push(new GridValidator());
        } else if ("zoom".equals(key)) {
            context.push(new ZoomValidator());
        } else if ("scale".equals(key)) {
            context.push(new ScaleValidator());
        } else if ("anchor".equals(key) || "displacement".equals(key)) {
            context.push(
                    new TupleValidator(
                            Arrays.asList(new ExpressionValidator(), new ExpressionValidator())));
        } else if ("entries".equals(key)) {
            context.push(
                    new SequenceValidator(
                            new TupleValidator(
                                    Arrays.asList(
                                            new ColorValidator(), // Colour
                                            new ExpressionValidator(), // Opacity
                                            new ExpressionValidator(), // Quantity
                                            new ScalarValidator() { // Label (arbitrary string, not
                                                // an expression)

                                                @Override
                                                protected String validate(
                                                        String value,
                                                        ScalarEvent evt,
                                                        YsldValidateContext context) {
                                                    return null;
                                                }
                                            }))));
        } else if ("params".equals(key)) {
            context.push(new PermissiveValidator());
        }
    }
}

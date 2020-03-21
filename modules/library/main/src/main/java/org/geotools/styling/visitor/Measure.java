/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.visitor;

import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.measure.Units;
import org.geotools.util.Converters;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.NilExpression;
import si.uom.SI;
import systems.uom.common.USCustomary;

/**
 * Helper class that parses a measure with eventual local unit of measure and helps the {@link
 * RescalingMode} enumeration to perfom its scaling job
 *
 * @author Andrea Aime - GeoSolutions
 */
class Measure {

    static final FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    Expression expression;

    Double value;

    Unit<Length> uom;

    Unit<Length> defaultUnit;

    public Measure(String value, Unit<Length> defaultUnit) {
        setDefaultUnit(defaultUnit);
        processLiteralExpression(value, defaultUnit);
    }

    public Measure(Expression unscaled, Unit<Length> defaultUnit) {
        this.expression = unscaled;
        setDefaultUnit(defaultUnit);
        this.uom = defaultUnit;
        if (unscaled instanceof Literal) {
            processLiteralExpression((Literal) unscaled, defaultUnit);
        } else {
            // see if we can still optimize
            PropertyIsNull test = ff.isNull(unscaled);
            Filter simplified = (Filter) test.accept(new SimplifyingFilterVisitor(), null);
            if (simplified == Filter.INCLUDE) {
                // special case, the expression was nil to start with
                this.expression = NilExpression.NIL;
                this.uom = defaultUnit;
            } else if (simplified instanceof PropertyIsNull) {
                PropertyIsNull pin = (PropertyIsNull) simplified;
                Expression se = pin.getExpression();
                if (se instanceof Literal) {
                    processLiteralExpression((Literal) se, defaultUnit);
                } else {
                    this.expression = se;
                    this.uom = defaultUnit;
                }
            }
        }
    }

    private void setDefaultUnit(Unit<Length> defaultUnit) {
        if (defaultUnit == null) {
            this.defaultUnit = Units.PIXEL;
        } else {
            this.defaultUnit = defaultUnit;
        }
    }

    private void processLiteralExpression(Literal literal, Unit<Length> defaultUnit) {
        // check if we have a uom attached at the end of the expression
        String value = literal.evaluate(null, String.class);
        if (value == null) {
            // let it go without doing anything, it may be a ConstantExpression.NULL
            return;
        }
        processLiteralExpression(value, defaultUnit);
    }

    private void processLiteralExpression(String value, Unit<Length> defaultUnit) {
        Unit<Length> uom = defaultUnit;
        String unitless = value;

        if (value.endsWith("px")) {
            unitless = value.substring(0, value.length() - 2);
            uom = Units.PIXEL;
        } else if (value.endsWith("ft")) {
            unitless = value.substring(0, value.length() - 2);
            uom = USCustomary.FOOT;
        } else if (value.endsWith("m")) {
            unitless = value.substring(0, value.length() - 1);
            uom = SI.METRE;
        }
        Double measure = Converters.convert(unitless, Double.class);
        if (measure == null) {
            throw new IllegalArgumentException(
                    "Invalid measure '"
                            + value
                            + "', was expecting a number, eventually followed by px, m or ft");
        }
        this.expression = ff.literal(value);
        this.value = measure;
        this.uom = uom;
    }

    /** Returns true if the uom is set and is not pixel */
    boolean isRealWorldUnit() {
        return uom != null && uom != Units.PIXEL;
    }

    /** Returns true if the uom is pixel within a symbolizer whose default unit is also pixel */
    boolean isPixelInPixelDefault() {
        return (uom == null || uom == defaultUnit)
                && (defaultUnit == null || defaultUnit == Units.PIXEL);
    }

    /**
     * @return true, if the uom is a real world unit within a symbolizer whose default unit is
     *     pixel.
     */
    public boolean isRealWorldUnitInPixelDefault() {
        return isRealWorldUnit() && (defaultUnit == null || defaultUnit == Units.PIXEL);
    }
}

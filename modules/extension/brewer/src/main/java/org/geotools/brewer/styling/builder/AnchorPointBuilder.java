/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.brewer.styling.builder;

import org.geotools.styling.AnchorPoint;
import org.opengis.filter.expression.Expression;

/**
 * AnchorPoint allows you specify which part of a graphic indicates the location.
 *
 * As an example if your graphic is a pin the AnchorPoint will be the end of the pin.
 *
 * <pre>
 * <code>AnchorPointBuilder<?> b = new AnchorPointBuilder();
 * AnchorPoint anchor = b.x(0.5).y(0.9).build();
 * </code>
 * </pre
 *
 * @author Jody Garnett (LISAsoft)
 *
 *
 *
 *
 */
public class AnchorPointBuilder extends AbstractStyleBuilder<AnchorPoint> {
    double defaultX;

    double defaultY;

    private Expression x;

    private Expression y;

    public AnchorPointBuilder() {
        this(null, 0, 0);
    }

    public AnchorPointBuilder(AbstractStyleBuilder<?> parent, double defaultX, double defaultY) {
        super(parent);
        this.defaultX = defaultX;
        this.defaultY = defaultY;
        reset();
    }

    public AnchorPoint build() {
        if (unset) {
            return null;
        }
        AnchorPoint anchorPoint = sf.anchorPoint(x, y);
        if (parent == null) {
            reset();
        }
        return anchorPoint;
    }

    public AnchorPointBuilder x(Expression x) {
        unset = false;
        this.x = x;
        return this;
    }

    public AnchorPointBuilder x(double x) {
        return x(literal(x));
    }

    public AnchorPointBuilder x(String cqlExpression) {
        return x(cqlExpression(cqlExpression));
    }

    public AnchorPointBuilder y(Expression y) {
        unset = false;
        this.y = y;
        return this;
    }

    public AnchorPointBuilder y(double y) {
        return y(literal(y));
    }

    public AnchorPointBuilder y(String cqlExpression) {
        return y(cqlExpression(cqlExpression));
    }

    public AnchorPointBuilder reset() {
        x = literal(defaultX);
        y = literal(defaultY);
        unset = false;
        return this;
    }

    public AnchorPointBuilder reset(AnchorPoint anchorPoint) {
        if (anchorPoint == null) {
            return reset();
        }
        x = anchorPoint.getAnchorPointX();
        y = anchorPoint.getAnchorPointY();
        unset = false;
        return this;
    }

    public AnchorPointBuilder unset() {
        return (AnchorPointBuilder) super.unset();
    }

    public AnchorPointBuilder reset(org.opengis.style.AnchorPoint anchorPoint) {
        if (anchorPoint == null) {
            return unset();
        }
        x = anchorPoint.getAnchorPointX();
        y = anchorPoint.getAnchorPointY();
        unset = false;
        return this;
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().text().labelText("label").pointPlacement().anchor().init(this);
    }
}

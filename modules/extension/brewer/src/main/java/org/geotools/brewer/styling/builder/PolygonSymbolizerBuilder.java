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

import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.api.filter.expression.Expression;
import org.geotools.styling.PolygonSymbolizerImpl;

public class PolygonSymbolizerBuilder extends SymbolizerBuilder<PolygonSymbolizerImpl> {
    StrokeBuilder stroke = new StrokeBuilder(this).unset();

    FillBuilder fill = new FillBuilder(this).unset();

    Expression geometry = null;

    Unit<Length> uom;

    public PolygonSymbolizerBuilder() {
        this(null);
    }

    PolygonSymbolizerBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public PolygonSymbolizerBuilder geometry(Expression geometry) {
        this.geometry = geometry;
        return this;
    }

    public PolygonSymbolizerBuilder geometry(String cqlExpression) {
        return geometry(cqlExpression(cqlExpression));
    }

    public StrokeBuilder stroke() {
        unset = false;
        stroke.reset();
        return stroke;
    }

    public FillBuilder fill() {
        unset = false;
        fill.reset();
        return fill;
    }

    public PolygonSymbolizerBuilder uom(Unit<Length> uom) {
        unset = false;
        this.uom = uom;
        return this;
    }

    @Override
    public PolygonSymbolizerImpl build() {
        if (unset) {
            return null;
        }
        PolygonSymbolizerImpl ps = sf.createPolygonSymbolizer(stroke.build(), fill.build(), null);
        if (geometry != null) {
            ps.setGeometry(geometry);
        }
        if (uom != null) {
            ps.setUnitOfMeasure(uom);
        }
        ps.getOptions().putAll(options);
        if (parent == null) {
            reset();
        }
        return ps;
    }

    @Override
    public PolygonSymbolizerBuilder reset() {
        stroke.unset();
        fill.unset();
        options.clear();
        unset = false;
        return this;
    }

    public PolygonSymbolizerBuilder reset(org.geotools.api.style.PolygonSymbolizer symbolizer) {
        if (symbolizer == null) {
            return unset();
        }
        if (symbolizer instanceof PolygonSymbolizerImpl) {
            return reset((PolygonSymbolizerImpl) symbolizer);
        }
        stroke.reset(symbolizer.getStroke());
        fill.reset(symbolizer.getFill());
        uom = symbolizer.getUnitOfMeasure();
        geometry = property(symbolizer.getGeometryPropertyName());

        unset = false;
        return this;
    }

    @Override
    public PolygonSymbolizerBuilder reset(PolygonSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return unset();
        }
        stroke.reset(symbolizer.getStroke());
        fill.reset(symbolizer.getFill());
        uom = symbolizer.getUnitOfMeasure();
        geometry = symbolizer.getGeometry();

        unset = false;
        return this;
    }

    @Override
    public PolygonSymbolizerBuilder unset() {
        return (PolygonSymbolizerBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().polygon().init(this);
    }
}

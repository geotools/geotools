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
package org.geotools.styling.builder;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Stroke;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class LineSymbolizerBuilder extends SymbolizerBuilder<LineSymbolizer> {
    StrokeBuilder strokeBuilder = new StrokeBuilder(this);

    Expression geometry = null;

    Unit<Length> uom = null;

    public LineSymbolizerBuilder() {
        this(null);
    }

    LineSymbolizerBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public LineSymbolizerBuilder geometry(Expression geometry) {
        this.geometry = geometry;
        return this;
    }

    public LineSymbolizerBuilder geometry(String cqlExpression) {
        return geometry(cqlExpression(cqlExpression));
    }

    public StrokeBuilder stroke() {
        unset = false;
        return strokeBuilder;
    }

    public LineSymbolizerBuilder uom(Unit<Length> uom) {
        unset = false;
        this.uom = uom;
        return this;
    }

    public LineSymbolizer build() {
        if (unset) {
            return null; // builder was constructed but never used
        }
        Stroke stroke = strokeBuilder.build();
        if (stroke == null) {
            stroke = Stroke.DEFAULT;
        }
        LineSymbolizer ls = sf.createLineSymbolizer(stroke, null);
        if (geometry != null) {
            ls.setGeometry(geometry);
        }
        if (uom != null) {
            ls.setUnitOfMeasure(uom);
        }
        ls.getOptions().putAll(options);
        if (parent == null) {
            reset();
        }
        return ls;
    }

    public LineSymbolizerBuilder reset() {
        strokeBuilder.reset();
        geometry = null;
        unset = false;
        uom = null;
        return this;
    }

    public LineSymbolizerBuilder reset(LineSymbolizer original) {
        if (original == null) {
            return unset();
        }
        geometry = original.getGeometry();
        strokeBuilder.reset(original.getStroke());
        uom = original.getUnitOfMeasure();
        return this;
    }

    public LineSymbolizerBuilder reset(org.opengis.style.LineSymbolizer original) {
        if (original instanceof LineSymbolizer) {
            return reset((LineSymbolizer) original);
        }
        if (original == null) {
            return unset();
        }
        geometry = property(original.getGeometryPropertyName());
        strokeBuilder.reset(original.getStroke());
        uom = original.getUnitOfMeasure();
        return this;
    }

    public LineSymbolizerBuilder unset() {
        return (LineSymbolizerBuilder) super.unset();
    }

    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().line().init(this);
    }

}

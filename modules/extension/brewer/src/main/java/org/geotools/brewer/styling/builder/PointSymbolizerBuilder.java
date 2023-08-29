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
import org.geotools.api.style.PointSymbolizer;

public class PointSymbolizerBuilder extends SymbolizerBuilder<PointSymbolizer> {
    Expression geometry;

    GraphicBuilder graphic = (GraphicBuilder) new GraphicBuilder(this).unset();

    Unit<Length> uom = null;

    public PointSymbolizerBuilder() {
        this(null);
    }

    public PointSymbolizerBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public PointSymbolizerBuilder geometry(Expression geometry) {
        this.geometry = geometry;
        return this;
    }

    public PointSymbolizerBuilder geometry(String cqlExpression) {
        return geometry(cqlExpression(cqlExpression));
    }

    public GraphicBuilder graphic() {
        unset = false;
        return graphic;
    }

    public PointSymbolizerBuilder uom(Unit<Length> uom) {
        this.uom = uom;
        return this;
    }

    @Override
    public PointSymbolizer build() {
        if (unset) {
            return null;
        }
        PointSymbolizer ps = sf.createPointSymbolizer();
        ps.setGeometry(geometry);
        ps.setGraphic(graphic.build());
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
    public PointSymbolizerBuilder reset() {
        this.geometry = null;
        this.graphic.reset(); // TODO: See what the actual default is
        this.uom = null;
        unset = false;

        return this;
    }

    @Override
    public Builder<PointSymbolizer> reset(org.geotools.api.style.PointSymbolizer original) {
        if (original == null) {
            return unset();
        }
        this.geometry = property(original.getGeometryPropertyName());
        this.graphic.reset(original.getGraphic());
        this.uom = original.getUnitOfMeasure();
        unset = false;

        return this;
    }

    @Override
    public PointSymbolizerBuilder unset() {
        return (PointSymbolizerBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().point().reset(this.build());
    }
}

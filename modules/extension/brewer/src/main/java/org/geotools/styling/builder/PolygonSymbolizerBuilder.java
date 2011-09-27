package org.geotools.styling.builder;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.geotools.styling.PolygonSymbolizer;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class PolygonSymbolizerBuilder extends AbstractStyleBuilder<PolygonSymbolizer> {
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

    public PolygonSymbolizer build() {
        if (unset) {
            return null;
        }
        PolygonSymbolizer ps = sf.createPolygonSymbolizer(stroke.build(), fill.build(), null);
        if (geometry != null) {
            ps.setGeometry(geometry);
        }
        if (uom != null) {
            ps.setUnitOfMeasure(uom);
        }
        if (parent == null) {
            reset();
        }
        return ps;
    }

    public PolygonSymbolizerBuilder reset() {
        stroke.unset();
        fill.unset();
        unset = false;
        return this;
    }

    public PolygonSymbolizerBuilder reset(org.opengis.style.PolygonSymbolizer symbolizer) {
        if (symbolizer == null) {
            return unset();
        }
        if (symbolizer instanceof PolygonSymbolizer) {
            return reset((PolygonSymbolizer) symbolizer);
        }
        stroke.reset(symbolizer.getStroke());
        fill.reset(symbolizer.getFill());
        uom = symbolizer.getUnitOfMeasure();
        geometry = property(symbolizer.getGeometryPropertyName());

        unset = false;
        return this;
    }

    public PolygonSymbolizerBuilder reset(PolygonSymbolizer symbolizer) {
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

    public PolygonSymbolizerBuilder unset() {
        return (PolygonSymbolizerBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().polygon().init(this);
    }
}

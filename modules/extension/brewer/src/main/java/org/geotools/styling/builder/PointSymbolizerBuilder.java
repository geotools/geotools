package org.geotools.styling.builder;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.geotools.Builder;
import org.geotools.styling.PointSymbolizer;
import org.opengis.filter.expression.Expression;

public class PointSymbolizerBuilder extends AbstractStyleBuilder<PointSymbolizer> {
    Expression geometry;

    GraphicBuilder graphic = new GraphicBuilder(this).unset();

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
        if (parent == null) {
            reset();
        }
        return ps;
    }

    public PointSymbolizerBuilder reset() {
        this.geometry = null;
        this.graphic.reset(); // TODO: See what the actual default is
        this.uom = null;
        unset = false;

        return this;
    }

    public Builder<PointSymbolizer> reset(PointSymbolizer original) {
        if (original == null) {
            return unset();
        }
        this.geometry = original.getGeometry();
        this.graphic.reset(original.getGraphic());
        this.uom = original.getUnitOfMeasure();
        unset = false;

        return this;
    }

    public Builder<PointSymbolizer> reset(org.opengis.style.PointSymbolizer original) {
        if (original == null) {
            return unset();
        } else if (original instanceof PointSymbolizer) {
            return reset((PointSymbolizer) original);
        }
        this.geometry = property(original.getGeometryPropertyName());
        this.graphic.reset(original.getGraphic());
        this.uom = original.getUnitOfMeasure();
        unset = false;

        return this;
    }

    public PointSymbolizerBuilder unset() {
        return (PointSymbolizerBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().point().reset(this.build());
    }
}

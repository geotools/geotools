package org.geotools.styling.builder;

import java.awt.Color;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.filter.expression.ExpressionBuilder;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleFactory;
import org.opengis.filter.expression.Expression;

public class StrokeBuilder<P> implements Builder<Stroke> {
    P parent;
    
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    
    ChildExpressionBuilder<StrokeBuilder<P>> color = new ChildExpressionBuilder<StrokeBuilder<P>>(this);

    ChildExpressionBuilder<StrokeBuilder<P>> width = new ChildExpressionBuilder<StrokeBuilder<P>>(this);

    ChildExpressionBuilder<StrokeBuilder<P>> opacity = new ChildExpressionBuilder<StrokeBuilder<P>>(this);

    ChildExpressionBuilder<StrokeBuilder<P>> lineCap = new ChildExpressionBuilder<StrokeBuilder<P>>(this);

    ChildExpressionBuilder<StrokeBuilder<P>> lineJoin = new ChildExpressionBuilder<StrokeBuilder<P>>(this);

    float[] dashArray = null;

    ChildExpressionBuilder<StrokeBuilder<P>> dashOffset = new ChildExpressionBuilder<StrokeBuilder<P>>(this);

    GraphicBuilder<StrokeBuilder<P>> graphicFill = new GraphicBuilder<StrokeBuilder<P>>();

    GraphicBuilder<StrokeBuilder<P>> graphicStroke = new GraphicBuilder<StrokeBuilder<P>>();

    private boolean unset;

    public StrokeBuilder() {
        reset();
    }
    public StrokeBuilder(P parent) {
        this.parent = parent;
        reset();
    }
    public StrokeBuilder<P> unset() {
        reset();
        unset = true;
        return this;
    }

    /**
     * Reset stroke to default values.
     */
    public StrokeBuilder<P> reset() {
        color.reset(Stroke.DEFAULT.getColor());
        width.reset(Stroke.DEFAULT.getWidth());
        opacity.reset(Stroke.DEFAULT.getOpacity());
        lineCap.reset(Stroke.DEFAULT.getLineCap());
        lineJoin.reset(Stroke.DEFAULT.getLineJoin());
        dashArray = Stroke.DEFAULT.getDashArray();
        dashOffset.reset(Stroke.DEFAULT.getDashOffset());
        graphicFill.unset();
        graphicStroke.reset();
        unset = false;
        return this;
    }

    /**
     * Reset builder to provided original stroke.
     * 
     * @param stroke
     */
    public StrokeBuilder<P> reset(Stroke stroke) {
        color.reset(stroke.getColor());
        width.reset(stroke.getWidth());
        opacity.reset(stroke.getOpacity());
        lineCap.reset(stroke.getLineCap());
        lineJoin.reset(stroke.getLineJoin());
        dashArray = stroke.getDashArray();
        dashOffset.reset(stroke.getDashOffset());
        graphicFill.reset(stroke.getGraphicFill());
        graphicStroke.reset(stroke.getGraphicStroke());
        unset = false;
        return this;
    }

    public StrokeBuilder<P> color(Expression color) {
        this.color.reset(color);
        unset = false;
        return this;
    }
    public StrokeBuilder<P> color(Color color) {
        this.color.literal(color);
        unset = false;
        return this;
    }
    public StrokeBuilder<P> color(String color) {
        this.color.literal(color);
        unset = false;
        return this;
    }

    public ExpressionBuilder color() {
        return color;
    }

    public StrokeBuilder<P> width(Expression width) {
        this.width.reset(width);
        unset = false;
        return this;
    }
    
    public StrokeBuilder<P> width(int width) {
        this.width.literal( width );
        unset = false;
        return this;
    }
    
    public StrokeBuilder<P> width(double width) {
        this.width.literal( width );
        unset = false;
        return this;
    }

    public ChildExpressionBuilder<StrokeBuilder<P>> width() {
        return width;
    }

    public StrokeBuilder<P> opacity(Expression opacity) {
        this.opacity.reset(opacity);
        unset = false;
        return this;
    }
    public StrokeBuilder<P> opacity(double opacity) {
        this.opacity.literal(opacity);
        unset = false;
        return this;
    }
    public ExpressionBuilder opacity() {
        return opacity;
    }

    public StrokeBuilder<P> lineCap(Expression lineCap) {
        this.lineCap.reset(lineCap);
        unset = false;
        return this;
    }

    public ChildExpressionBuilder<StrokeBuilder<P>> lineCap() {
        return lineCap;
    }

    public StrokeBuilder<P> lineJoin(Expression lineJoin) {
        this.lineJoin.reset(lineJoin);
        unset = false;
        return this;
    }

    public ChildExpressionBuilder<StrokeBuilder<P>> lineJoin() {
        return lineJoin;
    }

    public StrokeBuilder<P> dashArray(float[] dashArray) {
        this.dashArray = dashArray;
        unset = false;
        return this;
    }

    public float[] dashArray() {
        return dashArray;
    }

    public StrokeBuilder<P> dashOffet(Expression dashOffet) {
        this.dashOffset.reset(dashOffet);
        unset = false;
        return this;
    }

    public StrokeBuilder<P> dashOffet(int offset) {
        this.dashOffset.literal( offset );
        unset = false;
        return this;
    }
    
    public StrokeBuilder<P> dashOffet(double offset) {
        this.dashOffset.literal( offset );
        unset = false;
        return this;
    }
    
    public ChildExpressionBuilder<StrokeBuilder<P>> dashOffset() {
        return dashOffset;
    }

    public GraphicBuilder<StrokeBuilder<P>> graphicStroke() {
        unset = false;
        return graphicStroke;
    }

    public GraphicBuilder<StrokeBuilder<P>> fillBuilder() {
        unset = false;
        return graphicFill;
    }

    public Stroke build() {
        if (unset) {
            return null;
        }
        Stroke stroke = sf.createStroke(color.build(), width.build(), opacity.build(), lineJoin
                .build(), lineCap.build(), dashArray, dashOffset.build(), graphicFill.build(),
                this.graphicStroke.build());
        reset();
        return stroke;
    }
}

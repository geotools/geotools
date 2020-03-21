/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2015, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.styling.TextSymbolizer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Displacement;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.Symbol;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.util.Converters;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.style.GraphicalSymbol;

/**
 * This is a style visitor that will produce a copy of the provided style rescaled by a provided
 * factor.
 *
 * <p>The provided scale will be use to modify all line widths, font sizes and so forth. We may need
 * to go the extra distance and play with the min/max scale on rules, and if there is any DPI
 * specific madness going on we are going to cry.
 *
 * <p>According to the specification we are supposed to use environmental variables to make our
 * styles render in a resolution independent manner. The current GeoTools environment variable
 * visitor only does processing for <b>mapscale</b> but does not have a dpi substitution. On the
 * plus side this visitor accepts a general Expression and you are free to use an environmental
 * variable expression in order to make sure a normal base style is tweaked in all the right spots.
 *
 * <p>
 *
 * @author Jody Garnett (Refractions Research)
 */
public class RescaleStyleVisitor extends DuplicatingStyleVisitor {

    /** This is the scale used as a multiplication factory for everything that has a size. */
    protected Expression scale;

    protected Unit<Length> defaultUnit;

    public RescaleStyleVisitor(double scale) {
        this(CommonFactoryFinder.getFilterFactory2(null), scale);
    }

    public RescaleStyleVisitor(Expression scale) {
        this(CommonFactoryFinder.getFilterFactory2(null), scale);
    }

    public RescaleStyleVisitor(FilterFactory2 filterFactory, double scale) {
        this(filterFactory, filterFactory.literal(scale));
    }

    public RescaleStyleVisitor(FilterFactory2 filterFactory, Expression scale) {
        super(CommonFactoryFinder.getStyleFactory(null), filterFactory);
        this.scale = scale;
    }
    /**
     * Used to rescale the provided expr.
     *
     * <p>We do optimize the case where the provided expression is a literal; no sense doing a
     * calculation each time if we don't have to.
     *
     * @return expr multiplied by the provided scale
     */
    protected Expression rescale(Expression expr) {
        if (expr == null) {
            return null;
        }
        if (expr == Expression.NIL) {
            return Expression.NIL;
        }

        Measure m = new Measure(expr, defaultUnit);
        return RescalingMode.KeepUnits.rescaleToExpression(scale, m);
    }

    /** Rescale a list of expressions, can handle null. */
    protected List<Expression> rescale(List<Expression> expressions) {
        if (expressions == null || expressions.isEmpty()) {
            return expressions;
        }
        List<Expression> rescaled = new ArrayList<>(expressions.size());
        for (Expression expression : expressions) {
            rescaled.add(rescale(expression));
        }
        return rescaled;
    }

    /** Rescale using listMultiply, if there is only one entry. */
    protected List<Expression> rescaleDashArray(List<Expression> expressions) {
        if (expressions == null || expressions.isEmpty()) {
            return expressions;
        }

        Expression rescaleToExpression = rescale(ff.literal(1));
        // How to test, if it is a measure with a unit or not?
        String data = ((Literal) rescaleToExpression).getValue().toString();
        boolean evaluate =
                rescaleToExpression instanceof Literal
                        && Character.isDigit(data.charAt(data.length() - 1));

        List<Expression> rescaled = new ArrayList<>(expressions.size());
        for (Expression expression : expressions) {
            Expression rescale = ff.function("listMultiply", rescaleToExpression, expression);
            if (expression instanceof Literal && evaluate) {
                rescaled.add(ff.literal(rescale.evaluate(null)));
            } else {
                rescaled.add(rescale);
            }
        }
        return rescaled;
    }

    /**
     * Increase stroke width.
     *
     * <p>Based on feedback we may need to change the dash array as well.
     *
     * <p>
     */
    public void visit(org.geotools.styling.Stroke stroke) {
        Stroke copy = sf.getDefaultStroke();
        copy.setColor(copy(stroke.getColor()));
        copy.setDashArray(rescaleDashArray(stroke.dashArray()));
        copy.setDashOffset(rescale(stroke.getDashOffset()));
        copy.setGraphicFill(copy(stroke.getGraphicFill()));
        copy.setGraphicStroke(copy(stroke.getGraphicStroke()));
        copy.setLineCap(copy(stroke.getLineCap()));
        copy.setLineJoin(copy(stroke.getLineJoin()));
        copy.setOpacity(copy(stroke.getOpacity()));
        copy.setWidth(rescale(stroke.getWidth()));
        pages.push(copy);
    }

    /** Make graphics (such as used with PointSymbolizer) bigger */
    public void visit(Graphic gr) {
        Graphic copy = null;

        Displacement displacementCopy = null;

        if (gr.getDisplacement() != null) {
            gr.getDisplacement().accept(this);
            displacementCopy = (Displacement) pages.pop();
        }

        AnchorPoint anchorCopy = null;
        if (gr.getAnchorPoint() != null) {
            gr.getAnchorPoint().accept(this);
            anchorCopy = (AnchorPoint) pages.pop();
        }

        List<GraphicalSymbol> symbols = gr.graphicalSymbols();
        List<GraphicalSymbol> symbolsCopy = new ArrayList<>(symbols.size());

        for (GraphicalSymbol symbol : symbols) {
            if (symbol instanceof Symbol) {
                symbolsCopy.add(copy((Symbol) symbol));
            } else {
                throw new RuntimeException("Don't know how to rescale " + symbol);
            }
        }

        Expression opacityCopy = copy(gr.getOpacity());
        Expression rotationCopy = copy(gr.getRotation());
        Expression sizeCopy = rescaleGraphicSize(gr);

        copy = sf.createDefaultGraphic();
        copy.setDisplacement(displacementCopy);
        copy.setAnchorPoint(anchorCopy);
        copy.graphicalSymbols().clear();
        copy.graphicalSymbols().addAll(symbolsCopy);
        copy.setOpacity(opacityCopy);
        copy.setRotation(rotationCopy);
        copy.setSize(sizeCopy);

        pages.push(copy);
    }

    protected Expression rescaleGraphicSize(Graphic gr) {
        return rescale(gr.getSize());
    }

    @Override
    public void visit(TextSymbolizer text) {
        this.defaultUnit = text.getUnitOfMeasure();
        try {
            super.visit(text);
            TextSymbolizer copy = (TextSymbolizer) pages.peek();

            // rescales fonts
            for (Font font : copy.fonts()) {
                if (font != null) {
                    font.setSize(rescale(font.getSize()));
                }
            }

            // rescales label placement
            LabelPlacement placement = copy.getLabelPlacement();
            if (placement instanceof PointPlacement) {
                // rescales point label placement
                PointPlacement pointPlacement = (PointPlacement) placement;
                Displacement disp = pointPlacement.getDisplacement();
                if (disp != null) {
                    disp.setDisplacementX(rescale(disp.getDisplacementX()));
                    disp.setDisplacementY(rescale(disp.getDisplacementY()));
                    pointPlacement.setDisplacement(disp);
                }
            } else if (placement instanceof LinePlacement) {
                // rescales line label placement
                LinePlacement linePlacement = (LinePlacement) placement;
                linePlacement.setGap(rescale(linePlacement.getGap()));
                linePlacement.setInitialGap(rescale(linePlacement.getInitialGap()));
                linePlacement.setPerpendicularOffset(
                        rescale(linePlacement.getPerpendicularOffset()));
            }
            copy.setLabelPlacement(placement);

            // rescale the halo
            if (copy.getHalo() != null) {
                copy.getHalo().setRadius(rescale(copy.getHalo().getRadius()));
            }

            // deal with the format options specified in pixels
            Map<String, String> options = copy.getOptions();
            rescaleOption(options, SPACE_AROUND_KEY, DEFAULT_SPACE_AROUND);
            rescaleOption(options, MAX_DISPLACEMENT_KEY, DEFAULT_MAX_DISPLACEMENT);
            rescaleOption(options, MIN_GROUP_DISTANCE_KEY, DEFAULT_MIN_GROUP_DISTANCE);
            rescaleOption(options, LABEL_REPEAT_KEY, DEFAULT_LABEL_REPEAT);
            rescaleOption(options, AUTO_WRAP_KEY, DEFAULT_AUTO_WRAP);
            rescaleArrayOption(options, GRAPHIC_MARGIN_KEY, 0);
        } finally {
            this.defaultUnit = null;
        }
    }

    @Override
    public void visit(Symbolizer sym) {
        this.defaultUnit = sym.getUnitOfMeasure();
        try {
            super.visit(sym);
        } finally {
            this.defaultUnit = null;
        }
    }

    @Override
    public void visit(PointSymbolizer sym) {
        this.defaultUnit = sym.getUnitOfMeasure();
        try {
            super.visit(sym);
        } finally {
            this.defaultUnit = null;
        }
    }

    @Override
    public void visit(LineSymbolizer sym) {
        this.defaultUnit = sym.getUnitOfMeasure();
        try {
            super.visit(sym);
            LineSymbolizer copy = (LineSymbolizer) pages.peek();
            copy.setPerpendicularOffset(rescale(copy.getPerpendicularOffset()));
        } finally {
            this.defaultUnit = null;
        }
    }

    @Override
    public void visit(PolygonSymbolizer sym) {
        this.defaultUnit = sym.getUnitOfMeasure();
        try {
            super.visit(sym);

            PolygonSymbolizer copy = (PolygonSymbolizer) pages.peek();
            rescaleArrayOption(copy.getOptions(), PolygonSymbolizer.GRAPHIC_MARGIN_KEY, 0);
        } finally {
            this.defaultUnit = null;
        }
    }

    @Override
    public void visit(RasterSymbolizer sym) {
        this.defaultUnit = sym.getUnitOfMeasure();
        try {
            super.visit(sym);
        } finally {
            this.defaultUnit = null;
        }
    }

    /** Rescales the specified vendor option */
    protected void rescaleOption(Map<String, String> options, String key, double defaultValue) {
        double scaleFactor = (double) scale.evaluate(null, Double.class);
        if (options.get(key) != null) {
            double rescaled = Converters.convert(options.get(key), Double.class) * scaleFactor;
            options.put(key, String.valueOf(rescaled));
        } else if (defaultValue != 0) {
            options.put(key, String.valueOf(defaultValue * scaleFactor));
        }
    };

    /** Rescales the specified vendor option */
    protected void rescaleOption(Map<String, String> options, String key, int defaultValue) {
        double scaleFactor = (double) scale.evaluate(null, Double.class);
        if (options.get(key) != null) {
            int rescaled =
                    (int)
                            Math.round(
                                    Converters.convert(options.get(key), Double.class)
                                            * scaleFactor);
            options.put(key, String.valueOf(rescaled));
        } else if (defaultValue != 0) {
            options.put(key, String.valueOf((int) Math.round(defaultValue * scaleFactor)));
        }
    };

    /** Rescales the specified vendor option */
    protected void rescaleArrayOption(Map<String, String> options, String key, int defaultValue) {
        double scaleFactor = (double) scale.evaluate(null, Double.class);
        if (options.get(key) != null) {
            String strValue = options.get(key);
            String[] splitted = strValue.split("\\s+");
            StringBuilder sb = new StringBuilder();
            for (String value : splitted) {
                double rescaled = (int) Math.round(Double.parseDouble(value) * scaleFactor);
                sb.append((int) rescaled).append(" ");
            }
            sb.setLength(sb.length() - 1);
            options.put(key, sb.toString());
        } else if (defaultValue != 0) {
            options.put(key, String.valueOf((int) Math.round(defaultValue * scaleFactor)));
        }
    };
}

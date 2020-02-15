/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.geotools.filter.ConstantExpression;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.renderer.style.DynamicSymbolFactoryFinder;
import org.geotools.renderer.style.ExpressionExtractor;
import org.geotools.renderer.style.ExternalGraphicFactory;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.Displacement;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.Halo;
import org.geotools.styling.ImageOutline;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.OverlapBehavior;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.ShadedRelief;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleVisitor;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.geotools.styling.UserLayer;
import org.opengis.feature.Feature;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.style.GraphicalSymbol;

/**
 * Parses a style or part of it and returns the size of the largest stroke and the biggest point
 * symbolizer whose width is specified with a literal expression.<br>
 * Also provides an indication whether the stroke width is accurate, or if a non literal width has
 * been found.
 */
public class MetaBufferEstimator extends FilterAttributeExtractor implements StyleVisitor {
    /** The logger for the rendering module. */
    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(MetaBufferEstimator.class);

    protected FilterAttributeExtractor attributeExtractor = new FilterAttributeExtractor();

    /** @uml.property name="estimateAccurate" */
    protected boolean estimateAccurate = true;

    /** @uml.property name="buffer" */
    protected int buffer = 0;

    /**
     * Feature used to evaluate sizes (optional, without it expressions based on attributes won't
     * work)
     */
    Feature sample;

    /** Builds an estimator suitable for styles without expressions */
    public MetaBufferEstimator() {}

    /**
     * Builds an estimator suitable for styles with expression, will evaluate against the provided
     * feature
     */
    public MetaBufferEstimator(Feature sample) {
        this.sample = sample;
    }

    /**
     * Should you reuse this extractor multiple time, calling this method will reset the buffer and
     * flags
     */
    public void reset() {
        estimateAccurate = true;
        buffer = 0;
    }

    /** @uml.property name="buffer" */
    public int getBuffer() {
        return buffer;
    }

    /** @uml.property name="estimateAccurate" */
    public boolean isEstimateAccurate() {
        return estimateAccurate;
    }

    public void visit(Style style) {
        style.featureTypeStyles().forEach(ft -> ft.accept(this));
    }

    public void visit(Rule rule) {
        Filter filter = rule.getFilter();

        if (filter != null) {
            filter.accept(this, null);
        }

        rule.symbolizers().forEach(s -> s.accept(this));
    }

    public void visit(FeatureTypeStyle fts) {
        for (Rule rule : fts.rules()) {
            rule.accept(this);
        }
    }

    public void visit(Fill fill) {
        // nothing to do here
    }

    /** @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Stroke) */
    public void visit(Stroke stroke) {
        try {
            Expression width = stroke.getWidth();
            if (!isNull(width)) {
                evaluateWidth(width);
            }
            if (stroke.getGraphicStroke() != null) {
                stroke.getGraphicStroke().accept(this);
            }
        } catch (ClassCastException e) {
            estimateAccurate = false;
            LOGGER.info("Could not parse stroke width, " + "it's a literal but not a Number...");
        }
    }

    protected boolean isNull(Expression exp) {
        return exp == null
                || exp instanceof NilExpression
                || (exp instanceof ConstantExpression
                        && ((ConstantExpression) exp).getValue() == null);
    }

    /** @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Symbolizer) */
    public void visit(Symbolizer sym) {
        if (sym instanceof PointSymbolizer) {
            visit((PointSymbolizer) sym);
        }

        if (sym instanceof LineSymbolizer) {
            visit((LineSymbolizer) sym);
        }

        if (sym instanceof PolygonSymbolizer) {
            visit((PolygonSymbolizer) sym);
        }

        if (sym instanceof TextSymbolizer) {
            visit((TextSymbolizer) sym);
        }

        if (sym instanceof RasterSymbolizer) {
            visit((RasterSymbolizer) sym);
        }
    }

    public void visit(RasterSymbolizer rs) {
        if (rs.getGeometryPropertyName() != null) {
            attributeNames.add(rs.getGeometryPropertyName());

            // FIXME
            // LiteRenderer2 trhwos an Exception:
            // Do not know how to deep copy
            // org.geotools.coverage.grid.GridCoverage2D
            // attributeNames.add("grid");
        }

        if (rs.getImageOutline() != null) {
            rs.getImageOutline().accept(this);
        }

        if (rs.getOpacity() != null) {
            rs.getOpacity().accept(this, null);
        }
    }

    /** @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.PointSymbolizer) */
    public void visit(PointSymbolizer ps) {
        if (ps.getGraphic() != null) {
            ps.getGraphic().accept(this);
        }
    }

    /** @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.LineSymbolizer) */
    public void visit(LineSymbolizer line) {
        if (line.getStroke() != null) {
            line.getStroke().accept(this);
        }
    }

    /** @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.PolygonSymbolizer) */
    public void visit(PolygonSymbolizer poly) {
        if (poly.getStroke() != null) {
            poly.getStroke().accept(this);
        }
    }

    /** @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.TextSymbolizer) */
    public void visit(TextSymbolizer text) {
        // while we cannot account for the label size, we should at least
        // account for its height, anchor point, and eventual offsets
        if (text.fonts() != null && !text.fonts().isEmpty()) {
            for (Font font : text.fonts()) {
                int textSize = getPositiveValue(font.getSize());
                int delta = -1;
                if (text.getLabelPlacement() instanceof PointPlacement) {
                    PointPlacement pp = (PointPlacement) text.getLabelPlacement();
                    Displacement pd = pp.getDisplacement();
                    if (pd != null) {
                        int dx = getPositiveValue(pd.getDisplacementX());
                        int dy = getPositiveValue(pd.getDisplacementY());
                        delta = Math.max(dx, dy);
                    }
                    AnchorPoint ap = pp.getAnchorPoint();
                    if (ap != null) {
                        double ax = Math.abs(getDouble(ap.getAnchorPointX()) - 0.5);
                        double ay = Math.abs(getDouble(ap.getAnchorPointY()) - 0.5);
                        int anchorDelta = (int) Math.ceil(Math.max(ax, ay) * textSize);
                        if (delta > 0) {
                            delta += anchorDelta;
                        } else {
                            delta = anchorDelta;
                        }
                    }
                }
                int total = -1;
                if (delta > 0) {
                    if (textSize > 0) {
                        total = delta + textSize;
                    } else {
                        total = delta;
                    }
                } else if (textSize > 0) {
                    total = textSize;
                }

                buffer = Math.max(buffer, total);
            }
        }

        // take into account label shields if any
        if (text instanceof TextSymbolizer2) {
            Graphic graphic = ((TextSymbolizer2) text).getGraphic();
            if (graphic != null) {
                graphic.accept(this);
            }
        }
    }

    /** @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Graphic) */
    public void visit(Graphic gr) {
        try {
            Expression grSize = gr.getSize();
            int imageSize = -1;
            boolean isSizeNull = isNull(grSize);
            boolean isSizeConstant = false;

            if (!isSizeNull) {
                isSizeConstant = isConstant(grSize);
                if (isSizeConstant) {
                    imageSize = (int) Math.ceil(grSize.evaluate(null, Double.class));
                } else {
                    estimateAccurate = false;
                    return;
                }
            }

            for (GraphicalSymbol gs : gr.graphicalSymbols()) {
                if (gs instanceof ExternalGraphic) {
                    ExternalGraphic eg = (ExternalGraphic) gs;
                    Icon icon = null;
                    if (eg.getInlineContent() != null) {
                        icon = eg.getInlineContent();
                    } else {
                        String location = eg.getLocation().toExternalForm();
                        // expand embedded cql expression
                        Expression expanded = ExpressionExtractor.extractCqlExpressions(location);
                        // if not a literal there is an attribute dependency
                        if (!(expanded instanceof Literal)) {
                            estimateAccurate = false;
                            return;
                        }

                        Iterator<ExternalGraphicFactory> it =
                                DynamicSymbolFactoryFinder.getExternalGraphicFactories();
                        while (it.hasNext() && icon == null) {
                            try {
                                ExternalGraphicFactory factory = it.next();
                                icon = factory.getIcon(null, expanded, eg.getFormat(), imageSize);
                            } catch (Exception e) {
                                LOGGER.log(
                                        Level.FINE,
                                        "Error occurred evaluating external graphic",
                                        e);
                            }
                        }
                    }
                    // evaluate the icon if found, if not SLD asks us to go to the next one
                    if (icon != null) {
                        int size = Math.max(icon.getIconHeight(), icon.getIconWidth());
                        if (size > buffer) {
                            buffer = size;
                        }
                        return;
                    }
                } else if (gs instanceof Mark) {
                    Mark mark = (Mark) gs;
                    int markSize;
                    if (isSizeConstant) {
                        markSize = imageSize;
                    } else {
                        markSize = SLDStyleFactory.DEFAULT_MARK_SIZE;
                    }
                    if (mark.getStroke() != null) {
                        int strokeWidth = getPositiveValue(mark.getStroke().getWidth());
                        if (strokeWidth < 0) {
                            estimateAccurate = false;
                        } else {
                            markSize += strokeWidth;
                        }
                    }

                    if (markSize > buffer) {
                        this.buffer = markSize;
                    }

                    return;
                }

                // if we got here we could not find a way to actually estimate the graphic size
                estimateAccurate = false;
            }
        } catch (ClassCastException e) {
            estimateAccurate = false;
            LOGGER.info("Could not parse graphic size, " + "it's a literal but not a Number...");
        } catch (Exception e) {
            estimateAccurate = false;
            LOGGER.log(
                    Level.INFO,
                    "Error occured during the graphic size estimation, "
                            + "meta buffer estimate cannot be performed",
                    e);
        }
    }

    protected void evaluateWidth(Expression width) {
        int value = getPositiveValue(width);
        if (value < 0) {
            estimateAccurate = false;
        } else if (value > buffer) {
            buffer = value;
        }
    }

    protected int getPositiveValue(Expression ex) {
        double value = getDouble(ex);
        if (value == -1) {
            return -1;
        } else {
            return (int) Math.ceil(value);
        }
    }

    protected double getDouble(Expression ex) {
        if (isConstant(ex) || sample != null) {
            Double result = ex.evaluate(sample, Double.class);
            if (result != null) {
                return result;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    protected boolean isConstant(Expression ex) {
        // quick common cases first
        if (ex instanceof Literal) {
            return true;
        } else if (ex instanceof PropertyName) {
            return false;
        }
        // ok, check for attribute dependencies and volatile functions then
        attributeExtractor.clear();
        ex.accept(attributeExtractor, null);
        return attributeExtractor.isConstantExpression();
    }

    /** @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Mark) */
    public void visit(Mark mark) {
        // nothing to do here
    }

    /** @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.ExternalGraphic) */
    public void visit(ExternalGraphic exgr) {
        // nothing to do
    }

    /** @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.PointPlacement) */
    public void visit(PointPlacement pp) {
        // nothing to do here
    }

    /** @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.AnchorPoint) */
    public void visit(AnchorPoint ap) {
        // nothing to do here
    }

    /** @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Displacement) */
    public void visit(Displacement dis) {
        // nothing to do here
    }

    /** @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.LinePlacement) */
    public void visit(LinePlacement lp) {
        // nothing to do here
    }

    /** @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Halo) */
    public void visit(Halo halo) {
        // nothing to do here
    }

    public void visit(StyledLayerDescriptor sld) {
        StyledLayer[] layers = sld.getStyledLayers();

        for (int i = 0; i < layers.length; i++) {
            if (layers[i] instanceof NamedLayer) {
                ((NamedLayer) layers[i]).accept(this);
            } else if (layers[i] instanceof UserLayer) {
                ((UserLayer) layers[i]).accept(this);
            }
        }
    }

    public void visit(NamedLayer layer) {
        Style[] styles = layer.getStyles();

        for (int i = 0; i < styles.length; i++) {
            styles[i].accept(this);
        }
    }

    public void visit(UserLayer layer) {
        Style[] styles = layer.getUserStyles();

        for (int i = 0; i < styles.length; i++) {
            styles[i].accept(this);
        }
    }

    public void visit(FeatureTypeConstraint ftc) {
        ftc.accept(this);
    }

    public void visit(ColorMap map) {
        // nothing to do here
    }

    public void visit(ColorMapEntry entry) {
        // nothing to do here
    }

    public void visit(ContrastEnhancement contrastEnhancement) {
        // nothing to do here
    }

    public void visit(ImageOutline outline) {
        outline.accept(this);
    }

    public void visit(ChannelSelection cs) {
        // nothing to do here
    }

    public void visit(OverlapBehavior ob) {
        // nothing to do here
    }

    public void visit(SelectedChannelType sct) {
        // nothing to do here
    }

    public void visit(ShadedRelief sr) {
        // nothing to do here
    }
}

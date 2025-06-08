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
import javax.swing.Icon;
import org.geotools.api.feature.Feature;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.NilExpression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.style.AnchorPoint;
import org.geotools.api.style.ChannelSelection;
import org.geotools.api.style.ColorMap;
import org.geotools.api.style.ColorMapEntry;
import org.geotools.api.style.ContrastEnhancement;
import org.geotools.api.style.Displacement;
import org.geotools.api.style.ExternalGraphic;
import org.geotools.api.style.FeatureTypeConstraint;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Font;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.Halo;
import org.geotools.api.style.ImageOutline;
import org.geotools.api.style.LinePlacement;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.Mark;
import org.geotools.api.style.NamedLayer;
import org.geotools.api.style.OverlapBehavior;
import org.geotools.api.style.PointPlacement;
import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.PolygonSymbolizer;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.Rule;
import org.geotools.api.style.SelectedChannelType;
import org.geotools.api.style.ShadedRelief;
import org.geotools.api.style.Stroke;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.StyledLayer;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.api.style.UserLayer;
import org.geotools.filter.ConstantExpression;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.renderer.style.DynamicSymbolFactoryFinder;
import org.geotools.renderer.style.ExpressionExtractor;
import org.geotools.renderer.style.ExternalGraphicFactory;
import org.geotools.renderer.style.SLDStyleFactory;

/**
 * Parses a style or part of it and returns the size of the largest stroke and the biggest point symbolizer whose width
 * is specified with a literal expression.<br>
 * Also provides an indication whether the stroke width is accurate, or if a non literal width has been found.
 */
public class MetaBufferEstimator extends FilterAttributeExtractor implements StyleVisitor {
    /** The logger for the rendering module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(MetaBufferEstimator.class);

    protected FilterAttributeExtractor attributeExtractor = new FilterAttributeExtractor();

    /** @uml.property name="estimateAccurate" */
    protected boolean estimateAccurate = true;

    /** @uml.property name="buffer" */
    protected int buffer = 0;

    /** Feature used to evaluate sizes (optional, without it expressions based on attributes won't work) */
    Feature sample;

    /** Builds an estimator suitable for styles without expressions */
    public MetaBufferEstimator() {}

    /** Builds an estimator suitable for styles with expression, will evaluate against the provided feature */
    public MetaBufferEstimator(Feature sample) {
        this.sample = sample;
    }

    /** Should you reuse this extractor multiple time, calling this method will reset the buffer and flags */
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

    @Override
    public void visit(Style style) {
        style.featureTypeStyles().forEach(ft -> ft.accept(this));
    }

    @Override
    public void visit(Rule rule) {
        Filter filter = rule.getFilter();

        if (filter != null) {
            filter.accept(this, null);
        }

        rule.symbolizers().forEach(s -> s.accept(this));
    }

    @Override
    public void visit(FeatureTypeStyle fts) {
        for (Rule rule : fts.rules()) {
            rule.accept(this);
        }
    }

    @Override
    public void visit(Fill fill) {
        // nothing to do here
    }

    /** @see StyleVisitor#visit(org.geotools.api.style.Stroke) */
    @Override
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
                || exp instanceof ConstantExpression && ((ConstantExpression) exp).getValue() == null;
    }

    /** @see StyleVisitor#visit(org.geotools.api.style.Symbolizer) */
    @Override
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

    @Override
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

    /** @see StyleVisitor#visit(org.geotools.api.style.PointSymbolizer) */
    @Override
    public void visit(PointSymbolizer ps) {
        if (ps.getGraphic() != null) {
            ps.getGraphic().accept(this);
        }
    }

    /** @see StyleVisitor#visit(org.geotools.api.style.LineSymbolizer) */
    @Override
    public void visit(LineSymbolizer line) {
        if (line.getStroke() != null) {
            line.getStroke().accept(this);
        }
    }

    /** @see StyleVisitor#visit(org.geotools.api.style.PolygonSymbolizer) */
    @Override
    public void visit(PolygonSymbolizer poly) {
        if (poly.getStroke() != null) {
            poly.getStroke().accept(this);
        }
    }

    /** @see StyleVisitor#visit(org.geotools.api.style.TextSymbolizer) */
    @Override
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
        Graphic graphic = text.getGraphic();
        if (graphic != null) {
            graphic.accept(this);
        }
    }

    /** @see StyleVisitor#visit(org.geotools.api.style.Graphic) */
    @Override
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

                        Iterator<ExternalGraphicFactory> it = DynamicSymbolFactoryFinder.getExternalGraphicFactories();
                        while (it.hasNext() && icon == null) {
                            try {
                                ExternalGraphicFactory factory = it.next();
                                icon = factory.getIcon(null, expanded, eg.getFormat(), imageSize);
                            } catch (Exception e) {
                                LOGGER.log(Level.FINE, "Error occurred evaluating external graphic", e);
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
                    "Error occured during the graphic size estimation, " + "meta buffer estimate cannot be performed",
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

    /** @see StyleVisitor#visit(org.geotools.api.style.Mark) */
    @Override
    public void visit(Mark mark) {
        // nothing to do here
    }

    /** @see StyleVisitor#visit(org.geotools.api.style.ExternalGraphic) */
    @Override
    public void visit(ExternalGraphic exgr) {
        // nothing to do
    }

    /** @see StyleVisitor#visit(org.geotools.api.style.PointPlacement) */
    @Override
    public void visit(PointPlacement pp) {
        // nothing to do here
    }

    /** @see StyleVisitor#visit(org.geotools.api.style.AnchorPoint) */
    @Override
    public void visit(AnchorPoint ap) {
        // nothing to do here
    }

    /** @see StyleVisitor#visit(org.geotools.api.style.Displacement) */
    @Override
    public void visit(Displacement dis) {
        // nothing to do here
    }

    /** @see StyleVisitor#visit(org.geotools.api.style.LinePlacement) */
    @Override
    public void visit(LinePlacement lp) {
        // nothing to do here
    }

    /** @see StyleVisitor#visit(org.geotools.api.style.Halo) */
    @Override
    public void visit(Halo halo) {
        // nothing to do here
    }

    @Override
    public void visit(StyledLayerDescriptor sld) {
        StyledLayer[] layers = sld.getStyledLayers();

        for (StyledLayer layer : layers) {
            if (layer instanceof NamedLayer) {
                ((NamedLayer) layer).accept(this);
            } else if (layer instanceof UserLayer) {
                ((UserLayer) layer).accept(this);
            }
        }
    }

    @Override
    public void visit(NamedLayer layer) {
        Style[] styles = layer.getStyles();

        for (Style style : styles) {
            style.accept(this);
        }
    }

    @Override
    public void visit(UserLayer layer) {
        Style[] styles = layer.getUserStyles();

        for (Style style : styles) {
            style.accept(this);
        }
    }

    @Override
    public void visit(FeatureTypeConstraint ftc) {
        ftc.accept(this);
    }

    @Override
    public void visit(ColorMap map) {
        // nothing to do here
    }

    @Override
    public void visit(ColorMapEntry entry) {
        // nothing to do here
    }

    @Override
    public void visit(ContrastEnhancement contrastEnhancement) {
        // nothing to do here
    }

    @Override
    public void visit(ImageOutline outline) {
        outline.accept(this);
    }

    @Override
    public void visit(ChannelSelection cs) {
        // nothing to do here
    }

    @Override
    public void visit(OverlapBehavior ob) {
        // nothing to do here
    }

    @Override
    public void visit(SelectedChannelType sct) {
        // nothing to do here
    }

    @Override
    public void visit(ShadedRelief sr) {
        // nothing to do here
    }
}

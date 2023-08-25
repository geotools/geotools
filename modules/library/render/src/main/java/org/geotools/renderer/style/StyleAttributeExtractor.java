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
package org.geotools.renderer.style;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Set;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.style.*;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.styling.Font;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.OverlapBehavior;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.TextSymbolizer;

/**
 * A simple visitor whose purpose is to extract the set of attributes used by a Style, that is,
 * those that the Style expects to find in order to work properly
 *
 * @author Andrea Aime - OpenGeo
 */
public class StyleAttributeExtractor extends FilterAttributeExtractor implements StyleVisitor {

    /**
     * Returns PropertyNames rather than strings (includes namespace info)
     *
     * @return an array of the attribute found so far during the visit
     */
    public Set<PropertyName> getAttributes() {
        return Collections.unmodifiableSet(propertyNames);
    }

    /** if the default geometry is used, this will be true. See GEOS-469 */
    boolean defaultGeometryUsed = false;

    /**
     * Symbolizer geometry is enabled by default, but there are relevant cases in which we don't
     * desire that
     */
    boolean symbolizerGeometriesVisitEnabled = true;

    public boolean isSymbolizerGeometriesVisitEnabled() {
        return symbolizerGeometriesVisitEnabled;
    }

    /** Enables/disables visit of the symbolizer geometry property (on by default) */
    public void setSymbolizerGeometriesVisitEnabled(boolean symbolizerGeometriesVisitEnabled) {
        this.symbolizerGeometriesVisitEnabled = symbolizerGeometriesVisitEnabled;
    }

    /**
     * reads the read-only-property. See GEOS-469
     *
     * @return true if any of the symbolizers visted use the default geometry.
     */
    public boolean getDefaultGeometryUsed() {
        return defaultGeometryUsed;
    }

    @Override
    public void visit(org.geotools.api.style.Style style) {
        style.featureTypeStyles().forEach(ft -> ft.accept(this));
    }

    @Override
    public void visit(org.geotools.api.style.Rule rule) {
        Filter filter = rule.getFilter();

        if (filter != null) {
            filter.accept(this, null);
        }

        rule.symbolizers().forEach(s -> s.accept(this));
    }

    @Override
    public void visit(org.geotools.api.style.FeatureTypeStyle fts) {
        for (org.geotools.api.style.Rule rule : fts.rules()) {
            rule.accept(this);
        }
    }

    @Override
    public void visit(org.geotools.api.style.Fill fill) {
        if (fill.getColor() != null) {
            fill.getColor().accept(this, null);
        }

        if (fill.getGraphicFill() != null) {
            fill.getGraphicFill().accept(this);
        }

        if (fill.getOpacity() != null) {
            fill.getOpacity().accept(this, null);
        }
    }

    /** @see StyleVisitor#visit(org.geotools.styling.Stroke) */
    @Override
    public void visit(org.geotools.api.style.Stroke stroke) {
        Stroke input = (Stroke) stroke;
        if (input.getColor() != null) {
            input.getColor().accept(this, null);
        }

        if (input.getDashOffset() != null) {
            input.getDashOffset().accept(this, null);
        }

        if (input.getGraphicFill() != null) {
            input.getGraphicFill().accept(this);
        }

        if (input.getGraphicStroke() != null) {
            input.getGraphicStroke().accept(this);
        }

        if (input.getLineCap() != null) {
            input.getLineCap().accept(this, null);
        }

        if (input.getLineJoin() != null) {
            input.getLineJoin().accept(this, null);
        }

        if (input.getOpacity() != null) {
            input.getOpacity().accept(this, null);
        }

        if (input.getWidth() != null) {
            input.getWidth().accept(this, null);
        }

        if (input.dashArray() != null) {
            for (Expression expression : input.dashArray()) {
                expression.accept(this, null);
            }
        }
    }

    /** @see StyleVisitor#visit(org.geotools.styling.Symbolizer) */
    @Override
    public void visit(org.geotools.api.style.Symbolizer sym) {
        if (sym instanceof PointSymbolizer) {
            visit((org.geotools.api.style.PointSymbolizer) sym);
        }

        if (sym instanceof LineSymbolizer) {
            visit((org.geotools.api.style.LineSymbolizer) sym);
        }

        if (sym instanceof PolygonSymbolizer) {
            visit((org.geotools.api.style.PolygonSymbolizer) sym);
        }

        if (sym instanceof TextSymbolizer) {
            visit((org.geotools.api.style.TextSymbolizer) sym);
        }

        if (sym instanceof RasterSymbolizer) {
            visit((org.geotools.api.style.RasterSymbolizer) sym);
        }
    }

    @Override
    public void visit(org.geotools.api.style.RasterSymbolizer rs) {
        RasterSymbolizer input = (RasterSymbolizer) rs;
        if (symbolizerGeometriesVisitEnabled) {
            if (input.getGeometry() != null) {
                input.getGeometry().accept(this, null);
            }
        }

        if (input.getImageOutline() != null) {
            input.getImageOutline().accept(this);
        }

        if (input.getOpacity() != null) {
            input.getOpacity().accept(this, null);
        }
    }

    @Override
    public void visit(org.geotools.api.style.PointSymbolizer ps) {
        PointSymbolizer input = (PointSymbolizer) ps;
        if (symbolizerGeometriesVisitEnabled) {
            if (input.getGeometry() != null) {
                input.getGeometry().accept(this, null);
            } else {
                this.defaultGeometryUsed = true; // they want the default geometry (see GEOS-469)
            }
        }

        if (input.getGraphic() != null) {
            input.getGraphic().accept(this);
        }
    }

    /** @see StyleVisitor#visit(org.geotools.styling.LineSymbolizer) */
    @Override
    public void visit(org.geotools.api.style.LineSymbolizer line) {
        LineSymbolizer input = (LineSymbolizer) line;
        if (symbolizerGeometriesVisitEnabled) {
            if (input.getGeometry() != null) {
                input.getGeometry().accept(this, null);
            } else {
                this.defaultGeometryUsed = true; // they want the default geometry (see GEOS-469)
            }
        }

        if (input.getPerpendicularOffset() != null) {
            input.getPerpendicularOffset().accept(this, null);
        }

        if (input.getStroke() != null) {
            input.getStroke().accept(this);
        }
    }

    @Override
    public void visit(org.geotools.api.style.PolygonSymbolizer poly) {
        PolygonSymbolizer input = (PolygonSymbolizer) poly;
        if (symbolizerGeometriesVisitEnabled) {
            if (input.getGeometry() != null) {
                input.getGeometry().accept(this, null);
            } else {
                this.defaultGeometryUsed = true; // they want the default geometry (see GEOS-469)
            }
        }

        if (input.getStroke() != null) {
            input.getStroke().accept(this);
        }

        if (input.getFill() != null) {
            input.getFill().accept(this);
        }
    }

    /** @see StyleVisitor#visit(org.geotools.styling.TextSymbolizer) */
    @Override
    public void visit(org.geotools.api.style.TextSymbolizer text) {
        TextSymbolizer input = (TextSymbolizer) text;
        if (symbolizerGeometriesVisitEnabled) {
            if (input.getGeometry() != null) {
                input.getGeometry().accept(this, null);
            } else {
                this.defaultGeometryUsed = true; // they want the default geometry (see GEOS-469)
            }
        }

        if (input.getGraphic() != null) {
            input.getGraphic().accept(this);
        }

        if (input.getFill() != null) {
            input.getFill().accept(this);
        }

        if (input.getHalo() != null) {
            input.getHalo().accept(this);
        }

        if (input.fonts() != null) {
            for (Font font : input.fonts()) {
                if (font.getFamily() != null) {
                    for (Expression list : font.getFamily()) {
                        list.accept(this, null);
                    }
                }
                if (font.getSize() != null) {
                    font.getSize().accept(this, null);
                }

                if (font.getStyle() != null) {
                    font.getStyle().accept(this, null);
                }

                if (font.getWeight() != null) {
                    font.getWeight().accept(this, null);
                }
            }
        }

        if (input.getHalo() != null) {
            input.getHalo().accept(this);
        }

        if (input.getLabel() != null) {
            input.getLabel().accept(this, null);
        }

        if (input.getLabelPlacement() != null) {
            input.getLabelPlacement().accept(this);
        }

        if (input.getPriority() != null) {
            input.getPriority().accept(this, null);
        }
    }

    /** @see StyleVisitor#visit(org.geotools.styling.Graphic) */
    @Override
    public void visit(org.geotools.api.style.Graphic gr) {
        for (GraphicalSymbol symbol : gr.graphicalSymbols()) {
            if (symbol instanceof Symbol) {
                ((Symbol) symbol).accept(this);
            } else {
                throw new RuntimeException("Don't know how to visit " + symbol);
            }
        }

        if (gr.getOpacity() != null) {
            gr.getOpacity().accept(this, null);
        }

        if (gr.getRotation() != null) {
            gr.getRotation().accept(this, null);
        }

        if (gr.getSize() != null) {
            gr.getSize().accept(this, null);
        }

        if (gr.getDisplacement() != null) gr.getDisplacement().accept(this);

        if (gr.getAnchorPoint() != null) gr.getAnchorPoint().accept(this);
    }

    /** @see StyleVisitor#visit(org.geotools.styling.Mark) */
    @Override
    public void visit(org.geotools.api.style.Mark mark) {
        if (mark.getFill() != null) {
            mark.getFill().accept(this);
        }

        if (mark.getStroke() != null) {
            mark.getStroke().accept(this);
        }

        if (mark.getWellKnownName() != null) {
            if (mark.getWellKnownName() instanceof Literal) {
                visitCqlExpression(mark.getWellKnownName().evaluate(null, String.class));
            } else {
                mark.getWellKnownName().accept(this, null);
            }
        }
    }

    /** Handles the special CQL expressions embedded in the style markers since the time */
    private void visitCqlExpression(String expression) {
        Expression parsed = ExpressionExtractor.extractCqlExpressions(expression);
        if (parsed != null) parsed.accept(this, null);
    }

    /** @see StyleVisitor#visit(org.geotools.styling.ExternalGraphic) */
    @Override
    public void visit(org.geotools.api.style.ExternalGraphic exgr) {
        // add dynamic support for ExternalGrapic format attribute
        visitCqlExpression(exgr.getFormat());

        try {
            if (exgr.getLocation() != null) visitCqlExpression(exgr.getLocation().toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "Errors while inspecting " + "the location of an external graphic", e);
        }
    }

    /** @see StyleVisitor#visit(org.geotools.styling.PointPlacement) */
    @Override
    public void visit(org.geotools.api.style.PointPlacement pp) {
        if (pp.getAnchorPoint() != null) {
            pp.getAnchorPoint().accept(this);
        }

        if (pp.getDisplacement() != null) {
            pp.getDisplacement().accept(this);
        }

        if (pp.getRotation() != null) {
            pp.getRotation().accept(this, null);
        }
    }

    /** @see StyleVisitor#visit(org.geotools.styling.AnchorPoint) */
    @Override
    public void visit(org.geotools.api.style.AnchorPoint ap) {
        if (ap.getAnchorPointX() != null) {
            ap.getAnchorPointX().accept(this, null);
        }

        if (ap.getAnchorPointY() != null) {
            ap.getAnchorPointY().accept(this, null);
        }
    }

    /** @see StyleVisitor#visit(org.geotools.styling.Displacement) */
    @Override
    public void visit(org.geotools.api.style.Displacement dis) {
        if (dis.getDisplacementX() != null) {
            dis.getDisplacementX().accept(this, null);
        }

        if (dis.getDisplacementY() != null) {
            dis.getDisplacementY().accept(this, null);
        }
    }

    /** @see StyleVisitor#visit(org.geotools.styling.LinePlacement) */
    @Override
    public void visit(org.geotools.api.style.LinePlacement lp) {
        if (lp.getPerpendicularOffset() != null) {
            lp.getPerpendicularOffset().accept(this, null);
        }
    }

    /** @see StyleVisitor#visit(org.geotools.styling.Halo) */
    @Override
    public void visit(org.geotools.api.style.Halo halo) {
        if (halo.getFill() != null) {
            halo.getFill().accept(this);
        }

        if (halo.getRadius() != null) {
            halo.getRadius().accept(this, null);
        }
    }

    @Override
    public void visit(org.geotools.api.style.StyledLayerDescriptor sld) {
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
    public void visit(org.geotools.api.style.NamedLayer layer) {
        org.geotools.styling.Style[] styles = (org.geotools.styling.Style[]) layer.getStyles();

        for (org.geotools.styling.Style style : styles) {
            style.accept(this);
        }
    }

    @Override
    public void visit(org.geotools.api.style.UserLayer layer) {
        org.geotools.styling.Style[] styles = (org.geotools.styling.Style[]) layer.getUserStyles();

        for (org.geotools.styling.Style style : styles) {
            style.accept(this);
        }
    }

    @Override
    public void visit(org.geotools.api.style.FeatureTypeConstraint ftc) {
        ftc.accept(this);
    }

    @Override
    public void visit(org.geotools.api.style.ColorMap map) {
        ColorMapEntry[] entries = map.getColorMapEntries();

        for (ColorMapEntry entry : entries) {
            entry.accept(this);
        }
    }

    @Override
    public void visit(org.geotools.api.style.ColorMapEntry entry) {
        entry.accept(this);
    }

    @Override
    public void visit(org.geotools.api.style.ContrastEnhancement contrastEnhancement) {
        contrastEnhancement.accept(this);
    }

    @Override
    public void visit(org.geotools.api.style.ImageOutline outline) {
        outline.getSymbolizer().accept(this);
    }

    @Override
    public void visit(org.geotools.api.style.ChannelSelection cs) {
        cs.accept(this);
    }

    @Override
    public void visit(org.geotools.api.style.OverlapBehavior ob) {
        OverlapBehavior.cast(ob).accept(this);
    }

    @Override
    public void visit(org.geotools.api.style.SelectedChannelType sct) {
        sct.accept(this);
    }

    @Override
    public void visit(org.geotools.api.style.ShadedRelief sr) {
        sr.accept(this);
    }
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import java.net.MalformedURLException;

import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.renderer.style.ExpressionExtractor;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Literal;


/**
 * A simple visitor whose purpose is to extract the set of attributes used by a
 * Style, that is, those that the Style expects to find in order to work
 * properly
 *
 * @author Andrea Aime - OpenGeo
 *
 * @source $URL$
 */
public class StyleAttributeExtractor extends FilterAttributeExtractor
    implements StyleVisitor {

    /**
     *   if the default geometry is used, this will be true.  See GEOS-469
     */
    boolean defaultGeometryUsed = false;

    /**
     * reads the read-only-property.
     * See GEOS-469
     *
     * @return true if any of the symbolizers visted use the default geometry.
     */
    public boolean getDefaultGeometryUsed()
    {
        return defaultGeometryUsed;
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Style)
     */
    public void visit(org.geotools.styling.Style style) {
        FeatureTypeStyle[] ftStyles = style.getFeatureTypeStyles();

        for (int i = 0; i < ftStyles.length; i++) {
            ftStyles[i].accept(this);
        }
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Rule)
     */
    public void visit(Rule rule) {
        Filter filter = rule.getFilter();

        if (filter != null) {
            filter.accept( this, null );
        }

        Symbolizer[] symbolizers = rule.getSymbolizers();

        if (symbolizers != null) {
            for (int i = 0; i < symbolizers.length; i++) {
                Symbolizer symbolizer = symbolizers[i];
                symbolizer.accept(this);
            }
        }

        Graphic[] legendGraphics = rule.getLegendGraphic();

        if (legendGraphics != null) {
        }
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.FeatureTypeStyle)
     */
    public void visit(FeatureTypeStyle fts) {
        Rule[] rules = fts.getRules();

        for (int i = 0; i < rules.length; i++) {
            Rule rule = rules[i];
            rule.accept(this);
        }
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Fill)
     */
    public void visit(Fill fill) {
        if (fill.getBackgroundColor() != null) {
            fill.getBackgroundColor().accept((ExpressionVisitor) this, null);
        }

        if (fill.getColor() != null) {
            fill.getColor().accept(this, null );
        }

        if (fill.getGraphicFill() != null) {
            fill.getGraphicFill().accept(this);
        }

        if (fill.getOpacity() != null) {
            fill.getOpacity().accept(this, null );
        }
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Stroke)
     */
    public void visit(Stroke stroke) {
        if (stroke.getColor() != null) {
            stroke.getColor().accept(this, null );
        }

        if (stroke.getDashOffset() != null) {
            stroke.getDashOffset().accept(this, null );
        }

        if (stroke.getGraphicFill() != null) {
            stroke.getGraphicFill().accept(this);
        }

        if (stroke.getGraphicStroke() != null) {
            stroke.getGraphicStroke().accept(this);
        }

        if (stroke.getLineCap() != null) {
            stroke.getLineCap().accept(this, null);
        }

        if (stroke.getLineJoin() != null) {
            stroke.getLineJoin().accept(this, null);
        }

        if (stroke.getOpacity() != null) {
            stroke.getOpacity().accept(this, null);
        }

        if (stroke.getWidth() != null) {
            stroke.getWidth().accept(this, null );
        }
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Symbolizer)
     */
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
        if (rs.getGeometry() != null) {
            rs.getGeometry().accept(this, null);
        }

        if (rs.getImageOutline() != null) {
            rs.getImageOutline().accept(this);
        }

        if (rs.getOpacity() != null) {
            rs.getOpacity().accept(this, null);
        }
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.PointSymbolizer)
     */
    public void visit(PointSymbolizer ps) {
        if (ps.getGeometry() != null) {
            ps.getGeometry().accept(this, null);
        }   else {
            this.defaultGeometryUsed = true; // they want the default geometry (see GEOS-469)
        }

        if (ps.getGraphic() != null) {
            ps.getGraphic().accept(this);
        }

    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.LineSymbolizer)
     */
    public void visit(LineSymbolizer line) {
        if (line.getGeometry() != null) {
            line.getGeometry().accept(this, null);
        } else {
            this.defaultGeometryUsed = true; // they want the default geometry (see GEOS-469)
        }

        if (line.getStroke() != null) {
            line.getStroke().accept(this);
        }
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.PolygonSymbolizer)
     */
    public void visit(PolygonSymbolizer poly) {
        if (poly.getGeometry() != null) {
            poly.getGeometry().accept(this, null);
        } else {
            this.defaultGeometryUsed = true; // they want the default geometry (see GEOS-469)
        }

        if (poly.getStroke() != null) {
            poly.getStroke().accept(this);
        }

        if (poly.getFill() != null) {
            poly.getFill().accept(this);
        }
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.TextSymbolizer)
     */
    public void visit(TextSymbolizer text) {
        if (text.getGeometry() != null) {
            text.getGeometry().accept(this, null);
        } else {
            this.defaultGeometryUsed = true; // they want the default geometry (see GEOS-469)
        }

        if (text instanceof TextSymbolizer2)
        {
            if ( ((TextSymbolizer2)text).getGraphic() !=null)
                ((TextSymbolizer2)text).getGraphic().accept(this);
        }

        if (text.getFill() != null) {
            text.getFill().accept(this);
        }

        if (text.getHalo() != null) {
            text.getHalo().accept(this);
        }

        if (text.getFonts() != null) {
            Font[] fonts = text.getFonts();

            for (int i = 0; i < fonts.length; i++) {
                Font font = fonts[i];

                if (font.getFontFamily() != null) {
                    font.getFontFamily().accept(this,null);
                }

                if (font.getFontSize() != null) {
                    font.getFontSize().accept(this,null);
                }

                if (font.getFontStyle() != null) {
                    font.getFontStyle().accept(this,null);
                }

                if (font.getFontWeight() != null) {
                    font.getFontWeight().accept(this,null);
                }
            }
        }

        if (text.getHalo() != null) {
            text.getHalo().accept(this);
        }

        if (text.getLabel() != null) {
            text.getLabel().accept(this,null);
        }

        if (text.getPlacement() != null) {
            text.getPlacement().accept(this);
        }

        if (text.getPriority() != null) {
            text.getPriority().accept(this,null);
        }
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Graphic)
     */
    public void visit(Graphic gr) {
        if (gr.getSymbols() != null) {
            Symbol[] symbols = gr.getSymbols();

            for (int i = 0; i < symbols.length; i++) {
                Symbol symbol = symbols[i];
                symbol.accept(this);
            }
        }

        if (gr.getOpacity() != null) {
            gr.getOpacity().accept(this,null);
        }

        if (gr.getRotation() != null) {
            gr.getRotation().accept(this,null);
        }

        if (gr.getSize() != null) {
            gr.getSize().accept(this,null);
        }
        
        if (gr.getDisplacement() != null)
            gr.getDisplacement().accept(this);
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Mark)
     */
    public void visit(Mark mark) {
        if (mark.getFill() != null) {
            mark.getFill().accept(this);
        }

        if (mark.getStroke() != null) {
            mark.getStroke().accept(this);
        }

        if (mark.getRotation() != null) {
            mark.getRotation().accept(this,null);
        }

        if (mark.getSize() != null) {
            mark.getSize().accept(this,null);
        }
        
        if(mark.getWellKnownName() != null) {
            if(mark.getWellKnownName() instanceof Literal) {
                visitCqlExpression(mark.getWellKnownName().evaluate(null, String.class));
            } else {
                mark.getWellKnownName().accept(this,null);
            }
        }
    }

    /**
     * Handles the special CQL expressions embedded in the style markers since
     * the time 
     * @param expression
     */
    private void visitCqlExpression(String expression) {
        Expression parsed = ExpressionExtractor.extractCqlExpressions(expression);
        if(parsed != null)
            parsed.accept(this, null);
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.ExternalGraphic)
     */
    public void visit(ExternalGraphic exgr) {
        // add dynamic support for ExternalGrapic format attribute
        visitCqlExpression(exgr.getFormat());

        try {
            if(exgr.getLocation() != null)
                visitCqlExpression(exgr.getLocation().toString());
        } catch(MalformedURLException e) {
            throw new RuntimeException("Errors while inspecting " +
            		"the location of an external graphic", e);
        }
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.PointPlacement)
     */
    public void visit(PointPlacement pp) {
        if (pp.getAnchorPoint() != null) {
            pp.getAnchorPoint().accept(this);
        }

        if (pp.getDisplacement() != null) {
            pp.getDisplacement().accept(this);
        }

        if (pp.getRotation() != null) {
            pp.getRotation().accept(this,null);
        }
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.AnchorPoint)
     */
    public void visit(AnchorPoint ap) {
        if (ap.getAnchorPointX() != null) {
            ap.getAnchorPointX().accept(this,null);
        }

        if (ap.getAnchorPointY() != null) {
            ap.getAnchorPointY().accept(this,null);
        }
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Displacement)
     */
    public void visit(Displacement dis) {
        if (dis.getDisplacementX() != null) {
            dis.getDisplacementX().accept(this,null);
        }

        if (dis.getDisplacementY() != null) {
            dis.getDisplacementY().accept(this,null);
        }
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.LinePlacement)
     */
    public void visit(LinePlacement lp) {
        if (lp.getPerpendicularOffset() != null) {
            lp.getPerpendicularOffset().accept(this,null);
        }
    }

    /**
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Halo)
     */
    public void visit(Halo halo) {
        if (halo.getFill() != null) {
            halo.getFill().accept(this);
        }

        if (halo.getRadius() != null) {
            halo.getRadius().accept(this,null);
        }
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
        org.geotools.styling.Style[] styles = layer.getStyles();

        for (int i = 0; i < styles.length; i++) {
            styles[i].accept(this);
        }
    }

    public void visit(UserLayer layer) {
        org.geotools.styling.Style[] styles = layer.getUserStyles();

        for (int i = 0; i < styles.length; i++) {
            styles[i].accept(this);
        }
    }

    public void visit(FeatureTypeConstraint ftc) {
        ftc.accept(this);
    }

    public void visit(ColorMap map) {
        ColorMapEntry[] entries = map.getColorMapEntries();

        for (int i = 0; i < entries.length; i++) {
            entries[i].accept(this);
        }
    }

    public void visit(ColorMapEntry entry) {
        entry.accept(this);
    }

    public void visit(ContrastEnhancement contrastEnhancement) {
        contrastEnhancement.accept(this);
        
    }

    public void visit(ImageOutline outline) {
        outline.getSymbolizer().accept(this);
        
    }

    public void visit(ChannelSelection cs) {
        cs.accept(this);
        
    }

    public void visit(OverlapBehavior ob) {
        ob.accept(this);
        
    }

    public void visit(SelectedChannelType sct) {
        sct.accept(this);
        
    }

    public void visit(ShadedRelief sr) {
        sr.accept(this);
        
    }
}

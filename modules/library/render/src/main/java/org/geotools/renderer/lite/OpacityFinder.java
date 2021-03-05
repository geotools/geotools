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
package org.geotools.renderer.lite;

import org.geotools.styling.AbstractStyleVisitor;
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
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.UserLayer;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * Searches for translucent symbolizers
 *
 * @author jones
 */
public class OpacityFinder extends AbstractStyleVisitor implements StyleVisitor {
    private Class<?>[] acceptableTypes;

    public boolean hasOpacity;

    public OpacityFinder(Class<?>[] acceptableTypes) {
        this.acceptableTypes = acceptableTypes;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Style)
     */
    @Override
    public void visit(Style style) {
        for (FeatureTypeStyle featureTypeStyle : style.featureTypeStyles()) {
            if (hasOpacity) {
                break;
            }

            featureTypeStyle.accept(this);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Rule)
     */
    @Override
    public void visit(Rule rule) {
        for (Symbolizer symbolizer : rule.symbolizers()) {
            if (hasOpacity) {
                break;
            }

            symbolizer.accept(this);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.FeatureTypeStyle)
     */
    @Override
    public void visit(FeatureTypeStyle fts) {
        for (Rule rule : fts.rules()) {
            if (hasOpacity) {
                break;
            }

            rule.accept(this);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Fill)
     */
    @Override
    public void visit(Fill fill) {
        checkOpacity(fill.getOpacity());
    }

    private void checkOpacity(Expression exp) {
        if (exp != null) {
            if (exp instanceof Literal) {
                Literal literal = (Literal) exp;
                Object obj = literal.getValue();
                float opacity;

                if (obj instanceof Integer) {
                    Integer i = (Integer) obj;
                    opacity = i.floatValue();
                } else if (obj instanceof Float) {
                    Float i = (Float) obj;
                    opacity = i.floatValue();
                } else if (obj instanceof Double) {
                    Double i = (Double) obj;
                    opacity = i.floatValue();
                } else if (obj instanceof Short) {
                    Short i = (Short) obj;
                    opacity = i.floatValue();
                } else if (obj instanceof Byte) {
                    Byte i = (Byte) obj;
                    opacity = i.floatValue();
                } else if (obj instanceof String) {
                    try {
                        Double value = Double.valueOf((String) obj);
                        opacity = value.floatValue();
                    } catch (NumberFormatException e) {
                        return;
                    }
                } else {
                    return;
                }
                if ((opacity > 0.01) && (opacity < 0.99)) {
                    this.hasOpacity = true;
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Stroke)
     */
    @Override
    public void visit(Stroke stroke) {
        checkOpacity(stroke.getOpacity());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Symbolizer)
     */
    @Override
    public void visit(Symbolizer sym) {
        if (sym instanceof PointSymbolizer) {
            PointSymbolizer ps = (PointSymbolizer) sym;
            ps.accept(this);
        }

        if (sym instanceof LineSymbolizer) {
            LineSymbolizer ps = (LineSymbolizer) sym;
            ps.accept(this);
        }

        if (sym instanceof PolygonSymbolizer) {
            PolygonSymbolizer ps = (PolygonSymbolizer) sym;
            ps.accept(this);
        }

        if (sym instanceof RasterSymbolizer) {
            RasterSymbolizer rs = (RasterSymbolizer) sym;
            rs.accept(this);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.PointSymbolizer)
     */
    @Override
    public void visit(PointSymbolizer ps) {
        if (isAcceptable(ps)) {
            ps.getGraphic().accept(this);
        }
    }

    private boolean isAcceptable(Symbolizer s) {
        for (Class<?> type : acceptableTypes) {
            if (type.isAssignableFrom(s.getClass())) {
                return true;
            }
        }

        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.LineSymbolizer)
     */
    @Override
    public void visit(LineSymbolizer line) {
        if (isAcceptable(line)) {
            if (line.getStroke() != null) line.getStroke().accept(this);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.PolygonSymbolizer)
     */
    @Override
    public void visit(PolygonSymbolizer poly) {
        if (isAcceptable(poly)) {
            if (poly.getStroke() != null) poly.getStroke().accept(this);
            if (poly.getFill() != null) poly.getFill().accept(this);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.TextSymbolizer)
     */
    @Override
    public void visit(TextSymbolizer text) {}

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.RasterSymbolizer)
     */
    @Override
    public void visit(RasterSymbolizer raster) {
        if (isAcceptable(raster)) {
            if (raster.getOpacity() != null) {
                checkOpacity(raster.getOpacity());
            }
            if (raster.getColorMap() != null) {
                raster.getColorMap().accept(this);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Graphic)
     */
    @Override
    public void visit(Graphic gr) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Mark)
     */
    @Override
    public void visit(Mark mark) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.ExternalGraphic)
     */
    @Override
    public void visit(ExternalGraphic exgr) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.PointPlacement)
     */
    @Override
    public void visit(PointPlacement pp) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.AnchorPoint)
     */
    @Override
    public void visit(AnchorPoint ap) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Displacement)
     */
    @Override
    public void visit(Displacement dis) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.LinePlacement)
     */
    @Override
    public void visit(LinePlacement lp) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Halo)
     */
    @Override
    public void visit(Halo halo) {
        // TODO Auto-generated method stub
    }

    /** @param args */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }

    @Override
    public void visit(StyledLayerDescriptor sld) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(NamedLayer layer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(UserLayer layer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(FeatureTypeConstraint ftc) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ColorMap cm) {
        for (ColorMapEntry cme : cm.getColorMapEntries()) {
            cme.accept(this);
        }
    }

    @Override
    public void visit(ColorMapEntry cme) {
        if (cme.getOpacity() != null) {
            checkOpacity(cme.getOpacity());
        }
    }

    @Override
    public void visit(ContrastEnhancement contrastEnhancement) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ImageOutline outline) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ChannelSelection cs) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(OverlapBehavior ob) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(SelectedChannelType sct) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ShadedRelief sr) {
        // TODO Auto-generated method stub

    }
}

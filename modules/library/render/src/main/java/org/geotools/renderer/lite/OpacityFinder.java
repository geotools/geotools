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

import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
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
import org.geotools.api.style.Graphic;
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
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.api.style.UserLayer;
import org.geotools.styling.AbstractStyleVisitor;

/**
 * Searches for translucent symbolizers
 *
 * @author jones
 */
public class OpacityFinder extends AbstractStyleVisitor implements StyleVisitor {
    private final Class<?>[] acceptableTypes;

    public boolean hasOpacity;

    public OpacityFinder(Class<?>[] acceptableTypes) {
        this.acceptableTypes = acceptableTypes;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Style)
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
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Rule)
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
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.FeatureTypeStyle)
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
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Fill)
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
                if (opacity > 0.01 && opacity < 0.99) {
                    this.hasOpacity = true;
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Stroke)
     */
    @Override
    public void visit(Stroke stroke) {
        checkOpacity(stroke.getOpacity());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Symbolizer)
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
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.PointSymbolizer)
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
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.LineSymbolizer)
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
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.PolygonSymbolizer)
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
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.TextSymbolizer)
     */
    @Override
    public void visit(TextSymbolizer text) {}

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.RasterSymbolizer)
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
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Graphic)
     */
    @Override
    public void visit(Graphic gr) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Mark)
     */
    @Override
    public void visit(Mark mark) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.ExternalGraphic)
     */
    @Override
    public void visit(ExternalGraphic exgr) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.PointPlacement)
     */
    @Override
    public void visit(PointPlacement pp) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.AnchorPoint)
     */
    @Override
    public void visit(AnchorPoint ap) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Displacement)
     */
    @Override
    public void visit(Displacement dis) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.LinePlacement)
     */
    @Override
    public void visit(LinePlacement lp) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.api.style.Halo)
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

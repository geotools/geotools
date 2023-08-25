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
import org.geotools.api.style.AbstractStyleVisitor;
import org.geotools.api.style.ColorMapEntry;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.Symbolizer;
import org.geotools.styling.*;

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
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Style)
     */
    @Override
    public void visit(org.geotools.api.style.Style style) {
        for (org.geotools.api.style.FeatureTypeStyle featureTypeStyle : style.featureTypeStyles()) {
            if (hasOpacity) {
                break;
            }

            featureTypeStyle.accept(this);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Rule)
     */
    @Override
    public void visit(org.geotools.api.style.Rule rule) {
        for (org.geotools.api.style.Symbolizer symbolizer : rule.symbolizers()) {
            if (hasOpacity) {
                break;
            }

            symbolizer.accept(this);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.FeatureTypeStyle)
     */
    @Override
    public void visit(org.geotools.api.style.FeatureTypeStyle fts) {
        for (org.geotools.api.style.Rule rule : fts.rules()) {
            if (hasOpacity) {
                break;
            }

            rule.accept(this);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Fill)
     */
    @Override
    public void visit(org.geotools.api.style.Fill fill) {
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
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Stroke)
     */
    @Override
    public void visit(org.geotools.api.style.Stroke stroke) {
        checkOpacity(stroke.getOpacity());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Symbolizer)
     */
    @Override
    public void visit(org.geotools.api.style.Symbolizer sym) {
        if (sym instanceof PointSymbolizerImpl) {
            PointSymbolizerImpl ps = (PointSymbolizerImpl) sym;
            ps.accept(this);
        }

        if (sym instanceof LineSymbolizerImpl) {
            LineSymbolizerImpl ps = (LineSymbolizerImpl) sym;
            ps.accept(this);
        }

        if (sym instanceof PolygonSymbolizerImpl) {
            PolygonSymbolizerImpl ps = (PolygonSymbolizerImpl) sym;
            ps.accept(this);
        }

        if (sym instanceof RasterSymbolizerImpl) {
            RasterSymbolizerImpl rs = (RasterSymbolizerImpl) sym;
            rs.accept(this);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.PointSymbolizer)
     */
    @Override
    public void visit(org.geotools.api.style.PointSymbolizer ps) {
        if (isAcceptable((Symbolizer) ps)) {
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
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.LineSymbolizer)
     */
    @Override
    public void visit(org.geotools.api.style.LineSymbolizer line) {
        if (isAcceptable((Symbolizer) line)) {
            if (line.getStroke() != null) line.getStroke().accept(this);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.PolygonSymbolizer)
     */
    @Override
    public void visit(org.geotools.api.style.PolygonSymbolizer poly) {
        if (isAcceptable((Symbolizer) poly)) {
            if (poly.getStroke() != null) poly.getStroke().accept(this);
            if (poly.getFill() != null) poly.getFill().accept(this);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.TextSymbolizer)
     */
    @Override
    public void visit(org.geotools.api.style.TextSymbolizer text) {}

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.RasterSymbolizer)
     */
    @Override
    public void visit(org.geotools.api.style.RasterSymbolizer raster) {
        if (isAcceptable((Symbolizer) raster)) {
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
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Graphic)
     */
    @Override
    public void visit(org.geotools.api.style.Graphic gr) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Mark)
     */
    @Override
    public void visit(org.geotools.api.style.Mark mark) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.ExternalGraphic)
     */
    @Override
    public void visit(org.geotools.api.style.ExternalGraphic exgr) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.PointPlacement)
     */
    @Override
    public void visit(org.geotools.api.style.PointPlacement pp) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.AnchorPoint)
     */
    @Override
    public void visit(org.geotools.api.style.AnchorPoint ap) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Displacement)
     */
    @Override
    public void visit(org.geotools.api.style.Displacement dis) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.LinePlacement)
     */
    @Override
    public void visit(org.geotools.api.style.LinePlacement lp) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.api.style.StyleVisitor#visit(org.geotools.styling.Halo)
     */
    @Override
    public void visit(org.geotools.api.style.Halo halo) {
        // TODO Auto-generated method stub
    }

    /** @param args */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }

    @Override
    public void visit(org.geotools.api.style.StyledLayerDescriptor sld) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(org.geotools.api.style.NamedLayer layer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(org.geotools.api.style.UserLayer layer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(org.geotools.api.style.FeatureTypeConstraint ftc) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(org.geotools.api.style.ColorMap cm) {
        for (ColorMapEntry cme : cm.getColorMapEntries()) {
            cme.accept(this);
        }
    }

    @Override
    public void visit(org.geotools.api.style.ColorMapEntry cme) {
        if (cme.getOpacity() != null) {
            checkOpacity(cme.getOpacity());
        }
    }

    @Override
    public void visit(org.geotools.api.style.ContrastEnhancement contrastEnhancement) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(org.geotools.api.style.ImageOutline outline) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(org.geotools.api.style.ChannelSelection cs) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(org.geotools.api.style.OverlapBehavior ob) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(org.geotools.api.style.SelectedChannelType sct) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(org.geotools.api.style.ShadedRelief sr) {
        // TODO Auto-generated method stub

    }
}

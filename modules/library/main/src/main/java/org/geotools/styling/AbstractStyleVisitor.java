/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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

import org.opengis.style.GraphicalSymbol;

/**
 * A basic implementation of the StyleVisitor interface.
 *
 * <p>This class implements the full StyleVisitor interface and visits all components of a style
 * object tree.
 */
public class AbstractStyleVisitor implements StyleVisitor {

    @Override
    public void visit(StyledLayerDescriptor sld) {
        for (StyledLayer sl : sld.getStyledLayers()) {
            if (sl instanceof UserLayer) {
                ((UserLayer) sl).accept(this);
            } else if (sl instanceof NamedLayer) {
                ((NamedLayer) sl).accept(this);
            }
        }
    }

    @Override
    public void visit(NamedLayer layer) {
        for (Style s : layer.getStyles()) {
            s.accept(this);
        }
        for (FeatureTypeConstraint ftc : layer.getLayerFeatureConstraints()) {
            ftc.accept(this);
        }
    }

    @Override
    public void visit(UserLayer layer) {
        for (Style s : layer.getUserStyles()) {
            s.accept(this);
        }
        for (FeatureTypeConstraint ftc : layer.getLayerFeatureConstraints()) {
            ftc.accept(this);
        }
    }

    @Override
    public void visit(FeatureTypeConstraint ftc) {}

    @Override
    public void visit(Style style) {
        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            fts.accept(this);
        }
    }

    @Override
    public void visit(Rule rule) {
        for (Symbolizer sym : rule.symbolizers()) {
            sym.accept(this);
        }
    }

    @Override
    public void visit(FeatureTypeStyle fts) {
        for (Rule r : fts.rules()) {
            r.accept(this);
        }
    }

    @Override
    public void visit(Fill fill) {
        if (fill.getGraphicFill() != null) {
            fill.getGraphicFill().accept(this);
        }
    }

    @Override
    public void visit(Stroke stroke) {
        if (stroke.getGraphicFill() != null) {
            stroke.getGraphicFill().accept(this);
        }
        if (stroke.getGraphicStroke() != null) {
            stroke.getGraphicStroke().accept(this);
        }
    }

    @Override
    public void visit(Symbolizer sym) {
        if (sym instanceof RasterSymbolizer) {
            visit((RasterSymbolizer) sym);
        } else if (sym instanceof LineSymbolizer) {
            visit((LineSymbolizer) sym);
        } else if (sym instanceof PolygonSymbolizer) {
            visit((PolygonSymbolizer) sym);
        } else if (sym instanceof PointSymbolizer) {
            visit((PointSymbolizer) sym);
        } else if (sym instanceof TextSymbolizer) {
            visit((TextSymbolizer) sym);
        } else {
            throw new RuntimeException("visit(Symbolizer) unsupported");
        }
    }

    @Override
    public void visit(PointSymbolizer ps) {
        if (ps.getDescription() != null) {
            ps.getDescription().accept(this);
        }
        if (ps.getGraphic() != null) {
            ps.getGraphic().accept(this);
        }
    }

    @Override
    public void visit(LineSymbolizer line) {
        if (line.getDescription() != null) {
            line.getDescription().accept(this);
        }
        if (line.getStroke() != null) {
            line.getStroke().accept(this);
        }
    }

    @Override
    public void visit(PolygonSymbolizer poly) {
        if (poly.getDescription() != null) {
            poly.getDescription().accept(this);
        }
        if (poly.getDisplacement() != null) {
            poly.getDisplacement().accept(this);
        }
        if (poly.getFill() != null) {
            poly.getFill().accept(this);
        }
        if (poly.getStroke() != null) {
            poly.getStroke().accept(this);
        }
    }

    @Override
    public void visit(TextSymbolizer text) {
        if (text.getDescription() != null) {
            text.getDescription().accept(this);
        }
        if (text.getFill() != null) {
            text.getFill().accept(this);
        }
        if (text.getHalo() != null) {
            text.getHalo().accept(this);
        }
        if (text.getLabelPlacement() != null) {
            text.getLabelPlacement().accept(this);
        }
    }

    @Override
    public void visit(RasterSymbolizer raster) {
        if (raster.getChannelSelection() != null) {
            raster.getChannelSelection().accept(this);
        }
        if (raster.getColorMap() != null) {
            raster.getColorMap().accept(this);
        }
        if (raster.getContrastEnhancement() != null) {
            raster.getContrastEnhancement().accept(this);
        }
        if (raster.getDescription() != null) {
            raster.getDescription().accept(this);
        }
        if (raster.getImageOutline() != null) {
            raster.getImageOutline().accept(this);
        }

        if (raster.getShadedRelief() != null) {
            raster.getShadedRelief().accept(this);
        }
    }

    @Override
    public void visit(Graphic gr) {
        if (gr.getAnchorPoint() != null) {
            gr.getAnchorPoint().accept(this);
        }
        if (gr.getDisplacement() != null) {
            gr.getDisplacement().accept(this);
        }
        for (GraphicalSymbol gs : gr.graphicalSymbols()) {
            if (gs instanceof Symbol) {
                ((Symbol) gs).accept(this);
            } else {
                throw new RuntimeException("Don't know how to visit " + gs);
            }
        }
    }

    @Override
    public void visit(Mark mark) {
        //        if (mark.getExternalMark() != null) {
        //            mark.getExternalMark().accept(this, null);
        //        }
        if (mark.getFill() != null) {
            mark.getFill().accept(this);
        }
        if (mark.getStroke() != null) {
            mark.getStroke().accept(this);
        }
    }

    @Override
    public void visit(ExternalGraphic exgr) {
        // for (org.opengis.style.ColorReplacement cr : exgr.getColorReplacements()) {
        // cr.accept(visitor, extraData)
        // }
    }

    @Override
    public void visit(PointPlacement pp) {
        if (pp.getAnchorPoint() != null) {
            pp.getAnchorPoint().accept(this);
        }
        if (pp.getDisplacement() != null) {
            pp.getDisplacement().accept(this);
        }
    }

    @Override
    public void visit(AnchorPoint ap) {}

    @Override
    public void visit(Displacement dis) {}

    @Override
    public void visit(LinePlacement lp) {}

    @Override
    public void visit(Halo halo) {
        if (halo.getFill() != null) {
            halo.getFill().accept(this);
        }
    }

    @Override
    public void visit(ColorMap colorMap) {
        for (ColorMapEntry cme : colorMap.getColorMapEntries()) {
            cme.accept(this);
        }
    }

    @Override
    public void visit(ColorMapEntry colorMapEntry) {}

    @Override
    public void visit(ContrastEnhancement contrastEnhancement) {}

    @Override
    public void visit(ImageOutline outline) {
        if (outline.getSymbolizer() != null) {
            outline.getSymbolizer().accept(this);
        }
    }

    @Override
    public void visit(ChannelSelection cs) {
        if (cs.getGrayChannel() != null) {
            cs.getGrayChannel().accept(this);
        }
        if (cs.getRGBChannels() != null) {
            for (SelectedChannelType ch : cs.getRGBChannels()) {
                if (ch != null) {
                    ch.accept(this);
                }
            }
        }
    }

    @Override
    public void visit(OverlapBehavior ob) {}

    @Override
    public void visit(SelectedChannelType sct) {
        if (sct.getContrastEnhancement() != null) {
            sct.getContrastEnhancement().accept(this);
        }
    }

    @Override
    public void visit(ShadedRelief sr) {}
}

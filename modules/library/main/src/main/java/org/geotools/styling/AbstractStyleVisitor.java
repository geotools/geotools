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

/**
 * A basic implementation of the StyleVisitor interface.
 * <p>
 * This class implements the full StyleVisitor interface and visits all components of a style object
 * tree. 
 * </p>
 */
public class AbstractStyleVisitor implements StyleVisitor {

    @Override
    public void visit(StyledLayerDescriptor sld) {
        for (StyledLayer sl : sld.getStyledLayers()) {
            if (sl instanceof UserLayer) {
                ((UserLayer)sl).accept(this);
            } else if (sl instanceof NamedLayer) {
                ((NamedLayer)sl).accept(this);
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
    public void visit(FeatureTypeConstraint ftc) {
    }

    @Override
    public void visit(Style style) {
        for (FeatureTypeStyle fts : style.getFeatureTypeStyles()) {
            fts.accept(this);
        }
    }

    @Override
    public void visit(Rule rule) {
        for (Symbolizer sym : rule.getSymbolizers()) {
            sym.accept(this);
        }
    }

    @Override
    public void visit(FeatureTypeStyle fts) {
        for (Rule r : fts.getRules()) {
            r.accept(this);
        }
    }

    @Override
    public void visit(Fill fill) {
        if (fill.getColor() != null) {
            //fill.getColor().accept(visitor, extraData)
        }
        if (fill.getGraphicFill() != null) {
            fill.getGraphicFill().accept(this);
        }
        if (fill.getOpacity() != null) {
            //fill.getOpacity().accept(visitor, extraData)
        }
    }

    @Override
    public void visit(Stroke stroke) {
        if (stroke.getColor() != null) {
            //stroke.getColor().accept(visitor, extraData)
        }
        if (stroke.getDashOffset() != null) {
            //stroke.getDashOffset().accept(visitor, extraData)
        }
        if (stroke.getGraphicFill() != null) {
            stroke.getGraphicFill().accept(this);
        }
        if (stroke.getGraphicStroke() != null) {
            stroke.getGraphicStroke().accept(this);
        }
        if (stroke.getLineCap() != null) {
            //stroke.getLineCap().accept(visitor, extraData)
        }
        if (stroke.getLineJoin() != null) {
            //stroke.getLineJoin().accept(visitor, extraData)
        }
        if (stroke.getOpacity() != null) {
            //stroke.getOpacity().accept(visitor, extraData)
        }
        if (stroke.getWidth() != null) {
            //stroke.getWidth().accept(visitor, extraData)
        }
    }

    @Override
    public void visit(Symbolizer sym) {
        if( sym instanceof RasterSymbolizer){
            visit( (RasterSymbolizer) sym );
        }
        else if( sym instanceof LineSymbolizer){
            visit( (LineSymbolizer) sym );
        }
        else if( sym instanceof PolygonSymbolizer){
            visit( (PolygonSymbolizer) sym );
        }
        else if( sym instanceof PointSymbolizer){
            visit( (PointSymbolizer) sym );
        }
        else if( sym instanceof TextSymbolizer){
            visit( (TextSymbolizer) sym );
        }
        else {
            throw new RuntimeException("visit(Symbolizer) unsupported");
        }
    }

    @Override
    public void visit(PointSymbolizer ps) {
        if (ps.getDescription() != null) {
            ps.getDescription().accept(this);
        }
        if (ps.getGeometry() != null) {
            //ps.getGeometry().accept(visitor, extraData)
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
        if (line.getGeometry() != null) {
            //line.getGeometry().accept(visitor, extraData)
        }
        if (line.getPerpendicularOffset() != null) {
            //line.getPerpendicularOffset().accept(visitor, extraData)
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
        if (poly.getGeometry() != null) {
            //poly.getGeometry().accept(visitor, extraData);
        }
        if (poly.getPerpendicularOffset() != null) {
            //poly.getPerpendicularOffset().accept(visitor, extraData)
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
        if (text.getFont() != null) {
            //text.getFont().accept(this, null);
        }
        if (text.getGeometry() != null) {
            //text.getGeometry().accept(visitor, extraData)
        }
        if (text.getHalo() != null) {
            text.getHalo().accept(this);
        }
        if (text.getLabel() != null) {
            //text.getLabel().accept(visitor, extraData)
        }
        if (text.getLabelPlacement() != null) {
            text.getLabelPlacement().accept(this);
        }
        if (text.getPriority() != null) {
            //text.getPriority().accept(visitor, extraData)
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
        if (raster.getGeometry() != null) {
            //raster.getGeometry().accept(visitor, extraData)
        }
        if (raster.getImageOutline() != null) {
            raster.getImageOutline().accept(this);
        }
        if (raster.getOpacity() != null) {
            //raster.getOpacity().accept(visitor, extraData)
        }
        if (raster.getOverlap() != null) {
            //raster.getOverlap().accept(visitor, extraData);
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
        for (ExternalGraphic eg : gr.getExternalGraphics()) {
            eg.accept(this);
        }

        if (gr.getGap() != null) {
            //gr.getGap().accept(visitor, extraData)
        }
        if (gr.getInitialGap() != null) {
            //gr.getInitialGap().accept(visitor, extraData)
        }
        for (Mark m : gr.getMarks()) {
            m.accept(this);
        }
        if (gr.getOpacity() != null) {
            //gr.getOpacity().accept(visitor, extraData)
        }
        if (gr.getRotation() != null) {
            //gr.getRotation().accept(visitor, extraData)
        }
        if (gr.getSize() != null) {
            //gr.getSize().accept(visitor, extraData)
        }

    }

    @Override
    public void visit(Mark mark) {
        if (mark.getExternalMark() != null) {
            //mark.getExternalMark().accept(this, extraData);
        }
        if (mark.getFill() != null) {
            mark.getFill().accept(this);
        }
        if (mark.getStroke() != null) {
            mark.getStroke().accept(this);
        }
        if (mark.getWellKnownName() != null) {
            //mark.getWellKnownName().accept(visitor, extraData)
        }
    }

    @Override
    public void visit(ExternalGraphic exgr) {
        for (org.opengis.style.ColorReplacement cr : exgr.getColorReplacements()) {
            //cr.accept(visitor, extraData)
        }
    }

    @Override
    public void visit(PointPlacement pp) {
        if (pp.getAnchorPoint() != null) {
            pp.getAnchorPoint().accept(this);
        }
        if (pp.getDisplacement() != null) {
            pp.getDisplacement().accept(this);
        }
        if (pp.getRotation() != null) {
            //pp.getRotation().accept(visitor, extraData)
        }
    }

    @Override
    public void visit(AnchorPoint ap) {
        if (ap.getAnchorPointX() != null) {
            //ap.getAnchorPointX().accept(visitor, extraData)
        }
        if (ap.getAnchorPointY() != null) {
            //ap.getAnchorPointY().accept(visitor, extraData)
        }
    }

    @Override
    public void visit(Displacement dis) {
        if (dis.getDisplacementX() != null) {
            //dis.getDisplacementX().accept(visitor, extraData)
        }
        if (dis.getDisplacementY() != null) {
            //dis.getDisplacementY().accept(visitor, extraData)
        }
    }

    @Override
    public void visit(LinePlacement lp) {
        if (lp.getGap() != null) {
            //lp.getGap().accept(visitor, extraData)
        }
        if (lp.getInitialGap() != null) {
            //lp.getInitialGap().accept(visitor, extraData)
        }
        if (lp.getPerpendicularOffset() != null) {
            //lp.getPerpendicularOffset().accept(visitor, extraData)
        }
    }

    @Override
    public void visit(Halo halo) {
        if (halo.getFill() != null) {
            halo.getFill().accept(this);
        }
        if (halo.getRadius() != null) {
            //halo.getRadius().accept(visitor, extraData)
        }
    }

    @Override
    public void visit(ColorMap colorMap) {
        for (ColorMapEntry cme : colorMap.getColorMapEntries()) {
            cme.accept(this);
        }
        if (colorMap.getFunction() != null) {
            //colorMap.getFunction().accept(visitor, extraData)
        }
    }

    @Override
    public void visit(ColorMapEntry colorMapEntry) {
        if (colorMapEntry.getColor() != null) {
            //colorMapEntry.getColor().accept(visitor, extraData)
        }
        if (colorMapEntry.getOpacity() != null) {
            //colorMapEntry.getOpacity().accept(visitor, extraData)
        }
        if (colorMapEntry.getQuantity() != null) {
            //colorMapEntry.getQuantity().accept(visitor, extraData)
        }
    }

    @Override
    public void visit(ContrastEnhancement contrastEnhancement) {
        if (contrastEnhancement.getGammaValue() != null) {
            //contrastEnhancement.getGammaValue().accept(visitor, extraData)
        }
    }

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
        for (SelectedChannelType ch : cs.getRGBChannels()) {
            ch.accept(this);
        }
    }

    @Override
    public void visit(OverlapBehavior ob) {
    }

    @Override
    public void visit(SelectedChannelType sct) {
        if (sct.getContrastEnhancement() != null) {
            sct.getContrastEnhancement().accept(this);    
        }
    }

    @Override
    public void visit(ShadedRelief sr) {
        if (sr.getReliefFactor() != null) {
            //sr.getReliefFactor().accept(visitor, extraData)
        }
    }

}

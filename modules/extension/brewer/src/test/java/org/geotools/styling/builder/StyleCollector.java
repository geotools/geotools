package org.geotools.styling.builder;

import java.util.ArrayList;
import java.util.List;

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
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.UserLayer;

/**
 * 
 *
 * @source $URL$
 */
public class StyleCollector implements StyleVisitor {

    List<FeatureTypeStyle> featureTypeStyles = new ArrayList<FeatureTypeStyle>();

    List<Rule> rules = new ArrayList<Rule>();

    List<Symbolizer> symbolizers = new ArrayList<Symbolizer>();
    
    List<Style> styles = new ArrayList<Style>();
    
    List<StyledLayer> layers = new ArrayList<StyledLayer>();

    @Override
    public void visit(StyledLayerDescriptor sld) {
        for (StyledLayer sl : sld.getStyledLayers()) {
            if (sl instanceof UserLayer) {
                ((UserLayer) sl).accept(this);
            } else if(sl instanceof NamedLayer) {
                ((NamedLayer) sl).accept(this);
            }
            
        }
    }

    @Override
    public void visit(NamedLayer layer) {
        layers.add(layer);
        for (Style style : layer.getStyles()) {
            style.accept(this);
        }
    }

    @Override
    public void visit(UserLayer layer) {
        layers.add(layer);
        for (Style style : layer.getUserStyles()) {
            style.accept(this);
        }
    }

    @Override
    public void visit(FeatureTypeConstraint ftc) {

    }

    @Override
    public void visit(Style style) {
        styles.add(style);
        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            featureTypeStyles.add(fts);
            fts.accept(this);
        }

    }

    @Override
    public void visit(Rule rule) {
        for (Symbolizer symbolizer : rule.symbolizers()) {
            symbolizers.add(symbolizer);
        }
    }

    @Override
    public void visit(FeatureTypeStyle fts) {
        for (Rule rule : fts.rules()) {
            rules.add(rule);
            rule.accept(this);
        }

    }

    @Override
    public void visit(Fill fill) {

    }

    @Override
    public void visit(Stroke stroke) {

    }

    @Override
    public void visit(Symbolizer sym) {

    }

    @Override
    public void visit(PointSymbolizer ps) {

    }

    @Override
    public void visit(LineSymbolizer line) {

    }

    @Override
    public void visit(PolygonSymbolizer poly) {

    }

    @Override
    public void visit(TextSymbolizer text) {

    }

    @Override
    public void visit(RasterSymbolizer raster) {

    }

    @Override
    public void visit(Graphic gr) {

    }

    @Override
    public void visit(Mark mark) {

    }

    @Override
    public void visit(ExternalGraphic exgr) {

    }

    @Override
    public void visit(PointPlacement pp) {

    }

    @Override
    public void visit(AnchorPoint ap) {

    }

    @Override
    public void visit(Displacement dis) {

    }

    @Override
    public void visit(LinePlacement lp) {

    }

    @Override
    public void visit(Halo halo) {

    }

    @Override
    public void visit(ColorMap colorMap) {

    }

    @Override
    public void visit(ColorMapEntry colorMapEntry) {

    }

    @Override
    public void visit(ContrastEnhancement contrastEnhancement) {

    }

    @Override
    public void visit(ImageOutline outline) {

    }

    @Override
    public void visit(ChannelSelection cs) {

    }

    @Override
    public void visit(OverlapBehavior ob) {

    }

    @Override
    public void visit(SelectedChannelType sct) {

    }

    @Override
    public void visit(ShadedRelief sr) {

    }

}

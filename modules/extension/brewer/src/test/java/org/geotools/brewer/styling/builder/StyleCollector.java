package org.geotools.brewer.styling.builder;

import java.util.ArrayList;
import java.util.List;
import org.geotools.api.style.ChannelSelection;
import org.geotools.api.style.ColorMap;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.Halo;
import org.geotools.api.style.ImageOutline;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.Mark;
import org.geotools.api.style.NamedLayer;
import org.geotools.api.style.PointPlacement;
import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.PolygonSymbolizer;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.Rule;
import org.geotools.api.style.SelectedChannelType;
import org.geotools.api.style.Stroke;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.StyledLayer;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.api.style.UserLayer;
import org.geotools.styling.AbstractStyleVisitor;

public class StyleCollector extends AbstractStyleVisitor implements StyleVisitor {

    List<FeatureTypeStyle> featureTypeStyles = new ArrayList<>();

    List<Rule> rules = new ArrayList<>();

    List<Symbolizer> symbolizers = new ArrayList<>();

    List<Style> styles = new ArrayList<>();

    List<StyledLayer> layers = new ArrayList<>();

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
    public void visit(Style style) {
        styles.add(style);
        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            featureTypeStyles.add(fts);
            fts.accept(this);
        }
    }

    @Override
    public void visit(Rule rule) {
        symbolizers.addAll(rule.symbolizers());
    }

    @Override
    public void visit(FeatureTypeStyle fts) {
        for (Rule rule : fts.rules()) {
            rules.add(rule);
            rule.accept(this);
        }
    }

    @Override
    public void visit(Fill fill) {}

    @Override
    public void visit(Stroke stroke) {}

    @Override
    public void visit(Symbolizer sym) {}

    @Override
    public void visit(PointSymbolizer ps) {}

    @Override
    public void visit(LineSymbolizer line) {}

    @Override
    public void visit(PolygonSymbolizer poly) {}

    @Override
    public void visit(TextSymbolizer text) {}

    @Override
    public void visit(RasterSymbolizer raster) {}

    @Override
    public void visit(Graphic gr) {}

    @Override
    public void visit(Mark mark) {}

    @Override
    public void visit(PointPlacement pp) {}

    @Override
    public void visit(Halo halo) {}

    @Override
    public void visit(ColorMap colorMap) {}

    @Override
    public void visit(ImageOutline outline) {}

    @Override
    public void visit(ChannelSelection cs) {}

    @Override
    public void visit(SelectedChannelType sct) {}
}

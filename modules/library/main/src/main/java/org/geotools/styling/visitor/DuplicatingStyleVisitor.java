/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006 - 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.visitor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import javax.swing.Icon;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.*;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.styling.*;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.Description;
import org.geotools.styling.Displacement;
import org.geotools.styling.Extent;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.ExternalMark;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.GraphicLegend;
import org.geotools.styling.Halo;
import org.geotools.styling.ImageOutline;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.OtherText;
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
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.UserLayer;

/**
 * Creates a deep copy of a Style, this class is *NOT THREAD SAFE*.
 *
 * <p>This class makes use of an internal stack to story the copied result, retrieve with a call to
 * getCopy() after visiting:
 *
 * <pre><code>
 * DuplicatingStyleVisitor copyStyle = new DuplicatingStyleVisitor();
 * rule.accepts( copyStyle );
 * Rule rule = (Rule) copyStyle.getCopy();
 * </code></pre>
 *
 * <p>This class is often used as a base for an anoymous subclass where a style transformation is
 * needed (such as removing PointSymbolizers or changing the scale - see RescaleStyleVisitor for an
 * example).
 *
 * @author Jesse Eichar
 */
public class DuplicatingStyleVisitor implements StyleVisitor {

    protected final StyleFactory sf;
    protected final FilterFactory ff;
    protected boolean STRICT;

    /** We are using aggregation here to contain our DuplicatingFilterVisitor. */
    protected final DuplicatingFilterVisitor copyFilter;

    /** This is our internal stack; used to maintain state as we copy sub elements. */
    protected Stack<Object> pages = new Stack<>();

    public DuplicatingStyleVisitor() {
        this(CommonFactoryFinder.getStyleFactory(null));
    }

    public DuplicatingStyleVisitor(StyleFactory styleFactory) {
        this(styleFactory, CommonFactoryFinder.getFilterFactory(null));
    }

    public DuplicatingStyleVisitor(StyleFactory styleFactory, FilterFactory filterFactory) {
        this(styleFactory, filterFactory, new DuplicatingFilterVisitor(filterFactory));
    }

    /**
     * Builds a new duplicating style visitor using a custom {@link DuplicatingStyleVisitor}
     *
     * @param styleFactory Creates new style objects during style duplication
     * @param filterFactory Creates new filters and expressions during style duplication
     * @param filterCloner Copies filters during style duplication
     */
    public DuplicatingStyleVisitor(
            StyleFactory styleFactory,
            FilterFactory filterFactory,
            DuplicatingFilterVisitor filterCloner) {
        this.copyFilter = filterCloner;
        this.sf = styleFactory;
        this.ff = filterFactory;
        this.STRICT = false;
    }

    /** True if we should enforce equality after a copy. */
    public void setStrict(boolean strict) {
        STRICT = strict;
    }

    public Object getCopy() {
        return pages.peek();
    }

    @Override
    public void visit(org.geotools.api.style.StyledLayerDescriptor sld) {

        StyledLayer[] layers = (StyledLayer[]) sld.getStyledLayers();
        StyledLayer[] layersCopy = new StyledLayer[layers.length];
        final int length = layers.length;
        for (int i = 0; i < length; i++) {
            if (layers[i] instanceof UserLayer) {
                ((UserLayer) layers[i]).accept(this);
                layersCopy[i] = (UserLayer) pages.pop();
            } else if (layers[i] instanceof NamedLayer) {
                ((NamedLayer) layers[i]).accept(this);
                layersCopy[i] = (NamedLayer) pages.pop();
            }
        }

        StyledLayerDescriptor copy = sf.createStyledLayerDescriptor();
        copy.setAbstract(sld.getAbstract());
        copy.setName(sld.getName());
        copy.setTitle(sld.getTitle());
        copy.setStyledLayers(layersCopy);

        if (STRICT && !copy.equals(sld)) {
            throw new IllegalStateException("Was unable to duplicate provided SLD:" + sld);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.NamedLayer layer) {

        Style[] style = (Style[]) layer.getStyles();
        Style[] styleCopy = new Style[style.length];
        int length = style.length;
        for (int i = 0; i < length; i++) {
            if (style[i] != null) {
                style[i].accept(this);
                styleCopy[i] = (Style) pages.pop();
            }
        }

        FeatureTypeConstraint[] lfc = (FeatureTypeConstraint[]) layer.getLayerFeatureConstraints();
        FeatureTypeConstraint[] lfcCopy = new FeatureTypeConstraint[lfc.length];

        length = lfc.length;
        for (int i = 0; i < length; i++) {
            if (lfc[i] != null) {
                lfc[i].accept(this);
                lfcCopy[i] = (FeatureTypeConstraint) pages.pop();
            }
        }

        org.geotools.styling.NamedLayer copy = (NamedLayer) sf.createNamedLayer();
        copy.setName(layer.getName());
        length = styleCopy.length;
        for (int i = 0; i < length; i++) {
            copy.addStyle(styleCopy[i]);
        }

        copy.setLayerFeatureConstraints(lfcCopy);
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.UserLayer layer) {

        Style[] style = (Style[]) layer.getUserStyles();
        int length = style.length;
        Style[] styleCopy = new Style[length];
        for (int i = 0; i < length; i++) {
            if (style[i] != null) {
                style[i].accept(this);
                styleCopy[i] = (Style) pages.pop();
            }
        }

        FeatureTypeConstraint[] lfc = (FeatureTypeConstraint[]) layer.getLayerFeatureConstraints();
        FeatureTypeConstraint[] lfcCopy = new FeatureTypeConstraint[lfc.length];

        length = lfc.length;
        for (int i = 0; i < length; i++) {
            if (lfc[i] != null) {
                lfc[i].accept(this);
                lfcCopy[i] = (FeatureTypeConstraint) pages.pop();
            }
        }

        org.geotools.styling.UserLayer copy = sf.createUserLayer();
        copy.setName(layer.getName());
        copy.setUserStyles(styleCopy);
        copy.setLayerFeatureConstraints(lfcCopy);

        if (STRICT && !copy.equals(layer)) {
            throw new IllegalStateException("Was unable to duplicate provided UserLayer:" + layer);
        }
        pages.push((org.geotools.api.style.NamedLayer) copy);
    }

    @Override
    public void visit(org.geotools.api.style.Style style) {

        List<FeatureTypeStyle> ftsCopy = new ArrayList<>();
        for (org.geotools.api.style.FeatureTypeStyle fts : style.featureTypeStyles()) {
            if (fts != null) {
                fts.accept(this);
                if (!pages.empty()) ftsCopy.add((FeatureTypeStyle) pages.pop());
            }
        }

        Style copy = sf.createStyle();
        copy.getDescription().setAbstract(style.getDescription().getAbstract());
        copy.setName(style.getName());
        copy.getDescription().setTitle(style.getDescription().getTitle());
        copy.featureTypeStyles().addAll(ftsCopy);
        copy.setBackground(copy((Fill) style.getBackground()));

        if (STRICT && !copy.equals(style)) {
            throw new IllegalStateException("Was unable to duplicate provided Style:" + style);
        }
        pages.push(copy);
    }

    public Object visit(org.geotools.api.style.Style style, Object extraData) {

        List<FeatureTypeStyle> ftsCopy = new ArrayList<>();
        for (org.geotools.api.style.FeatureTypeStyle fts : style.featureTypeStyles()) {
            if (fts != null) {
                fts.accept(this);
                if (!pages.empty()) ftsCopy.add((FeatureTypeStyle) pages.pop());
            }
        }
        Style input = (Style) style;
        Style copy = sf.createStyle();
        copy.getDescription().setAbstract(input.getDescription().getAbstract());
        copy.setName(input.getName());
        copy.getDescription().setTitle(input.getDescription().getTitle());
        copy.featureTypeStyles().addAll(ftsCopy);
        copy.setBackground(copy(input.getBackground()));

        if (STRICT && !copy.equals(style)) {
            throw new IllegalStateException("Was unable to duplicate provided Style:" + style);
        }
        pages.push(copy);
        return extraData;
    }

    @Override
    public void visit(org.geotools.api.style.Rule rule) {

        Filter filterCopy = null;

        if (rule.getFilter() != null) {
            Filter filter = rule.getFilter();
            filterCopy = copy(filter);
        }

        List<Symbolizer> symsCopy =
                rule.symbolizers().stream()
                        .map(s -> copy(s))
                        .filter(s -> s != null)
                        .collect(Collectors.toList());

        GraphicLegend legendCopy = (GraphicLegend) copy((Graphic) rule.getLegend());

        org.geotools.api.style.Description descCopy = rule.getDescription();
        descCopy = copy(descCopy);

        Rule copy = (Rule) sf.createRule();
        copy.symbolizers().addAll(symsCopy);
        copy.setDescription(descCopy);
        copy.setLegend(legendCopy);
        copy.setName(rule.getName());
        copy.setFilter(filterCopy);
        copy.setElseFilter(rule.isElseFilter());
        copy.setMaxScaleDenominator(rule.getMaxScaleDenominator());
        copy.setMinScaleDenominator(rule.getMinScaleDenominator());
        copy.getOptions().putAll(rule.getOptions());
        if (STRICT && !copy.equals(rule)) {
            throw new IllegalStateException("Was unable to duplicate provided Rule:" + rule);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.FeatureTypeStyle fts) {

        FeatureTypeStyle copy = new FeatureTypeStyle(fts);

        List<Rule> rulesCopy =
                fts.rules().stream()
                        .filter(r -> r != null)
                        .map(
                                r -> {
                                    r.accept(this);
                                    return !pages.isEmpty() ? (Rule) pages.pop() : null;
                                })
                        .filter(r -> r != null)
                        .collect(Collectors.toList());

        copy.rules().clear();
        copy.rules().addAll(rulesCopy);

        if (fts.getTransformation() != null) {
            copy.setTransformation(copy(fts.getTransformation()));
        }
        if (fts.getOnlineResource() != null) {
            copy.setOnlineResource(fts.getOnlineResource());
        }
        copy.getOptions().clear();
        copy.getOptions().putAll(fts.getOptions());

        if (STRICT && !copy.equals(fts)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided FeatureTypeStyle:" + fts);
        }

        pages.push(copy);
    }

    /**
     * Copy list of expressions.
     *
     * @return copy of expressions or null if list was null
     */
    protected List<Expression> copyExpressions(List<Expression> expressions) {
        if (expressions == null) return null;
        List<Expression> copy = new ArrayList<>(expressions.size());
        for (Expression expression : expressions) {
            copy.add(copy(expression));
        }
        return copy;
    }

    /**
     * Null safe expression copy.
     *
     * <p>This method will perform a null check, and save you some lines of code:
     *
     * <pre><code>
     * copy.setBackgroundColor( copyExpr( fill.getColor()) );
     * </code></pre>
     *
     * @return copy of expression or null if expression was null
     */
    protected Expression copy(Expression expression) {
        if (expression == null) return null;
        return (Expression) expression.accept(copyFilter, ff);
    }

    /** Null safe copy of filter. */
    protected Filter copy(Filter filter) {
        if (filter == null) return null;
        return (Filter) filter.accept(copyFilter, ff);
    }

    /**
     * Null safe graphic copy
     *
     * @return copy of graphic or null if not provided
     */
    protected Graphic copy(Graphic graphic) {
        if (graphic == null) return null;

        graphic.accept(this);
        return (Graphic) pages.pop();
    }

    /**
     * Null safe fill copy
     *
     * @return copy of graphic or null if not provided
     */
    protected Fill copy(Fill fill) {
        if (fill == null) return null;

        fill.accept(this);
        return (Fill) pages.pop();
    }

    /**
     * Null safe copy of float array.
     *
     * @return copy of array or null if not provided
     */
    protected float[] copy(float[] array) {
        if (array == null) return null;

        float[] copy = new float[array.length];
        System.arraycopy(array, 0, copy, 0, array.length);
        return copy;
    }

    /**
     * Null safe map copy, used for external graphic custom properties.
     *
     * @return copy of map
     */
    protected <K, V> Map<K, V> copy(Map<K, V> customProperties) {
        if (customProperties == null) return null;
        return new HashMap<>(customProperties);
    }

    /**
     * Null safe copy of stroke.
     *
     * @return copy of stroke if provided
     */
    protected org.geotools.api.style.Stroke copy(org.geotools.api.style.Stroke stroke) {
        if (stroke == null) return null;
        stroke.accept(this);
        return (org.geotools.api.style.Stroke) pages.pop();
    }

    /**
     * Null safe copy of shaded relief.
     *
     * @return copy of shaded or null if not provided
     */
    protected ShadedRelief copy(ShadedRelief shaded) {
        if (shaded == null) return null;
        Expression reliefFactor = copy(shaded.getReliefFactor());
        ShadedRelief copy = sf.createShadedRelief(reliefFactor);
        copy.setBrightnessOnly(shaded.isBrightnessOnly());

        return copy;
    }

    /**
     * Null safe copy of description
     *
     * @return copy of shaded or null if not provided
     */
    protected org.geotools.api.style.Description copy(org.geotools.api.style.Description desc) {
        if (desc == null) return null;
        Description copy = new Description(desc.getTitle(), desc.getAbstract());
        return copy;
    }

    protected ExternalGraphic copy(ExternalGraphic externalGraphic) {
        if (externalGraphic == null) return null;
        externalGraphic.accept(this);
        return (ExternalGraphic) pages.pop();
    }

    protected Mark copy(Mark mark) {
        if (mark == null) return null;
        mark.accept(this);
        return (Mark) pages.pop();
    }

    private ExternalMark copy(org.geotools.styling.ExternalMark other) {
        if (other == null) {
            return null;
        } else if (other.getInlineContent() != null) {
            return sf.externalMark(other.getInlineContent());
        } else {
            return sf.externalMark(
                    other.getOnlineResource(), other.getFormat(), other.getMarkIndex());
        }
    }

    protected ColorMapEntry copy(ColorMapEntry entry) {
        if (entry == null) return null;

        entry.accept(this);
        return (ColorMapEntry) pages.pop();
    }

    protected Symbolizer copy(org.geotools.api.style.Symbolizer symbolizer) {
        if (symbolizer == null) return null;

        symbolizer.accept(this);
        Symbolizer result = pages.empty() ? null : (Symbolizer) pages.pop();
        return result;
    }

    protected OverlapBehavior copy(org.geotools.api.style.OverlapBehavior ob) {

        return OverlapBehavior.cast(ob);
    }

    protected ContrastEnhancement copy(ContrastEnhancement contrast) {
        if (contrast == null) return null;

        ContrastEnhancement copy = sf.createContrastEnhancement();
        copy.setGammaValue(copy(contrast.getGammaValue()));
        copy.setMethod(contrast.getMethod());
        copy.setOptions(contrast.getOptions());

        return copy;
    }

    protected ColorMap copy(ColorMap colorMap) {
        if (colorMap == null) return null;

        colorMap.accept(this);
        return (ColorMap) pages.pop();
    }

    protected SelectedChannelType[] copy(SelectedChannelType... channels) {
        if (channels == null) return null;

        SelectedChannelType[] copy = new SelectedChannelType[channels.length];
        for (int i = 0; i < channels.length; i++) {
            copy[i] = copy(channels[i]);
        }
        return copy;
    }

    protected SelectedChannelType copy(SelectedChannelType selectedChannelType) {
        if (selectedChannelType == null) return null;
        ContrastEnhancement enhancement = copy(selectedChannelType.getContrastEnhancement());
        Expression name =
                (Expression) selectedChannelType.getChannelName().accept(copyFilter, null);
        SelectedChannelType copy = sf.createSelectedChannelType(name, enhancement);

        return copy;
    }

    protected ChannelSelection copy(ChannelSelection channelSelection) {
        if (channelSelection == null) return null;

        if (channelSelection.getGrayChannel() != null) {
            return (ChannelSelection)
                    sf.createChannelSelection(copy(channelSelection.getGrayChannel()));
        } else {
            return sf.createChannelSelection(copy(channelSelection.getRGBChannels()));
        }
    }

    /**
     * Null safe copy of font list.
     *
     * <p>Right now style visitor does not let us visit fonts!
     *
     * @return copy of provided fonts
     */
    protected List<Font> copyFonts(List<Font> fonts) {
        if (fonts == null) {
            return null;
        }
        List<Font> copy = new ArrayList<>(fonts.size());
        for (Font font : fonts) {
            copy.add(copy(font));
        }
        return copy;
    }

    /** Null safe copy of a single font */
    protected Font copy(Font font) {
        if (font == null) return font;

        List<Expression> fontFamily = copyExpressions(font.getFamily());
        Expression fontStyle = copy(font.getStyle());
        Expression fontWeight = copy(font.getWeight());
        Expression fontSize = copy(font.getSize());
        Font copy = sf.font(fontFamily, fontStyle, fontWeight, fontSize);
        return copy;
    }

    /**
     * Null safe copy of halo.
     *
     * @return copy of halo if provided
     */
    protected Halo copy(Halo halo) {
        if (halo == null) return null;
        halo.accept(this);
        return (Halo) pages.pop();
    }

    /**
     * Null safe copy of displacement.
     *
     * @return copy of displacement if provided
     */
    protected Displacement copy(Displacement displacement) {
        if (displacement == null) return null;
        displacement.accept(this);
        return (Displacement) pages.pop();
    }

    protected LabelPlacement copy(org.geotools.api.style.LabelPlacement placement) {
        if (placement == null) return null;
        placement.accept(this);
        return (LabelPlacement) pages.pop();
    }

    protected Symbol copy(Symbol symbol) {
        if (symbol == null) return null;
        symbol.accept(this);
        return (Symbol) pages.pop();
    }

    /**
     * Null safe copy of anchor point.
     *
     * @return copy of anchor point if provided
     */
    protected AnchorPoint copy(AnchorPoint anchorPoint) {
        if (anchorPoint == null) return null;
        anchorPoint.accept(this);
        return (AnchorPoint) pages.pop();
    }

    @Override
    public void visit(org.geotools.api.style.Fill fill) {
        Fill input = (Fill) fill;
        Fill copy = sf.getDefaultFill();
        copy.setColor(copy(input.getColor()));
        copy.setGraphicFill(copy(input.getGraphicFill()));
        copy.setOpacity(copy(input.getOpacity()));

        if (STRICT && !copy.equals(input)) {
            throw new IllegalStateException("Was unable to duplicate provided Fill:" + fill);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.Stroke stroke) {
        Stroke input = (Stroke) stroke;
        Stroke copy = sf.getDefaultStroke();
        copy.setColor(copy(input.getColor()));
        copy.setDashArray(copyExpressions(input.dashArray()));
        copy.setDashOffset(copy(input.getDashOffset()));
        copy.setGraphicFill(copy(input.getGraphicFill()));
        copy.setGraphicStroke(copy(input.getGraphicStroke()));
        copy.setLineCap(copy(input.getLineCap()));
        copy.setLineJoin(copy(input.getLineJoin()));
        copy.setOpacity(copy(input.getOpacity()));
        copy.setWidth(copy(input.getWidth()));

        if (STRICT && !copy.equals(input)) {
            throw new IllegalStateException("Was unable to duplicate provided Stroke:" + stroke);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.Symbolizer sym) {
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
    public void visit(org.geotools.api.style.PointSymbolizer ps) {
        PointSymbolizer input = (PointSymbolizer) ps;
        PointSymbolizer copy = sf.getDefaultPointSymbolizer();

        copy.setGeometry(copy(input.getGeometry()));

        copy.setUnitOfMeasure(input.getUnitOfMeasure());
        copy.setGraphic(copy(input.getGraphic()));
        copy.getOptions().putAll(input.getOptions());

        if (STRICT) {
            if (!copy.equals(ps)) {
                throw new IllegalStateException("Was unable to duplicate provided Graphic:" + ps);
            }
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.LineSymbolizer line) {
        LineSymbolizer input = (LineSymbolizer) line;
        LineSymbolizer copy = sf.getDefaultLineSymbolizer();

        copy.setGeometry(copy(input.getGeometry()));

        copy.setUnitOfMeasure(input.getUnitOfMeasure());
        copy.setStroke(copy(input.getStroke()));
        copy.getOptions().putAll(input.getOptions());
        copy.setPerpendicularOffset(input.getPerpendicularOffset());

        if (STRICT && !copy.equals(line)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided LineSymbolizer:" + line);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.PolygonSymbolizer poly) {
        PolygonSymbolizer input = (PolygonSymbolizer) poly;
        PolygonSymbolizer copy = sf.createPolygonSymbolizer();
        copy.setFill(copy(input.getFill()));

        copy.setGeometry(copy(input.getGeometry()));

        copy.setUnitOfMeasure(input.getUnitOfMeasure());
        copy.setStroke(copy(input.getStroke()));
        copy.getOptions().putAll(input.getOptions());

        if (STRICT && !copy.equals(poly)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided PolygonSymbolizer:" + poly);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.TextSymbolizer text) {
        TextSymbolizer input = (TextSymbolizer) text;
        TextSymbolizer copy = sf.createTextSymbolizer();

        copy.setFill(copy(input.getFill()));
        copy.fonts().clear();
        copy.fonts().addAll(copyFonts(input.fonts()));

        copy.setGeometry(copy(input.getGeometry()));

        copy.setUnitOfMeasure(input.getUnitOfMeasure());
        copy.setHalo(copy(input.getHalo()));
        copy.setLabel(copy(input.getLabel()));
        copy.setLabelPlacement(copy(input.getLabelPlacement()));
        copy.setPriority(copy(input.getPriority()));
        copy.getOptions().putAll(input.getOptions());

        copy.setGraphic(copy(input.getGraphic()));
        copy.setSnippet(copy(input.getSnippet()));
        copy.setFeatureDescription(copy(input.getFeatureDescription()));
        copy.setOtherText(copy(input.getOtherText()));

        if (STRICT && !copy.equals(text)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided TextSymbolizer:" + text);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.RasterSymbolizer raster) {
        RasterSymbolizer input = (RasterSymbolizer) raster;
        RasterSymbolizer copy = sf.createRasterSymbolizer();
        copy.setChannelSelection(copy(input.getChannelSelection()));
        copy.setColorMap(copy(input.getColorMap()));
        copy.setContrastEnhancement(copy(input.getContrastEnhancement()));

        copy.setGeometry(copy(input.getGeometry()));

        copy.setUnitOfMeasure(input.getUnitOfMeasure());
        copy.setImageOutline(copy(input.getImageOutline()));
        copy.setOpacity(copy(input.getOpacity()));
        copy.setOverlapBehavior(input.getOverlapBehavior());
        copy.setShadedRelief(copy(input.getShadedRelief()));

        if (STRICT && !copy.equals(raster)) {
            throw new IllegalStateException("Was unable to duplicate provided raster:" + raster);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.Graphic gr) {
        Graphic input = (Graphic) gr;
        Displacement displacementCopy = copy(input.getDisplacement());
        List<GraphicalSymbol> symbolsCopy = copy(input.graphicalSymbols());
        Expression opacityCopy = copy(input.getOpacity());
        Expression rotationCopy = copy(input.getRotation());
        Expression sizeCopy = copy(input.getSize());
        AnchorPoint anchorCopy = copy((AnchorPoint) input.getAnchorPoint());

        Graphic copy = sf.createDefaultGraphic();

        copy.setDisplacement(displacementCopy);
        copy.graphicalSymbols().clear();
        copy.graphicalSymbols().addAll(symbolsCopy);
        copy.setOpacity(opacityCopy);
        copy.setRotation(rotationCopy);
        copy.setSize(sizeCopy);
        copy.setAnchorPoint(anchorCopy);

        if (STRICT) {
            if (!copy.equals(gr)) {
                throw new IllegalStateException("Was unable to duplicate provided Graphic:" + gr);
            }
        }
        pages.push(copy);
    }

    private List<GraphicalSymbol> copy(List<GraphicalSymbol> symbols) {
        List<GraphicalSymbol> copy = new ArrayList<>(symbols.size());
        for (GraphicalSymbol symbol : symbols) {
            if (symbol instanceof Symbol) {
                copy.add(copy((Symbol) symbol));
            } else {
                throw new RuntimeException("Don't know how to copy " + symbol);
            }
        }
        return copy;
    }

    @Override
    public void visit(org.geotools.api.style.Mark mark) {
        Mark input = (Mark) mark;
        Mark copy = sf.createMark();
        copy.setFill(copy(input.getFill()));
        copy.setStroke(copy(input.getStroke()));
        copy.setWellKnownName(copy(input.getWellKnownName()));
        copy.setExternalMark(copy(input.getExternalMark()));

        if (STRICT && !copy.equals(mark)) {
            throw new IllegalStateException("Was unable to duplicate provided Mark:" + mark);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.ExternalGraphic exgr) {
        URL uri = null;
        try {
            uri = exgr.getLocation();
        } catch (MalformedURLException huh) {

        }
        String format = exgr.getFormat();
        Icon inlineContent = exgr.getInlineContent();
        ExternalGraphic copy;
        if (inlineContent != null) {
            copy = sf.createExternalGraphic(inlineContent, format);
        } else {
            copy = sf.createExternalGraphic(uri, format);
        }
        copy.setCustomProperties(copy(exgr.getCustomProperties()));

        if (STRICT && !copy.equals(exgr)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided ExternalGraphic:" + exgr);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.PointPlacement pp) {
        PointPlacement input = (PointPlacement) pp;
        PointPlacement copy = sf.getDefaultPointPlacement();
        copy.setAnchorPoint(copy(input.getAnchorPoint()));
        copy.setDisplacement(copy(input.getDisplacement()));
        copy.setRotation(copy(input.getRotation()));

        if (STRICT && !copy.equals(pp)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided PointPlacement:" + pp);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.AnchorPoint ap) {
        Expression x = copy(ap.getAnchorPointX());
        Expression y = copy(ap.getAnchorPointY());
        AnchorPoint copy = sf.createAnchorPoint(x, y);

        if (STRICT && !copy.equals(ap)) {
            throw new IllegalStateException("Was unable to duplicate provided AnchorPoint:" + ap);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.Displacement dis) {
        Expression x = copy(dis.getDisplacementX());
        Expression y = copy(dis.getDisplacementY());
        Displacement copy = sf.createDisplacement(x, y);

        if (STRICT && !copy.equals(dis)) {
            throw new IllegalStateException("Was unable to duplicate provided Displacement:" + dis);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.LinePlacement lp) {
        Expression offset = copy(lp.getPerpendicularOffset());
        LinePlacement copy = sf.createLinePlacement(offset);
        copy.setAligned(lp.isAligned());
        copy.setGap(copy(lp.getGap()));
        copy.setGeneralized(lp.isGeneralizeLine());
        copy.setInitialGap(copy(lp.getInitialGap()));
        copy.setRepeated(lp.isRepeated());

        if (STRICT && !copy.equals(lp)) {
            throw new IllegalStateException("Was unable to duplicate provided LinePlacement:" + lp);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.Halo halo) {
        Halo input = (Halo) halo;
        Fill fill = copy(input.getFill());
        Expression radius = copy(input.getRadius());
        Halo copy = sf.createHalo(fill, radius);

        if (STRICT && !copy.equals(input)) {
            throw new IllegalStateException("Was unable to duplicate provided raster:" + halo);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.FeatureTypeConstraint ftc) {
        FeatureTypeConstraint input = (FeatureTypeConstraint) ftc;
        String typeName = input.getFeatureTypeName();
        Filter filter = copy(input.getFilter());
        org.geotools.api.style.Extent[] extents1 = input.getExtents();
        Extent[] extents = copy(extents1);
        FeatureTypeConstraint copy = sf.createFeatureTypeConstraint(typeName, filter, extents);

        if (STRICT && !copy.equals(input)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided FeatureTypeConstraint:" + ftc);
        }
        pages.push(copy);
    }

    protected Extent[] copy(org.geotools.api.style.Extent... extents) {
        if (extents == null) {
            return null;
        }
        Extent[] copy = new Extent[extents.length];
        for (int i = 0; i < extents.length; i++) {
            copy[i] = copy(extents[i]);
        }
        return copy;
    }

    protected Extent copy(org.geotools.api.style.Extent extent) {
        String name = extent.getName();
        String value = extent.getValue();
        Extent copy = sf.createExtent(name, value);
        return copy;
    }

    private org.geotools.api.style.OtherText copy(org.geotools.api.style.OtherText otherText) {
        if (otherText == null) return null;

        // TODO: add methods to the factory to create OtherText instances
        // sf.createOtherText();
        OtherText copy = new OtherText();
        copy.setTarget(otherText.getTarget());
        copy.setText(copy(otherText.getText()));
        return copy;
    }

    @Override
    public void visit(org.geotools.api.style.ColorMap colorMap) {

        ColorMap copy = sf.createColorMap();
        copy.setType(colorMap.getType());
        copy.setExtendedColors(colorMap.getExtendedColors());
        org.geotools.api.style.ColorMapEntry[] entries = colorMap.getColorMapEntries();
        if (entries != null) {
            for (org.geotools.api.style.ColorMapEntry entry : entries) {
                copy.addColorMapEntry(copy((ColorMapEntry) entry));
            }
        }
        if (STRICT && !copy.equals(colorMap)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided ColorMap:" + colorMap);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.ColorMapEntry colorMapEntry) {
        ColorMapEntry copy = sf.createColorMapEntry();
        copy.setColor(copy(colorMapEntry.getColor()));
        copy.setLabel(colorMapEntry.getLabel());
        copy.setOpacity(copy(colorMapEntry.getOpacity()));
        copy.setQuantity(copy(colorMapEntry.getQuantity()));

        if (STRICT && !copy.equals(colorMapEntry)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided ColorMapEntry:" + colorMapEntry);
        }
        pages.push(copy);
    }

    /**
     * Called when accept is called on a raster ContrastEnhancement element
     *
     * @param contrastEnhancement the {@link ContrastEnhancement} to visit.
     */
    @Override
    public void visit(org.geotools.api.style.ContrastEnhancement contrastEnhancement) {
        final ContrastEnhancement copy = sf.createContrastEnhancement();
        copy.setMethod(contrastEnhancement.getMethod());
        copy.setOptions(contrastEnhancement.getOptions());

        copy.setGammaValue(contrastEnhancement.getGammaValue());
        if (STRICT && !copy.equals(contrastEnhancement)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided contrastEnhancement:" + contrastEnhancement);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.ImageOutline outline) {
        // copy the symbolizer
        final Symbolizer symb = (Symbolizer) outline.getSymbolizer();
        final Symbolizer copySymb = copy(symb);

        final ImageOutline copy = sf.createImageOutline(copySymb);
        copy.setSymbolizer(copySymb);
        if (STRICT && !copy.equals(outline)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided ImageOutline:" + outline);
        }
        pages.push(copy);
    }

    /**
     * Called when accept is called on a raster {@link ChannelSelection} element
     *
     * @param cs the {@link ChannelSelection} to visit.
     */
    @Override
    public void visit(org.geotools.api.style.ChannelSelection cs) {}

    @Override
    public void visit(org.geotools.api.style.OverlapBehavior ob) {
        final String behavior = (String) ob.name();
        if (behavior.equalsIgnoreCase(OverlapBehavior.AVERAGE_RESCTRICTION)) {
            pages.push(OverlapBehavior.AVERAGE_RESCTRICTION);
        } else if (behavior.equalsIgnoreCase(OverlapBehavior.EARLIEST_ON_TOP_RESCTRICTION)) {
            pages.push(OverlapBehavior.EARLIEST_ON_TOP_RESCTRICTION);
        } else if (behavior.equalsIgnoreCase(OverlapBehavior.LATEST_ON_TOP_RESCTRICTION)) {
            pages.push(OverlapBehavior.LATEST_ON_TOP_RESCTRICTION);
        } else if (behavior.equalsIgnoreCase(OverlapBehavior.RANDOM_RESCTRICTION)) {
            pages.push(OverlapBehavior.RANDOM_RESCTRICTION);
        } else {
            throw new IllegalStateException(
                    "Was unable to duplicate provided OverlapBehavior:" + ob);
        }
    }

    @Override
    public void visit(org.geotools.api.style.SelectedChannelType sct) {
        SelectedChannelType input = (SelectedChannelType) sct;
        final SelectedChannelType copy =
                sf.createSelectedChannelType(
                        sct.getChannelName(), copy(input.getContrastEnhancement()));
        if (STRICT && !copy.equals(sct)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided SelectedChannelType:" + sct);
        }
        pages.push(copy);
    }

    @Override
    public void visit(org.geotools.api.style.ShadedRelief sr) {
        final ShadedRelief copy = sf.createShadedRelief(copy(sr.getReliefFactor()));
        copy.setBrightnessOnly(sr.isBrightnessOnly());
        if (STRICT && !copy.equals(sr)) {
            throw new IllegalStateException("Was unable to duplicate provided ShadedRelief:" + sr);
        }
        pages.push(copy);
    }
}

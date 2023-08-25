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
 *
 * Created on 14 October 2002, 15:50
 */
package org.geotools.styling;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import javax.swing.Icon;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.metadata.citation.OnLineResource;
import org.geotools.api.style.ContrastMethod;
import org.geotools.api.style.GraphicFill;
import org.geotools.api.style.GraphicLegend;
import org.geotools.api.style.GraphicStroke;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.OverlapBehavior;
import org.geotools.api.style.SemanticType;
import org.geotools.api.util.InternationalString;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.GeoTools;

/**
 * Factory for creating Styles; based on the GeoAPI StyleFactory interface.
 *
 * <p>This factory is simple; it just creates styles with no logic or magic default values. For
 * magic default values please read the SE or SLD specification; or use an appropriate builder.
 *
 * @author Jody Garnett
 * @version $Id$
 */
public class StyleFactoryImpl2 implements org.geotools.api.style.StyleFactory {
    private FilterFactory filterFactory;

    public StyleFactoryImpl2() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    protected StyleFactoryImpl2(FilterFactory factory) {
        filterFactory = factory;
    }

    @Override
    public AnchorPoint anchorPoint(Expression x, Expression y) {
        return new AnchorPoint(filterFactory, x, y);
    }

    @Override
    public ChannelSelection channelSelection(org.geotools.api.style.SelectedChannelType gray) {
        ChannelSelectionImpl channelSelection = new ChannelSelectionImpl();
        channelSelection.setGrayChannel(gray);
        return channelSelection;
    }

    @Override
    public ChannelSelectionImpl channelSelection(
            org.geotools.api.style.SelectedChannelType red,
            org.geotools.api.style.SelectedChannelType green,
            org.geotools.api.style.SelectedChannelType blue) {
        ChannelSelectionImpl channelSelection = new ChannelSelectionImpl();
        channelSelection.setRGBChannels(red, green, blue);
        return channelSelection;
    }

    @Override
    public ColorMap colorMap(Expression propertyName, Expression... mapping) {
        Expression[] arguments = new Expression[mapping.length + 2];
        arguments[0] = propertyName;
        for (int i = 0; i < mapping.length; i++) {
            arguments[i + 1] = mapping[i];
        }
        Function function = filterFactory.function("Categorize", arguments);
        ColorMap colorMap = new ColorMap(function);

        return colorMap;
    }

    @Override
    public ColorReplacement colorReplacement(Expression propertyName, Expression... mapping) {
        Expression[] arguments = new Expression[mapping.length + 2];
        arguments[0] = propertyName;
        for (int i = 0; i < mapping.length; i++) {
            arguments[i + 1] = mapping[i];
        }
        Function function = filterFactory.function("Recode", arguments);
        ColorReplacement colorMap = new ColorReplacement(function);

        return colorMap;
    }

    @Override
    public ContrastEnhancement contrastEnhancement(Expression gamma, String method) {
        ContrastMethod meth = ContrastMethod.NONE;
        if (ContrastMethod.NORMALIZE.matches(method)) {
            meth = ContrastMethod.NORMALIZE;
        } else if (ContrastMethod.HISTOGRAM.matches(method)) {
            meth = ContrastMethod.HISTOGRAM;
        } else if (ContrastMethod.LOGARITHMIC.matches(method)) {
            meth = ContrastMethod.LOGARITHMIC;
        } else if (ContrastMethod.EXPONENTIAL.matches(method)) {
            meth = ContrastMethod.EXPONENTIAL;
        }
        return new ContrastEnhancement(filterFactory, gamma, meth);
    }

    @Override
    public ContrastEnhancement contrastEnhancement(Expression gamma, ContrastMethod method) {
        return new ContrastEnhancement(filterFactory, gamma, method);
    }

    @Override
    public Description description(InternationalString title, InternationalString description) {
        return new Description(title, description);
    }

    @Override
    public Displacement displacement(Expression dx, Expression dy) {
        return new Displacement(dx, dy);
    }

    @Override
    public ExternalGraphic externalGraphic(Icon inline, Collection<org.geotools.api.style.ColorReplacement> replacements) {
        ExternalGraphic externalGraphic = new ExternalGraphic(inline, replacements, null);
        return externalGraphic;
    }

    @Override
    public ExternalGraphic externalGraphic(
            OnLineResource resource, String format, Collection<org.geotools.api.style.ColorReplacement> replacements) {
        ExternalGraphic externalGraphic = new ExternalGraphic(null, replacements, resource);
        externalGraphic.setFormat(format);
        return externalGraphic;
    }

    @Override
    public ExternalMark externalMark(Icon inline) {
        return new ExternalMark(inline);
    }

    @Override
    public ExternalMark externalMark(OnLineResource resource, String format, int markIndex) {
        return new ExternalMark(resource, format, markIndex);
    }

    @Override
    public FeatureTypeStyle featureTypeStyle(
            String name,
            org.geotools.api.style.Description description,
            Id definedFor,
            Set<Name> featureTypeNames,
            Set<SemanticType> types,
            List<org.geotools.api.style.Rule> rules) {
        FeatureTypeStyle featureTypeStyle = new FeatureTypeStyle();
        featureTypeStyle.setName(name);

        if (description != null && description.getTitle() != null) {
            featureTypeStyle.getDescription().setTitle(description.getTitle());
        }
        if (description != null && description.getAbstract() != null) {
            featureTypeStyle.getDescription().setAbstract(description.getAbstract());
        }
        // featureTypeStyle.setFeatureInstanceIDs( defainedFor );
        featureTypeStyle.featureTypeNames().addAll(featureTypeNames);
        featureTypeStyle.semanticTypeIdentifiers().addAll(types);

        for (org.geotools.api.style.Rule rule : rules) {
            if (rule instanceof Rule) {
                featureTypeStyle.rules().add((Rule) rule);
            } else {
                featureTypeStyle.rules().add(new Rule(rule));
            }
        }
        return featureTypeStyle;
    }

    @Override
    public Fill fill(GraphicFill graphicFill, Expression color, Expression opacity) {
        Fill fill = new Fill(filterFactory);
        fill.setGraphicFill(graphicFill);
        fill.setColor(color);
        fill.setOpacity(opacity);
        return fill;
    }

    @Override
    public Font font(
            List<Expression> family, Expression style, Expression weight, Expression size) {
        Font font = new Font();
        font.getFamily().addAll(family);
        font.setStyle(style);
        font.setWeight(weight);
        font.setSize(size);

        return font;
    }

    @Override
    public Graphic graphic(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchor,
            org.geotools.api.style.Displacement disp) {

        Graphic graphic = new Graphic(filterFactory);
        if (symbols != null) {
            for (GraphicalSymbol graphicalSymbol : symbols) {
                if (graphicalSymbol instanceof ExternalGraphic) {
                    graphic.graphicalSymbols().add(ExternalGraphic.cast(graphicalSymbol));
                } else if (graphicalSymbol instanceof Mark) {
                    graphic.graphicalSymbols().add(Mark.cast(graphicalSymbol));
                }
            }
        }
        graphic.setOpacity(opacity);
        graphic.setSize(size);
        graphic.setRotation(rotation);
        graphic.setAnchorPoint(anchor);
        graphic.setDisplacement(disp);
        return graphic;
    }

    @Override
    public Graphic graphicFill(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement) {

        Graphic graphicFill = new Graphic(filterFactory);
        if (symbols != null) {
            for (GraphicalSymbol graphicalSymbol : symbols) {
                if (graphicalSymbol instanceof ExternalGraphic) {
                    graphicFill.graphicalSymbols().add(ExternalGraphic.cast(graphicalSymbol));
                } else if (graphicalSymbol instanceof Mark) {
                    graphicFill.graphicalSymbols().add(Mark.cast(graphicalSymbol));
                }
            }
        }
        graphicFill.setOpacity(opacity);
        graphicFill.setSize(size);
        graphicFill.setRotation(rotation);
        graphicFill.setAnchorPoint(anchorPoint);
        graphicFill.setDisplacement(displacement);

        return graphicFill;
    }

    @Override
    public Graphic graphicLegend(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement) {
        Graphic graphicLegend = new Graphic(filterFactory);
        if (symbols != null) {
            for (GraphicalSymbol graphicalSymbol : symbols) {
                if (graphicalSymbol instanceof ExternalGraphic) {
                    graphicLegend.graphicalSymbols().add(ExternalGraphic.cast(graphicalSymbol));
                } else if (graphicalSymbol instanceof Mark) {
                    graphicLegend.graphicalSymbols().add(Mark.cast(graphicalSymbol));
                }
            }
        }
        graphicLegend.setOpacity(opacity);
        graphicLegend.setSize(size);
        graphicLegend.setRotation(rotation);
        graphicLegend.setAnchorPoint(anchorPoint);
        graphicLegend.setDisplacement(displacement);

        return graphicLegend;
    }

    @Override
    public Graphic graphicStroke(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement,
            Expression initialGap,
            Expression gap) {
        Graphic graphicStroke = new Graphic(filterFactory);
        if (symbols != null) {
            for (GraphicalSymbol graphicalSymbol : symbols) {
                if (graphicalSymbol instanceof ExternalGraphic) {
                    graphicStroke.graphicalSymbols().add(ExternalGraphic.cast(graphicalSymbol));
                } else if (graphicalSymbol instanceof Mark) {
                    graphicStroke.graphicalSymbols().add(Mark.cast(graphicalSymbol));
                }
            }
        }
        graphicStroke.setOpacity(opacity);
        graphicStroke.setSize(size);
        graphicStroke.setRotation(rotation);
        graphicStroke.setAnchorPoint(anchorPoint);
        graphicStroke.setDisplacement(displacement);
        graphicStroke.setInitialGap(initialGap);
        graphicStroke.setGap(gap);

        return graphicStroke;
    }

    @Override
    public Halo halo(org.geotools.api.style.Fill fill, Expression radius) {
        Halo halo = new Halo();
        halo.setFill(fill);
        halo.setRadius(radius);

        return halo;
    }

    @Override
    public LinePlacement linePlacement(
            Expression offset,
            Expression initialGap,
            Expression gap,
            boolean repeated,
            boolean aligned,
            boolean generalizedLine) {
        LinePlacement placement = new LinePlacement(filterFactory);
        placement.setPerpendicularOffset(offset);
        placement.setInitialGap(initialGap);
        placement.setGap(gap);
        placement.setRepeated(repeated);
        placement.setAligned(aligned);
        placement.setGeneralized(generalizedLine);

        return placement;
    }

    @Override
    @SuppressWarnings("unchecked")
    public LineSymbolizer lineSymbolizer(
            String name,
            Expression geometry,
            org.geotools.api.style.Description description,
            Unit<?> unit,
            org.geotools.api.style.Stroke stroke,
            Expression offset) {
        LineSymbolizer copy = new LineSymbolizer();
        copy.setDescription(description);
        copy.setGeometry(geometry);
        copy.setName(name);
        copy.setPerpendicularOffset(offset);
        copy.setStroke(stroke);
        copy.setUnitOfMeasure((Unit<Length>) unit);
        return copy;
    }

    @Override
    public Mark mark(
            Expression wellKnownName,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Stroke stroke) {

        Mark mark = new Mark(filterFactory, null);
        mark.setWellKnownName(wellKnownName);
        mark.setFill(fill);
        mark.setStroke(stroke);

        return mark;
    }

    @Override
    public Mark mark(
            org.geotools.api.style.ExternalMark externalMark,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Stroke stroke) {
        Mark mark = new Mark();
        mark.setExternalMark(externalMark);
        mark.setFill(fill);
        mark.setStroke(stroke);

        return mark;
    }

    @Override
    public PointPlacement pointPlacement(
            org.geotools.api.style.AnchorPoint anchor,
            org.geotools.api.style.Displacement displacement,
            Expression rotation) {
        PointPlacement pointPlacment = new PointPlacement(filterFactory);
        pointPlacment.setAnchorPoint(anchor);
        pointPlacment.setDisplacement(displacement);
        pointPlacment.setRotation(rotation);
        return pointPlacment;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PointSymbolizer pointSymbolizer(
            String name,
            Expression geometry,
            org.geotools.api.style.Description description,
            Unit<?> unit,
            org.geotools.api.style.Graphic graphic) {
        PointSymbolizer copy = new PointSymbolizer();
        copy.setDescription(description);
        copy.setGeometryPropertyName(((PropertyName) geometry).getPropertyName());
        copy.setGraphic(graphic);
        copy.setName(name);
        copy.setUnitOfMeasure((Unit<Length>) unit);
        return copy;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PolygonSymbolizer polygonSymbolizer(
            String name,
            Expression geometry,
            org.geotools.api.style.Description description,
            Unit<?> unit,
            org.geotools.api.style.Stroke stroke,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Displacement displacement,
            Expression offset) {
        PolygonSymbolizer polygonSymbolizer = new PolygonSymbolizer();
        polygonSymbolizer.setStroke(stroke);
        polygonSymbolizer.setDescription(description);
        polygonSymbolizer.setDisplacement(displacement);
        polygonSymbolizer.setFill(fill);
        polygonSymbolizer.setGeometryPropertyName(((PropertyName) geometry).getPropertyName());
        polygonSymbolizer.setName(name);
        polygonSymbolizer.setPerpendicularOffset(offset);
        polygonSymbolizer.setUnitOfMeasure((Unit<Length>) unit);
        return polygonSymbolizer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RasterSymbolizer rasterSymbolizer(
            String name,
            Expression geometry,
            org.geotools.api.style.Description description,
            Unit<?> unit,
            Expression opacity,
            org.geotools.api.style.ChannelSelection channelSelection,
            OverlapBehavior overlapsBehaviour,
            org.geotools.api.style.ColorMap colorMap,
            org.geotools.api.style.ContrastEnhancement contrast,
            org.geotools.api.style.ShadedRelief shaded,
            org.geotools.api.style.Symbolizer outline) {
        RasterSymbolizer rasterSymbolizer = new RasterSymbolizer(filterFactory);
        rasterSymbolizer.setChannelSelection(channelSelection);
        rasterSymbolizer.setColorMap(colorMap);
        rasterSymbolizer.setContrastEnhancement(contrast);
        rasterSymbolizer.setDescription(description);
        if (geometry != null) {
            rasterSymbolizer.setGeometryPropertyName(((PropertyName) geometry).getPropertyName());
        } else {
            rasterSymbolizer.setGeometryPropertyName(null);
        }
        rasterSymbolizer.setImageOutline(outline);
        rasterSymbolizer.setName(name);
        rasterSymbolizer.setOpacity(opacity);
        rasterSymbolizer.setOverlapBehavior(overlapsBehaviour);
        rasterSymbolizer.setShadedRelief(shaded);
        rasterSymbolizer.setUnitOfMeasure((Unit<Length>) unit);
        return rasterSymbolizer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExtensionSymbolizer extensionSymbolizer(
            String name,
            String propertyName,
            org.geotools.api.style.Description description,
            Unit<?> unit,
            String extensionName,
            Map<String, Expression> parameters) {
        // We need a factory extension mechanism here to register additional
        // symbolizer implementations
        VendorSymbolizerImpl extension = new VendorSymbolizerImpl();
        extension.setName(name);
        extension.setGeometryPropertyName(propertyName);
        extension.setDescription(description);
        extension.setUnitOfMeasure((Unit<Length>) unit);
        extension.setExtensionName(extensionName);
        extension.getParameters().putAll(parameters);

        return extension;
    }

    static org.geotools.styling.Symbolizer cast(org.geotools.api.style.Symbolizer symbolizer) {
        if (symbolizer instanceof org.geotools.api.style.PolygonSymbolizer) {
            return PolygonSymbolizer.cast(symbolizer);
        } else if (symbolizer instanceof org.geotools.api.style.LineSymbolizer) {
            return LineSymbolizer.cast(symbolizer);
        } else if (symbolizer instanceof org.geotools.api.style.PointSymbolizer) {
            return PointSymbolizer.cast(symbolizer);
        } else if (symbolizer instanceof org.geotools.api.style.RasterSymbolizer) {
            return RasterSymbolizer.cast(symbolizer);
        } else if (symbolizer instanceof org.geotools.api.style.TextSymbolizer) {
            return TextSymbolizer.cast(symbolizer);
        }
        // the day there is any implementation, handle org.geotools.api.style.ExtensionSymbolizer
        return null; // must be some new extension?
    }

    @Override
    public Rule rule(
            String name,
            org.geotools.api.style.Description description,
            GraphicLegend legend,
            double min,
            double max,
            List<org.geotools.api.style.Symbolizer> symbolizers,
            Filter filter) {
        Rule rule = new Rule();
        rule.setName(name);
        rule.setDescription(description);
        rule.setLegend(legend);
        rule.setMinScaleDenominator(min);
        rule.setMaxScaleDenominator(max);
        if (symbolizers != null) {
            for (org.geotools.api.style.Symbolizer symbolizer : symbolizers) {
                rule.symbolizers().add(cast(symbolizer));
            }
        }
        if (filter != null) {
            rule.setFilter(filter);
            rule.setElseFilter(false);
        } else {
            rule.setElseFilter(true);
        }
        return rule;
    }

    @Override
    public SelectedChannelType selectedChannelType(
            Expression channelName,
            org.geotools.api.style.ContrastEnhancement contrastEnhancement) {
        SelectedChannelType selectedChannelType = new SelectedChannelType(filterFactory);
        selectedChannelType.setChannelName(channelName);
        selectedChannelType.setContrastEnhancement(contrastEnhancement);
        return selectedChannelType;
    }

    @Override
    public SelectedChannelType selectedChannelType(
            String channelName, org.geotools.api.style.ContrastEnhancement contrastEnhancement) {
        SelectedChannelType selectedChannelType = new SelectedChannelType(filterFactory);
        selectedChannelType.setChannelName(channelName);
        selectedChannelType.setContrastEnhancement(contrastEnhancement);
        return selectedChannelType;
    }

    @Override
    public ShadedRelief shadedRelief(Expression reliefFactor, boolean brightnessOnly) {
        ShadedRelief shadedRelief = new ShadedRelief(filterFactory);
        shadedRelief.setReliefFactor(reliefFactor);
        shadedRelief.setBrightnessOnly(brightnessOnly);
        return shadedRelief;
    }

    @Override
    public Stroke stroke(
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset) {
        Stroke stroke = new Stroke(filterFactory);
        stroke.setColor(color);
        stroke.setOpacity(opacity);
        stroke.setWidth(width);
        stroke.setLineJoin(join);
        stroke.setLineCap(cap);
        stroke.setDashArray(dashes);
        stroke.setDashOffset(offset);
        return stroke;
    }

    @Override
    public Stroke stroke(
            GraphicFill fill,
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset) {
        Stroke stroke = new Stroke(filterFactory);
        stroke.setGraphicFill(fill);
        stroke.setColor(color);
        stroke.setOpacity(opacity);
        stroke.setWidth(width);
        stroke.setLineJoin(join);
        stroke.setLineCap(cap);
        stroke.setDashArray(dashes);
        stroke.setDashOffset(offset);
        return stroke;
    }

    @Override
    public Stroke stroke(
            GraphicStroke stroke,
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset) {
        Stroke s = new Stroke(filterFactory);
        s.setColor(color);
        s.setWidth(width);
        s.setOpacity(opacity);
        s.setLineJoin(join);
        s.setLineCap(cap);
        s.setDashArray(dashes);
        s.setDashOffset(offset);
        s.setGraphicStroke(Graphic.cast(stroke));

        return s;
    }

    @Override
    public Style style(
            String name,
            org.geotools.api.style.Description description,
            boolean isDefault,
            List<org.geotools.api.style.FeatureTypeStyle> featureTypeStyles,
            org.geotools.api.style.Symbolizer defaultSymbolizer) {
        Style style = new Style();
        style.setName(name);
        style.setDescription(description);
        style.setDefault(isDefault);
        if (featureTypeStyles != null) {
            for (org.geotools.api.style.FeatureTypeStyle featureTypeStyle : featureTypeStyles) {
                style.featureTypeStyles().add(FeatureTypeStyle.cast(featureTypeStyle));
            }
        }
        style.setDefaultSpecification(cast(defaultSymbolizer));
        return style;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TextSymbolizer textSymbolizer(
            String name,
            Expression geometry,
            org.geotools.api.style.Description description,
            Unit<?> unit,
            Expression label,
            org.geotools.api.style.Font font,
            org.geotools.api.style.LabelPlacement placement,
            org.geotools.api.style.Halo halo,
            org.geotools.api.style.Fill fill) {

        TextSymbolizer tSymb = new TextSymbolizer(filterFactory);
        tSymb.setName(name);
        tSymb.setFill(fill);
        tSymb.setUnitOfMeasure((Unit<Length>) unit);
        tSymb.setFont(font);
        tSymb.setGeometryPropertyName(((PropertyName) geometry).getPropertyName());
        tSymb.setHalo(halo);
        tSymb.setLabel(label);
        tSymb.setLabelPlacement(placement);
        // tSymb.setGraphic( GraphicImpl.cast(graphic));
        tSymb.setDescription(description);
        return tSymb;
    }
}

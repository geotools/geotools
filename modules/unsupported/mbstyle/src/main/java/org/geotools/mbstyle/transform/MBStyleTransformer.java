/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.transform;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.measure.unit.NonSI;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.mbstyle.BackgroundMBLayer;
import org.geotools.mbstyle.CircleMBLayer;
import org.geotools.mbstyle.FillMBLayer;
import org.geotools.mbstyle.LineMBLayer;
import org.geotools.mbstyle.MBLayer;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.RasterMBLayer;
import org.geotools.mbstyle.SymbolMBLayer;
import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.sprite.MapboxGraphicFactory;
import org.geotools.styling.*;
import org.geotools.text.Text;
import org.geotools.util.logging.Logging;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ContrastMethod;
import org.opengis.style.GraphicFill;
import org.opengis.style.SemanticType;
import org.opengis.style.Symbolizer;

/**
 * Responsible for traverse {@link MBStyle} and generating {@link StyledLayerDescriptor}.
 * 
 * @author Jody Garnett (Jody Garnett)
 */
public class MBStyleTransformer {

    private FilterFactory2 ff;

    private StyleFactory sf;
    
    private StyleBuilder sb;

    private List<String> defaultFonts;
    
    private static final Logger LOGGER = Logging.getLogger(MBStyleTransformer.class);

    public enum AnchorPosition {
        CENTER("center"), // 0.5, 0.5 (x, y)
        LEFT("left"), // 0.0, 0.5
        RIGHT("right"), // 1.0, 0.5
        TOP("top"), // 0.5, 1.0
        BOTTOM("bottom"), // 0.5, 0.0
        TOP_LEFT("top-left"), // 0.0, 1.0
        TOP_RIGHT("top-right"), // 1.0, 1.0
        BOTTOM_LEFT("bottom-left"), // 0.0, 0.0
        BOTTOM_RIGHT("bottom-right"); // 1.0, 0.0

        private final String jsonString;

        AnchorPosition(String jsonString) {
            this.jsonString = jsonString;
        }

        public Double getAnchorX(String jsonString) {
            AnchorPosition pos = AnchorPosition.valueOf(jsonString);
            Double x = 0.0;
            switch (pos) {
                case BOTTOM:
                    x = 0.5;
                    break;
                case BOTTOM_LEFT:
                    x = 0.0;
                    break;
                case BOTTOM_RIGHT:
                    x = 1.0;
                    break;
                case CENTER:
                    x = 0.5;
                    break;
                case LEFT:
                    x = 0.0;
                    break;
                case RIGHT:
                    x = 1.0;
                    break;
                case TOP:
                    x = 0.5;
                    break;
                case TOP_LEFT:
                    x = 0.0;
                    break;
                case TOP_RIGHT:
                    x = 1.0;
                    break;
            }
            return x;
        }

        public Double getAnchorY(String jsonString) {
            AnchorPosition pos = AnchorPosition.valueOf(jsonString);
            Double y = 0.0;
            switch (pos) {
                case BOTTOM:
                    y = 0.0;
                    break;
                case BOTTOM_LEFT:
                    y = 0.0;
                    break;
                case BOTTOM_RIGHT:
                    y = 0.0;
                    break;
                case CENTER:
                    y = 0.5;
                    break;
                case LEFT:
                    y = 0.5;
                    break;
                case RIGHT:
                    y = 0.5;
                    break;
                case TOP:
                    y = 1.0;
                    break;
                case TOP_LEFT:
                    y = 1.0;
                    break;
                case TOP_RIGHT:
                    y = 1.0;
                    break;
            }
            return y;
        }
    }

    public MBStyleTransformer() {
        defaultFonts = new ArrayList<>();
        defaultFonts.add("Open Sans Regular");
        defaultFonts.add("Arial Unicode MS Regular");
        ff = CommonFactoryFinder.getFilterFactory2();
        sf = CommonFactoryFinder.getStyleFactory();
        sb = new StyleBuilder();
    }

    /**
     * Transform MBStyle to a GeoTools UserLayer.
     * 
     * @param layer MBStyle
     * @return user layer
     */
    public StyledLayerDescriptor tranform(MBStyle mbStyle) {
        List<MBLayer> layers = mbStyle.layers();
        if (layers.isEmpty()) {
            throw new MBFormatException("layers empty");
        }

        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        Style style = sf.createStyle();
        for (MBLayer layer : layers) {
            if (layer.visibility()) {
                FeatureTypeStyle featureTypeStyle = transform(layer, mbStyle);
                style.featureTypeStyles().add(featureTypeStyle);
            }
        }
        
        if( style.featureTypeStyles().isEmpty() ){
            throw new MBFormatException("No visibile layers");
        }
        
        UserLayer userLayer = sf.createUserLayer();
        userLayer.userStyles().add(style);
        
        sld.layers().add(userLayer);
        sld.setName(mbStyle.getName());
        return sld;
    }

    /**
     * 
     * Transforms a given {@link MBLayer} to a GeoTools {@link FeatureTypeStyle}.
     * 
     * @param layer The MBLayer to transform.
     * @param styleContext The {@link MBStyle} to use as a context, e.g. for sprite and glyph address resolution.
     * @return A feature type style from the provided layer.
     */
    public FeatureTypeStyle transform(MBLayer layer, MBStyle styleContext) {
        if( !layer.visibility()){
            return null; // layer layout visibility 'none'
        }
        if (layer instanceof FillMBLayer) {
            return transform((FillMBLayer) layer, styleContext);
        } else if (layer instanceof RasterMBLayer) {
            return transform((RasterMBLayer) layer);
        } else if (layer instanceof LineMBLayer) {
            return transform((LineMBLayer) layer);
        } else if (layer instanceof CircleMBLayer) {
            return transform((CircleMBLayer) layer);
        } else if (layer instanceof BackgroundMBLayer) {
            return transform((BackgroundMBLayer) layer);
        } else if (layer instanceof SymbolMBLayer) {
            return transform((SymbolMBLayer) layer, styleContext);
        }

        throw new MBFormatException(layer.getType() + " not yet supported.");
    }

    /**
     * Transform MBFillLayer to GeoTools FeatureTypeStyle.
     * <p>
     * Notes:</p>
     * <ul>
     * <li>stroke-width is assumed to be 1 (not specified by MapBox style)
     * </ul>
     * @param layer Describing polygon fill styling
     * @param styleContext The MBStyle to which this layer belongs, used as a context for things like resolving sprite and glyph names to full urls.
     * @return FeatureTypeStyle 
     */
    public FeatureTypeStyle transform(FillMBLayer layer, MBStyle styleContext) {
        PolygonSymbolizer symbolizer;
        // use factory to avoid defaults values        
        Stroke stroke = sf.stroke(
                layer.fillOutlineColor(),
                ff.literal(1),
                ff.literal(1),
                ff.literal("miter"),
                ff.literal("butt"),
                null,
                null);
        
        // from fill pattern or fill color
        Fill fill;
        if (layer.getFillPattern() != null) {
            // TODO: Fill graphic (with external graphics)
            ExternalGraphic eg = createExternalGraphicForSprite(layer.getFillPattern(), styleContext);
            GraphicFill gf = sf.graphicFill(Arrays.asList(eg), layer.fillOpacity(), null, null, null, layer.toDisplacement());            
            fill = sf.fill(gf, null, null);                
        } else {
            fill = sf.fill(null, layer.fillColor(), layer.fillOpacity());
        }

        // TODO: Is there a better way to select the first geometry?
        symbolizer = sf.polygonSymbolizer(
                 layer.getId(),
                 ff.property((String)null),
                 sf.description(Text.text("fill"),null),
                 NonSI.PIXEL,
                 stroke,
                 fill,
                 layer.toDisplacement(),
                 ff.literal(0));
        
        Rule rule = sf.rule(layer.getId(), null,  null, 0.0, Double.POSITIVE_INFINITY, Arrays.asList(symbolizer), Filter.INCLUDE);

        // Set legend graphic to null.
        //TODO: How do other style transformers set a null legend?
        rule.setLegendGraphic(new Graphic[0]);
        
        
        return sf.featureTypeStyle(
                layer.getId(),
                sf.description(
                        Text.text("MBStyle "+layer.getId()),
                        Text.text("Generated for "+layer.getSourceLayer())),
                null, // (unused)
                Collections.emptySet(),
                Collections.singleton(SemanticType.POLYGON), // we only expect this to be applied to polygons 
                Arrays.asList(rule)
                );
    }

    /**
     * Transform {@link RasterMBLayer} to GeoTools FeatureTypeStyle.
     * <p>
     * Notes:
     * </p>
     * <ul>
     * <li>Assumes 3-band RGB</li>
     * </ul>
     * 
     * @param layer Describing raster fill styling
     * @return FeatureTypeStyle
     */
    FeatureTypeStyle transform(RasterMBLayer layer) {
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NONE);
        ChannelSelection sel = sf.channelSelection(sf.createSelectedChannelType("1", ce),
                sf.createSelectedChannelType("2", ce), sf.createSelectedChannelType("3", ce));

        // Use of builder is easier for code examples; but fills in SLD defaults
        // Currently only applies the opacity.
        RasterSymbolizer symbolizer = sf.rasterSymbolizer(layer.getId(), null,
                sf.description(Text.text("raster"), null), NonSI.PIXEL, layer.opacity(), sel,
                null, null, ce, null, null);

        List<org.opengis.style.Rule> rules = new ArrayList<>();
        Rule rule = sf.rule(layer.getId(), null, null, 0.0, Double.MAX_VALUE,
                Arrays.asList(symbolizer), Filter.INCLUDE);
        rules.add(rule);
        return sf.featureTypeStyle(layer.getId(),
                sf.description(Text.text("MBStyle " + layer.getId()),
                               Text.text("Generated for " + layer.getSourceLayer())),
                null, 
                Collections.emptySet(), 
                Collections.singleton(SemanticType.RASTER), 
                rules);
    }   
    
    /**
     * Transform {@link LineMBLayer} to GeoTools FeatureTypeStyle.
     * <p>
     * Notes:
     * </p>
     * <ul>
     * </ul>
     * 
     * @param layer Describing line styling
     * @return FeatureTypeStyle
     */
    FeatureTypeStyle transform(LineMBLayer layer) {
        Stroke stroke = sf.stroke(layer.lineColor(), layer.lineOpacity(), layer.lineWidth(),
                layer.lineJoin(), layer.lineCap(), null, null); // last "offset" is really "dash offset"
        stroke.setDashArray(layer.lineDasharray());        
        LineSymbolizer ls = sf.lineSymbolizer(layer.getId(), null,
                sf.description(Text.text("line"), null), NonSI.PIXEL, stroke, layer.lineOffset());
        
        // Left for the special effects sprint:
        // layer.linePattern(); // Graphic fill
        // layer.lineBlur();
        // layer.lineGapWidth();
        // layer.lineMiterLimit();
        // layer.lineRoundLimit();
        // layer.getLineTranslateAnchor();
        // layer.toDisplacement()

        List<org.opengis.style.Rule> rules = new ArrayList<>();
        Rule rule = sf.rule(layer.getId(), null, null, 0.0, Double.POSITIVE_INFINITY,
                Arrays.asList(ls), Filter.INCLUDE);
        
        rules.add(rule);
        return sf.featureTypeStyle(layer.getId(),
                sf.description(Text.text("MBStyle " + layer.getId()),
                        Text.text("Generated for " + layer.getSourceLayer())),
                null, Collections.emptySet(), Collections.singleton(SemanticType.LINE), rules);
    }
    
    /**
     * Transform {@link CircleMBLayerle} to GeoTools FeatureTypeStyle.
     * <p>
     * Notes:
     * </p>
     * <ul>
     * </ul>
     * 
     * @param layer Describing circle styling
     * @return FeatureTypeStyle
     */
    FeatureTypeStyle transform(CircleMBLayer layer) {
        // default linecap because StrokeImpl.getOpacity has a bug. If lineCap == null, it returns a default opacity.
        Stroke s = sf.stroke(layer.circleStrokeColor(), layer.circleStrokeOpacity(),
                layer.circleStrokeWidth(), null, Stroke.DEFAULT.getLineCap(), null, null);
        Fill f = sf.fill(null, layer.circleColor(), layer.circleOpacity());
        Mark m = sf.mark(ff.literal("circle"), f, s);

        Graphic gr = sf.graphic(Arrays.asList(m), null,
                ff.multiply(ff.literal(2), layer.circleRadius()), null, null,
                layer.toDisplacement());
        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(m);

        PointSymbolizer ps = sf
                .pointSymbolizer(layer.getId(), ff.property((String) null),
                        sf.description(Text.text("MBStyle " + layer.getId()),
                                Text.text("Generated for " + layer.getSourceLayer())),
                        NonSI.PIXEL, gr);

        List<org.opengis.style.Rule> rules = new ArrayList<>();
        Rule rule = sf.rule(layer.getId(), null, null, 0.0, Double.POSITIVE_INFINITY,
                Arrays.asList(ps), Filter.INCLUDE);

        rules.add(rule);
        return sf.featureTypeStyle(layer.getId(),
                sf.description(Text.text("MBStyle " + layer.getId()),
                        Text.text("Generated for " + layer.getSourceLayer())),
                null, Collections.emptySet(), Collections.singleton(SemanticType.POINT), rules);
    }

    /**
     * Transform {@link BackgroundMBLayer} to GeoTools FeatureTypeStyle.
     * <p>
     * Notes:
     * </p>
     * <ul>
     * </ul>
     * 
     * @param layer Describing background styling
     * @return FeatureTypeStyle
     */
    FeatureTypeStyle transform(BackgroundMBLayer layer) {
        // TODO - How to create the background polygon?

        Fill fill;
        if (layer.getBackgroundPattern() != null) {
            // TODO Use the background pattern
            fill = sf.fill(null, null, layer.backgroundOpacity());
        } else {
            fill = sf.fill(null, layer.backgroundColor(), layer.backgroundOpacity());
        }

        Symbolizer symbolizer = sf.polygonSymbolizer(layer.getId(),
                // TODO: Is there a better way to do this? Is there a select first geometry syntax?
                ff.property((String) null), sf.description(Text.text("fill"), null), NonSI.PIXEL,
                null, // stroke
                fill, null, ff.literal(0));
        List<Symbolizer> symbolizers = new ArrayList<Symbolizer>();
        symbolizers.add(symbolizer);

        // List of opengis rules here (needed for constructor)
        List<org.opengis.style.Rule> rules = new ArrayList<>();
        Rule rule = sf.rule(layer.getId(), null, null, 0.0, Double.POSITIVE_INFINITY, symbolizers,
                Filter.INCLUDE);
        rule.setLegendGraphic(new Graphic[0]);

        rules.add(rule);
        return sf.featureTypeStyle(layer.getId(),
                sf.description(Text.text("MBStyle " + layer.getId()),
                        Text.text("Generated for " + layer.getSourceLayer())),
                null, // (unused)
                Collections.emptySet(), Collections.singleton(SemanticType.POLYGON), // we only expect this to be applied to polygons
                rules);
    }
    
    /**
     * Transform {@link SymbolMBLayer} to GeoTools FeatureTypeStyle.
     * <p>
     * Notes:
     * </p>
     * <ul>
     * </ul>
     * 
     * @param layer Describing symbol styling
     * @return FeatureTypeStyle
     */
    FeatureTypeStyle transform(SymbolMBLayer layer, MBStyle styleContext) {
        List<Symbolizer> symbolizers = new ArrayList<Symbolizer>();
        Font font = null;
        LabelPlacement labelPlacement;
        LinePlacement linePlacement;
        PointPlacement pointPlacement;

        if (SymbolMBLayer.SymbolPlacement.LINE.equals(layer.getSymbolPlacement())) {
            // TODO complete development of LineSymbolizer


        } else {
            if (layer.getIconImage().isEmpty()) { // icon-image not provided, using default graphic
                PointSymbolizer pointSymbolizer = sf.pointSymbolizer(layer.getId(),ff.property((String) null),
                        sf.description(Text.text("text"), null), NonSI.PIXEL, sf.createDefaultGraphic());
            } else {
                ExternalGraphic eg = createExternalGraphicForSprite(ff.literal(layer.getIconImage()), styleContext);
                PointSymbolizer pointSymbolizer = sf.pointSymbolizer(layer.getId(),ff.property((String) null),
                        sf.description(Text.text("text"), null), NonSI.PIXEL,
                        sf.createGraphic(new ExternalGraphic[] { eg }, null, null, ff.literal(layer.getIconOpacity()), ff.literal(layer.getIconSize()), ff.literal(layer.getIconRotate())));
                symbolizers.add(pointSymbolizer);
            }
        }

        if (!layer.getTextField().isEmpty()) { // A text-field was provided - need a TextSymbolizer.
            Halo halo = sf.halo( sf.fill(null, layer.textHaloColor(), null), layer.textHaloWidth());
            Fill fill = sf.fill(null, layer.textColor(), layer.textOpacity());

            if (layer.getTextFont().isEmpty()) {
                font = sb.createFont(ff.literal(defaultFonts), ff.literal("normal"), ff.literal("normal"), layer.textSize());
            } else {
                font = sb.createFont(ff.literal(layer.getTextFont()), ff.literal("normal"), ff.literal("normal"), layer.textSize());
            }

            if (symbolizers.get(0) instanceof PointSymbolizer) {
                pointPlacement = sb.createPointPlacement();
                pointPlacement.setAnchorPoint(getAnchorPoint(layer.getTextAnchor().toString()));
                pointPlacement.setDisplacement(sb.createDisplacement(layer.getTextOffset()[0], layer.getTextOffset()[1]));
                pointPlacement.setRotation(ff.literal(layer.getTextRotate()));
                labelPlacement = pointPlacement;
            } else {
                // TODO finish lineplacement creation;
                linePlacement = sb.createLinePlacement(null);
                labelPlacement = linePlacement;
            }
            TextSymbolizer symbolizer = sf.textSymbolizer(layer.getId(), ff.property((String) null),
                    sf.description(Text.text("text"), null), NonSI.PIXEL, layer.textField(),
                    font, labelPlacement, halo, fill);
            symbolizers.add(symbolizer);
        }
        // layer.textHaloBlur();
        // symbolizer.getOptions().put("autoWrap", layer.textMaxWidth()); // TODO - Pixels (GS) vs ems (MB); Vendor options with expressions?

        // List of opengis rules here (needed for constructor)
        List<org.opengis.style.Rule> rules = new ArrayList<>();
        Rule rule = sf.rule(layer.getId(), null, null, 0.0, Double.POSITIVE_INFINITY, symbolizers,
                Filter.INCLUDE);
        rule.setLegendGraphic(new Graphic[0]);

        rules.add(rule);
        return sf.featureTypeStyle(layer.getId(),
                sf.description(Text.text("MBStyle " + layer.getId()),
                        Text.text("Generated for " + layer.getSourceLayer())),
                null, // (unused)
                Collections.emptySet(), Collections.singleton(SemanticType.POLYGON), // we only expect this to be applied to polygons
                rules);
    }
    
    /**
     * Takes the name of an icon, and an {@link MBStyle} as a context, and returns an External Graphic referencing the full URL of the image for consumption
     * by the {@link MapboxGraphicFactory}. (The format of the image will be {@link MapboxGraphicFactory#FORMAT}).
     * 
     * @see {@link MapboxGraphicFactory} for more information.
     * 
     * @param iconName The name of the icon inside the spritesheet.
     * @param styleContext The style context in which to resolve the icon name to the full sprite URL (for consumption by the {@link MapboxGraphicFactory}).
     * @return An external graphic with the full URL of the mage for the {@link MapboxGraphicFactory}.
     */
    private ExternalGraphic createExternalGraphicForSprite(Expression iconName, MBStyle styleContext) {
        Expression spriteUrl;
        
        if (styleContext != null && styleContext.getSprite() != null) {
            String spriteBase = styleContext.getSprite().trim() + "?icon=";
            spriteUrl = ff.function("Concatenate", ff.literal(spriteBase),
                    iconName);
        } else {
            spriteUrl = iconName;
        }

        // TODO: (Functions milestone) The icon name can be a function, so evaluating the expression to a string (below) is wrong.
        // Evaluate it for now, because (for now) External Graphics do not take an expression for the URL.
        // TODO: Allow External Graphics to take an expression for the URL
        String spriteUrlStr = spriteUrl.evaluate(null, String.class);

        return sf.createExternalGraphic(spriteUrlStr, MapboxGraphicFactory.FORMAT);

    }

    /**
     * Given a string of "bottom-right" or "top-left" find the x,y coordinates and create an AnchorPoint
     * @param textAnchor The value of the "text-anchor" property in the mapbox style.
     * @return AnchorPoint
     */
    AnchorPoint getAnchorPoint(String textAnchor) {
        Double anchorX = AnchorPosition.valueOf(textAnchor).getAnchorX(textAnchor);
        Double anchorY = AnchorPosition.valueOf(textAnchor).getAnchorY(textAnchor);
        return sb.createAnchorPoint(anchorX, anchorY);
    }

}

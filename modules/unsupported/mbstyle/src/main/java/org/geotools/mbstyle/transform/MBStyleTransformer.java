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

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.mbstyle.*;
import org.geotools.mbstyle.SymbolMBLayer.TextAnchor;
import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.parse.MBObjectStops;
import org.geotools.mbstyle.sprite.SpriteGraphicFactory;
import org.geotools.renderer.style.ExpressionExtractor;
import org.geotools.styling.*;
import org.geotools.text.Text;
import org.geotools.util.logging.Logging;
import org.json.simple.parser.JSONParser;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.style.ContrastMethod;
import org.opengis.style.GraphicFill;
import org.opengis.style.SemanticType;
import org.opengis.style.Symbolizer;

import javax.measure.unit.NonSI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    
    protected static Pattern mapboxTokenPattern = Pattern.compile("\\{(.*?)\\}");
    
    private static final Logger LOGGER = Logging.getLogger(MBStyleTransformer.class);

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
     * @param mbStyle MBStyle
     * @return user layer
     */
    public StyledLayerDescriptor transform(MBStyle mbStyle) {
        JSONParser parser = new JSONParser();

        List<MBLayer> layers = mbStyle.layers();
        if (layers.isEmpty()) {
            throw new MBFormatException("layers empty");
        }


        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        Style style = sf.createStyle();

        for (MBLayer layer : layers) {
            Boolean hasStops = false;
            if (layer.visibility()) {
                if (layer.getPaint() != null) {
                    hasStops = MBObjectStops.hasStops(layer.getPaint());
                }
                if (layer.getLayout() != null) {
                    hasStops = MBObjectStops.hasStops(layer.getPaint());
                }

                FeatureTypeStyle featureTypeStyle = null;

                // TODO reduce property and zoom functions before transformation
                layer = MBObjectStops.reducePropertyAndZoomFunctions(layer);
                featureTypeStyle = transform(layer, mbStyle);
                style.featureTypeStyles().add(featureTypeStyle);

//                List<Long> stopLevels = MBObjectStops.getStopLevels(layer);
//                if (stopLevels.size() > 0 && hasStops) {
//                    List<Long> layerStops = MBObjectStops.getLayerStopLevels(layer);
//                    try {
//                        List<MBLayer> stopLayers = MBObjectStops.getLayerStyleForStops(layer, layerStops);
//                        for (MBLayer l : stopLayers) {
//                            long stopLevel = MBObjectStops.getStop(l);
//                            List<long[]> ranges = MBObjectStops.getStopLevelRanges(stopLevels);
//                            long[] rangeForStopLevel = MBObjectStops.getRangeForStop(stopLevel, ranges);
//                            Double minScaleDenominator = MBObjectStops.zoomLevelToScaleDenominator(rangeForStopLevel[0]);
//                            Double maxScaleDenominator = null;
//                            if (stopLevel != rangeForStopLevel[1] && rangeForStopLevel[1] != -1) {
//                                maxScaleDenominator = MBObjectStops.zoomLevelToScaleDenominator(rangeForStopLevel[1]);
//                            }
//                            System.out.print(l.getJson().toJSONString());
//                            featureTypeStyle = transform(l, mbStyle, minScaleDenominator, maxScaleDenominator);
////                            Rule rule = featureTypeStyle.rules().get(0);
////
////                            rule.setMinScaleDenominator(minScaleDenominator);
////                            rule.setMaxScaleDenominator(maxScaleDenominator);
//
//                            style.featureTypeStyles().add(featureTypeStyle);
//                        }
//                    } catch (ParseException e) {
//
//                    }

//                } else {
                    //featureTypeStyle = transform(layer, mbStyle);

//                }
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
     * Transforms a given {@link MBLayer} to a GeoTools {@link FeatureTypeStyle}.
     * 
     * @param layer The MBLayer to transform.
     * @param styleContext The {@link MBStyle} to use as a context, e.g. for sprite and glyph address resolution.
     * @param minScaleDenominator Used to determine zoom level restructions for generated rules
     * @param maxScaleDenominator Used to determine zoom level restructions for generated rules
     * @return A feature type style from the provided layer.
     */
    public FeatureTypeStyle transform(MBLayer layer, MBStyle styleContext,
            Double minScaleDenominator, Double maxScaleDenominator) {
        // TODO: Would prefer to accept zoom levels here (less concepts in our API)
        // If we accept zoom levels we may be able to reduce, and return a list of FeatureTypeStyles
        // (with the understanding that the list may be empty if the MBLayer does not contribute any content
        //  at a specific zoom level range)
        FeatureTypeStyle style = transform(layer, styleContext);
        for (Rule rule : style.rules()) {
            if (minScaleDenominator != null) {
                rule.setMinScaleDenominator(minScaleDenominator);
            }
            if (maxScaleDenominator != null) {
                rule.setMaxScaleDenominator(maxScaleDenominator);
            }
        }
        
        return style;
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
        } else if (layer instanceof FillExtrusionMBLayer) {
            return transform(layer, styleContext);
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
        if (layer.hasFillPattern()) {
            
            // If the fill-pattern is a literal string (not a function), then
            // we need to support Mapbox {token} replacement.
            Expression fillPatternExpr = layer.fillPattern();
            if (fillPatternExpr instanceof Literal) {
                String text = fillPatternExpr.evaluate(null, String.class);
                if (text.trim().isEmpty()) {
                    fillPatternExpr = ff.literal(" ");
                } else {
                    fillPatternExpr = cqlExpressionFromTokens(text);
                }
            }
            
            ExternalGraphic eg = createExternalGraphicForSprite(fillPatternExpr, styleContext);
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
        
        Rule rule = sf.rule(
                layer.getId(),
                null, 
                null,
                0.0,
                Double.POSITIVE_INFINITY,
                Arrays.asList(symbolizer),
                layer.filter());

        // Set legend graphic to null.
        //TODO: How do other style transformers set a null legend? SLD/SE difference - fix setLegend(null) to empty list.
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
        Rule rule = sf.rule(
                layer.getId(),
                null,
                null,
                0.0,
                Double.POSITIVE_INFINITY,
                Arrays.asList(ls),
                layer.filter());
        
        rules.add(rule);
        return sf.featureTypeStyle(layer.getId(),
                sf.description(Text.text("MBStyle " + layer.getId()),
                        Text.text("Generated for " + layer.getSourceLayer())),
                null, Collections.emptySet(), Collections.singleton(SemanticType.LINE), rules);
    }
    
    /**
     * Transform {@link CircleMBLayer} to GeoTools FeatureTypeStyle.
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
        Rule rule = sf.rule(
                layer.getId(),
                null,
                null,
                0.0,
                Double.POSITIVE_INFINITY,
                Arrays.asList(ps),
                layer.filter());

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
        if (layer.hasBackgroundPattern()) {
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
        Rule rule = sf.rule(
                layer.getId(),
                null,
                null,
                0.0,
                Double.POSITIVE_INFINITY,
                symbolizers,
                layer.filter());
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

        LabelPlacement labelPlacement;
        
        // Create point or line placement
        
        // Functions not yet supported for symbolPlacement, so try to evaluate or use default.
        String symbolPlacementVal = requireLiteral(layer.symbolPlacement(), String.class, "point", "symbol-placement", layer.getId());
        
        if ("point".equalsIgnoreCase(symbolPlacementVal.trim())) {
            // Point Placement (default)            
            PointPlacement pointP = sb.createPointPlacement();
            // Set anchor point (translated by text-translate)
            // TODO - GeoTools AnchorPoint doesn't seem to have an effect on PointPlacement    
            pointP.setAnchorPoint(layer.anchorPoint());

            // MapBox text-offset: +y means down
            Displacement textTranslate = layer.textTranslateDisplacement();             
            textTranslate.setDisplacementY(ff.multiply(ff.literal(-1), textTranslate.getDisplacementY()));
            pointP.setDisplacement(textTranslate);

            pointP.setRotation(layer.textRotate());

            labelPlacement = pointP;
        } else {
            // Line Placement
            
            LinePlacement lineP = sb.createLinePlacement(null);
            lineP.setRepeated(true);
            
            // TODO pixels (geotools) vs ems (mapbox) for text-offset
            lineP.setPerpendicularOffset(
                    ff.multiply(ff.literal(-1), layer.textOffsetDisplacement().getDisplacementY()));

            labelPlacement = lineP;
        }

        Halo halo = sf.halo(sf.fill(null, layer.textHaloColor(), null), layer.textHaloWidth());
        Fill fill = sf.fill(null, layer.textColor(), layer.textOpacity());

        Font font;
        if (layer.getTextFont() == null) {
            font = sb.createFont(ff.literal(defaultFonts), ff.literal("normal"),
                    ff.literal("normal"), layer.textSize());
        } else {
            // TODO fonts
            font = sb.createFont(ff.literal(layer.getTextFont()), ff.literal("normal"),
                    ff.literal("normal"), layer.textSize());
        }

        // If the textField is a literal string (not a function), then
        // we need to support Mapbox token replacement.
        Expression textExpression = layer.textField();
        if (textExpression instanceof Literal) {
            String text = textExpression.evaluate(null, String.class);
            if (text.trim().isEmpty()) {
                textExpression = ff.literal(" ");
            } else {
                textExpression = cqlExpressionFromTokens(text);
            }
        }

        TextSymbolizer2 symbolizer = (TextSymbolizer2) sf.textSymbolizer(layer.getId(),
                ff.property((String) null), sf.description(Text.text("text"), null), NonSI.PIXEL,
                textExpression, font, labelPlacement, halo, fill);        

        // TODO Vendor options can't be expressions.
        Number symbolSpacing = requireLiteral(layer.symbolSpacing(), Number.class, 250,
                "symbol-spacing", layer.getId());
        symbolizer.getOptions().put("repeat", String.valueOf(symbolSpacing));

        // text max angle
        // layer.getTextMaxAngle();
        // symbolizer.getOptions().put("maxAngleDelta", "40");

        // conflictResolution
        // Mapbox allows text overlap and icon overlap separately. GeoTools only has conflictResolution.       
        Boolean textAllowOverlap = requireLiteral(layer.textAllowOverlap(), Boolean.class, false,
                "text-allow-overlap", layer.getId());
        Boolean iconAllowOverlap = requireLiteral(layer.iconAllowOverlap(), Boolean.class, false,
                "icon-allow-overlap", layer.getId());

        symbolizer.getOptions().put("conflictResolution",
                String.valueOf(!(textAllowOverlap || iconAllowOverlap)));      
        
        // TODO Vendor options can't be expressions
        
        String textFitVal = requireLiteral(layer.iconTextFit(), String.class, "none", "icon-text-fit", layer.getId()).trim();                
        if ("height".equalsIgnoreCase(textFitVal) || "width".equalsIgnoreCase(textFitVal)) {
            symbolizer.getOptions().put("graphic-resize",
                    "stretch");
        } else if ("both".equalsIgnoreCase(textFitVal)) {
            symbolizer.getOptions().put("graphic-resize",
                    "proportional");  
        } else {
            // Default
            symbolizer.getOptions().put("graphic-resize",
                    "none");
        }
        
        // TODO Mapbox allows you to sapecify an array of values, one for each side
        if (layer.getIconTextFitPadding() != null && !layer.getIconTextFitPadding().isEmpty()) {
            symbolizer.getOptions().put("graphic-margin",
                    String.valueOf(layer.getIconTextFitPadding().get(0)));
        } else {
            symbolizer.getOptions().put("graphic-margin",
                    "0");
        }

        // halo blur
        // layer.textHaloBlur();

        // auto wrap
        // symbolizer.getOptions().put("autoWrap", layer.textMaxWidth()); // TODO - Pixels (GS) vs ems (MB); Vendor options with expressions?

        // If the layer has an icon image, add it to our symbolizer
        if (layer.hasIconImage()) {

            // If the iconImage is a literal string (not a function), then
            // we need to support Mapbox token replacement.
            // Note: the URL is expected to be a CQL STRING ...
            Expression iconExpression = layer.iconImage();
            if (iconExpression instanceof Literal) {
                iconExpression = cqlExpressionFromTokens(iconExpression.evaluate(null, String.class));
            }

            ExternalGraphic eg = createExternalGraphicForSprite(iconExpression, styleContext);
            // TODO layer.iconSize() - MapBox uses multiplier, GeoTools uses pixels
            Graphic g = sf.graphic(Arrays.asList(eg), layer.iconOpacity(), null,
                    layer.iconRotate(), null, null);
            Displacement d = layer.iconOffsetDisplacement();
            d.setDisplacementY(d.getDisplacementY());
            g.setDisplacement(d);
            symbolizer.setGraphic(g);
        }

        symbolizers.add(symbolizer);

        // List of opengis rules here (needed for constructor)
        List<org.opengis.style.Rule> rules = new ArrayList<>();
        Rule rule = sf.rule(layer.getId(), null, null, 0.0, Double.POSITIVE_INFINITY, symbolizers,
                layer.filter());
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
     *
     * Transform {@link FillExtrusionMBLayer} to GeoTools FeatureTypeStyle.
     */
    FeatureTypeStyle transform(FillExtrusionMBLayer layer, MBStyle styleContext) {
        PolygonSymbolizer symbolizer;

        // from fill pattern or fill color
        Fill fill;

        DisplacementImpl displacement = new DisplacementImpl();

        displacement.setDisplacementX(layer.getFillExtrusionBase().doubleValue());
        displacement.setDisplacementY(layer.getFillExtrusionHeight().doubleValue());

        if (layer.getFillExtrusionPattern() != null) {
            // TODO: Fill graphic (with external graphics)
            ExternalGraphic eg = createExternalGraphicForSprite(layer.getFillExtrusionPattern(), styleContext);
            GraphicFill gf = sf.graphicFill(Arrays.asList(eg), layer.fillExtrusionOpacity(), null, null, null, displacement);
            fill = sf.fill(gf, null, null);
        } else {
            fill = sf.fill(null, layer.fillExtrusionColor(), layer.fillExtrusionOpacity());
        }

        // TODO: Is there a better way to select the first geometry?
        symbolizer = sf.polygonSymbolizer(
                layer.getId(),
                ff.property((String)null),
                sf.description(Text.text("fill"),null),
                NonSI.PIXEL,
                null,
                fill,
                displacement,
                ff.literal(0));

        Rule rule = sf.rule(
                layer.getId(),
                null,
                null,
                0.0,
                Double.POSITIVE_INFINITY,
                Arrays.asList(symbolizer),
                layer.filter());

        // Set legend graphic to null.
        //TODO: How do other style transformers set a null legend? SLD/SE difference - fix setLegend(null) to empty list.
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

    List<FeatureTypeStyle> transform(FillExtrusionMBLayer layer, MBStyle styleContext, Boolean useMultipleFeatureTypeStyles) {
        PolygonSymbolizer symbolizer;

        // from fill pattern or fill color
        Fill fill;

        DisplacementImpl displacement = new DisplacementImpl();

        displacement.setDisplacementX(layer.getFillExtrusionBase().doubleValue());
        displacement.setDisplacementY(layer.getFillExtrusionHeight().doubleValue());

        if (layer.getFillExtrusionPattern() != null) {
            // TODO: Fill graphic (with external graphics)
            ExternalGraphic eg = createExternalGraphicForSprite(layer.getFillExtrusionPattern(), styleContext);
            GraphicFill gf = sf.graphicFill(Arrays.asList(eg), layer.fillExtrusionOpacity(), null, null, null, displacement);
            fill = sf.fill(gf, null, null);
        } else {
            fill = sf.fill(null, layer.fillExtrusionColor(), layer.fillExtrusionOpacity());
        }

        // Creating 3D polygons from the fill Extrusion
        // Extrusions require 3 FeatureTypeStyles 2 with offset functions and 1 isometric function
        // Using hard coded values for now.

        // TODO: figure out how to use property values from the data.
        Expression shadows = ff.function("offset", ff.property("geom"),
                ff.literal(3),
                ff.literal(-3));
        Expression sides = ff.function("isometric", ff.property("geom"), ff.literal(5));
        Expression roofs = ff.function("offset", ff.property("geom"),
                ff.literal(0),
                ff.literal(5));

        List<Expression> extrusionExpressions = new ArrayList<>();
        extrusionExpressions.add(shadows);
        extrusionExpressions.add(sides);
        extrusionExpressions.add(roofs);

        List<Symbolizer> symbolizers = new ArrayList<>();
        List<org.opengis.style.Rule> rules = new ArrayList<>();

        for (Expression e : extrusionExpressions) {
            symbolizer = sf.createPolygonSymbolizer();
            symbolizer.setName(layer.getId());
            symbolizer.setGeometry(e);
            symbolizer.setDescription(sf.description(Text.text("fill"),null));
            symbolizer.setUnitOfMeasure(NonSI.PIXEL);
            symbolizer.setStroke(null);
            symbolizer.setFill(fill);
            symbolizer.setDisplacement(displacement);
//            symbolizer = sf.polygonSymbolizer(
//                    layer.getId(),
//                    e,
//                    sf.description(Text.text("fill"),null),
//                    NonSI.PIXEL,
//                    null,
//                    fill,
//                    displacement,
//                    ff.literal(0));
            symbolizers.add(symbolizer);
        }


        // TODO: Is there a better way to select the first geometry?
        List<FeatureTypeStyle> ftsList = new ArrayList<>();
        for (Symbolizer s : symbolizers) {
            FeatureTypeStyle fs = sf.createFeatureTypeStyle();
            Rule rule = sf.rule(
                    layer.getId(),
                    null,
                    null,
                    0.0,
                    Double.POSITIVE_INFINITY,
                    Arrays.asList(s),
                    layer.filter());
            // Set legend graphic to null.
            //TODO: How do other style transformers set a null legend? SLD/SE difference - fix setLegend(null) to empty list.
            rule.setLegendGraphic(new Graphic[0]);
            fs.rules().add(rule);
            ftsList.add(fs);
        }

        return ftsList;
    }

    /**
     * <p>
     * Takes the name of an icon, and an {@link MBStyle} as a context, and returns an External Graphic referencing the full URL of the image for
     * consumption by the {@link SpriteGraphicFactory}. (The format of the image will be {@link SpriteGraphicFactory#FORMAT}).
     * </p>
     * 
     * @see {@link SpriteGraphicFactory} for more information.
     * 
     * @param iconName The name of the icon inside the spritesheet.
     * @param styleContext The style context in which to resolve the icon name to the full sprite URL (for consumption by the
     *        {@link SpriteGraphicFactory}).
     * @return An external graphic with the full URL of the mage for the {@link SpriteGraphicFactory}.
     */
    private ExternalGraphic createExternalGraphicForSprite(Expression iconName, MBStyle styleContext) {
        String spriteUrl;
        String iconNameCql = ECQL.toCQL(iconName);
        
        /*
         * Note: The provided iconName {@link Expression} will be embedded in the {@link ExternalGraphic}'s URL as a CQL string, in order to support
         * Mapbox functions. The {@link SLDStyleFactory} will transform it back into a proper {@link Expression} before sending it to the {@link
         * SpriteGraphicFactory}.
         */

        if (styleContext != null && styleContext.getSprite() != null) {
            String spriteBase = styleContext.getSprite().trim() + "#";
            spriteUrl = spriteBase + "${" + iconNameCql + "}"; 
        } else {
            spriteUrl = iconNameCql;
        }

        return sf.createExternalGraphic(spriteUrl, SpriteGraphicFactory.FORMAT);
    }

    /**
     * Given a string of "bottom-right" or "top-left" find the x,y coordinates and create an AnchorPoint
     * @param textAnchor The value of the "text-anchor" property in the mapbox style.
     * @return AnchorPoint
     */
    AnchorPoint getAnchorPoint(String textAnchor) {
        TextAnchor anchor = TextAnchor.parse(textAnchor);
        return sb.createAnchorPoint(anchor.getX(), anchor.getY());
    }     
    
    /**
     * <p>Take a string that may contain Mapbox-style tokens, and convert it to a CQL expression string.</p>
     * 
     * <p>E.g., convert "<code>String with {tokens}</code>" to a CQL Expression (String) "<code>String with ${tokens}</code>".</p>
     * 
     * <p>See documentation of Mapbox {token} values, linked below.</p>
     * 
     * @see <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/#layout-symbol-icon-image">Mapbox Style Spec: {token} values for icon-image</a>
     * @see <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/#layout-symbol-text-field">Mapbox Style Spec: {token} values for text-field</a>
     * 
     * @param tokenStr A string with mapbox-style tokens
     * @return A CQL Expression
     */
    public String cqlStringFromTokens(String tokenStr) {
        // Find all {tokens} and turn them into CQL ${expressions}
        Matcher m = mapboxTokenPattern.matcher(tokenStr);
        return m.replaceAll("\\${$1}");       
    }

    /**
     * <p>Take a string that may contain Mapbox-style tokens, and convert it to a CQL expression.</p>
     * 
     * <p>E.g., convert "<code>String with {tokens}</code>" to a CQL Expression: "<code>String with ${tokens}</code>".</p>
     * 
     * <p>See documentation of Mapbox {token} values, linked below.</p>
     * 
     * @see <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/#layout-symbol-icon-image">Mapbox Style Spec: {token} values for icon-image</a>
     * @see <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/#layout-symbol-text-field">Mapbox Style Spec: {token} values for text-field</a>
     * 
     * @param tokenStr A string with mapbox-style tokens
     * @return A CQL Expression
     */
    public Expression cqlExpressionFromTokens(String tokenStr) {
        try {
            return ExpressionExtractor.extractCqlExpressions(cqlStringFromTokens(tokenStr));
        } catch (IllegalArgumentException iae) {
            LOGGER.warning(
                    "Exception converting Mapbox token string to CQL expression. Mapbox token string was: \""
                            + tokenStr + "\". Exception was: " + iae.getMessage());
            return ff.literal(tokenStr);
        }
    }

    /**
     * Utility method for getting a concrete value out of an expression, used by transformer methods when GeoTools is unable to accept an expression.
     * <ul>
     * <li>If the provided {@link Expression} is a {@link Literal}, evaluates it and returns the value.</li>
     * <li>Otherwise, returns the provided fallback value and logs a warning that dynamic styling is not yet supported for this property.</li>
     * </ul>
     * 
     * @param expression The expression
     * @param clazz The type to provide as the context for the expression's evaluation.
     * @param fallback The value to return if the expression is not a literal
     * @param propertyName The name of the property that the expression corresponds to, for logging purposes.
     * @param layerId The ID of the layer that the expression corresponds to, for logging purposes.
     * @return The evaluated value of the provided {@link Expression}, or the provided fallback value.
     */
    protected static <T> T requireLiteral(Expression expression, Class<T> clazz, T fallback,
            String propertyName, String layerId) {
        if (expression instanceof Literal) {
            T value = expression.evaluate(null, clazz);
            if (value != null) {
                return value;
            } else {
                return fallback;
            }
        } else {
            LOGGER.warning("Mapbox '" + propertyName
                    + "' property: functions not yet supported for this property, falling back to default value."
                    + " (layerId = '" + layerId + "')");
            return fallback;
        }
    }

}

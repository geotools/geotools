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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.measure.unit.NonSI;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.mbstyle.FillMBLayer;
import org.geotools.mbstyle.LineMBLayer;
import org.geotools.mbstyle.MBFormatException;
import org.geotools.mbstyle.MBLayer;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.RasterMBLayer;
import org.geotools.styling.*;
import org.geotools.text.Text;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ContrastMethod;
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

    public MBStyleTransformer() {
        ff = CommonFactoryFinder.getFilterFactory2();
        sf = CommonFactoryFinder.getStyleFactory();
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
            FeatureTypeStyle featureTypeStyle = transform(layer);
            style.featureTypeStyles().add(featureTypeStyle);
        }
        UserLayer userLayer = sf.createUserLayer();
        userLayer.userStyles().add(style);
        
        sld.layers().add(userLayer);
        sld.setName(mbStyle.getName());
        return sld;
    }

    public FeatureTypeStyle transform(MBLayer layer) {
        if (layer instanceof FillMBLayer) {
            return transform((FillMBLayer) layer);
        } else if (layer instanceof RasterMBLayer) {
            return transform((RasterMBLayer) layer);
        } else if (layer instanceof LineMBLayer) {
            return transform((LineMBLayer) layer);
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
     * @return FeatureTypeStyle 
     */
    public FeatureTypeStyle transform(FillMBLayer layer) {
        PolygonSymbolizer symbolizer;
        // use of factory is more verbose, but we supply every value (no defaults)
        
        // stroke from fill outline color and opacity
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
        if( layer.getFillPattern() != null ){
            fill = sf.fill(null,null, layer.fillOpacity());
        }
        else {
            fill = sf.fill(null,  layer.fillColor(),  layer.fillOpacity());
        }
        // String name, Expression geometry,
        symbolizer = sf.polygonSymbolizer(
                 layer.getId(),
                 //TODO: Is there a better way to do this? Is there a select first geometry syntax?
                 ff.property((String)null),
                 sf.description(Text.text("fill"),null),
                 NonSI.PIXEL,
                 stroke,
                 fill,
                 layer.toDisplacement(),
                 ff.literal(0));
        List<Symbolizer> symbolizers = new ArrayList<Symbolizer>();
        symbolizers.add(symbolizer);
        
        // List of opengis rules here (needed for constructor)
        List<org.opengis.style.Rule> rules = new ArrayList<>();
        Rule rule = sf.rule(layer.getId(), null,  null, 0.0, Double.POSITIVE_INFINITY,symbolizers, Filter.INCLUDE);
        //TODO: How do other style transformers set a null legend?
        rule.setLegendGraphic(new Graphic[0]);
        rules.add(rule);
        return sf.featureTypeStyle(
                layer.getId(),
                sf.description(
                        Text.text("MBStyle "+layer.getId()),
                        Text.text("Generated for "+layer.getSourceLayer())),
                null, // (unused)
                Collections.emptySet(),
                Collections.singleton(SemanticType.POLYGON), // we only expect this to be applied to polygons 
                rules
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
                sf.description(Text.text("raster"), null), NonSI.PIXEL, layer.getOpacity(), sel,
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

}

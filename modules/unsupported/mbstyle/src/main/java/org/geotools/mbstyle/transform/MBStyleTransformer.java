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
import java.util.Collections;
import java.util.List;

import javax.measure.unit.NonSI;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.mbstyle.FillMBLayer;
import org.geotools.mbstyle.MBFormatException;
import org.geotools.mbstyle.MBLayer;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.RasterMBLayer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.geotools.text.Text;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
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
        if( layer instanceof FillMBLayer){
            return transform( (FillMBLayer) layer );
        }
//        else if( layer instanceof RasterMBLayer){
//            return transform( (RasterMBLayer) layer );
//        }
        return null;
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
                 ff.property("."),
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
        Rule rule = sf.rule(layer.getId(), null,  null, 0.0, Double.MAX_VALUE,symbolizers, Filter.INCLUDE);
        rules.add(rule);
        return sf.featureTypeStyle(
                layer.getId(),
                sf.description(
                        Text.text("MBStil "+layer.getId()),
                        Text.text("Generated for "+layer.getSourceLayer())),
                null, // (unused)
                Collections.emptySet(),
                Collections.singleton(SemanticType.POLYGON), // we only expect this to be applied to polygons 
                rules
                );
    }
}

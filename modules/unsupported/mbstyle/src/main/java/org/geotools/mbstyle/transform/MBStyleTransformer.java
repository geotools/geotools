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
import org.geotools.mbstyle.MBFillLayer;
import org.geotools.mbstyle.MBStyle;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.opengis.filter.FilterFactory2;
import org.opengis.style.FeatureTypeStyle;

/**
 * Responsible for traverse {@link MBStyle} and generating {@link StyledLayerDescriptor}.
 * 
 * @author Jody Garnett (Jody Garnett)
 */
public class MBStyleTransformer {

    private FilterFactory2 ff;

    private StyleFactory sf;

    private StyleBuilder builder;

    public MBStyleTransformer() {
        ff = CommonFactoryFinder.getFilterFactory2();
        sf = CommonFactoryFinder.getStyleFactory();
        builder = new StyleBuilder(sf, ff);
    }

    /**
     * Transform MBStyle to a GeoTools UserLayer.
     * 
     * @param layer MBStyle
     * @return user layer
     */
    UserLayer tranform(MBStyle style) {
        return null;
    }

    /**
     * Transform MBFillLayer to GeoTools FeatureTypeStyle.
     * 
     * @param fill
     * @return
     */
    FeatureTypeStyle transform(MBFillLayer fill) {
        PolygonSymbolizer symbolizer = builder.createPolygonSymbolizer();
        symbolizer.setFill( builder.createFill(fill.getFillColor() ));
        
        return builder.createFeatureTypeStyle(symbolizer);
    }
}

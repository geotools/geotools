/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */

package org.geotools.demo.filter;

import java.net.URL;


import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.styling.Fill;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.swing.JMapFrame;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

public class InterpolateFunctionExample {

    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    private JMapFrame frame;
    private AbstractGridCoverage2DReader reader;

    public static void main(String[] args) throws Exception {
        InterpolateFunctionExample me = new InterpolateFunctionExample();
        me.displayShapefile();
    }

    private void displayShapefile() throws Exception {
        URL url = getClass().getResource("/data/shapefiles/countries.shp");

        FileDataStore store = FileDataStoreFinder.getDataStore(url);
        FeatureSource featureSource = store.getFeatureSource();

        // Create a map context and add our shapefile to it
        MapContext map = new DefaultMapContext();
        map.setTitle("Interpolated polygon fill color");
        map.addLayer(featureSource, createInterpolatedStyle());

        // Now display the map
        JMapFrame.showMap(map);
    }
    
    private Style createInterpolatedStyle() {
        Expression[] params = {
            // lookup value is population size
            ff.property("POP_CNTRY"),

            // interp points
            ff.literal(0),
            ff.literal("#0000ff"),

            ff.literal(1.0e+7),
            ff.literal("#00ff00"),

            ff.literal(1.0e+9),
            ff.literal("#ff0000"),

            // specify linear colour interpolation
            ff.literal("linear"),
            ff.literal("color")
        };

        Function fn = ff.function("Interpolate", params);
        Fill fill = sf.createFill(fn);
        Stroke stroke = sf.createStroke(ff.literal("#000000"), ff.literal(1.0));
        PolygonSymbolizer sym = sf.createPolygonSymbolizer(stroke, fill, null);
        return SLD.wrapSymbolizers(new Symbolizer[]{sym});
    }
}


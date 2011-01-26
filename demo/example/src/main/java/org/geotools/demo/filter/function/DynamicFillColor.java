/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.filter.function;

import java.net.URL;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Demonstrates using a custom-written filter function to dynamically set
 * the fill and outline color of polygon features based on the value of a
 * specified feature attribute. Here we use the countries shapefile and
 * the log of population size as the value to base color on.
 *
 * @author Michael Bedward
 *
 * @source $URL$
 */
public class DynamicFillColor {

    private FilterFactory filterFactory;
    private StyleFactory styleFactory;

    /**
     * Construtor - just initializes our factory objects
     */
    public DynamicFillColor() {
        filterFactory = CommonFactoryFinder.getFilterFactory(null);
        styleFactory = CommonFactoryFinder.getStyleFactory(null);
    }

    /**
     * Creates a new Style object for polygon features in which the fill
     * and outline stroke colors will be based on the specified feature
     * attribute (a numeric field).
     *
     * @param attrName the name of the feature attribute to use as the
     *        value field
     *
     * @param minValue the minimum expected feature value
     *
     * @param maxValue the maximum expected feature value
     *
     * @param logValues if true, calculations will use the log of feature
     *        value (in this case min and max value parameters should be on
     *        the log scale); if false feature values are used directly
     *
     * @return a new Style object containing the dynamic color function
     */
    public Style createColorRampStyle(String attrName, float minValue, float maxValue, boolean logValues) {
        Style style = styleFactory.createStyle();

        Expression valueExpr = null;
        if (logValues) {
            /*
             * Here we use one of GeoTools provided maths functions which
             * will calculate log values
             */
            valueExpr = filterFactory.function("log", filterFactory.property(attrName));

        } else {
            /*
             * Here we specify the feature attribute which will be used
             * directly
             */
            valueExpr = filterFactory.property(attrName);
        }

        /*
         * Create a new function object and set it as the basis for Fill 
         * and Stroke colors
         */
        ColorRampFunction fn = new ColorRampFunction(valueExpr, minValue, maxValue, 0.8f, 0.8f);
        Fill fill = styleFactory.createFill(fn);
        Stroke stroke = styleFactory.createStroke(fn, filterFactory.literal(1.0));
        PolygonSymbolizer symbolizer = styleFactory.createPolygonSymbolizer(stroke, fill, "the_geom");

        /*
         * Package up our dynamic symbolizer created above in the hierarchy
         * of rule, feature type style, style
         */
        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(symbolizer);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        style.featureTypeStyles().add(fts);

        return style;
    }

    /**
     * Main method, runs a demo displaying the countries shapefile with fill
     * and outline colors based on population data
     *
     * @param args ignored
     *
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        DynamicFillColor me = new DynamicFillColor();
        me.demo();
    }

    /**
     *
     * @throws java.lang.Exception
     */
    private void demo() throws Exception {
        URL url = DynamicFillColor.class.getResource("/data/shapefiles/countries.shp");
        ShapefileDataStore shapefile = new ShapefileDataStore(url);

        /*
         * Get the feature source and coordinate reference system
         * from the shapefile
         */
        String typeName = shapefile.getTypeNames()[0];
        FeatureSource featureSource = shapefile.getFeatureSource(typeName);
        FeatureType schema = featureSource.getSchema();
        CoordinateReferenceSystem crs = schema.getCoordinateReferenceSystem();

        /*
         * Create the dynamic fill Style using the POP_CNTRY attribute
         * and specifying that we want to use log values (5 and 22 are
         * approximate limits of the population data on the log scale)
         */
        Style style = createColorRampStyle("POP_CNTRY", 5, 22, true);

        /*
         * Create our map and dispaly it
         */
        MapContext map = new DefaultMapContext(crs);
        map.addLayer(featureSource, style);

        JMapFrame.showMap(map);
    }
}

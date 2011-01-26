// docs start source
/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.tutorial.style;

import java.awt.Color;
import java.io.File;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.ExceptionMonitor;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.geotools.swing.styling.JSimpleStyleDialog;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class StyleLab {

    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    public static void main(String[] args) throws Exception {
        StyleLab me = new StyleLab();
        me.displayShapefile();
    }

    // docs end main

    // docs start display
    /**
     * Prompts the user for a shapefile (unless a filename is provided
     * on the command line; then creates a simple Style and displays
     * the shapefile on screen
     */
    private void displayShapefile() throws Exception {
        File file = JFileDataStoreChooser.showOpenFile("shp", null);
        if (file == null) {
            return;
        }

        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        FeatureSource featureSource = store.getFeatureSource();

        // Create a map context and add our shapefile to it
        MapContext map = new DefaultMapContext();
        map.setTitle("StyleLab");

        // Create a basic Style to render the features
        Style style = createStyle(file, featureSource);

        // Add the features and the associated Style object to
        // the MapContext as a new MapLayer
        map.addLayer(featureSource, style);

        // Now display the map
        JMapFrame.showMap(map);
    }

    // docs end display

    // docs start create style
    /**
     * Create a Style to display the features. If an SLD file is in the same
     * directory as the shapefile then we will create the Style by processing
     * this. Otherwise we display a JSimpleStyleDialog to prompt the user for
     * preferences.
     */
    private Style createStyle(File file, FeatureSource featureSource) {
        File sld = toSLDFile(file);
        if (sld != null) {
            return createFromSLD(sld);
        }

        SimpleFeatureType schema = (SimpleFeatureType)featureSource.getSchema();
        return JSimpleStyleDialog.showDialog(null, schema);
    }

    // docs end create style

    // docs start sld
    /**
     * Figure out if a valid SLD file is available.
     */
    public File toSLDFile(File file)  {
        String path = file.getAbsolutePath();
        String base = path.substring(0,path.length()-4);
        String newPath = base + ".sld";
        File sld = new File( newPath );
        if( sld.exists() ){
            return sld;
        }
        newPath = base + ".SLD";
        sld = new File( newPath );
        if( sld.exists() ){
            return sld;
        }
        return null;
    }

    /**
     * Create a Style object from a definition in a SLD document
     */
    private Style createFromSLD(File sld) {
        try {
            SLDParser stylereader = new SLDParser(styleFactory, sld.toURI().toURL());
            Style[] style = stylereader.readXML();
            return style[0];
            
        } catch (Exception e) {
            ExceptionMonitor.show(null, e, "Problem creating style");
        }
        return null;
    }

    // docs end sld


    // docs start alternative
    /**
     * Here is a programmatic alternative to using JSimpleStyleDialog to
     * get a Style. This methods works out what sort of feature geometry
     * we have in the shapefile and then delegates to an appropriate style
     * creating method.
     */
    private Style createStyle2(FeatureSource featureSource) {
        SimpleFeatureType schema = (SimpleFeatureType)featureSource.getSchema();
        Class geomType = schema.getGeometryDescriptor().getType().getBinding();

        if (Polygon.class.isAssignableFrom(geomType)
                || MultiPolygon.class.isAssignableFrom(geomType)) {
            return createPolygonStyle();

        } else if (LineString.class.isAssignableFrom(geomType)
                || MultiLineString.class.isAssignableFrom(geomType)) {
            return createLineStyle();

        } else {
            return createPointStyle();
        }
    }

    /**
     * Create a Style to draw polygon features with a thin blue outline and
     * a cyan fill
     */
    private Style createPolygonStyle() {

        // create a partially opaque outline stroke
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(Color.BLUE),
                filterFactory.literal(1),
                filterFactory.literal(0.5));

        // create a partial opaque fill
        Fill fill = styleFactory.createFill(
                filterFactory.literal(Color.CYAN),
                filterFactory.literal(0.5));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }
    
    /**
     * Create a Style to draw line features as thin blue lines
     */
    private Style createLineStyle() {
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(Color.BLUE),
                filterFactory.literal(1));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        LineSymbolizer sym = styleFactory.createLineSymbolizer(stroke, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    /**
     * Create a Style to draw point features as circles with blue outlines
     * and cyan fill
     */
    private Style createPointStyle() {
        Graphic gr = styleFactory.createDefaultGraphic();

        Mark mark = styleFactory.getCircleMark();

        mark.setStroke(styleFactory.createStroke(
                filterFactory.literal(Color.BLUE), filterFactory.literal(1)));

        mark.setFill(styleFactory.createFill(filterFactory.literal(Color.CYAN)));

        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(mark);
        gr.setSize(filterFactory.literal(5));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

}

// docs end source

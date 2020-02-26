/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.swt.utils;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.Layer;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.swt.control.ExceptionMonitor;
import org.geotools.util.factory.GeoTools;
import org.geotools.xml.styling.SLDParser;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.style.ContrastMethod;

/**
 * Utilities class.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class Utils {
    /** The default {@link StyleFactory} to use. */
    public static StyleFactory styleFactory =
            CommonFactoryFinder.getStyleFactory(GeoTools.getDefaultHints());

    /** The default {@link FilterFactory} to use. */
    public static FilterFactory filterFactory =
            CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());

    /** The default {@link StyleBuilder} to use. */
    public static StyleBuilder sb = new StyleBuilder(styleFactory, filterFactory);

    private static final Class<?> BASE_GRID_CLASS = org.opengis.coverage.grid.GridCoverage.class;

    private static final Class<?> BASE_READER_CLASS =
            org.opengis.coverage.grid.GridCoverageReader.class;

    /**
     * Sets the location of the shell to the center of the screen.
     *
     * @param shell the shell to place.
     */
    public static void setShellLocation(Shell shell) {
        Rectangle monitorArea = shell.getDisplay().getPrimaryMonitor().getBounds();
        Rectangle shellArea = shell.getBounds();
        int x = monitorArea.x + (monitorArea.width - shellArea.width) / 2;
        int y = monitorArea.y + (monitorArea.height - shellArea.height) / 2;
        shell.setLocation(x, y);
    }

    /**
     * Transform an awt {@link java.awt.Rectangle} instance into a swt one.
     *
     * <p>The coordinates are rounded to integer for the swt object.
     *
     * @param rect2d The awt rectangle to map.
     * @return an swt <code>Rectangle</code> object.
     */
    public static Rectangle toSwtRectangle(java.awt.Rectangle rect2d) {
        return new Rectangle(
                (int) Math.round(rect2d.getMinX()),
                (int) Math.round(rect2d.getMinY()),
                (int) Math.round(rect2d.getWidth()),
                (int) Math.round(rect2d.getHeight()));
    }

    /**
     * Transform a swt Rectangle instance into an awt one.
     *
     * @param rect the swt <code>Rectangle</code>
     * @return a {@link java.awt.Rectangle} instance with the appropriate location and size.
     */
    public static java.awt.Rectangle toAwtRectangle(Rectangle rect) {
        java.awt.Rectangle rect2d = new java.awt.Rectangle();
        rect2d.setRect(rect.x, rect.y, rect.width, rect.height);
        return rect2d;
    }

    /**
     * Create a Style to display the features.
     *
     * <p>If an SLD file is in the same directory as the shapefile then we will create the Style by
     * processing this.
     */
    public static Style createStyle(File file, SimpleFeatureSource featureSource) {
        File sld = toSLDFile(file);
        if (sld != null) {
            return createFromSLD(sld);
        }

        return createStyle2(featureSource);
    }

    /**
     * Figure out if a valid SLD file is available.
     *
     * @param file the file to search for style sidecar file.
     * @return the style file or null.
     */
    public static File toSLDFile(File file) {
        String path = file.getAbsolutePath();
        String base = path.substring(0, path.length() - 4);
        String newPath = base + ".sld";
        File sld = new File(newPath);
        if (sld.exists()) {
            return sld;
        }
        newPath = base + ".SLD";
        sld = new File(newPath);
        if (sld.exists()) {
            return sld;
        }
        return null;
    }

    /**
     * Create a Style object from a definition in a SLD document
     *
     * @param sld the sld file.
     * @return the created {@link Style} or <code>null</code>.
     */
    public static Style createFromSLD(File sld) {
        try {
            SLDParser stylereader = new SLDParser(styleFactory, sld.toURI().toURL());
            Style[] style = stylereader.readXML();
            return style[0];

        } catch (Exception e) {
            ExceptionMonitor.show(null, e, "Problem creating style");
        }
        return null;
    }

    /**
     * Create a default {@link Style} ofr the featureSource.
     *
     * @param featureSource the source on which to create the style.
     * @return the style created.
     */
    public static Style createStyle2(SimpleFeatureSource featureSource) {
        SimpleFeatureType schema = (SimpleFeatureType) featureSource.getSchema();
        Class<?> geomType = schema.getGeometryDescriptor().getType().getBinding();

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
     * Create a default polygon style.
     *
     * @return the created style.
     */
    public static Style createPolygonStyle() {

        // create a partially opaque outline stroke
        Stroke stroke =
                styleFactory.createStroke(
                        filterFactory.literal(Color.BLUE),
                        filterFactory.literal(1),
                        filterFactory.literal(0.5));

        // create a partial opaque fill
        Fill fill =
                styleFactory.createFill(
                        filterFactory.literal(Color.CYAN), filterFactory.literal(0.5));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] {rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    /**
     * Create a default line style.
     *
     * @return the created style.
     */
    public static Style createLineStyle() {
        Stroke stroke =
                styleFactory.createStroke(
                        filterFactory.literal(Color.BLUE), filterFactory.literal(1));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        LineSymbolizer sym = styleFactory.createLineSymbolizer(stroke, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] {rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    /**
     * Create a default point style.
     *
     * @return the created style.
     */
    public static Style createPointStyle() {
        Graphic gr = styleFactory.createDefaultGraphic();

        Mark mark = styleFactory.getCircleMark();

        mark.setStroke(
                styleFactory.createStroke(
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
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] {rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    /**
     * Run a {@link Runnable} that needs to run in the Display thread.
     *
     * @param runner the runnable to run.
     * @param sync if <code>true</code>, the runnable is run in sync mode, else in async.
     */
    public static void runGuiRunnableSafe(Runnable runner, boolean sync) {
        if (Display.getCurrent() != null) {
            runner.run();
        } else {
            if (sync) {
                Display.getDefault().syncExec(runner);
            } else {
                Display.getDefault().asyncExec(runner);
            }
        }
    }

    /**
     * This method examines the names of the sample dimensions in the provided coverage looking for
     * "red...", "green..." and "blue..." (case insensitive match). If these names are not found it
     * uses bands 1, 2, and 3 for the red, green and blue channels. It then sets up a raster
     * symbolizer and returns this wrapped in a Style.
     *
     * @return a new Style object containing a raster symbolizer set up for RGB image
     */
    public static Style createRGBStyle(GridCoverage2DReader reader) {
        GridCoverage2D cov = null;
        try {
            cov = reader.read(null);
        } catch (IOException giveUp) {
            throw new RuntimeException(giveUp);
        }
        // We need at least three bands to create an RGB style
        int numBands = cov.getNumSampleDimensions();
        if (numBands < 3) {
            return null;
        }
        // Get the names of the bands
        String[] sampleDimensionNames = new String[numBands];
        for (int i = 0; i < numBands; i++) {
            GridSampleDimension dim = cov.getSampleDimension(i);
            sampleDimensionNames[i] = dim.getDescription().toString();
        }
        final int RED = 0, GREEN = 1, BLUE = 2;
        int[] channelNum = {-1, -1, -1};
        // We examine the band names looking for "red...", "green...", "blue...".
        // Note that the channel numbers we record are indexed from 1, not 0.
        for (int i = 0; i < numBands; i++) {
            String name = sampleDimensionNames[i].toLowerCase();
            if (name != null) {
                if (name.matches("red.*")) {
                    channelNum[RED] = i + 1;
                } else if (name.matches("green.*")) {
                    channelNum[GREEN] = i + 1;
                } else if (name.matches("blue.*")) {
                    channelNum[BLUE] = i + 1;
                }
            }
        }
        // If we didn't find named bands "red...", "green...", "blue..."
        // we fall back to using the first three bands in order
        if (channelNum[RED] < 0 || channelNum[GREEN] < 0 || channelNum[BLUE] < 0) {
            channelNum[RED] = 1;
            channelNum[GREEN] = 2;
            channelNum[BLUE] = 3;
        }
        // Now we create a RasterSymbolizer using the selected channels
        SelectedChannelType[] sct = new SelectedChannelType[cov.getNumSampleDimensions()];
        ContrastEnhancement ce =
                styleFactory.contrastEnhancement(
                        filterFactory.literal(1.0), ContrastMethod.NORMALIZE);
        for (int i = 0; i < 3; i++) {
            sct[i] = styleFactory.createSelectedChannelType(String.valueOf(channelNum[i]), ce);
        }
        RasterSymbolizer sym = styleFactory.getDefaultRasterSymbolizer();
        ChannelSelection sel = styleFactory.channelSelection(sct[RED], sct[GREEN], sct[BLUE]);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }

    /**
     * Check if the given map layer contains a grid coverage or a grid coverage reader.
     *
     * <p>Implementation note: we avoid referencing org.geotools.coverage.grid classes directly here
     * so that applications dealing only with other data types are not forced to have JAI in the
     * classpath.
     *
     * @param layer the map layer
     * @return true if this is a grid layer; false otherwise
     */
    public static boolean isGridLayer(Layer layer) {

        Collection<PropertyDescriptor> descriptors =
                layer.getFeatureSource().getSchema().getDescriptors();
        for (PropertyDescriptor desc : descriptors) {
            Class<?> binding = desc.getType().getBinding();

            if (BASE_GRID_CLASS.isAssignableFrom(binding)
                    || BASE_READER_CLASS.isAssignableFrom(binding)) {
                return true;
            }
        }

        return false;
    }

    public static String getGridAttributeName(Layer layer) {
        String attrName = null;

        Collection<PropertyDescriptor> descriptors =
                layer.getFeatureSource().getSchema().getDescriptors();
        for (PropertyDescriptor desc : descriptors) {
            Class<?> binding = desc.getType().getBinding();

            if (BASE_GRID_CLASS.isAssignableFrom(binding)
                    || BASE_READER_CLASS.isAssignableFrom(binding)) {
                attrName = desc.getName().getLocalPart();
                break;
            }
        }

        return attrName;
    }
}

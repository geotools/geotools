/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.shape;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.shapefile.Lock;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStore;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStoreFactory;
import org.geotools.data.shapefile.indexed.ShapeFileIndexer;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.resources.TestData;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryFinder;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;

import com.vividsolutions.jts.geom.Envelope;

/**
 * @TODO class description
 * 
 * @author jeichar
 * @since 2.1.x
 *
 * @source $URL$
 */
public class Timing {

    private static final FilterFactory filterFactory = FilterFactoryFinder
            .createFilterFactory();

    private static final int POINTS = 0;

    private static final int LINES = 1;

    private static final int POLYGONS = 2;

    private static boolean ALL_DATA = true;

    private static boolean DISPLAY = false;

    private static boolean ANTI_ALIASING = false;

    private static boolean RUN_SHAPE = true;

    private static boolean RUN_LITE = true;

    private static boolean RUN_TINY = false;

    private static boolean ACCURATE = true;

    private static boolean NO_REPROJECTION = true;

    private static boolean FILTER = false;

    private static boolean CPU_PROFILE = false;

    private static int SHAPE_TYPE = POLYGONS;

    private static boolean LABELING = false;

    private static boolean QUADTREE = false;

    private static int CYCLES = 4;

    private String getTestName() {
        String testName = "";
        if (SHAPE_TYPE == LINES) {
            testName += LINES_TYPE_NAME;
        } else if (SHAPE_TYPE == POLYGONS) {
            testName += POLY_TYPE_NAME;
        } else if (SHAPE_TYPE == POINTS) {
            testName += POINT_TYPE_NAME;
        }
        if (ALL_DATA) {
            testName += "_ALL";
        } else {
            testName += "_ZOOM";
        }
        if (ACCURATE) {
            testName += "_ACCURATE";
        } else {
            testName += "_INACCURATE";
        }
        if (NO_REPROJECTION) {
            testName += "_NO_REPROJECTION";
        } else {
            testName += "_REPROJECTED";
        }
        if (FILTER) {
            testName += "_FILTER";
        } else {
            testName += "_NO_FILTER";
        }
        if (CPU_PROFILE) {
            testName += "_PROFILE";
        }
        if (QUADTREE) {
            testName += "_QUADTREE";
        }
        return testName;
    }

    public final static FileWriter out;

    static {
        FileWriter tmp;
        try {
            String homePath = System.getProperty("user.home");
            File results = new File(homePath, "TimingResults.txt");
            tmp = new FileWriter(results, true);
        } catch (IOException e) {
            tmp = null;
            e.printStackTrace();
        }
        out = tmp;
    }

    static Style createLineStyle() throws Exception {
        return createLineStyle(null);
    }

    static Style createLineStyle(String typeName) throws Exception {
        if (typeName == null)
            typeName = LINES_TYPE_NAME;
        StyleFactory sFac = StyleFactoryFinder.createStyleFactory();
        // The following is complex, and should be built from

        LineSymbolizer linesym = sFac.createLineSymbolizer();
        Stroke myStroke = sFac.getDefaultStroke();
        myStroke.setColor(filterFactory.createLiteralExpression("#0000ff"));
        myStroke
                .setWidth(filterFactory.createLiteralExpression(new Integer(2)));
        linesym.setStroke(myStroke);

        Rule rule2 = sFac.createRule();
        rule2.setSymbolizers(new Symbolizer[] { linesym });
        if (FILTER) {
            AttributeExpression exp = filterFactory
                    .createAttributeExpression("STREETS");
            CompareFilter filter = filterFactory
                    .createCompareFilter(Filter.COMPARE_NOT_EQUALS);
            filter.addLeftValue(exp);
            filter.addRightValue(filterFactory.createLiteralExpression("blah"));
            rule2.setFilter(filter);
        }
        if (LABELING) {
            StyleBuilder builder = new StyleBuilder();
            TextSymbolizer textsym = sFac.createTextSymbolizer();
            textsym.setFill(sFac.getDefaultFill());
            textsym.setGeometryPropertyName("the_geom");
            textsym
                    .setLabel(filterFactory
                            .createLiteralExpression(LINES_LABEL));
            textsym.setFonts(new Font[] { builder.createFont(new java.awt.Font(
                    "Arial", java.awt.Font.PLAIN, 10)) });
            rule2.setSymbolizers(new Symbolizer[] { linesym, textsym });
        }
        FeatureTypeStyle fts2 = sFac.createFeatureTypeStyle();
        fts2.setRules(new Rule[] { rule2 });
        fts2.setFeatureTypeName(typeName);

        Style style = sFac.createStyle();
        style.setFeatureTypeStyles(new FeatureTypeStyle[] { fts2 });

        return style;
    }

    static Style createPolyStyle() throws Exception {
        return createPolyStyle(null);
    }

    static Style createPolyStyle(String typeName) throws Exception {
        if (typeName == null)
            typeName = POLY_TYPE_NAME;
        StyleFactory sFac = StyleFactoryFinder.createStyleFactory();
        // The following is complex, and should be built from
        Stroke myStroke = sFac.getDefaultStroke();
        myStroke.setColor(filterFactory.createLiteralExpression("#0000ff"));
        myStroke
                .setWidth(filterFactory.createLiteralExpression(new Integer(2)));
        Fill myFill = sFac.getDefaultFill();
        PolygonSymbolizer lineSym = sFac.createPolygonSymbolizer(myStroke,
                myFill, "the_geom");

        Rule rule2 = sFac.createRule();
        rule2.setSymbolizers(new Symbolizer[] { lineSym });
        if (FILTER) {
            AttributeExpression exp = filterFactory
                    .createAttributeExpression(POLY_LABEL);
            CompareFilter filter = filterFactory
                    .createCompareFilter(Filter.COMPARE_NOT_EQUALS);
            filter.addLeftValue(exp);
            filter.addRightValue(filterFactory.createLiteralExpression("blah"));
            rule2.setFilter(filter);
        }
        if (LABELING) {
            StyleBuilder builder = new StyleBuilder();

            TextSymbolizer textsym = sFac.createTextSymbolizer();
            textsym.setFill(sFac.createFill(filterFactory
                    .createLiteralExpression("#000000")));
            textsym.setGeometryPropertyName("the_geom");
            textsym.setLabel(filterFactory
                    .createAttributeExpression(POLY_LABEL));
            textsym.setFonts(new Font[] { builder.createFont(new java.awt.Font(
                    "Arial", java.awt.Font.PLAIN, 10)) });
            rule2.setSymbolizers(new Symbolizer[] { lineSym, textsym });
        }

        FeatureTypeStyle fts2 = sFac.createFeatureTypeStyle();
        fts2.setRules(new Rule[] { rule2 });
        fts2.setFeatureTypeName(typeName);

        Style style = sFac.createStyle();
        style.setFeatureTypeStyles(new FeatureTypeStyle[] { fts2 });

        return style;
    }

    static Style createPointStyle() throws Exception {
        return createPointStyle(null);
    }

    static Style createPointStyle(String typeName) throws Exception {
        if (typeName == null)
            typeName = POINT_TYPE_NAME;
        StyleFactory sFac = StyleFactoryFinder.createStyleFactory();
        StyleBuilder builder = new StyleBuilder(sFac);
        // The following is complex, and should be built from
        Stroke myStroke = sFac.getDefaultStroke();
        myStroke.setColor(filterFactory.createLiteralExpression("#0000ff"));
        myStroke
                .setWidth(filterFactory.createLiteralExpression(new Integer(2)));
        PointSymbolizer point = sFac.createPointSymbolizer(builder
                .createGraphic(), "the_geom");

        Rule rule2 = sFac.createRule();
        rule2.setSymbolizers(new Symbolizer[] { point });
        if (FILTER) {
            AttributeExpression exp = filterFactory
                    .createAttributeExpression(POINT_LABEL);
            CompareFilter filter = filterFactory
                    .createCompareFilter(Filter.COMPARE_NOT_EQUALS);
            filter.addLeftValue(exp);
            filter.addRightValue(filterFactory.createLiteralExpression("blah"));
            rule2.setFilter(filter);
        }
        if (LABELING) {

            TextSymbolizer textsym = sFac.createTextSymbolizer();
            textsym.setFill(sFac.createFill(filterFactory
                    .createLiteralExpression("#000000")));
            textsym.setGeometryPropertyName("the_geom");
            textsym.setLabel(filterFactory
                    .createAttributeExpression(POINT_LABEL));
            textsym.setFonts(new Font[] { builder.createFont(new java.awt.Font(
                    "Arial", java.awt.Font.PLAIN, 10)) });
            rule2.setSymbolizers(new Symbolizer[] { point, textsym });
        }

        FeatureTypeStyle fts2 = sFac.createFeatureTypeStyle();
        fts2.setRules(new Rule[] { rule2 });
        fts2.setFeatureTypeName(typeName);

        Style style = sFac.createStyle();
        style.setFeatureTypeStyles(new FeatureTypeStyle[] { fts2 });

        return style;
    }

    public static void main(String[] args) throws Exception {
        try {
            DISPLAY = false;
            RUN_LITE = true;
            RUN_SHAPE = true;
            RUN_TINY = false;
            ACCURATE = true;

            out.write("Battery 1\n");
            ALL_DATA = true;
            ANTI_ALIASING = true;
            FILTER = false;
            LABELING = false;
            NO_REPROJECTION = true;
            QUADTREE = false;
            runLineTest(5);
            runPolygonTest(8);
            runPointTest(20);

            out.write("Battery 2\n");
            
            QUADTREE = true;
            runLineTest(5);
            runPolygonTest(8);
            runPointTest(20);
            out.write("Battery 3\n");

            ALL_DATA = false;
            QUADTREE = false;
            runLineTest(5);
            runPolygonTest(8);
            runPointTest(20);
            out.write("Battery 4\n");

            QUADTREE = true;
            runLineTest(5);
            runPolygonTest(8);
            runPointTest(20);
            out.write("Battery 5\n");

            ALL_DATA = true;
            QUADTREE = true;
            NO_REPROJECTION = false;
            runLineTest(5);
            runPolygonTest(8);
            runPointTest(20);
            out.write("Battery 6\n");

            NO_REPROJECTION = true;
            LABELING=true;
            runLineTest(5);
            runPolygonTest(8);
            runPointTest(20);
            
        }catch (Throwable e) {
        	e.printStackTrace(new PrintWriter(out));
		} finally {
        	
            if (out != null && !DISPLAY && !CPU_PROFILE)
                out.close();

            System.exit(0);
        }
    }

    private static void runLineTest(int cycles) throws Exception, IOException {
        CYCLES = cycles;
        SHAPE_TYPE = LINES;
        runSuite();
    }

    private static void runPolygonTest(int cycles) throws Exception,
            IOException {
        CYCLES = cycles;
        SHAPE_TYPE = POLYGONS;
        runSuite();
    }

    private static void runPointTest(int cycles) throws Exception, IOException {
        CYCLES = cycles;
        SHAPE_TYPE = POINTS;
        runSuite();
    }

    private static void runSuite() throws Exception, IOException {
        Timing t = new Timing();
        if (RUN_SHAPE)
            t.runShapeRendererTest();

        if (RUN_TINY)
            t.runTinyTest();

        if (RUN_LITE)
            t.runLiteRendererTest();

    }

    private void runShapeRendererTest() throws Exception {
        MapContext context = getMapContext();
        ShapefileRenderer renderer = new ShapefileRenderer(context);

        if (ANTI_ALIASING) {
            RenderingHints hints = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            renderer.setJava2DHints(hints);
        }

        Image image;
        Graphics2D g;

        image = createImage();
        g = createGraphics(image);

        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);

        Envelope bounds = context.getLayerBounds();
        if (!ALL_DATA)
            bounds = new Envelope(bounds.getMinX() + bounds.getWidth() / 4,
                    bounds.getMaxX() - bounds.getWidth() / 4, bounds.getMinY()
                            + bounds.getHeight() / 4, bounds.getMaxY()
                            - bounds.getHeight() / 4);

        if (ACCURATE)
            renderer.paint(g, new Rectangle(w, h), bounds);
        long start = System.currentTimeMillis();

        renderer.paint(g, new Rectangle(w, h), bounds);
        if (ACCURATE) {
            for (int i = 0; i < CYCLES; i++)
                renderer.paint(g, new Rectangle(w, h), bounds);
        }

        long end = System.currentTimeMillis();
        if (ACCURATE) {
            if (out != null) {
                out.write("shape " + getTestName() + "=" + (end - start) / 3
                        + "\n");
            }
        } else if (out != null) {
            out.write("shape " + getTestName() + "=" + (end - start) + "\n");
        }

        if (DISPLAY) {
            display("shape", image, w, h);
        }
    }

    private Graphics2D createGraphics(Image image) {
        Graphics2D g;
        if (image instanceof VolatileImage) {
            g = ((VolatileImage) image).createGraphics();
        } else {
            g = ((BufferedImage) image).createGraphics();
        }
        return g;
    }

    private Image createImage() {
        Image image;
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        return image;
    }

    private MapContext getMapContext() throws Exception {
        URL url;
        if (SHAPE_TYPE == LINES) {
            url = new URL(LINES_FILE);
        } else if (SHAPE_TYPE == POLYGONS) {
            url = new URL(POLY_FILE);
        } else {
            url = new URL(POINT_FILE);
        }

        if (!QUADTREE) {
            String s = url.getPath();
            s = s.substring(0, s.lastIndexOf("."));
            File file = new File(s + ".qix");
            if (file.exists()) {
                file.delete();
            }
        }

        IndexedShapefileDataStoreFactory fac = new IndexedShapefileDataStoreFactory();
        IndexedShapefileDataStore store;
        Map params = new HashMap();
        params.put(IndexedShapefileDataStoreFactory.URLP.key, url);
        params.put(IndexedShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key,
                new Boolean(false));
        if (QUADTREE) {
            params.put(
                    IndexedShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key,
                    new Boolean(true));
        }
        store = (IndexedShapefileDataStore) fac.createDataStore(params);
        if (QUADTREE)
        	store.buildQuadTree(0);
        
        DefaultMapContext context = new DefaultMapContext();
        if (SHAPE_TYPE == LINES)
            context.addLayer(store.getFeatureSource(), createLineStyle());
        else if (SHAPE_TYPE == POLYGONS)
            context.addLayer(store.getFeatureSource(), createPolyStyle());
        else
            context.addLayer(store.getFeatureSource(), createPointStyle());

        if (NO_REPROJECTION)
            context.setAreaOfInterest(new Envelope(), store.getSchema()
                    .getDefaultGeometry().getCoordinateSystem());
        return context;
    }

    private void runTinyTest() throws Exception {
        ShapefileDataStoreFactory fac = new ShapefileDataStoreFactory();
        ShapefileDataStore store = (ShapefileDataStore) fac
                .createDataStore(TestData.getResource(Timing.class,
                        "theme1.shp"));
        DefaultMapContext context = new DefaultMapContext();
        context.addLayer(store.getFeatureSource(), createLineStyle("theme1"));
        if (NO_REPROJECTION)
            context.setAreaOfInterest(new Envelope(), store.getSchema()
                    .getDefaultGeometry().getCoordinateSystem());
        ShapefileRenderer renderer = new ShapefileRenderer(context);
        if (ANTI_ALIASING) {
            RenderingHints hints = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            renderer.setJava2DHints(hints);
        }
        Image image;
        Graphics2D g;

        image = createImage();
        g = createGraphics(image);

        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);

        Envelope bounds = new Envelope(-7.105552354197932, 8.20555235419793,
                -3.239388966356115, 4.191388966388683);

        if (!ALL_DATA)
            bounds = new Envelope(bounds.getMinX() + bounds.getWidth() / 4,
                    bounds.getMaxX() - bounds.getWidth() / 4, bounds.getMinY()
                            + bounds.getHeight() / 4, bounds.getMaxY()
                            - bounds.getHeight() / 4);

        if (ACCURATE)
            renderer.paint(g, new Rectangle(w, h), bounds);
        long start = System.currentTimeMillis();

        renderer.paint(g, new Rectangle(w, h), bounds);
        if (ACCURATE) {
            for (int i = 0; i < CYCLES; i++)
                renderer.paint(g, new Rectangle(w, h), bounds);
        }

        long end = System.currentTimeMillis();
        if (ACCURATE) {
            out.write("tiny " + getTestName() + "=" + (end - start) / 3 + "\n");
        } else
            out.write("tiny " + getTestName() + "=" + (end - start) + "\n");
        if (DISPLAY)
            display("tiny", image, w, h);

    }

    private void runLiteRendererTest() throws Exception {

        MapContext context = getMapContext();
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(context);
        Map basichints = new HashMap();
        basichints.put("optimizedDataLoadingEnabled", new Boolean(true));
        renderer.setRendererHints(basichints);
        if (ANTI_ALIASING) {
            RenderingHints hints = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            renderer.setJava2DHints(hints);
        }

        Image image;
        Graphics2D g;

        image = createImage();
        g = createGraphics(image);

        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        Envelope bounds = context.getLayerBounds();

        if (!ALL_DATA)
            bounds = new Envelope(bounds.getMinX() + bounds.getWidth() / 4,
                    bounds.getMaxX() - bounds.getWidth() / 4, bounds.getMinY()
                            + bounds.getHeight() / 4, bounds.getMaxY()
                            - bounds.getHeight() / 4);

        if (ACCURATE)
            renderer.paint(g, new Rectangle(w, h), bounds);
        long start = System.currentTimeMillis();

        renderer.paint(g, new Rectangle(w, h), bounds);
        if (ACCURATE) {
            for (int i = 0; i < CYCLES; i++)
                renderer.paint(g, new Rectangle(w, h), bounds);
        }
        long end = System.currentTimeMillis();
        if (ACCURATE) {
            if (out != null)
                out.write("lite " + getTestName() + "=" + (end - start) / 3
                        + "\n");
        } else if (out != null)
            out.write("lite " + getTestName() + "=" + (end - start) + "\n");
        if (DISPLAY)
            display("lite", image, w, h);

    }

    public static Frame display(String testName, final Image image, int w, int h) {
        Frame frame = new Frame(testName);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
            }
        });

        Panel p = new Panel() {
            /** <code>serialVersionUID</code> field */
            private static final long serialVersionUID = 1L;

            public void paint(Graphics g) {
                g.drawImage(image, 0, 0, this);
            }
        };
        frame.add(p);
        frame.setSize(w, h);
        frame.setVisible(true);
        return frame;
    }

    private static String LINES_WORK_FILE = "file:/Users/Jesse/dev/Data/uDigData/bc_roads.shp";

    private static String LINES_WORK_TYPE_NAME = "bc_roads";

    private static String LINES_WORK_LABEL = "STREET";

    private static String LINES_FILE = LINES_WORK_FILE;

    private static String LINES_TYPE_NAME = LINES_WORK_TYPE_NAME;

    private static String LINES_LABEL = LINES_WORK_LABEL;

    private static String POLY_FILE = "file:/Users/Jesse/dev/Data/uDigData/bc_parks_2001.shp";

    private static String POLY_TYPE_NAME = "bc_parks_2001";

    private static String POLY_LABEL = "PA_NAME";

    private static String POINT_FILE = "file:/Users/Jesse/dev/Data/uDigData/bc_pubs.shp";

    private static String POINT_TYPE_NAME = "bc_pubs";

    private static String POINT_LABEL = "NAME";

    int w = 512, h = 512;
}

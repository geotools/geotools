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

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.geotools.data.DataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.IllegalFilterException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryFinder;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.geotools.test.TestData;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;


/**
 * DOCUMENT ME!
 *
 * @author jamesm
 *
 * @source $URL$
 */
public class Rendering2DTest extends TestCase {
    /** path for test data */

    // private java.net.URL base = getClass().getResource("/testData/");

    /** The logger for the rendering module. */
    public static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.rendering");
    static final String LINE = "linefeature";
    static final String POLYGON = "polygonfeature";
    static final String POINT = "pointfeature";
    static final String RING = "ringfeature";
    static final String COLLECTION = "collfeature";

    public Rendering2DTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(Rendering2DTest.class);

        return suite;
    }

    Style loadTestStyle() throws IOException {
        StyleFactory factory = StyleFactoryFinder.createStyleFactory();

        java.net.URL surl = TestData.getResource(this, "test-sld.xml");
        SLDParser stylereader = new SLDParser(factory, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        UserLayer layer = (UserLayer) sld.getStyledLayers()[0];

        Style style = layer.getUserStyles()[0];

        return style;
    }

    Style createTestStyle() throws IllegalFilterException {
        return TestUtilites.createTestStyle(null, null);
    }

    public void testSimpleRender() throws Exception {
        // same as the datasource test, load in some features into a table
        LOGGER.finer("starting rendering2DTest");

        DataStore ds = TestUtilites.getPolygons();
        SimpleFeatureSource source = ds.getFeatureSource(ds.getTypeNames()[0]);
        Style style = createTestStyle();

        MapContext map = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        map.addLayer(source, style);

        ShapefileRenderer renderer = new ShapefileRenderer(map);
        ReferencedEnvelope env = map.getLayerBounds();
        TestUtilites.showRender("testSimpleRender", renderer, 1000, env);
    }

    public void testSimpleLineRender() throws Exception {
        // same as the datasource test, load in some features into a table
        //System.err.println("starting rendering2DTest");

        ShapefileRenderer renderer = createLineRenderer(TestUtilites.getLines());
        MapContext map = renderer.getContext();

        map.setAreaOfInterest(map.getLayer(0).getFeatureSource().getBounds(),
            map.getLayer(0).getFeatureSource().getSchema().getGeometryDescriptor()
               .getCoordinateReferenceSystem());

        ReferencedEnvelope env = map.getLayerBounds();
        env = new ReferencedEnvelope(env.getMinX() - 20, env.getMaxX() + 20,
                env.getMinY() - 20, env.getMaxY() + 20, env.getCoordinateReferenceSystem());
        map.setAreaOfInterest(env);
        TestUtilites.showRender("testSimpleLineRender", renderer, 3000, env);
    }

    public void testSimplePolygonRender() throws Exception {
        // same as the datasource test, load in some features into a table
        System.err.println("starting rendering2DTest");

        ShapefileRenderer renderer = createLineRenderer(TestUtilites
                .getPolygons());
        MapContext map = renderer.getContext();

        map.setAreaOfInterest(map.getLayer(0).getFeatureSource().getBounds(),
            map.getLayer(0).getFeatureSource().getSchema().getGeometryDescriptor()
               .getCoordinateReferenceSystem());

        ReferencedEnvelope env = map.getLayerBounds();
        env = new ReferencedEnvelope(env.getMinX() - 20, env.getMaxX() + 20,
                env.getMinY() - 20, env.getMaxY() + 20, env.getCoordinateReferenceSystem());
        map.setAreaOfInterest(env);
        TestUtilites.showRender("testSimpleLineRender", renderer, 3000, env);
    }

    public void testSimplePolygonRenderZoomedOut() throws Exception {
        // same as the datasource test, load in some features into a table
        System.err.println("starting rendering2DTest");

        ShapefileRenderer renderer = createLineRenderer(TestUtilites
                .getPolygons());
        MapContext map = renderer.getContext();

        map.setAreaOfInterest(map.getLayer(0).getFeatureSource().getBounds(),
            map.getLayer(0).getFeatureSource().getSchema().getGeometryDescriptor()
               .getCoordinateReferenceSystem());

        ReferencedEnvelope env = map.getLayerBounds();
        env = new ReferencedEnvelope(env.getMinX() - 200000, env.getMaxX() + 200000,
                env.getMinY() - 200000, env.getMaxY() + 200000, env.getCoordinateReferenceSystem());
        map.setAreaOfInterest(env);
        TestUtilites.showRender("testSimpleLineRender", renderer, 3000, env);
    }

    public void testSimpleLineRenderLargebbox() throws Exception {
        // same as the datasource test, load in some features into a table
        System.err.println("starting rendering2DTest");

        ShapefileRenderer renderer = createLineRenderer(TestUtilites.getLines());
        MapContext map = renderer.getContext();

        map.setAreaOfInterest(map.getLayer(0).getFeatureSource().getBounds(),
            map.getLayer(0).getFeatureSource().getSchema().getGeometryDescriptor()
               .getCoordinateReferenceSystem());

        ReferencedEnvelope env = map.getLayerBounds();
        env = new ReferencedEnvelope(env.getMinX() - env.getWidth(),
                env.getMaxX() + env.getWidth(),
                env.getMinY() - env.getHeight(), env.getMaxY()
                + env.getHeight(), env.getCoordinateReferenceSystem());
        map.setAreaOfInterest(env);
        TestUtilites.showRender("testSimpleLineRender", renderer, 3000, env);
    }

    public void testSimplePointRender() throws Exception {
        // same as the datasource test, load in some features into a table
        System.err.println("starting rendering2DTest");

        DataStore ds = TestUtilites.getPoints();
        SimpleFeatureSource source = ds.getFeatureSource(ds.getTypeNames()[0]);
        Style style = createTestStyle();

        MapContext map = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        map.addLayer(source, style);

        ShapefileRenderer renderer = new ShapefileRenderer(map);
        ReferencedEnvelope env = map.getLayerBounds();
        env = new ReferencedEnvelope(env.getMinX() - 20, env.getMaxX() + 20,
                env.getMinY() - 20, env.getMaxY() + 20, env.getCoordinateReferenceSystem());
        TestUtilites.showRender("testSimplePointRender", renderer, 1000, env);
    }

    public void testReprojection() throws Exception {
        DataStore ds = TestUtilites.getPolygons();
        SimpleFeatureSource source = ds.getFeatureSource(ds.getTypeNames()[0]);
        Style style = createTestStyle();
        CoordinateReferenceSystem crs = ReferencingFactoryFinder.getCRSFactory(null)
        .createFromWKT("PROJCS[\"NAD_1983_UTM_Zone_10N\",GEOGCS[\"GCS_North_American_1983\",DATUM[\"D_North_American_1983\",TOWGS84[0,0,0,0,0,0,0],SPHEROID[\"GRS_1980\",6378137,298.257222101]],PRIMEM[\"Greenwich\",0],UNIT[\"Degree\",0.017453292519943295]],PROJECTION[\"Transverse_Mercator\"],PARAMETER[\"False_Easting\",500000],PARAMETER[\"False_Northing\",0],PARAMETER[\"Central_Meridian\",-123],PARAMETER[\"Scale_Factor\",0.9996],PARAMETER[\"Latitude_Of_Origin\",0],UNIT[\"Meter\",1]]");

        MapContext map = new DefaultMapContext(crs);
        map.addLayer(source, style);

        ShapefileRenderer renderer = new ShapefileRenderer(map);


        ReferencedEnvelope bounds = map.getLayerBounds();

        TestUtilites.showRender("testReprojection", renderer, 1000, bounds);

    }

    public void testLineReprojection() throws Exception {
        // same as the datasource test, load in some features into a table
        System.err.println("starting rendering2DTest");

        ShapefileRenderer renderer = createLineRenderer(TestUtilites.getLines());
        ReferencedEnvelope env = renderer.getContext().getAreaOfInterest();

        //        INTERACTIVE=true;
        TestUtilites.showRender("testSimpleLineRender", renderer, 3000, env);
    }

    public void testNullCRSPoly() throws Exception {
        // same as the datasource test, load in some features into a table
        System.err.println("starting testLiteRender2");

        ShapefileDataStore ds = TestUtilites.getDataStore("smallMultiPoly.shp");
        SimpleFeatureSource source = ds.getFeatureSource(ds.getTypeNames()[0]);
        Style style = TestUtilites.createTestStyle(ds.getSchema().getTypeName(),
                null);

        MapContext map = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        map.addLayer(source, style);

        ShapefileRenderer renderer = new ShapefileRenderer(map);
        ReferencedEnvelope env = map.getLayerBounds();

        //        renderer.setOptimizedDataLoadingEnabled(true);
        env = new ReferencedEnvelope(env.getMinX() - 1, env.getMaxX() + 1,
                env.getMinY() - 1, env.getMaxY() + 1, env.getCoordinateReferenceSystem());
        TestUtilites.showRender("testReprojection", renderer, 1000, env);

        // System.in.read();
    }

    public void testNullCRSLine() throws Exception {
        // same as the datasource test, load in some features into a table
        System.err.println("starting rendering2DTest");

        ShapefileRenderer renderer = createLineRenderer(TestUtilites
                .getDataStore("lineNoCRS.shp"));
        ReferencedEnvelope env = renderer.getContext().getAreaOfInterest();

        //        INTERACTIVE=true;
        TestUtilites.showRender("testSimpleLineRender", renderer, 3000, env);
    }

    public void testEnvelopePerformance() throws Exception {
        ShapefileRenderer renderer = createLineRenderer(TestUtilites.getLines());
        MapContext context = renderer.getContext();

        TestUtilites.CountingRenderListener l1 = new TestUtilites.CountingRenderListener(getName());
        renderer.addRenderListener(l1);

        BufferedImage image = new BufferedImage(300, 300,
                BufferedImage.TYPE_3BYTE_BGR);
        renderer.paint(image.createGraphics(), new Rectangle(300, 300),
            context.getAreaOfInterest());
        renderer.removeRenderListener(l1);

        TestUtilites.CountingRenderListener l2 = new TestUtilites.CountingRenderListener(getName());
        renderer.addRenderListener(l2);

        ReferencedEnvelope old = context.getAreaOfInterest();
        double w = old.getWidth()/4;
        double h = old.getHeight()/4;
        ReferencedEnvelope env = new ReferencedEnvelope( old.getCenter(0)-w, old.getCenter(0)+w,old.getCenter(1)-h, old.getCenter(1)+h, old.getCoordinateReferenceSystem());
        renderer.paint(image.createGraphics(), new Rectangle(300, 300), env);
        assertTrue(l1.count > l2.count);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ds DOCUMENT ME!
     *
     * @return
     *
     * @throws Exception
     */
    private ShapefileRenderer createLineRenderer(ShapefileDataStore ds)
        throws Exception {
        SimpleFeatureSource source = ds.getFeatureSource(ds.getTypeNames()[0]);
        Style style = TestUtilites.createTestStyle(null, ds.getTypeNames()[0]);

        MapContext map = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        map.addLayer(source, style);

        ShapefileRenderer renderer = new ShapefileRenderer(map);
        ReferencedEnvelope env = map.getLayerBounds();
        if( env.getCoordinateReferenceSystem()==null )
            env=new ReferencedEnvelope((Envelope)env, DefaultEngineeringCRS.GENERIC_2D );
        map.setAreaOfInterest(env);

        return renderer;
    }
}

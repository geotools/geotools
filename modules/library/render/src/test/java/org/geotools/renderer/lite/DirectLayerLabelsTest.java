/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import junit.framework.TestCase;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapContext;
import org.geotools.map.MapViewport;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryFinder;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** @author ian */
public class DirectLayerLabelsTest extends TestCase {

    private long timout = 3000;
    private static final int CENTERX = 130;
    private static final int CENTERY = 40;
    /** @throws java.lang.Exception */
    @Before
    public void setUp() throws Exception {
        // System.setProperty(TestData.INTERACTIVE_TEST_KEY, "true");
        super.setUp();
    }

    @Test
    public void testPointLabeling() throws Exception {
        FeatureCollection collection = createPointFeatureCollection();
        Style style = loadStyle("PointStyle.sld");
        assertNotNull(style);
        MapContext map = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        map.addLayer(collection, style);
        DirectLayer dl =
                new DirectLayer() {

                    @Override
                    public ReferencedEnvelope getBounds() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public void draw(Graphics2D graphics, MapContent map, MapViewport viewport) {
                        graphics.setColor(Color.BLACK);
                        graphics.drawString("DirectLayer", 10, 10);
                    }
                };
        map.addLayer(dl);
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(map);
        ReferencedEnvelope env = map.getLayerBounds();
        int boundary = 10;
        env =
                new ReferencedEnvelope(
                        env.getMinX() - boundary,
                        env.getMaxX() + boundary,
                        env.getMinY() - boundary,
                        env.getMaxY() + boundary,
                        null);
        RendererBaseTest.showRender("testDirectLabeling", renderer, timout, env);
        map.dispose();
    }

    private Style loadStyle(String sldFilename) throws IOException {
        StyleFactory factory = StyleFactoryFinder.createStyleFactory();

        java.net.URL surl = TestData.getResource(this, sldFilename);
        SLDParser stylereader = new SLDParser(factory, surl);

        Style style = stylereader.readXML()[0];
        return style;
    }

    private SimpleFeatureCollection createPointFeatureCollection() throws Exception {
        AttributeDescriptor[] types = new AttributeDescriptor[2];

        GeometryFactory geomFac = new GeometryFactory();
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;

        MemoryDataStore data = new MemoryDataStore();
        data.addFeature(createPointFeature(2, 2, "LongLabel1", crs, geomFac, types));
        data.addFeature(createPointFeature(4, 4, "LongLabel2", crs, geomFac, types));
        data.addFeature(createPointFeature(0, 4, "LongLabel3", crs, geomFac, types));
        // data.addFeature(createPointFeature(2,0,"Label4",crs, geomFac, types));
        data.addFeature(createPointFeature(2, 6, "LongLabel6", crs, geomFac, types));

        return data.getFeatureSource(Rendering2DTest.POINT).getFeatures();
    }

    private SimpleFeature createPointFeature(
            int x,
            int y,
            String name,
            CoordinateReferenceSystem crs,
            GeometryFactory geomFac,
            AttributeDescriptor[] types)
            throws Exception {
        Coordinate c = new Coordinate(x, y);
        Point point = geomFac.createPoint(c);
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        if (crs != null) builder.add("point", point.getClass(), crs);
        else builder.add("centre", point.getClass());
        builder.add("name", String.class);
        builder.setName("pointfeature");
        SimpleFeatureType type = builder.buildFeatureType();
        return SimpleFeatureBuilder.build(type, new Object[] {point, name}, null);
    }
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.DefaultMapContext;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

public class LabelObstacleTest {

    static MemoryDataStore mem;

    @BeforeClass
    public static void setUpData() throws Exception {
        mem = new MemoryDataStore();

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        tb.setName("roads");
        tb.setSRS("epsg:4326");
        tb.add("geom", LineString.class);
        tb.add("name", String.class);
        mem.createSchema(tb.buildFeatureType());

        tb.setName("points");
        tb.add("geom", Point.class);
        mem.createSchema(tb.buildFeatureType());

        tb.setName("lines");
        tb.add("geom", LineString.class);
        mem.createSchema(tb.buildFeatureType());

        tb.setName("polys");
        tb.add("geom", Polygon.class);
        mem.createSchema(tb.buildFeatureType());

        tb.setName("lines2");
        tb.add("geom", MultiLineString.class);
        tb.add("name", String.class);
        mem.createSchema(tb.buildFeatureType());
        
        loadData(mem, "roads");
        loadData(mem, "points");
        loadData(mem, "lines");
        loadData(mem, "polys");
        loadData(mem, "lines2");
    }

    static void loadData(MemoryDataStore mem, String name) throws Exception {
        WKTReader wkt = new WKTReader();

        FeatureWriter w = mem.getFeatureWriter(name, Transaction.AUTO_COMMIT);
        BufferedReader r = new BufferedReader(
                new InputStreamReader(
                        LabelObstacleTest.class.getResourceAsStream("test-data/obstacles/" + name
                                + ".txt")));
        String line = null;
        while ((line = r.readLine()) != null) {
            String[] values = line.split(";");
            SimpleFeature f = (SimpleFeature) w.next();
            for (int i = 0; i < f.getAttributeCount(); i++) {
                AttributeDescriptor ad = f.getType().getDescriptor(i);
                if (ad instanceof GeometryDescriptor) {
                    f.setAttribute(i, wkt.read(values[i]));
                } else {
                    f.setAttribute(i, values[i]);
                }
            }

            w.write();
        }

        r.close();
    }

    Style style(String name) throws Exception {
        // return RendererBaseTest.loadStyle(this, "test-data/obstacles/" + name + ".sld");
        SLDParser p = new SLDParser(CommonFactoryFinder.getStyleFactory(null), getClass()
                .getResourceAsStream("test-data/obstacles/" + name + ".sld"));
        return p.readXML()[0];
    }

    Style[] styles(String... names) throws Exception {
        List<Style> styles = new ArrayList();
        for (String name : names) {
            styles.add(style(name));
        }
        return styles.toArray(new Style[styles.size()]);
    }

    FeatureSource[] sources(String... names) throws Exception {
        List<FeatureSource> sources = new ArrayList();
        for (String name : names) {
            sources.add(mem.getFeatureSource(name));
        }
        return sources.toArray(new FeatureSource[sources.size()]);
    }

    BufferedImage render(FeatureSource[] sources, Style[] styles) throws Exception {
        DefaultMapContext map = new DefaultMapContext();

        ReferencedEnvelope env = sources[0].getBounds();
        for (int i = 1; i < sources.length; i++) {
            env.expandToInclude(sources[i].getBounds());
        }
        map.setAreaOfInterest(env);
        map.setCoordinateReferenceSystem(env.getCoordinateReferenceSystem());
        for (int i = 0; i < sources.length; i++) {
            map.addLayer(sources[i], styles[i]);
        }

        StreamingRenderer r = new StreamingRenderer();
        r.setContext(map);

        return RendererBaseTest.showRender("testPointLabeling", r, 5000, env);
    }

    File file(String name) {
        return new File("src/test/resources/org/geotools/renderer/lite/test-data/obstacles/" + name
                + ".png");
    }

    @Test
    public void testExternalGraphic() throws Exception {
        BufferedImage img = render(sources("roads", "points"), styles("label", "grin"));
        ImageAssert.assertEquals(file("externalGraphic"), img, 10);
    }

    @Test
    public void testMark() throws Exception {
        BufferedImage img = render(sources("roads", "points"), styles("label", "mark"));
        ImageAssert.assertEquals(file("mark"), img, 10);
    }
    
    @Test
    public void testPolygon() throws Exception {
        BufferedImage img = render(sources("roads", "polys"), styles("label", "poly"));
        ImageAssert.assertEquals(file("poly"), img, 10);
    }

    @Test
    public void testLine() throws Exception {
        BufferedImage img = render(sources("roads", "lines"), styles("label", "line"));
        ImageAssert.assertEquals(file("line"), img, 10);
    }
    
    @Test
    public void testLineWithGraphicStroke() throws Exception {
        BufferedImage img = render(sources("lines2"), styles("hatch"));
        ImageAssert.assertEquals(file("hatch"), img, 10);
    }
}

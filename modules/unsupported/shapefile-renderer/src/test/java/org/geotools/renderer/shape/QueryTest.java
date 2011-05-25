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

import java.util.Arrays;
import java.util.Collections;

import junit.framework.TestCase;

import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.renderer.RenderListener;
import org.geotools.styling.Style;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.And;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Intersects;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;


/**
 *
 *
 * @source $URL$
 */ 
public class QueryTest extends TestCase {
    private static final boolean INTERACTIVE = false;
    private SimpleFeatureSource source;
    private Style style;
    private DefaultMapContext map;
    Envelope bounds = new Envelope(-5, 5, -5, 5);

    protected void setUp() throws Exception {
        source = TestUtilites.getDataStore("theme1.shp").getFeatureSource();
        style = TestUtilites.createTestStyle("theme1", null);
        map = new DefaultMapContext();
        map.addLayer(source, style);
    }

    public void testFidFilter() throws Exception {
        FeatureId id = TestUtilites.filterFactory.featureId("theme1.2");
        Query q = new DefaultQuery("theme1",
                TestUtilites.filterFactory.id(Collections.singleton(id)));
        map.getLayer(0).setQuery(q);

        ShapefileRenderer renderer = new ShapefileRenderer(map);
        renderer.addRenderListener(new RenderListener() {
                public void featureRenderer(SimpleFeature feature) {
                    assertEquals("theme1.2", feature.getID());
                }

                public void errorOccurred(Exception e) {
                    throw new RuntimeException(e);
                }
            });
        TestUtilites.INTERACTIVE = INTERACTIVE;
        TestUtilites.showRender("testFidFilter", renderer, 1000, bounds, 1);
    }

    public void testBBOXFilter() throws Exception {
        String geom = source.getSchema().getGeometryDescriptor().getLocalName();
        BBOX filter = TestUtilites.filterFactory.bbox(geom, -4, -3, -2, 0, null);

        Query q = new DefaultQuery("theme1", filter);
        map.getLayer(0).setQuery(q);

        ShapefileRenderer renderer = new ShapefileRenderer(map);
        renderer.addRenderListener(new RenderListener() {
                public void featureRenderer(SimpleFeature feature) {
                    assertEquals("theme1.1", feature.getID());
                }

                public void errorOccurred(Exception e) {
                    throw new RuntimeException(e);
                }
            });
        TestUtilites.showRender("testFidFilter", renderer, 1000, bounds, 1);
    }
    
    public void testMixedFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Intersects bbox = ff.intersects(ff.property("the_geom"), ff.literal(new ReferencedEnvelope(-4, -2, 0, -3, null)));
        PropertyIsEqualTo idEqual = ff.equals(ff.property("ID"), ff.literal(1.0));
        PropertyIsEqualTo nameEqual = ff.equals(ff.property("NAME"), ff.literal("dave street"));
        And filter = ff.and(Arrays.asList(new org.opengis.filter.Filter[] {bbox, idEqual, nameEqual}));

        Query q = new DefaultQuery("theme1", filter);
        map.getLayer(0).setQuery(q);

        ShapefileRenderer renderer = new ShapefileRenderer(map);
        renderer.addRenderListener(new RenderListener() {
                public void featureRenderer(SimpleFeature feature) {
                    assertEquals("theme1.1", feature.getID());
                }

                public void errorOccurred(Exception e) {
                    throw new RuntimeException(e);
                }
            });
        TestUtilites.showRender("testFidFilter", renderer, 1000, bounds, 1);
    }
    
    public void testBeyondFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        GeometryFactory gf = new GeometryFactory();
        Literal point = ff.literal(gf.createPoint(new Coordinate(1, 0)));
        Beyond filter = ff.beyond(ff.property("the_geom"), point, 0.3, (String) null);

        Query q = new DefaultQuery("theme1", filter);
        map.getLayer(0).setQuery(q);

        ShapefileRenderer renderer = new ShapefileRenderer(map);
        renderer.addRenderListener(new RenderListener() {
                public void featureRenderer(SimpleFeature feature) {
                    assertEquals("theme1.2", feature.getID());
                }

                public void errorOccurred(Exception e) {
                    throw new RuntimeException(e);
                }
            });
        TestUtilites.showRender("testFidFilter", renderer, 1000, bounds, 1);
    }
    
    public void testDWithinFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        GeometryFactory gf = new GeometryFactory();
        Literal point = ff.literal(gf.createPoint(new Coordinate(1, 0)));
        DWithin filter = ff.dwithin(ff.property("the_geom"), point, 0.15, (String) null);

        Query q = new DefaultQuery("theme1", filter);
        map.getLayer(0).setQuery(q);

        ShapefileRenderer renderer = new ShapefileRenderer(map);
        renderer.addRenderListener(new RenderListener() {
                public void featureRenderer(SimpleFeature feature) {
                    assertEquals("theme1.1", feature.getID());
                }

                public void errorOccurred(Exception e) {
                    throw new RuntimeException(e);
                }
            });
        TestUtilites.showRender("testFidFilter", renderer, 1000, bounds, 1);
    }

}

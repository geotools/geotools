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
package org.geotools.jdbc;

import java.util.Collections;
import java.util.HashSet;

import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;


public class TestData {
    
    public String ROAD = "road";
    public String ROAD_ID = "id";
    public String ROAD_GEOM = "geom";
    public String ROAD_NAME = "name";
    
    public String RIVER = "river";
    public String RIVER_ID = "id";
    public String RIVER_GEOM = "geom";
    public String RIVER_RIVER = "river";
    public String RIVER_FLOW = "flow";
    
    public int initialFidValue = 0;
    public GeometryFactory gf;
    public FilterFactory ff;
    public String namespace = "http://www.geotools.org/test";
    public SimpleFeatureType roadType;
    public SimpleFeatureType subRoadType;
    public SimpleFeature[] roadFeatures;
    public ReferencedEnvelope roadBounds;
    public ReferencedEnvelope rd12Bounds;
    public Filter rd1Filter;
    public Filter rd2Filter;
    public Filter rd12Filter;
    public SimpleFeature newRoad;
    public SimpleFeatureType riverType;
    public SimpleFeatureType subRiverType;
    public SimpleFeature[] riverFeatures;
    public ReferencedEnvelope riverBounds;
    public Filter rv1Filter;
    public SimpleFeature newRiver;

    public TestData(int initialFidValue) throws Exception {
        this.initialFidValue = initialFidValue;

        gf = new GeometryFactory();
        ff = CommonFactoryFinder.getFilterFactory(null);
    }

    public void build() throws Exception {
        createRoadData();
        createRiverData();    
    }
    
    void createRoadData() throws Exception {
        roadType = DataUtilities.createType(namespace + "." + ROAD, ROAD_ID+":0," + ROAD_GEOM+":LineString," + ROAD_NAME+":String");
        subRoadType = DataUtilities.createType(namespace + ROAD, ROAD_ID+":0,"+ ROAD_GEOM+":LineString");
        gf = new GeometryFactory();

        roadFeatures = new SimpleFeature[3];

        //           3,2
        //  2,2 +-----+-----+ 4,2
        //     /     rd1     \
        // 1,1+               +5,1
        roadFeatures[0] = SimpleFeatureBuilder.build(roadType,
                new Object[] { new Integer(1), line(new int[] { 1, 1, 2, 2, 4, 2, 5, 1 }), "r1", },
                ROAD + "." + (initialFidValue));

        //       + 3,4
        //       + 3,3
        //  rd2  + 3,2
        //       |
        //    3,0+
        roadFeatures[1] = SimpleFeatureBuilder.build(roadType,
                new Object[] { new Integer(2), line(new int[] { 3, 0, 3, 2, 3, 3, 3, 4 }), "r2" },
                ROAD + "." + (initialFidValue + 1));

        //     rd3     + 5,3
        //            / 
        //  3,2 +----+ 4,2
        roadFeatures[2] = SimpleFeatureBuilder.build(roadType,
                new Object[] { new Integer(3), line(new int[] { 3, 2, 4, 2, 5, 3 }), "r3" },
                ROAD +"." + (initialFidValue + 2));
        roadBounds = new ReferencedEnvelope(CRS.decode("EPSG:4326"));
        roadBounds.expandToInclude(new ReferencedEnvelope(roadFeatures[0].getBounds()));
        roadBounds.expandToInclude(new ReferencedEnvelope(roadFeatures[1].getBounds()));
        roadBounds.expandToInclude(new ReferencedEnvelope(roadFeatures[2].getBounds()));

        rd1Filter = ff.id(Collections.singleton(ff.featureId(ROAD + "." + (initialFidValue))));
        rd2Filter = ff.id(Collections.singleton(ff.featureId(ROAD + "." + (initialFidValue + 1))));

        HashSet fids = new HashSet();
        fids.add(ff.featureId(ROAD + "." + (initialFidValue)));
        fids.add(ff.featureId(ROAD + "." + (initialFidValue + 1)));
        rd12Filter = ff.id(fids);

        rd12Bounds = new ReferencedEnvelope();
        rd12Bounds.expandToInclude(new ReferencedEnvelope(roadFeatures[0].getBounds()));
        rd12Bounds.expandToInclude(new ReferencedEnvelope(roadFeatures[1].getBounds()));
        //   + 2,3
        //  / rd4
        // + 1,2
        newRoad = SimpleFeatureBuilder.build(roadType,
                new Object[] { new Integer(4), line(new int[] { 1, 2, 2, 3 }), "r4" },
                ROAD + "." + (initialFidValue + 3));
    }

    void createRiverData() throws Exception {
        riverType = DataUtilities.createType(namespace + "." + RIVER,
                RIVER_ID + ":0," + RIVER_GEOM + ":MultiLineString," + RIVER_RIVER + ":String," + RIVER_FLOW + ":0.0");
        subRiverType = DataUtilities.createType(namespace + "." + RIVER, RIVER_RIVER + ":String," + RIVER_FLOW + ":0.0");
        gf = new GeometryFactory();
        riverFeatures = new SimpleFeature[2];

        //       9,7     13,7
        //        +------+
        //  5,5  /
        //  +---+ rv1
        //   7,5 \
        //    9,3 +----+ 11,3
        riverFeatures[0] = SimpleFeatureBuilder.build(riverType,
                new Object[] {
                    new Integer(1),
                    lines(new int[][] {
                            { 5, 5, 7, 4 },
                            { 7, 5, 9, 7, 13, 7 },
                            { 7, 5, 9, 3, 11, 3 }
                        }), "rv1", new Double(4.5)
                }, RIVER + "." + (initialFidValue));

        //         + 6,10    
        //        /
        //    rv2+ 4,8
        //       |
        //   4,6 +
        riverFeatures[1] = SimpleFeatureBuilder.build(riverType,
                new Object[] {
                    new Integer(2), lines(new int[][] {
                            { 4, 6, 4, 8, 6, 10 }
                        }), "rv2", new Double(3.0)
                }, RIVER +"." + (initialFidValue + 1));
        riverBounds = new ReferencedEnvelope(CRS.decode("EPSG:4326"));
        riverBounds.expandToInclude(ReferencedEnvelope.reference(riverFeatures[0].getBounds()));
        riverBounds.expandToInclude(ReferencedEnvelope.reference(riverFeatures[1].getBounds()));

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        rv1Filter = ff.id(Collections.singleton(ff.featureId(RIVER + ".rv1")));

        //  9,5   11,5   
        //   +-----+
        //     rv3  \ 
        //           + 13,3
        //                     
        newRiver = SimpleFeatureBuilder.build(riverType,
                new Object[] {
                    new Integer(3), lines(new int[][] {
                            { 9, 5, 11, 5, 13, 3 }
                        }), "rv3", new Double(1.5)
                }, RIVER + "." + (initialFidValue + 2));
    }

    /**
     * Creates a line from the specified (<var>x</var>,<var>y</var>) coordinates.
     * The coordinates are stored in a flat array.
     */
    public LineString line(int[] xy) {
        Coordinate[] coords = new Coordinate[xy.length / 2];

        for (int i = 0; i < xy.length; i += 2) {
            coords[i / 2] = new Coordinate(xy[i], xy[i + 1]);
        }

        return gf.createLineString(coords);
    }

    /**
     * Creates a multiline from the specified (<var>x</var>,<var>y</var>) coordinates.
     */
    public MultiLineString lines(int[][] xy) {
        LineString[] lines = new LineString[xy.length];

        for (int i = 0; i < xy.length; i++) {
            lines[i] = line(xy[i]);
        }

        return gf.createMultiLineString(lines);
    }

    /**
     * Creates a polygon from the specified (<var>x</var>,<var>y</var>) coordinates.
     * The coordinates are stored in a flat array.
     */
    public Polygon polygon(int[] xy) {
        LinearRing shell = ring(xy);

        return gf.createPolygon(shell, null);
    }

    /**
     * Creates a line from the specified (<var>x</var>,<var>y</var>) coordinates and
     * an arbitrary amount of holes.
     */
    public Polygon polygon(int[] xy, int[][] holes) {
        if ((holes == null) || (holes.length == 0)) {
            return polygon(xy);
        }

        LinearRing shell = ring(xy);

        LinearRing[] rings = new LinearRing[holes.length];

        for (int i = 0; i < xy.length; i++) {
            rings[i] = ring(holes[i]);
        }

        return gf.createPolygon(shell, rings);
    }

    /**
     * Creates a ring from the specified (<var>x</var>,<var>y</var>) coordinates.
     * The coordinates are stored in a flat array.
     */
    public LinearRing ring(int[] xy) {
        Coordinate[] coords = new Coordinate[xy.length / 2];

        for (int i = 0; i < xy.length; i += 2) {
            coords[i / 2] = new Coordinate(xy[i], xy[i + 1]);
        }

        return gf.createLinearRing(coords);
    }
}

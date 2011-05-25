/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;

/**
 * A set of constructs and utility methods used to test the data module.
 * <p>
 * By isolating a common set of {@link SimpleFeature}s, {@link SimpleFeatureType}s and {@link Filter}s
 * we are able to reduce the amount of overhead in setting up new tests.
 * </p>
 * <p>
 * We have also special cased {@link #assertEquals(Geometry, Geometry)} to work around
 * {@code Geometry.equals( Object )} not working as expected.
 * </p>
 * <p>
 * This code has been made part of the public {@code geotools.jar} to provide
 * a starting point for test cases involving Data constructs.
 * </p>
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett, Refractions Research
 *
 * @todo It should be possible to move this class in the {@code sample-data} module.
 */
public class DataTestCase extends TestCase {
    protected GeometryFactory gf;
    protected SimpleFeatureType roadType; // road: id,geom,name
    protected SimpleFeatureType subRoadType; // road: id,geom    
    protected SimpleFeature[] roadFeatures;
    protected ReferencedEnvelope roadBounds;
    protected ReferencedEnvelope rd12Bounds;    
    protected Filter rd1Filter;
    protected Filter rd2Filter;
    protected Filter rd12Filter;
    protected SimpleFeature newRoad;
    
    protected SimpleFeatureType riverType; // river: id, geom, river, flow
    protected SimpleFeatureType subRiverType; // river: river, flow     
    protected SimpleFeature[] riverFeatures;
    protected ReferencedEnvelope riverBounds;
    protected Filter rv1Filter;
    protected SimpleFeature newRiver;    

    protected SimpleFeatureType lakeType; // lake: id, geom, name
    protected SimpleFeature[] lakeFeatures;
    protected ReferencedEnvelope lakeBounds;
    protected FilterFactory2 ff;
    
    
    /**
     * Creates a default test case with the given name.
     */
    public DataTestCase(final String name) {
        super(name);
    }

    /**
     * Invoked before a test is run. The default implementation invokes {@link #dataSetUp}.
     */
    protected void setUp() throws Exception {
        ff = (FilterFactory2) CommonFactoryFinder.getFilterFactory2(null);
        dataSetUp();
    }
    
    /**
     * Loads the data.
     *
     * @see #setUp()
     */
    protected void dataSetUp() throws Exception {
        String namespace = getName();
        roadType = DataUtilities.createType(namespace + ".road",
                "id:0,geom:LineString,name:String");
        subRoadType = DataUtilities.createType(namespace + "road",
                "id:0,geom:LineString");
        gf = new GeometryFactory();

        roadFeatures = new SimpleFeature[3];

        //           3,2
        //  2,2 +-----+-----+ 4,2
        //     /     rd1     \
        // 1,1+               +5,1
        roadFeatures[0] = SimpleFeatureBuilder.build(roadType, new Object[] {
                new Integer(1),
                line(new int[] { 1, 1, 2, 2, 4, 2, 5, 1 }),
                "r1",
            },
            "road.rd1"
        );

        //       + 3,4
        //       + 3,3
        //  rd2  + 3,2
        //       |
        //    3,0+
        roadFeatures[1] = SimpleFeatureBuilder.build(roadType, new Object[] {
                new Integer(2), line(new int[] { 3, 0, 3, 2, 3, 3, 3, 4 }),
                "r2"
            },
            "road.rd2"
        );

        //     rd3     + 5,3
        //            / 
        //  3,2 +----+ 4,2
        roadFeatures[2] = SimpleFeatureBuilder.build(roadType, new Object[] {
                new Integer(3),
                line(new int[] { 3, 2, 4, 2, 5, 3 }), "r3"
            },
            "road.rd3"
        );
        roadBounds = new ReferencedEnvelope();
        roadBounds.expandToInclude( new ReferencedEnvelope(roadFeatures[0].getBounds()) );
        roadBounds.expandToInclude( new ReferencedEnvelope(roadFeatures[1].getBounds()) );
        roadBounds.expandToInclude( new ReferencedEnvelope(roadFeatures[2].getBounds()) );
                
        rd1Filter = ff.id(Collections.singleton(ff.featureId("road.rd1")));
        rd2Filter = ff.id(Collections.singleton(ff.featureId("road.rd2")));

        Id create = ff.id(new HashSet(Arrays.asList(ff.featureId("road.rd1"), ff.featureId("road.rd2"))));
        
        rd12Filter = create;
        
        rd12Bounds = new ReferencedEnvelope();
        rd12Bounds.expandToInclude(new ReferencedEnvelope(roadFeatures[0].getBounds()));
        rd12Bounds.expandToInclude(new ReferencedEnvelope(roadFeatures[1].getBounds()));        
        //   + 2,3
        //  / rd4
        // + 1,2
        newRoad = SimpleFeatureBuilder.build(roadType, new Object[] {
                    new Integer(4), line(new int[] { 1, 2, 2, 3 }), "r4"
                }, "road.rd4");

        riverType = DataUtilities.createType(namespace+".river",
                "id:0,geom:MultiLineString,river:String,flow:0.0");
        subRiverType = DataUtilities.createType(namespace+".river",
                "river:String,flow:0.0");
        gf = new GeometryFactory();
        riverFeatures = new SimpleFeature[2];

        //       9,7     13,7
        //        +------+
        //  5,5  /
        //  +---+ rv1
        //   7,5 \
        //    9,3 +----+ 11,3
        riverFeatures[0] = SimpleFeatureBuilder.build(riverType, new Object[] {
                    new Integer(1),
                    lines(new int[][] {
                            { 5, 5, 7, 4 },
                            { 7, 5, 9, 7, 13, 7 },
                            { 7, 5, 9, 3, 11, 3 }
                        }), "rv1", new Double(4.5)
                }, "river.rv1");

        //         + 6,10    
        //        /
        //    rv2+ 4,8
        //       |
        //   4,6 +
        riverFeatures[1] = SimpleFeatureBuilder.build(riverType, new Object[] {
                    new Integer(2),
                    lines(new int[][] {
                            { 4, 6, 4, 8, 6, 10 }
                        }), "rv2", new Double(3.0)
                }, "river.rv2");
        riverBounds = new ReferencedEnvelope();
        riverBounds.expandToInclude(ReferencedEnvelope.reference(riverFeatures[0].getBounds()));
        riverBounds.expandToInclude(ReferencedEnvelope.reference(riverFeatures[1].getBounds()));
                
        rv1Filter = ff.id(Collections.singleton(ff.featureId("river.rv1")));

        //  9,5   11,5   
        //   +-----+
        //     rv3  \ 
        //           + 13,3
        //                     
        newRiver = SimpleFeatureBuilder.build(riverType, new Object[] {
                new Integer(3),
                lines(new int[][] {
                        { 9, 5, 11, 5, 13, 3 }
                    }), "rv3", new Double(1.5)
            },
            "river.rv3"
        );
        
        lakeType = DataUtilities.createType(namespace+".lake",
                    "id:0,geom:Polygon:nillable,name:String");
        lakeFeatures = new SimpleFeature[1];
        //             + 14,8
        //            / \
        //      12,6 +   + 16,6
        //            \  | 
        //        14,4 +-+ 16,4
        //
        lakeFeatures[0] = SimpleFeatureBuilder.build(lakeType, new Object[]{
                new Integer(0),
                polygon( new int[]{ 12,6, 14,8, 16,6, 16,4, 14,4, 12,6} ),
                "muddy"
            },
            "lake.lk1"
        );
        lakeBounds = new ReferencedEnvelope();
        lakeBounds.expandToInclude(ReferencedEnvelope.reference(lakeFeatures[0].getBounds()));                 
    }

    /**
     * Set all data references to {@code null}, allowing garbage collection.
     * This method is automatically invoked after each test.
     */
    protected void tearDown() throws Exception {
        gf = null;
        roadType = null;
        subRoadType = null;
        roadFeatures = null;
        roadBounds = null;
        rd1Filter = null;
        rd2Filter = null;
        newRoad = null;
        riverType = null;
        subRiverType = null;     
        riverFeatures = null;
        riverBounds = null;
        rv1Filter = null;
        newRiver = null;                    
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
    public Polygon polygon( int[] xy ){
        LinearRing shell = ring( xy );
        return gf.createPolygon( shell, null );
    }

    /**
     * Creates a line from the specified (<var>x</var>,<var>y</var>) coordinates and
     * an arbitrary amount of holes.
     */
    public Polygon polygon( int[] xy, int[][] holes ){
        if( holes == null || holes.length == 0){
           return polygon( xy );
        }
        LinearRing shell = ring( xy );        
        
        LinearRing[] rings = new LinearRing[holes.length];

        for (int i = 0; i < xy.length; i++) {
            rings[i] = ring(holes[i]);
        }        
        return gf.createPolygon( shell, rings );        
    }

    /**
     * Creates a ring from the specified (<var>x</var>,<var>y</var>) coordinates.
     * The coordinates are stored in a flat array.
     */
    public LinearRing ring( int[] xy ){
        Coordinate[] coords = new Coordinate[xy.length / 2];

        for (int i = 0; i < xy.length; i += 2) {
            coords[i / 2] = new Coordinate(xy[i], xy[i + 1]);
        }

        return gf.createLinearRing(coords);        
    }

    /**
     * Compares two geometries for equality.
     */
    protected void assertEquals(Geometry expected, Geometry actual) {
        if (expected == actual) {
            return;
        }
        assertNotNull(expected);
        assertNotNull(actual);
        assertTrue(expected.equals(actual));
    }

    /**
     * Compares two geometries for equality.
     */
    protected void assertEquals(String message, Geometry expected, Geometry actual) {
        if (expected == actual) {
            return;
        }
        assertNotNull(message, expected);
        assertNotNull(message, actual);
        assertTrue(message, expected.equals(actual));
    }

    /**
     * Counts the number of Features returned by the specified reader.
     * <p>
     * This method will close the reader.
     * </p>
     */
    protected int count(  FeatureReader<SimpleFeatureType, SimpleFeature> reader ) throws IOException {
        if( reader == null) {
            return -1;
        }
        int count = 0;
        try {
            while( reader.hasNext() ){
                reader.next();
                count++;
            }
        } catch (NoSuchElementException e) {
            // bad dog!
            throw new DataSourceException("hasNext() lied to me at:"+count, e );
        } catch (Exception e) {
            throw new DataSourceException("next() could not understand feature at:"+count, e );
        }
        finally {
            reader.close();
        }
        return count;
    }

    /**
     * Counts the number of Features in the specified writer.
     * This method will close the writer.
     */
    protected int count(FeatureWriter<SimpleFeatureType, SimpleFeature> writer)
        throws NoSuchElementException, IOException {
        int count = 0;

        try {
            while (writer.hasNext()) {
                writer.next();
                count++;
            }
        } finally {
            writer.close();
        }

        return count;
    }               
}

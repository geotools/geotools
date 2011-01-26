/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts.spatialschema.geometry.primitive;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.geometry.jts.spatialschema.PositionFactoryImpl;
import org.geotools.geometry.jts.spatialschema.geometry.geometry.JTSGeometryFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.LineSegment;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

public class SurfaceImplTest extends TestCase {
    private PositionFactoryImpl postitionFactory;
    private PrimitiveFactoryImpl primitiveFactory;
    private JTSGeometryFactory geometryFactory;
    
    protected void setUp() throws Exception {
        postitionFactory = new PositionFactoryImpl( DefaultGeographicCRS.WGS84 );
        primitiveFactory = new PrimitiveFactoryImpl( DefaultGeographicCRS.WGS84 );
        geometryFactory = new JTSGeometryFactory( DefaultGeographicCRS.WGS84 );
        // TODO Auto-generated method stub
        super.setUp();
    }
    
    /** We need to create a large surface with 7000 points */
    public void testLargeSurface(){
         int NUMBER = 100000;
         double delta = 360.0 / (double) NUMBER;
         PointArray points = postitionFactory.createPointArray();
         for( double angle = 0.0; angle < 360.0; angle += delta ){
             double ordinates[] = new double[]{
                     Math.sin( Math.toRadians(angle) ),
                     Math.cos( Math.toRadians(angle) )
             };
             DirectPosition point = postitionFactory.createDirectPosition( ordinates );
             points.add( point );
         }
         List<OrientableCurve> curves = new ArrayList<OrientableCurve>();        
         // A curve will be created
         // - The curve will be set as parent curves for the Curve segments
         // - Start and end params for the CurveSegments will be set
         List<CurveSegment> segmentList = new ArrayList<CurveSegment>();
         for( int i=0; i<points.length();i++){
             int start = i;
             int end = (i+1)%points.size();
             DirectPosition point1 = points.getDirectPosition( start, null );
             DirectPosition point2 = points.getDirectPosition( end, null );
             LineSegment segment = geometryFactory.createLineSegment( point1, point2 );
             segmentList.add( segment );
         }
         Curve curve = primitiveFactory.createCurve( segmentList );
         curves.add( curve);
         Ring ring = primitiveFactory.createRing( curves );
         SurfaceBoundary boundary = primitiveFactory.createSurfaceBoundary(ring,new ArrayList());
         SurfaceImpl surface = (SurfaceImpl) primitiveFactory.createSurface(boundary);
         
         Geometry peer = surface.computeJTSPeer();
    }
}

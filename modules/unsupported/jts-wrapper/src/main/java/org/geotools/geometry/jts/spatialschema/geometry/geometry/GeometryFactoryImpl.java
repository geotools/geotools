/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/geometry/GeometryFactoryImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry.geometry;

// J2SE direct dependencies
import org.geotools.geometry.jts.spatialschema.geometry.DirectPositionImpl;
import org.geotools.geometry.jts.spatialschema.geometry.EnvelopeImpl;
import org.geotools.geometry.jts.spatialschema.geometry.aggregate.MultiPointImpl;
import org.geotools.geometry.jts.spatialschema.geometry.primitive.PolyhedralSurfaceImpl;
import org.geotools.geometry.jts.spatialschema.geometry.primitive.SurfaceBoundaryImpl;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.coordinate.Arc;
import org.opengis.geometry.coordinate.ArcByBulge;
import org.opengis.geometry.coordinate.ArcString;
import org.opengis.geometry.coordinate.ArcStringByBulge;
import org.opengis.geometry.coordinate.BSplineCurve;
import org.opengis.geometry.coordinate.BSplineSurface;
import org.opengis.geometry.coordinate.Geodesic;
import org.opengis.geometry.coordinate.GeodesicString;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.KnotType;
import org.opengis.geometry.coordinate.LineSegment;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Polygon;
import org.opengis.geometry.coordinate.PolyhedralSurface;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.coordinate.Tin;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;


/**
 * The {@code GeometryFactoryImpl} class/interface...
 * 
 * @author SYS Technologies
 * @author crossley
 *
 *
 * @source $URL$
 * @version $Revision $
 * @deprecated Use GeometryFactoryFinder
 */
public class GeometryFactoryImpl extends JTSGeometryFactory {
    public GeometryFactoryImpl( CoordinateReferenceSystem crs ){
        super( crs );
    }
}

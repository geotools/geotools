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
 *
 *    Refractions Research Inc. Can be found on the web at:
 *    http://www.refractions.net/
 *
 *    Created on Oct 31, 2003
 */
package org.geotools.data.oracle.sdo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author bowens
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 *
 * @source $URL$
 */
public interface TT 
{
	/** <code>TT</code> code representing unknown geometies (like splines) */
	public static final int UNKNOWN       = 00;

	/** <code>TT</code> code representing Point */
	public static final int POINT         = 01;

	/** <code>TT</code> code representing Line (or Curve) */
	public static final int LINE          = 02;

	/** <code>TT</code> code representing Curve (or Line) */
	public static final int CURVE         = 02;    
    
	/** <code>TT</code> code representing Polygon */
	public static final int POLYGON       = 03;

	/** <code>TT</code> code representing Collection */
	public static final int COLLECTION    = 04;   

	/** <code>TT</code> code representing Multpoint */
	public static final int MULTIPOINT    = 05;       

	/** <code>TT</code> code representing Multiline (or Multicurve) */
	public static final int MULTILINE     = 06;

	/** <code>TT</code> code representing Multicurve (or Multiline) */    
	public static final int MULTICURVE    = 06;

	/** <code>TT</code> code representing MULTIPOLYGON */
	public static final int MULTIPOLYGON  = 07;
	
	/** <code>TT</code> code representing SOLID */
    public static final int SOLID  = 8;
    
    /** <code>TT</code> code representing SOLID */
    public static final int MULTISOLID  = 9;
    
    /**
     * A map from geomery type, as a string, to JTS Geometry. See Oracle Spatial documentation,
     * Table 2-1, Valid SDO_GTYPE values.
     */
    public static final Map GEOM_CLASSES = Collections.unmodifiableMap(new GeomClasses());
        
        
    static final class GeomClasses extends HashMap {
        private static final long serialVersionUID = -3359664692996608331L;

        public GeomClasses() {
            super();
            put("UNKNOWN", Geometry.class);
            put("POINT", Point.class);
            put("LINE", LineString.class);
            put("CURVE", LineString.class);
            put("POLYGON", Polygon.class);
            put("COLLECTION", GeometryCollection.class);
            put("MULTIPOINT", MultiPoint.class);
            put("MULTILINE", MultiLineString.class);
            put("MULTICURVE", MultiLineString.class);
            put("MULTIPOLYGON", MultiPolygon.class);
        }
    }

}

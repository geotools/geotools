/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005-2007. All rights reserved.
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
 */

package org.geotools.data.db2;

import java.util.HashSet;
import java.util.Set;


/**
 * @author Christian Mueller
 * 
 * DB2 speficfic WKB Constants, contributed by David Adler
 *
 *
 *
 * @source $URL$
 */
public class DB2WKBConstants {
    
    // well-known binary geometry types
    // the original values used by DB2 didn't match the values
    // in the 2006 OGC spec for WKB when z and/or m is present
    // The names with "OCG" are the spec values
    // We support both to provide compatibility with existing
    // releases of DB2 but it may change in DB2 post-V9.7
    //

    protected final static int wkbPoint2D = 1;
    protected final static int wkbOGCPointZM = 3001;
    protected final static int wkbPointZM = 2000001;
    protected final static int wkbOGCPointZ = 1001;
    protected final static int wkbPointZ = 3000001;
    protected final static int wkbOGCPointM = 2001;
    protected final static int wkbPointM = 4000001;

    protected final static int wkbLineString2D = 2;
    protected final static int wkbOGCLineStringZM = 3002;
    protected final static int wkbLineStringZM = 2000002;
    protected final static int wkbOGCLineStringZ = 1002;
    protected final static int wkbLineStringZ = 3000002;
    protected final static int wkbOGCLineStringM = 2002;
    protected final static int wkbLineStringM = 4000002;

    protected final static int wkbPolygon2D = 3;
    protected final static int wkbOGCPolygonZM = 3003;
    protected final static int wkbPolygonZM = 2000005;
    protected final static int wkbOGCPolygonZ = 1003;
    protected final static int wkbPolygonZ = 3000005;
    protected final static int wkbOGCPolygonM = 2003;
    protected final static int wkbPolygonM = 4000005;

    protected final static int wkbMultiPoint2D = 4;
    protected final static int wkbOGCMultiPointZM = 3004;
    protected final static int wkbMultiPointZM = 2000007;
    protected final static int wkbOGCMultiPointZ = 1004;
    protected final static int wkbMultiPointZ = 3000007;
    protected final static int wkbOGCMultiPointM = 2004;
    protected final static int wkbMultiPointM = 4000007;

    protected final static int wkbMultiLineString2D = 5;
    protected final static int wkbOGCMultiLineStringZM = 3005;
    protected final static int wkbMultiLineStringZM = 2000009;
    protected final static int wkbOGCMultiLineStringZ = 1005;
    protected final static int wkbMultiLineStringZ = 3000009;
    protected final static int wkbOGCMultiLineStringM = 2005;
    protected final static int wkbMultiLineStringM = 4000009;

    protected final static int wkbMultiPolygon2D = 6;
    protected final static int wkbOGCMultiPolygonZM = 3006;
    protected final static int wkbMultiPolygonZM = 2000011;
    protected final static int wkbOGCMultiPolygonZ = 1006;
    protected final static int wkbMultiPolygonZ = 3000011;
    protected final static int wkbOGCMultiPolygonM = 2006;
    protected final static int wkbMultiPolygonM = 4000011;

    protected final static int wkbGeomCollection2D = 7;
    protected final static int wkbOGCGeomCollectionZM = 3007;
    protected final static int wkbGeomCollectionZM = 2000012;
    protected final static int wkbOGCGeomCollectionZ = 1007;
    protected final static int wkbGeomCollectionZ = 3000012;
    protected final static int wkbOGCGeomCollectionM = 2007;
    protected final static int wkbGeomCollectionM = 4000012; 
    
    protected static Set<Integer> zTypes;
    protected static Set<Integer> zmTypes;
    
    static {
        zTypes=new HashSet<Integer>();
        zTypes.add(wkbPointZ); 
        zTypes.add(wkbOGCPointZ);
        zTypes.add(wkbLineStringZ); 
        zTypes.add(wkbOGCLineStringZ);
        zTypes.add(wkbPolygonZ); 
        zTypes.add(wkbOGCPolygonZ);
        zTypes.add(wkbMultiPointZ); 
        zTypes.add(wkbOGCMultiPointZ);
        zTypes.add(wkbMultiLineStringZ); 
        zTypes.add(wkbOGCMultiLineStringZ);
        zTypes.add(wkbMultiPolygonZ); 
        zTypes.add(wkbOGCMultiPolygonZ);
        zTypes.add(wkbGeomCollectionZ); 
        zTypes.add(wkbOGCGeomCollectionZ);
        
        zmTypes=new HashSet<Integer>();
        zmTypes.add(wkbPointZM); 
        zmTypes.add(wkbOGCPointZM);
        zmTypes.add(wkbLineStringZM); 
        zmTypes.add(wkbOGCLineStringZM);
        zmTypes.add(wkbPolygonZM); 
        zmTypes.add(wkbOGCPolygonZM);
        zmTypes.add(wkbMultiPointZM); 
        zmTypes.add(wkbOGCMultiPointZM);
        zmTypes.add(wkbMultiLineStringZM); 
        zmTypes.add(wkbOGCMultiLineStringZM);
        zmTypes.add(wkbMultiPolygonZM); 
        zmTypes.add(wkbOGCMultiPolygonZM);
        zmTypes.add(wkbGeomCollectionZM); 
        zmTypes.add(wkbOGCGeomCollectionZM);
    }

}

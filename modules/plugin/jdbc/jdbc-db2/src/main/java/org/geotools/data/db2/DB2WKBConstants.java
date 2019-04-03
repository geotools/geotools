/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.db2;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Christian Mueller
 *     <p>DB2 speficfic WKB Constants
 */
public class DB2WKBConstants {

    // well-known binary geometry types
    // the original values used by DB2 didn't match the values
    // in the 2006 OGC spec for WKB when z and/or m is present
    // The names with "OCG" are the spec values
    // We support both to provide compatibility with existing
    // releases of DB2 but it may change in DB2 post-V9.7
    //

    protected static final int wkbPoint2D = 1;
    protected static final int wkbOGCPointZM = 3001;
    protected static final int wkbPointZM = 2000001;
    protected static final int wkbOGCPointZ = 1001;
    protected static final int wkbPointZ = 3000001;
    protected static final int wkbOGCPointM = 2001;
    protected static final int wkbPointM = 4000001;

    protected static final int wkbLineString2D = 2;
    protected static final int wkbOGCLineStringZM = 3002;
    protected static final int wkbLineStringZM = 2000002;
    protected static final int wkbOGCLineStringZ = 1002;
    protected static final int wkbLineStringZ = 3000002;
    protected static final int wkbOGCLineStringM = 2002;
    protected static final int wkbLineStringM = 4000002;

    protected static final int wkbPolygon2D = 3;
    protected static final int wkbOGCPolygonZM = 3003;
    protected static final int wkbPolygonZM = 2000005;
    protected static final int wkbOGCPolygonZ = 1003;
    protected static final int wkbPolygonZ = 3000005;
    protected static final int wkbOGCPolygonM = 2003;
    protected static final int wkbPolygonM = 4000005;

    protected static final int wkbMultiPoint2D = 4;
    protected static final int wkbOGCMultiPointZM = 3004;
    protected static final int wkbMultiPointZM = 2000007;
    protected static final int wkbOGCMultiPointZ = 1004;
    protected static final int wkbMultiPointZ = 3000007;
    protected static final int wkbOGCMultiPointM = 2004;
    protected static final int wkbMultiPointM = 4000007;

    protected static final int wkbMultiLineString2D = 5;
    protected static final int wkbOGCMultiLineStringZM = 3005;
    protected static final int wkbMultiLineStringZM = 2000009;
    protected static final int wkbOGCMultiLineStringZ = 1005;
    protected static final int wkbMultiLineStringZ = 3000009;
    protected static final int wkbOGCMultiLineStringM = 2005;
    protected static final int wkbMultiLineStringM = 4000009;

    protected static final int wkbMultiPolygon2D = 6;
    protected static final int wkbOGCMultiPolygonZM = 3006;
    protected static final int wkbMultiPolygonZM = 2000011;
    protected static final int wkbOGCMultiPolygonZ = 1006;
    protected static final int wkbMultiPolygonZ = 3000011;
    protected static final int wkbOGCMultiPolygonM = 2006;
    protected static final int wkbMultiPolygonM = 4000011;

    protected static final int wkbGeomCollection2D = 7;
    protected static final int wkbOGCGeomCollectionZM = 3007;
    protected static final int wkbGeomCollectionZM = 2000012;
    protected static final int wkbOGCGeomCollectionZ = 1007;
    protected static final int wkbGeomCollectionZ = 3000012;
    protected static final int wkbOGCGeomCollectionM = 2007;
    protected static final int wkbGeomCollectionM = 4000012;

    protected static Set<Integer> zTypes;
    protected static Set<Integer> zmTypes;

    static {
        zTypes = new HashSet<Integer>();
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

        zmTypes = new HashSet<Integer>();
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

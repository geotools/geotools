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
 */
package org.geotools.data.oracle.sdo;

//import oracle.spatial.geometry.JGeometry;


/**
 * Converts between JGeometry and JTS geometries. Is used by the SDO*Dialect classes, which
 * have proven not to deal performance gains, but I keep it here for historical purposes,
 * so that anyone willing to try this out won't have to re-code everything from scratch.
 * 
 * @author Andrea Aime - OpenGeo
 * 
 *
 * @source $URL$
 */
public class JGeometryConverter {
//    /**
//     * Converts a JGeometry into the equivalent JTS geometry 
//     * @param geom
//     * @return
//     */
//    public static Geometry toJTS(GeometryFactory gf, JGeometry geom) {
//        int dimensions = geom.getDimensions();
//        int lrs = geom.isLRSGeometry() ? dimensions : 0;
//        int gtype = dimensions * 1000 + lrs * 100 + geom.getType();
//        return SDO.create(gf, gtype, geom.getSRID(), geom.getPoint(), geom.getElemInfo(), geom.getOrdinatesArray());
//    }
//    
//    /**
//     * Converts a JTS geometry in the equivalent JTS geometry
//     * @param geom
//     * @return
//     */
//    public static JGeometry toJGeometry(Geometry geom, int srid) {
//        if( geom == null) 
//            throw new IllegalArgumentException("Cannot handle null geometries");
//        
//        int gtype = SDO.gType( geom );
//        double[] point = SDO.point( geom );
//        if(point != null) {
//            if(geom.getDimension() > 2)
//                return new JGeometry(point[0], point[1], point[2], srid);
//            else
//                return new JGeometry(point[0], point[1], srid);
//        } else {
//            int[] elemInfo = SDO.elemInfo( geom );
//            double[] ordinates = SDO.ordinates( geom );
//            return new JGeometry(gtype, srid, elemInfo, ordinates);
//        }                
//    }
}

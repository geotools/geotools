/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.h2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.InputStreamInStream;
import com.vividsolutions.jts.io.OutputStreamOutStream;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Static collection of JTS operations.
 * 
 * @author David Blasby, The Open Planning Project, dblasby@openplans.org
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 *
 * @source $URL$
 */
public class JTS {
    
    /**
     * Returns the current GeoTools version.
     */
    public static String GeoToolsVersion() {
        return "2.6-SNAPSHOT";
    }
    
    /**
     * Returns the Well Known Text of the geometry.
     */
    public static String AsWKT( byte[] wkb ) {
        if ( wkb == null ) {
            return null;
        }
        
        return fromWKB(wkb).toText();
    }

    /**
     * Returns the text representation of the envelope of the geometry.
     */
    public static String EnvelopeAsText( byte[] wkb ) {
        Envelope e = Envelope( wkb );
        if ( e != null ) {
            return e.toString();
        }
        
        return null;
    }
    
    /**
     * Reads a geometry from its well known text representation, specifying an 
     * srid.
     * 
     * @param wkt The well known text of the geometry.
     * @param srid The srid of the geometry
     * 
     * @return An array of bytes representing the geometry.
     */
    public static byte[] GeomFromText(String wkt, int srid) {
        if ( wkt == null ) {
            return null;
        }
        WKTReader reader = new WKTReader();
        try {
            Geometry g = reader.read( wkt );
            g.setSRID(srid);
            
            return toWKB( g );
        } 
        catch (ParseException e) {
            throw new RuntimeException( e );
        }
    }
    
    public static byte[] GeomFromWKB( byte[] wkb ) {
        return wkb;
    }
    
    /**
     * Returns the spatial reference identifier for the geometry.
     * <p>
     * This method will return -1 if <tt>wkb</tt> is <code>null</code>.
     * </p>
     * @param wkb The geometry.
     * 
     * @return The srid.
     */
    public static int GetSRID( byte[] wkb ) {
        if ( wkb == null ) {
            return -1;
        }
        return fromWKB(wkb).getSRID();
    }
    
    /**
     * Returns the envelope for a geometry.
     * <p>
     * This method will return an "null" envelope ({@link Envelope#setToNull()})
     * if <tt>wkb</tt> is <code>null</code>.
     * </p>
     * @param wkb The geometry.
     * @return The envelope of the geometry.
     */
    public static Envelope Envelope( byte[] wkb ) {
        if ( wkb == null ) {
            Envelope e = new Envelope();
            e.setToNull();
            return e;
        }
        
        return fromWKB(wkb).getEnvelopeInternal();
    }
    
    /**
     * Returns the type of the geometry as a string. Eg: 'LINESTRING', 'POLYGON', 
     * 'MULTIPOINT', etc.
     * <p>
     * This method returns <code>null</code> when <tt>wkb</tt> is <code>null</code>.
     * </p>
     * @param wkb The geometry.
     */
    public static String GeometryType( byte[] wkb ) {
        if ( wkb == null ) {
            return null;
        }
        
        Geometry g = fromWKB( wkb );
        return g != null ? g.getGeometryType().toUpperCase() : null;
    }
    
    private static Geometry fromWKB( byte[] wkb ) {
        
        try {
            ByteArrayInputStream bytes = 
                new ByteArrayInputStream( wkb, 0, wkb.length-4 );

            //read the geometry
            Geometry g = new WKBReader().read( new InputStreamInStream( bytes ) );
            
            //read the srid
            int srid = 0;
            srid |= wkb[wkb.length-4] & 0xFF;
            srid <<= 8;
            srid |= wkb[wkb.length-3] & 0xFF;
            srid <<= 8;
            srid |= wkb[wkb.length-2] & 0xFF;
            srid <<= 8;
            srid |= wkb[wkb.length-1] & 0xFF;
            g.setSRID(srid);
            
            return g;
            
        } 
        catch( Exception e ) {
            throw new RuntimeException( e );
        }
    }
    
    private static byte[] toWKB( Geometry g ) {
        try {
            WKBWriter w = new WKBWriter();
            
            //write the geometry
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            w.write( g , new OutputStreamOutStream( bytes ) );
   
            //supplement it with the srid
            int srid = g.getSRID();
            bytes.write( (byte)(srid >>> 24) );
            bytes.write( (byte)(srid >> 16 & 0xff) );
            bytes.write( (byte)(srid >> 8 & 0xff) );
            bytes.write( (byte)(srid & 0xff) );
            
            return bytes.toByteArray();
        } 
        catch (IOException e) {
            throw new RuntimeException( e );
        }
    }
//    
//    
//    /**
//     * Converts a well-known text string to a geometry.
//     */
//    public static Geometry geometryFromText(String wkt) {
//        WKTReader wktreader = new WKTReader();
//
//        try {
//            return wktreader.read(wkt);
//        } 
//        catch (Exception e) {
//            throw (IllegalArgumentException) new IllegalArgumentException("bad wkt").initCause( e );
//        }
//    }
//    
//    /**
//     * Converts a sequence sequence of bytes into a geometry.
//     */
//    public static Geometry geometryFromBytes(byte[] bytes) {
//        try {
//            return (Geometry) new ObjectInputStream( new ByteArrayInputStream( bytes ) ).readObject();
//        } 
//        catch ( Exception e ) {
//            throw (IllegalArgumentException) new IllegalArgumentException( "bad bytes" ).initCause( e );
//        }
//    }
//    
//    /**
//     * Calculates the bounding box of a geometry. 
//     * <p>
//     * {@link Geometry#getUserData()} is checked for an instance of 
//     * {@link CoordinateReferenceSystem} and set on the envelope if it exists.
//     * </p>
//     */
//    public static ReferencedEnvelope extent( Geometry g ) {
//        Envelope e = g.getEnvelopeInternal();
//        if ( g.getUserData() instanceof CoordinateReferenceSystem ) {
//            return new ReferencedEnvelope(e,(CoordinateReferenceSystem) g.getUserData());
//        }
//        
//        return new ReferencedEnvelope(e,null);
//    }
//
//    public static ReferencedEnvelope extentB( byte[] bytes ) {
//        return extent( geometryFromBytes( bytes ) );
//    }
//    
//   
//    public static Geometry setSRID( byte[] bytes, int srid ) {
//        Geometry g = geometryFromBytes(bytes);
//        g.setUserData(parseSRID(srid));
//        return g;
//    }
//    
//    public static int getSRID( byte[] bytes ) {
//        Geometry g = geometryFromBytes(bytes);
//        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) g.getUserData();
//        if ( crs != null ) {
//            //TODO: factor this out into geotools utility method
//            for (Iterator i = crs.getIdentifiers().iterator(); i.hasNext();) {
//                Identifier id = (Identifier) i.next();
//
//                //return "EPSG:" + id.getCode();
//                if ((id.getAuthority() != null)
//                        && id.getAuthority().getTitle().equals(Citations.EPSG.getTitle())) {
//                    return Integer.parseInt(id.getCode());
//                }
//            }
//        }
//        
//        return -1;
//    }
//    
//    
////    public static ReferencedEnvelope setSRID( ReferencedEnvelope box, int srid ) {
////        return new ReferencedEnvelope( box, parseSRID( srid ) );
////    }
//
//  
//    private static CoordinateReferenceSystem parseSRID( int srid ) {
//        try {
//            return CRS.decode("epsg:" + srid);
//        } 
//        catch (Exception e) {
//            throw new RuntimeException("Could not parse srid: " + srid );
//        }
//    }
}

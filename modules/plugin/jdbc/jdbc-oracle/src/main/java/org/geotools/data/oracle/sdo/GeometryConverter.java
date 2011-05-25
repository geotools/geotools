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
package org.geotools.data.oracle.sdo;

import java.sql.SQLException;
import java.util.Iterator;

import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.CHAR;
import oracle.sql.CharacterSet;
import oracle.sql.Datum;
import oracle.sql.NUMBER;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
/**
 * Sample use of SDO class for simple JTS Geometry.
 * <p>
 * If needed I can make a LRSGeometryConverter that allows
 * JTS Geometries with additional ordinates beyond xyz.
 * </p>
 * @author jgarnett
 *
 * @source $URL$
 */
public class GeometryConverter {
	protected OracleConnection connection;
	GeometryFactory geometryFactory;
    
	public GeometryConverter( OracleConnection connection ){
		this( connection, new GeometryFactory() );		
	}
	public GeometryConverter( OracleConnection connection, GeometryFactory geometryFactory ){
		this.geometryFactory = geometryFactory;
		this.connection = connection;
	}
    public static final String DATATYPE = "MDSYS.SDO_GEOMETRY";
    /**
     * Used to handle MDSYS.SDO_GEOMETRY.
     *  
     * @return <code>MDSYS.SDO_GEOMETRY</code>
     * 
     * @see net.refractions.jspatial.Converter#getDataType()
     */
    public String getDataTypeName() {
        return DATATYPE;
    }

    /**
     * Ensure that obj is a JTS Geometry (2D or 3D) with no LRS measures.
     * <p>
     * This Converter does not support SpatialCoordinates</p>
     * 
     * @param geom    the Geometry to be converted
     * @return <code>true</code> if <code>obj</code> is a JTS Geometry
     * @see net.refractions.jspatial.Converter#isCapable(java.lang.Object)
     */
    public boolean isCapable(Geometry geom) {
        if( geom == null ) return true;
        if( geom instanceof Point      || geom instanceof MultiPoint ||
        	geom instanceof LineString || geom instanceof MultiLineString ||
        	geom instanceof Polygon    || geom instanceof MultiPolygon ||
        	geom instanceof GeometryCollection )
        {
            int d = SDO.D( geom );
            int l = SDO.L( geom );
            return l == 0 && ( d == 2 || d == 3);
        }
        return false;
    }

    /**
     * Convert provided SDO_GEOMETRY to JTS Geometry.
     * <p>
     * Will return <code>null</code> as <code>null</code>.
     * </p>
     * 
     * @param sdoGeometry datum STRUCT to be converted to a double[]
     * 
     * @return JTS <code>Geometry</code> representing the provided <code>datum</code>
     * @throws SQLException
     * 
     * @see net.refractions.jspatial.Converter#toObject(oracle.sql.STRUCT)
     */
    public Geometry asGeometry(STRUCT sdoGeometry) throws SQLException {
        // Note Returning null for null Datum
        if( sdoGeometry == null ) return null;
        
        Datum data[] = sdoGeometry.getOracleAttributes();
        final int GTYPE = asInteger( data[0], 0 );
        final int SRID = asInteger( data[1], SDO.SRID_NULL );
        final double POINT[] = asDoubleArray( (STRUCT) data[2], Double.NaN );
        final int ELEMINFO[] = asIntArray( (ARRAY) data[3], 0 );
        final double ORDINATES[] = asDoubleArray( (ARRAY) data[4], Double.NaN );;
                
        return SDO.create( geometryFactory,
                           GTYPE,
                           SRID,
                           POINT,
                           ELEMINFO,
                           ORDINATES );
    }
    
    /**
     * Used to convert double[] to SDO_ODINATE_ARRAY.
     * <p>
     * Will return <code>null</code> as an empty <code>SDO_GEOMETRY</code></p>
     * 
     * @param geom Map to be represented as a STRUCT
     * @return STRUCT representing provided Map
     * @see net.refractions.jspatial.Converter#toDataType(java.lang.Object)
     */
    public STRUCT toSDO(Geometry geom) throws SQLException {
        return toSDO(geom, geom.getSRID());
    }
    
    /**
     * Used to convert double[] to SDO_ODINATE_ARRAY.
     * <p>
     * Will return <code>null</code> as an empty <code>SDO_GEOMETRY</code></p>
     * 
     * @param geom Map to be represented as a STRUCT
     * @return STRUCT representing provided Map
     * @see net.refractions.jspatial.Converter#toDataType(java.lang.Object)
     */
    public STRUCT toSDO(Geometry geom, int srid) throws SQLException {
        if( geom == null) return asEmptyDataType();
        
        int gtype = SDO.gType( geom );
        NUMBER SDO_GTYPE = new NUMBER( gtype );
        
        NUMBER SDO_SRID = (srid == SDO.SRID_NULL || srid == 0) ? null :
                          new NUMBER( srid );
        
        double[] point = SDO.point( geom );
        STRUCT SDO_POINT;
        
        ARRAY SDO_ELEM_INFO;
        ARRAY SDO_ORDINATES;
        
        if( point == null ) {
            final Envelope env = geom.getEnvelopeInternal();
            if(env.getWidth() > 0 && env.getHeight() > 0 && geom.getEnvelope().equals(geom)) {
                // rectangle optimization. Actually, more than an optimization. A few operators
                // do not work properly if they don't get rectangular geoms encoded as rectangles
                // SDO_FILTER is an example of this silly situation
                SDO_POINT = null;
                int elemInfo[] = new int[] {1, 1003, 3};
                double ordinates[];
                if(SDO.D(geom) == 2)
                    ordinates = new double[] {env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY()};
                else
                    ordinates = new double[] {env.getMinX(), env.getMinY(), 0, env.getMaxX(), env.getMaxY(), 0};
                
                SDO_POINT = null;
                SDO_ELEM_INFO = toARRAY( elemInfo, "MDSYS.SDO_ELEM_INFO_ARRAY" );
                SDO_ORDINATES = toARRAY( ordinates, "MDSYS.SDO_ORDINATE_ARRAY" );
            } else {
                int elemInfo[] = SDO.elemInfo( geom );
                double ordinates[] = SDO.ordinates( geom );
                
                SDO_POINT = null;
                SDO_ELEM_INFO = toARRAY( elemInfo, "MDSYS.SDO_ELEM_INFO_ARRAY" );
                SDO_ORDINATES = toARRAY( ordinates, "MDSYS.SDO_ORDINATE_ARRAY" );                        
            }
        } else { // Point Optimization
            Datum data[] = new Datum[]{
                toNUMBER( point[0] ),
                toNUMBER( point[1] ),
                toNUMBER( point[2] ),
            };
            SDO_POINT = toSTRUCT( data, "MDSYS.SDO_POINT_TYPE"  );
            SDO_ELEM_INFO = null;
            SDO_ORDINATES = null;
        }                
        Datum attributes[] = new Datum[]{
            SDO_GTYPE,
            SDO_SRID,
            SDO_POINT,
            SDO_ELEM_INFO,
            SDO_ORDINATES
        };
        return toSTRUCT( attributes, DATATYPE );        
    }
    
    
    
    /**
     * Representation of <code>null</code> as an Empty <code>SDO_GEOMETRY</code>.
     * 
     * @return <code>null</code> as a SDO_GEOMETRY
     */
    protected STRUCT asEmptyDataType() throws SQLException{
        return toSTRUCT( null, DATATYPE );
    }
    
    /** Convience method for STRUCT construction. */
    protected final STRUCT toSTRUCT( Datum attributes[], String dataType )
            throws SQLException
    {
    	if( dataType.startsWith("*.")){
    		dataType = "DRA."+dataType.substring(2);//TODO here
    	}
        StructDescriptor descriptor =
            StructDescriptor.createDescriptor( dataType, connection );
    
         return new STRUCT( descriptor, connection, attributes );
    }
    
    /** 
     * Convience method for ARRAY construction.
     * <p>
     * Compare and contrast with toORDINATE - which treats <code>Double.NaN</code>
     * as<code>NULL</code></p>
     */
    protected final ARRAY toARRAY( double doubles[], String dataType )
            throws SQLException
    {
        ArrayDescriptor descriptor =
            ArrayDescriptor.createDescriptor( dataType, connection );
        
         return new ARRAY( descriptor, connection, doubles );
    }
    
    /**
     * Convience method for ARRAY construction.
     * <p>
     * Forced to burn memory here - only way to actually place
     * <code>NULL</code> numbers in the ordinate stream.</p>
     * <ul>
     * <li>JTS: records lack of data as <code>Double.NaN</code></li>
     * <li>SDO: records lack of data as <code>NULL</code></li>
     * </ul>
     * <p>
     * The alternative is to construct the array from a array of
     * doubles, which does not record <code>NULL</code> NUMBERs.</p>
     * <p>
     * The results is an "MDSYS.SDO_ORDINATE_ARRAY"</p>
     * <code><pre>
     * list     = c1(1,2,0), c2(3,4,Double.NaN)
     * measures = {{5,6},{7,8}
     * 
     * toORDINATE( list, measures, 2 )
     * = (1,2,5,7, 3,4,6,8)
     * 
     * toORDINATE( list, measures, 3 )
     * = (1,2,0,5,7, 3,4,NULL,6,8)
     * 
     * toORDINATE( list, null, 2 )
     * = (1,2, 3,4)
     * </pre></code>
     * 
     * @param list     CoordinateList to be represented
     * @param measures Per Coordiante Measures, <code>null</code> if not required
     * @param D        Dimension of Coordinates (limited to 2d, 3d)         
     */
    protected final ARRAY toORDINATE( CoordinateList list,
                                      double measures[][],
                                      final int D)
            throws SQLException
    {
        ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor(
            "MDSYS.SDO_ORDINATE_ARRAY",
            connection
        );
        
        final int LENGTH = measures != null ? measures.length : 0;
        final int LEN = D + LENGTH;
        Datum data[] = new Datum[ list.size()*LEN];
        int offset = 0;
        int index = 0;
        Coordinate coord;
        
        for( Iterator i=list.iterator(); i.hasNext(); index++)
        {
            coord = (Coordinate) i.next();
            
            data[ offset++ ] = toNUMBER( coord.x );
            data[ offset++ ] = toNUMBER( coord.y );
            if( D == 3 )
            {
                data[ offset++ ] = toNUMBER( coord.x );
            }
            for( int j=0; j<LENGTH; j++)
            {
                data[ offset++ ] = toNUMBER( measures[ j][ index ] );
            }
        }
        return new ARRAY( descriptor, connection, data );
    }
    protected final ARRAY toORDINATE( double ords[] )
        throws SQLException
    {
        ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor(
            "MDSYS.SDO_ORDINATE_ARRAY",
            connection
        );
    
        final int LENGTH = ords.length;
        
        Datum data[] = new Datum[ LENGTH ];
        
        for( int i=0; i<LENGTH; i++ )
        {
            data[ i ] = toNUMBER( ords[i] );            
        }
        return new ARRAY( descriptor, connection, data );
    }
    protected final ARRAY toATTRIBUTE( double ords[], String desc )
    throws SQLException
    {
    	ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor(
    			desc,
    			connection
    	);
    	
    	final int LENGTH = ords.length;
    	
    	Datum data[] = new Datum[ LENGTH ];
    	
    	for( int i=0; i<LENGTH; i++ )
    	{
    		data[ i ] = toNUMBER( ords[i] );            
    	}
    	return new ARRAY( descriptor, connection, data );
    }

    /** 
     * Convience method for NUMBER construction.
     * <p>
     * Double.NaN is represented as <code>NULL</code> to agree
     * with JTS use.</p>
     */
    protected final NUMBER toNUMBER( double number ) throws SQLException{
        if( Double.isNaN( number )){
            return null;
        }
        return new NUMBER( number );
    }
    
    /** 
     * Convience method for ARRAY construction.
     */
    protected final ARRAY toARRAY( int ints[], String dataType )
        throws SQLException
    {
        ArrayDescriptor descriptor =
            ArrayDescriptor.createDescriptor( dataType, connection );
            
         return new ARRAY( descriptor, connection, ints );
    }
    
    /** Convience method for NUMBER construction */
    protected final NUMBER toNUMBER( int number )
    {
        return new NUMBER( number );
    }
    
    /** Convience method for CHAR construction */
    protected final CHAR toCHAR( String s )
    {
    	
    	// make sure if the string is larger than one character, only take the first character
    	if (s.length() > 1)
    		s = new String((new Character(s.charAt(0))).toString());
    	try {
			//BUG: make sure I am correct
			return new CHAR(s, CharacterSet.make(CharacterSet.ISO_LATIN_1_CHARSET));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    //
    // These functions present Datum as a Java type
    //     
    /** Presents datum as an int */
    protected int asInteger( Datum datum, final int DEFAULT )
            throws SQLException
    {
        if( datum == null) return DEFAULT;
        return ((NUMBER) datum).intValue();
    }
    /** Presents datum as a double */
    protected double asDouble( Datum datum, final double DEFAULT )
            throws SQLException
    {
        if( datum == null) return DEFAULT;
        return ((NUMBER) datum).doubleValue();
    }
    
    /** Presents struct as a double[] */
    protected double[] asDoubleArray( STRUCT struct, final double DEFAULT )
        throws SQLException
    {
        if( struct == null ) return null;
        return asDoubleArray( struct.getOracleAttributes(), DEFAULT );
    }
    
    /** Presents array as a double[] */
    protected double[] asDoubleArray( ARRAY array, final double DEFAULT )
        throws SQLException
    {
        if( array == null ) return null;
        if( DEFAULT == 0 ) return array.getDoubleArray();
        
        return asDoubleArray( array.getOracleArray(), DEFAULT );
    }
    
    /** Presents Datum[] as a double[] */
    protected double[] asDoubleArray( Datum data[], final double DEFAULT )
        throws SQLException
    {
        if( data== null ) return null;
        double array[] = new double[ data.length ];
        for( int i=0; i<data.length; i++ )
        {
            array[ i ] = asDouble( data[ i ], DEFAULT );
        }
        return array;
    }
    protected int[] asIntArray( ARRAY array, int DEFAULT )
        throws SQLException
    {
        if( array == null ) return null;
        if( DEFAULT == 0 ) return array.getIntArray();
        
        return asIntArray( array.getOracleArray(), DEFAULT );
    }
    /** Presents Datum[] as a int[] */
    protected int[] asIntArray( Datum data[], final int DEFAULT )
        throws SQLException
    {
        if( data== null ) return null;
        int array[] = new int[ data.length ];
        for( int i=0; i<data.length; i++ )
        {
            array[ i ] = asInteger( data[ i ], DEFAULT );
        }
        return array;
    }
}

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
package org.geotools.feature.simple;

import java.math.BigInteger;
import java.net.URI;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;

import javax.xml.namespace.QName;

import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.feature.type.SchemaImpl;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.GeometryType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Schema containing a set of "simple" types. 
 * <p>
 * This schema is used to create features with simple content. It contains 
 * attribute types which correspond to xml schema types from the xml schema 
 * and gml namespaces.
 * </p>
 * <p>
 * The attribute types in this schema maintain a unique mapping to java classes
 * so it can be used to map from java class to attribute type and vice versa.
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 * @source $URL$
 */
public class SimpleSchema extends SchemaImpl {
   
	//internal factory
	private static FeatureTypeFactory factory = new FeatureTypeFactoryImpl();
	
	//schema namespace
	public static final String NAMESPACE = "http://www.geotools.org/simple";
	//
    // Builtin Java Types
    //
    /** BOOLEAN to Boolean.class */        
    public static final AttributeType BOOLEAN = factory.createAttributeType(
		new NameImpl(NAMESPACE,"boolean"), Boolean.class, false, false,
		Collections.EMPTY_LIST, (AttributeType) null, null 
	);
    /** STRING to String.class */ 
    public static final AttributeType STRING = factory.createAttributeType(
        new NameImpl(NAMESPACE,"string"), String.class, false,
        false,Collections.EMPTY_LIST, (AttributeType) null, null
    );
    /** HEXBINRAY to byte[].class */
    public static final AttributeType HEXBINARY = factory.createAttributeType(
        new NameImpl(NAMESPACE,"string"), byte[].class, false,
        false,Collections.EMPTY_LIST, (AttributeType) null, null
    );
    /** QNAME to byte[].class */
    public static final AttributeType QNAME = factory.createAttributeType(
        new NameImpl(NAMESPACE,"QName"), QName.class, false,
        false,Collections.EMPTY_LIST, (AttributeType) null, null
    );
    /** QNAME to byte[].class */
    public static final AttributeType URI = factory.createAttributeType(
        new NameImpl(NAMESPACE,"anyUri"), URI.class, false,
        false,Collections.EMPTY_LIST, (AttributeType) null, null
    );
    //
    // Numerics
    //
//    /** NUMBER to Number.class */    
//    public static final AttributeType NUMBER = factory.createAttributeType(
//        new Name(NAMESPACE,"number"), Number.class, false,
//        false,Collections.EMPTY_LIST, (AttributeType) null, null
//    );
    /**
     * INT to java Integer.class
     */    
    public static final AttributeType INT = factory.createAttributeType(
        new NameImpl(NAMESPACE,"int"), Integer.class, false,
        false,Collections.EMPTY_LIST,null, null
    );
    /**
     * INTEGER to BigInteger
     */
    public static final AttributeType INTEGER = factory.createAttributeType(
        new NameImpl(NAMESPACE,"integer"), BigInteger.class, false,
        false,Collections.EMPTY_LIST,null, null
    );
    /**
     * FLOAT to java Float.class
     */      
    public static final AttributeType FLOAT = factory.createAttributeType(
        new NameImpl(NAMESPACE,"float"), Float.class, false,
        false,Collections.EMPTY_LIST,null, null
    );
    /** DOUBLE to Double.class */
    public static final AttributeType DOUBLE = factory.createAttributeType(
        new NameImpl(NAMESPACE,"double"), Double.class, false,
        false,Collections.EMPTY_LIST,null, null
    );
    /** LONG to Long.class */
    public static final AttributeType LONG = factory.createAttributeType(
        new NameImpl(NAMESPACE,"long"), Long.class, false,
        false,Collections.EMPTY_LIST,null, null
    );
    /** SHORT to Short.class */
    public static final AttributeType SHORT = factory.createAttributeType(
        new NameImpl(NAMESPACE,"short"), Short.class, false,
        false,Collections.EMPTY_LIST,null, null
    );
    /** BYTE to Byte.class */
    public static final AttributeType BYTE = factory.createAttributeType(
        new NameImpl(NAMESPACE,"byte"), Byte.class, false,
        false,Collections.EMPTY_LIST,null, null
    );

    //
    // TEMPORAL
    //
    /** DATE to java.sql.Date.class */
    public static final AttributeType DATE = factory.createAttributeType(
        new NameImpl(NAMESPACE,"date"), Date.class, false,
        false,Collections.EMPTY_LIST, (AttributeType) null, null
    );
    /** TIME to java.sq1.Time.class */
    public static final AttributeType TIME = factory.createAttributeType(
        new NameImpl(NAMESPACE,"time"), Time.class, false,
        false,Collections.EMPTY_LIST, (AttributeType) null, null
    );
    /**
     * DATETIME to java.sql.Timestamp
     * <p>
     * Data and a Time like a timestamp.
     */    
    public static final AttributeType DATETIME = factory.createAttributeType(
        new NameImpl(NAMESPACE,"datetime"), Timestamp.class, false,
        false,Collections.EMPTY_LIST, (AttributeType) null, null
    );
    
    //
    // Geomtries
    //
    /** Geometry to Geometry.class */
    public static final GeometryType GEOMETRY = factory.createGeometryType(
        new NameImpl(NAMESPACE,"GeometryPropertyType"), Geometry.class, null, false, false, 
        Collections.EMPTY_LIST, null, null
    );
    /** POINT (extends GEOMETRY) binds to Point.class */    
    public static final GeometryType POINT = factory.createGeometryType(
        new NameImpl(NAMESPACE,"PointPropertyType"), Point.class, null, false, false, 
        Collections.EMPTY_LIST, null, null
    );
    /** LINESTRING (extends GEOMETRY) binds to LineString.class */        
    public static final GeometryType LINESTRING = factory.createGeometryType(
        new NameImpl(NAMESPACE,"LineStringPropertyType"), LineString.class, null, false, 
        false, Collections.EMPTY_LIST, null, null
    );
//    /** LINEARRING (extends GEOMETRY) binds to LinearRing.class */            
//    public static final GeometryType LINEARRING = factory.createGeometryType(
//        new Name(NAMESPACE,"LinearRingPropertyType"), LinearRing.class, null, false, 
//        false, Collections.EMPTY_LIST, LINESTRING, null
//    );
    /**  POLYGON (extends GEOMETRY) binds to Polygon.class */            
    public static final GeometryType POLYGON = factory.createGeometryType(
        new NameImpl(NAMESPACE,"PolygonPropertyType"), Polygon.class, null, false, 
        false, Collections.EMPTY_LIST, null, null
    );
    /**  MULTIGEOMETRY (extends GEOMETRY) binds to GeometryCollection.class */                
    public static final GeometryType MULTIGEOMETRY = factory.createGeometryType(
        new NameImpl(NAMESPACE,"MultiGeometryPropertyType"), GeometryCollection.class, null,
        false, false, Collections.EMPTY_LIST, null, null
    );
    
    /**  MULTIPOINT (extends MULTIGEOMETRY) binds to MultiPoint.class */            
    public static final GeometryType MULTIPOINT = factory.createGeometryType(
        new NameImpl(NAMESPACE,"MultiPointPropertyType"), MultiPoint.class, null, false, false, 
        Collections.EMPTY_LIST, null, null
    );
    
    /**  MULTILINESTRING (extends MULTIGEOMETRY) binds to MultiLineString.class */            
    public static final GeometryType MULTILINESTRING = factory.createGeometryType(
        new NameImpl(NAMESPACE,"MultiLineStringPropertyType"), MultiLineString.class, null, 
        false, false, Collections.EMPTY_LIST, null, null
    );
    
    /** MULTIPOLYGON (extends MULTIGEOMETRY) binds to MultiPolygon.class */            
    public static final GeometryType MULTIPOLYGON = factory.createGeometryType(
        new NameImpl(NAMESPACE,"MultiPolytonPropertyType"), MultiPolygon.class, null, false, 
        false, Collections.EMPTY_LIST, MULTIGEOMETRY, null
    );
    
    public SimpleSchema() {
        super(NAMESPACE);
        
        put(INTEGER.getName(),INTEGER);
        put(DOUBLE.getName(),DOUBLE);
        put(LONG.getName(),LONG);
        put(FLOAT.getName(),FLOAT);
        put(SHORT.getName(),SHORT);
        put(BYTE.getName(),BYTE);
        //put(NUMBER.getName(),NUMBER);
        put(STRING.getName(),STRING);
        put(BOOLEAN.getName(),BOOLEAN);
        put(QNAME.getName(),QNAME);
        put(URI.getName(),URI);
        
        put(DATE.getName(),DATE);
        put(DATETIME.getName(),DATETIME);
        
        put(GEOMETRY.getName(),GEOMETRY);
        put(POINT.getName(),POINT);
        put(LINESTRING.getName(),LINESTRING);
        //put(LINEARRING.getName(),LINEARRING);
        put(POLYGON.getName(),POLYGON);
        put(MULTIGEOMETRY.getName(),MULTIGEOMETRY);
        put(MULTIGEOMETRY.getName(),MULTIGEOMETRY);
        put(MULTIPOINT.getName(),MULTIPOINT);
        put(MULTILINESTRING.getName(),MULTILINESTRING);
        put(MULTIPOLYGON.getName(),MULTIPOLYGON);
        
    }

}

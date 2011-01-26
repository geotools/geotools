/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.type;

import java.util.Collections;

import org.geotools.feature.DefaultAttributeType;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.JTS;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.geotools.referencing.crs.DefaultGeocentricCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Provides ...TODO summary sentence
 * <p>
 * TODO Description
 * </p><p>
 * </p><p>
 * Example Use:<pre><code>
 * GeometryAttributeType x = new GeometryAttributeType( ... );
 * TODO code example
 * </code></pre>
 * </p>
 * @author Leprosy
 * @since 0.3
 * TODO: test wkt geometry parse.
 * @source $URL$
 * 
 * @deprecated use {@link GeometryTypeImpl}.
 *
 */
public class GeometricAttributeType extends DefaultAttributeType implements org.geotools.feature.GeometryAttributeType{
    /** CoordianteSystem used by this GeometryAttributeType */
    //protected CoordinateReferenceSystem crs;
    protected GeometryFactory geometryFactory;
    

    public GeometricAttributeType(String name, Class type, boolean nillable, int min, int max,
        Object defaultValue, CoordinateReferenceSystem crs, Filter filter) {
        super(createAttributeType(name, type, crs,filter), name, nillable,min,max,defaultValue);
         
        geometryFactory = getCoordinateSystem() == null ? 
        		CSGeometryFactory.DEFAULT : new CSGeometryFactory(getCoordinateSystem());
        
        /*
        coordinateSystem = (cs != null) ? cs : LocalCoordinateSystem.CARTESIAN;
        geometryFactory = (cs == LocalCoordinateSystem.CARTESIAN)
            ? CSGeometryFactory.DEFAULT : new CSGeometryFactory(cs);
         */
    }

   public GeometricAttributeType(String name, Class type, boolean nillable,
        Object defaultValue, CoordinateReferenceSystem cs,Filter filter) {
        this(name, type, nillable,1,1, defaultValue, cs,filter);
   }

    public GeometricAttributeType(GeometricAttributeType copy, CoordinateReferenceSystem override) {
    	this( copy.getLocalName(), copy.getBinding(), copy.isNillable(), copy.getMinOccurs(),copy.getMaxOccurs(), null, crs(override), copy.getRestriction() );
    	
    	
        geometryFactory = (getCoordinateSystem() == DefaultGeocentricCRS.CARTESIAN)
            ? CSGeometryFactory.DEFAULT : new CSGeometryFactory(getCoordinateSystem());            
    }

    
    protected GeometricAttributeType(GeometryType type, Name name, int min, int max, boolean isNillable,Object defaultValue) {
		super(type, name, min, max, isNillable,defaultValue);
	}

    public GeometryType getType() {
        return (GeometryType) super.getType();
    }
    
	public CoordinateReferenceSystem getCoordinateSystem() {
        return ((GeometryType)getType()).getCoordinateReferenceSystem();
    }
	
	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return getCoordinateSystem();
	}

    public GeometryFactory getGeometryFactory() {
        return geometryFactory;
    }

    
    public Object duplicate(Object o) throws IllegalAttributeException {
        if (o == null)
            return o;
        if (o instanceof Geometry) {
            return ((Geometry)o).clone();
        }
        throw new IllegalAttributeException("Cannot duplicate " + o.getClass().getName());
    }

    public static CoordinateReferenceSystem crs( CoordinateReferenceSystem override ) {
    	CoordinateReferenceSystem crs = override;

        if (override != null) {
            crs = override;
        }

        if (crs == null) {
            crs = DefaultGeocentricCRS.CARTESIAN;
        }
        
        return crs;
    }
    
    public static GeometryType createAttributeType(String name,Class binding,CoordinateReferenceSystem crs,Filter restriction) {
    	return new GeometryTypeImpl( 
			new NameImpl(name),binding,crs,false,false,
			restriction != null ? Collections.singletonList(restriction) : Collections.EMPTY_LIST, 
			null,null);
    }
}
/**
 * Helper class used to force CS information on JTS Geometry
 */
class CSGeometryFactory extends GeometryFactory {
    
    static public GeometryFactory DEFAULT = new GeometryFactory();    
    static public PrecisionModel DEFAULT_PRECISON_MODEL = new PrecisionModel();

    public CSGeometryFactory(CoordinateReferenceSystem cs) {
        super(toPrecisionModel(cs), toSRID(cs));
    }

    public GeometryCollection createGeometryCollection(Geometry[] geometries) {
        GeometryCollection gc = super.createGeometryCollection(geometries);

        // JTS14
        //gc.setUserData( cs );
        return gc;
    }

    public LinearRing createLinearRing(Coordinate[] coordinates) {
        LinearRing lr = super.createLinearRing(coordinates);

        // JTS14
        //gc.setUserData( cs );
        return lr;
    }

    //
    // And so on
    // Utility Functions
    private static int toSRID(CoordinateReferenceSystem cs) {
        if ((cs == null) || (cs == DefaultGeocentricCRS.CARTESIAN)) {
            return 0;
        }

        // not sure how to tell SRID from CoordinateSystem?
        return 0;
    }

    private static PrecisionModel toPrecisionModel(CoordinateReferenceSystem cs) {
        if ((cs == null) || (cs == DefaultGeocentricCRS.CARTESIAN)) {
            return DEFAULT_PRECISON_MODEL;
        }

        return DEFAULT_PRECISON_MODEL;
    }
}


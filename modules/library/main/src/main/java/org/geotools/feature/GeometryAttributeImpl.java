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
package org.geotools.feature;


import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.Utilities;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.filter.identity.Identifier;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;

/**
 * TODO: rename to GeometricAttribute Provides ...TODO summary sentence
 * <p>
 * TODO Description
 * </p>
 * <p>
 * </p>
 * <p>
 * Example Use:
 * 
 * <pre><code>
 *         GeometryAttributeType x = new GeometryAttributeType( ... );
 *         TODO code example
 * </code></pre>
 * 
 * </p>
 * 
 * @author Leprosy
 * @since 0.3 TODO: test wkt geometry parse.
 *
 *
 * @source $URL$
 */
public class GeometryAttributeImpl extends AttributeImpl implements
		GeometryAttribute {
	/**
	 * bounds, derived
	 */
    protected BoundingBox bounds;

	public GeometryAttributeImpl( Object content, GeometryDescriptor descriptor, Identifier id ) {
		super(content, descriptor, id);
	}

	public GeometryType getType() {
	    return (GeometryType) super.getType();
	}
	
	public GeometryDescriptor getDescriptor() {
	    return (GeometryDescriptor) super.getDescriptor();
	}
	
	public Geometry getValue() {
		return (Geometry) super.getValue();
	}

	public void setValue(Object newValue) throws IllegalArgumentException,
	        IllegalStateException {
	    setValue( (Geometry) newValue );
	}
	
	public void setValue(Geometry geometry) {
		super.setValue(geometry);
	}

	/**
	 * Set the bounds for the contained geometry.
	 */
	public synchronized void setBounds( BoundingBox bbox ){
		bounds = bbox;
	}
	
	/**
	 * Returns the non null envelope of this attribute. If the attribute's
	 * geometry is <code>null</code> the returned Envelope
	 * <code>isNull()</code> is true.
	 * 
	 * @return 
	 */
	public synchronized BoundingBox getBounds() {
		if( bounds == null ){
			ReferencedEnvelope bbox = new ReferencedEnvelope(getType().getCoordinateReferenceSystem());
			Geometry geom = (Geometry) getValue();
			if (geom != null) {
				bbox.expandToInclude(geom.getEnvelopeInternal());
			}
			else {
			    bbox.setToNull();
			}
			bounds =  bbox;
		}
		return bounds;
	}

	public boolean equals(Object o) {
	    if ( this == o ) {
	        return true;
	    }
	    
		if (!(o instanceof GeometryAttributeImpl)) {
			return false;
		}
		
		GeometryAttributeImpl att = (GeometryAttributeImpl) o;

		//JD: since Geometry does not implement equals(Object) "properly",( ie
		// if you dont call equals(Geomtery) two geometries which are equal 
		// will not be equal) we dont call super.equals()
		
		if (!Utilities.equals(descriptor, att.descriptor))
			return false;

		if (!Utilities.equals(id, att.id))
			return false;

		if ( value != null && att.value != null ) {
		    //another lovley jts thing... comparing geometry collections that 
		    // arent multi point/line/poly throws an exception, so we nee dto 
		    // that comparison
		    if ( att.value instanceof GeometryCollection && 
	            !(att.value instanceof MultiPoint) && 
	            !(att.value instanceof MultiLineString) &&
	            !(att.value instanceof MultiPolygon) ) {
		        
		        if ( value instanceof GeometryCollection ) {
		            //compare the two collections 
		            GeometryCollection c1 = (GeometryCollection) value;
		            GeometryCollection c2 = (GeometryCollection) att.value;
		            
		            if ( c1.getNumGeometries() !=  c2.getNumGeometries() ) {
		                return false;
		            }
		            
		            for ( int i = 0; i < c1.getNumGeometries(); i++ ) {
		                Geometry g1 = c1.getGeometryN(i);
		                Geometry g2 = c2.getGeometryN(i);
		                
		                if ( !g1.equals(g2) ) {
		                    return false;
		                }
		            }
		            
		            return true;
		        }
		        else {
		            return false;
		        }
		    }
		   if ( !((Geometry)value).equals((Geometry)att.value)) {
				return false;
			}
		}
		else {
		    return Utilities.deepEquals(value, this.value);    
		}
		
		return true;
	}	
	
	public int hashCode() {
		int hash = descriptor.hashCode(); 
		
		if ( id != null ) {
		    hash += 37 * id.hashCode();
		}
		
		return hash;
	}
	
	   public String toString() {
	        StringBuffer sb = new StringBuffer(getClass().getSimpleName()).append(":");
	        sb.append(getDescriptor().getName().getLocalPart());
	        CoordinateReferenceSystem crs = getDescriptor().getType().getCoordinateReferenceSystem();
	        if(!getDescriptor().getName().getLocalPart().equals(getDescriptor().getType().getName().getLocalPart()) ||
	                id != null || crs != null){
	            sb.append("<");
	            sb.append(getDescriptor().getType().getName().getLocalPart());
	            if( id != null ){
	                sb.append( " id=");
	                sb.append( id );
	            }
	            if( crs != null ){
	                sb.append( " crs=");
	                sb.append( crs );
	            }
    	        if( id != null ){
    	            sb.append( " id=");
    	            sb.append( id );
    	        }
    	        sb.append(">");
	        }
	        sb.append("=");
	        sb.append(value);
	        return sb.toString();
	    }
}

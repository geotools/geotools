/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.filter.identity.GmlObjectIdImpl;
import org.geotools.util.Converters;
import org.geotools.util.Utilities;
import org.opengis.feature.Attribute;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.filter.identity.Identifier;

/**
 * Simple, mutable class to store attributes - that does not validate!
 * 
 * @author Jody Garnett
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/main/java/org/geotools/feature/LenientAttribute.java $
 * @version $Id: LenientAttribute.java 34640 2009-12-10 00:29:32Z simonegiannecchini $
 */
public class LenientAttribute extends PropertyImpl implements Attribute {

	/**
	 * id of the attribute.
	 */
	protected final Identifier id;

	public LenientAttribute(Object content, AttributeDescriptor descriptor,
			String id) {
	    super( content, descriptor );
	    if( id != null ){
	    	this.id = new GmlObjectIdImpl( id );
	    }
	    else {
	    	this.id = null;
	    }
	}

	public LenientAttribute(Object content, AttributeType type, String id) {
	    this( content, new AttributeDescriptorImpl( type, type.getName(), 1, 1, true, null), id );
	}

	public Identifier getIdentifier(){
		return id;
	}
	
	public AttributeDescriptor getDescriptor() {
	    return (AttributeDescriptor) super.getDescriptor();
	}
	
	public AttributeType getType() {
	    return (AttributeType) super.getType();
	}
	
	/**
	 * Override of setValue to convert the newValue to specified type if need
	 * be.
	 */
	public void setValue(Object newValue) throws IllegalArgumentException,
			IllegalStateException {

		// newValue = parse(newValue);

		// Types.validate(getType(), this, newValue);
		super.setValue( newValue );
	}

	/**
	 * Override of hashCode.
	 * 
	 * @return hashCode for this object.
	 */
	public int hashCode() {
	    return super.hashCode() + ( 37 * (id == null ? 0 : id.hashCode()) );
	}

	/**
	 * Override of equals.
	 * 
	 * @param other
	 *            the object to be tested for equality.
	 * 
	 * @return whether other is equal to this attribute Type.
	 */
	public boolean equals(Object obj) {
	    if ( this == obj ) {
	        return true;
	    }
	    
		if (!(obj instanceof AttributeImpl)) {
			return false;
		}

		if (!super.equals(obj)) {
		    return false;
		}
		
		AttributeImpl att = (AttributeImpl) obj;
		
		return Utilities.equals( id, att.id );
	}

	public boolean isValid() {
	    return true;
	}
	public String toString() {
		return super.toString() + ":" + id; 
	}
	
	/**
	 * Allows this Attribute to convert an argument to its prefered storage
	 * type. If no parsing is possible, returns the original value. If a parse
	 * is attempted, yet fails (i.e. a poor decimal format) throw the Exception.
	 * This is mostly for use internally in Features, but implementors should
	 * simply follow the rules to be safe.
	 * 
	 * @param value
	 *            the object to attempt parsing of.
	 * 
	 * @return <code>value</code> converted to the preferred storage of this
	 *         <code>AttributeType</code>. If no parsing was possible then
	 *         the same object is returned.
	 * 
	 * @throws IllegalArgumentException
	 *             if parsing is attempted and is unsuccessful.
	 */
	protected Object parse(Object value) throws IllegalArgumentException {
    	if ( value != null ) {
    		Class target = getType().getBinding(); 
    		if ( !target.isAssignableFrom( value.getClass() ) ) {
    			// attempt to convert
    			Object converted = Converters.convert(value,target);
    			if ( converted != null ) {
    				value = converted;
    			}
    		}
    	}
    	
    	return value;
    }
	
	public void validate() throws IllegalAttributeException {
	    // we're lenient, no validation
	}
}

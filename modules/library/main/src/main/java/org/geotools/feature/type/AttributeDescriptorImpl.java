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
package org.geotools.feature.type;

import java.util.Map;

import org.geotools.resources.Classes;
import org.geotools.util.Utilities;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;

public class AttributeDescriptorImpl extends PropertyDescriptorImpl 
	implements AttributeDescriptor {
	
	protected final Object defaultValue;
	
	public AttributeDescriptorImpl(
		AttributeType type, Name name, int min, int max, boolean isNillable, Object defaultValue
	) {
	    super(type,name,min,max,isNillable);
		
		this.defaultValue = defaultValue;
	}
	
	public AttributeType getType() {
		return (AttributeType) super.getType();
	}
    
	public Object getDefaultValue() {
		return defaultValue;
	}
	
    public int hashCode(){
		return super.hashCode() ^ 
		    (defaultValue != null ? defaultValue.hashCode() : 0 ); 
	}
	
	public boolean equals(Object o){
		if(!(o instanceof AttributeDescriptorImpl))
			return false;
		
		AttributeDescriptorImpl d = (AttributeDescriptorImpl)o;
	
		return super.equals(o) && Utilities.deepEquals( defaultValue, d.defaultValue );
	}	
	
	public String toString() {
	    StringBuffer sb = new StringBuffer(Classes.getShortClassName(this));
        sb.append(" ");
        sb.append( getName() );
        if( type != null ){
            sb.append( " <" );
            sb.append( type.getName().getLocalPart()  );
            sb.append(":");
            sb.append( Classes.getShortName( type.getBinding() ));
            sb.append( ">" );
        }
        if( isNillable  ){
            sb.append( " nillable" );            
        }
        if( minOccurs == 1 && maxOccurs == 1 ){
            // ignore the 1:1
        }
        else {
            sb.append( " " );
            sb.append( minOccurs );
            sb.append(  ":" );
            sb.append( maxOccurs );            
        }
        if( defaultValue != null ){
            sb.append( "\ndefault= " );
            sb.append( defaultValue );
        }
        if( userData != null && !userData.isEmpty() ){
            sb.append("\nuserData=(");
            for( Map.Entry entry : userData.entrySet() ){
                sb.append("\n\t");
                sb.append( entry.getKey() );
                sb.append( " ==> " );
                sb.append( entry.getValue() );
            }
            sb.append(")");
        }
        return sb.toString();
	}

	public String getLocalName() {
		return getName().getLocalPart();
	}
	
}

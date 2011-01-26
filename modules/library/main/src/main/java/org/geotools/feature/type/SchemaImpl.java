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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.Namespace;
import org.opengis.feature.type.Schema;

/**
 * Implementation of Schema.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class SchemaImpl implements Schema {
	HashMap<Name,AttributeType> contents;		
	String uri;
	
	/** Schema constructed w/ respect to provided URI */
	public SchemaImpl( String uri) {
		super();		
		this.uri = uri;
		this.contents = new HashMap();
	}

	public Set<Name> keySet() {
		return contents.keySet();
	}
	
	public int size() {
		return contents.size();
	}

	public boolean isEmpty() {
		return contents.isEmpty();
	}

	public boolean containsKey(Object key) {
		return contents.containsKey( key );
	}

	public boolean containsValue(Object value) {
		return contents.containsValue( value );
	}

	public AttributeType get(Object key) {
		return contents.get( key );
	}

	public AttributeType put(Name name, AttributeType type) {
	 	if( !(name instanceof Name) ){
			throw new IllegalArgumentException("Please use a Name");
		}
		Name n = (Name) name;
		if( !(n.toString().startsWith(uri.toString() ))){
			throw new IllegalArgumentException("Provided name was not in schema:"+uri );
		}
		if( !(type instanceof AttributeType) ){
			throw new IllegalArgumentException("Please use an AttributeType");
		}
		AttributeType t = (AttributeType) type;
		
		return contents.put( n, t );
	}

	public AttributeType remove(Object key) {
	    return contents.remove( key );
	}

	public void putAll(Map<? extends Name, ? extends AttributeType> t) {
		contents.putAll( t );
	}

	public void clear() {
		contents.clear();
	}

	public Collection<AttributeType> values() {
	    return contents.values();
	}
	
	public Set<Entry<Name, AttributeType>> entrySet() {
	 	return contents.entrySet();
	}
	
	public int hashCode() {
		return contents.hashCode();
	}
	public boolean equals(Object obj) {
		return contents.equals(obj);
	}
	public String toString() {
		return contents.toString();
	}
	
	public String getURI() {
	    return uri;
	}
	
	public void add(AttributeType type) {
	    put(type.getName(),type);
    }

	public Schema profile(Set<Name> profile) {
		return new ProfileImpl(this, profile);
	}
}

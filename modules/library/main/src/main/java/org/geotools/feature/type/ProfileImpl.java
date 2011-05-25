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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.Namespace;
import org.opengis.feature.type.Schema;

/**
 * A "sub" Schema used to select types for a specific use.
 * <p>
 * This class uses a custom key set to subset a parent Schema, and
 * is used as the return type of {@link SchemaImpl.profile}.
 * <p>
 * This Schema is <b>not</b> mutable, serving only as a view, you
 * may however define a more specific subset if needed.
 * <p>
 * Schema is often used to place limitation on expressed content
 * (as in the case of the GML Level 0 Profile), or used to define
 * a non conflicting set of "bindings" for the TypeBuilder(s).
 * </p>
 * @author Jody Garnett, Refractions Research Inc.
 *
 *
 * @source $URL$
 */
public class ProfileImpl implements Schema {

	/**
	 * Parent Schema
	 */
	private Schema parent;
	
	/**
	 * Keyset used by this profile (immutable).
	 */
	private Set<Name> profile;
	
	/**
	 * Profile contents (created in a lazy fashion).
	 */
	private Map contents = null;
	
	/**
	 * Subset parent schema with profile keys.
	 * 
	 * @param parent
	 * @param profile
	 */
	public ProfileImpl( Schema parent, Set<Name> profile ){
		this.parent = parent;
		
		this.profile = Collections.unmodifiableSet(profile);
	}
	
	public Set<Name> keySet() {
		return profile;
	}

	public String getURI() {
		return parent.getURI();
	}

	public Schema profile(Set<Name> profile) {
	    if( !this.profile.containsAll( profile ) ){
	        Set<Name> set = new TreeSet<Name>( profile );
	        set.removeAll( this.profile );
	        throw new IllegalArgumentException("Unable to profile the following names: "+set );
	    }
	    return parent.profile( profile );
	}

	public int size() {
		return profile.size();
	}

	public boolean isEmpty() {
		return profile.isEmpty();
	}

	public boolean containsKey(Object key) {
		return profile.contains( key );
	}

	public boolean containsValue(Object value) {
		return values().contains( value );
	}

	public AttributeType get(Object key) {
		if( profile.contains( key )){
			return parent.get( key );
		}
		return null;
	}

	public AttributeType put(Name key, AttributeType value) {
	    throw new UnsupportedOperationException("Profile not mutable");
	}
	
	public AttributeType remove(Object key) {
	 	throw new UnsupportedOperationException("Profile not mutable");
	}

	public void putAll(Map<? extends Name, ? extends AttributeType> t) {
    	throw new UnsupportedOperationException("Profile not mutable");
	}

	public void clear() {
	    throw new UnsupportedOperationException("Profile not mutable");
	}

	public void add(AttributeType type) {
	    throw new UnsupportedOperationException("Profile not mutable");
	}
	
	//public Collection values() {
	public Collection<AttributeType> values() {
	 	return contents().values();
	}

	//public Set<Name> entrySet() {
	public Set<Entry<Name, AttributeType>> entrySet() {
	 	return contents().entrySet();
	}
	
	private synchronized Map<Name,AttributeType> contents(){
		if( contents == null){
			contents = new LinkedHashMap();
			for( Iterator i=profile.iterator();i.hasNext();){
				Object key = i.next();
				contents.put( key, parent.get(key));
			}
		}
		return contents;
	}
}

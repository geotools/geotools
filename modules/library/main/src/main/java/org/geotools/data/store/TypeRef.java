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
package org.geotools.data.store;

/**
 * A real class for naming feature collections, see generic name for the geoapi take.
 * <p>
 * Compare contrast with XPath utility class in ext/view. These may merge if TypeRef
 * is reduced to an XPath expression in the future.
 * </p>
 * @author Jody Garnett, Refractions Research
 * @source $URL$
 */
public class TypeRef {
	private final String dataStoreId;
	private final String typeName;	
	public TypeRef( String dataStoreId, String typeName ){
		this.dataStoreId = dataStoreId;
		this.typeName = typeName;
	}
	public TypeRef( String typeRef ){		
		int split = typeRef.indexOf('/');
		if( split == -1 ){
			dataStoreId = null;
			typeName = typeRef;
		}
		else {
			dataStoreId = typeRef.substring(0,split);
			typeName = typeRef.substring(split+1);
		}		
	}
	static String parseDataStoreId( String typeRef ){
		int split = typeRef.indexOf('/');
		return split == -1 ? null : typeRef.substring(0,split);
	}
	static String parseTypeName( String typeRef ){
		int split = typeRef.indexOf('/');
		return split == -1 ? typeRef : typeRef.substring(split+1);
	}
	static TypeRef parseTypeRef( String typeRef ){
		return new TypeRef( typeRef );		
	}
	/** External representation of "dataStoreId/typeName" */ 
	public String toString(){
		return dataStoreId + "/" + typeName;
	}
	/**
	 * @return Returns the dataStoreId.
	 */
	public String getDataStoreId() {
		return dataStoreId;
	}
	/**
	 * @return Returns the typeName.
	 */
	public String getTypeName() {
		return typeName;
	}
}

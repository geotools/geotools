/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.data.sql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.geotools.data.jdbc.FeatureTypeHandler;
import org.geotools.data.jdbc.FeatureTypeInfo;
import org.geotools.data.jdbc.JDBC1DataStore;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.jdbc.fidmapper.FIDMapperFactory;
import org.opengis.feature.simple.SimpleFeatureType;

public class BypassSqlFeatureTypeHandler extends FeatureTypeHandler {

	private Map bypassSqlViews = new HashMap();

	private Map bypassSqlTypes = new HashMap();

	public BypassSqlFeatureTypeHandler(JDBC1DataStore store,
			FIDMapperFactory fmFactory, long cacheTimeOut) {
		super(store, fmFactory, cacheTimeOut);
		this.bypassSqlViews = new HashMap();
		this.bypassSqlTypes = new HashMap();
	}
	
	public void registerView(SimpleFeatureType viewType, String sqlQuery, FIDMapper fidMapper){
		String typeName = viewType.getTypeName();
		sqlQuery = sqlQuery.replaceAll("\n|\r", "");
		sqlQuery = sqlQuery.replaceAll("\t", " ");
		this.bypassSqlViews.put(typeName, sqlQuery);
		
		FeatureTypeInfo info = new FeatureTypeInfo(typeName, viewType, fidMapper);
		
		this.bypassSqlTypes.put(typeName, info);
	}

	
	public boolean isView(String typeName){
		return this.bypassSqlTypes.containsKey(typeName);
	}
	
	public String getQuery(String typeName){
		if(!isView(typeName)){
			throw new NoSuchElementException("There are no SQL query definition for FeatureType " + typeName);
		}
		return (String)this.bypassSqlViews.get(typeName);
	}

	public String[] getTypeNames() throws IOException {
		String[] typeNames = super.getTypeNames();
		List namesList = new ArrayList(Arrays.asList(typeNames));
		for(Iterator it = this.bypassSqlTypes.keySet().iterator(); it.hasNext();){
			namesList.add(it.next());
		}
		typeNames = (String[]) namesList.toArray(new String[namesList.size()]);
		return typeNames;
	}
	
	 public FeatureTypeInfo getFeatureTypeInfo(String featureTypeName)
     throws IOException {
		 if(this.bypassSqlTypes.containsKey(featureTypeName)){
			 FeatureTypeInfo info = (FeatureTypeInfo) this.bypassSqlTypes.get(featureTypeName);
			 return info;
		 }
		 return super.getFeatureTypeInfo(featureTypeName);
	 }
	 
	 public FIDMapper getFIDMapper(String typeName) throws IOException {
		 if(this.bypassSqlTypes.containsKey(typeName)){
			 FeatureTypeInfo info = getFeatureTypeInfo(typeName);
			 return info.getFIDMapper();
		 }
		 return super.getFIDMapper(typeName);
     }	 
		
}

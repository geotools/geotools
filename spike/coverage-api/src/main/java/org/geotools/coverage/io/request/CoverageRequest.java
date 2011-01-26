/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.request;

import java.util.Map;

import org.geotools.data.Parameter;
import org.geotools.factory.Hints;
import org.opengis.feature.type.Name;

/**
 * @author   Simone Giannecchini, GeoSolutions
 * @author   Jody Garnett
 */
public class CoverageRequest  {

	public enum RequestType {
	
	    READ,
	    
	    UPDATE;
	}

	/**
	 * @uml.property  name="name"
	 */
	private Name name;
	/**
	 * @uml.property  name="handle"
	 */
	private String handle;
	/**
	 * @uml.property  name="hints"
	 */
	private Hints hints;
	/**
	 * @uml.property  name="additionalParameters"
	 */
	private Map<String,Parameter<?>> additionalParameters;

	/**
	 * @see org.geotools.coverage.io.request.CoverageRequest#getHandle()
	 */
	public String getHandle(){
		return handle;
	}
	
	/**
	 * @see org.geotools.coverage.io.request.CoverageRequest#setHandle(java.lang.String)
	 */
	public void setHandle(String handle){
		this.handle = handle;
	}

	/** 
	 * @see org.geotools.coverage.io.request.CoverageRequest#getHints()
	 */
	public Hints getHints(){
		return hints;
	}

	/**
	 * @see org.geotools.coverage.io.request.CoverageRequest#setHints(Hints)
	 */
	public void setHints(Hints hints){
		this.hints = hints;
	}

	/**
	 * @see org.geotools.coverage.io.request.CoverageRequest#setAdditionalParameters(Map)
	 */
	public void setAdditionalParameters( Map<String,Parameter<?>> additionalParameters){
		this.additionalParameters = additionalParameters;
	}

	/**
	 * @see org.geotools.coverage.io.request.CoverageRequest#getAdditionalParameters()
	 */
	public Map<String, Parameter<?>> getAdditionalParameters() {
		return additionalParameters;
	}

	/**
	 * @see org.geotools.coverage.io.request.CoverageRequest#getName()
	 */
	public Name getName() {
		return name;
	}

	/**
	 * @see org.geotools.coverage.io.request.CoverageRequest#setName(org.opengis.feature.type.Name)
	 */
	public void setName(Name name) {
		this.name = name;
	}

}

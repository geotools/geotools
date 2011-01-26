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
package org.geotools.coverage.io.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.geotools.util.NullProgressListener;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.util.ProgressListener;
/**
 * 
 * @author Simone Giannecchini, GeoSolutions
 *
 */
@SuppressWarnings("unchecked")
public class CoverageResponse {
    

    /**
     * @author Simone Giannecchini, GeoSolutions
     */
    public enum Status {
        FAILURE, WARNING, SUCCESS, UNAVAILABLE
    }
	
	private List exceptions=Collections.synchronizedList(new ArrayList<Exception>());
	
	private String handle=null;
	
	private CoverageRequest originatingRequest=null;
	
	private List results= Collections.synchronizedList(new ArrayList<GridCoverage>());
	
	private Status status= Status.UNAVAILABLE;
	

	public Collection<? extends Exception> getExceptions() {
		synchronized (this.exceptions) {
			return new ArrayList<Exception>(this.exceptions);
		}
	}
	
	public void addExceptions(final Collection<? extends Exception> exceptions ){
		synchronized (this.exceptions) {
			this.exceptions.add(exceptions);
		}	
	}
	
	public void addException(Exception exception ){
		synchronized (this.exceptions) {
			this.exceptions.add(exception);
		}	
	}
	    /**
	     * The handle attribute is included to allow a client to associate a
	     * mnemonic name to the Query request. The purpose of the handle attribute
	     * is to provide an error handling mechanism for locating a statement that
	     * might fail.
	     * 
	     * @return the mnemonic name of the query request.
	     */
	public String getHandle() {
		return this.handle;
	}
	
	public void setHandle(final String handle){
		this.handle=handle;
	}

	public CoverageRequest getRequest() {
		return this.originatingRequest;
	}
	
	public void setRequest(final CoverageRequest coverageRequest){
		this.originatingRequest=coverageRequest;
	}

	    /**
	     * Returns the Coverages available with this coverage response.
	     * 
	     * @param listener
	     * @return a collection of coverages.
	     */
	public Collection<? extends Coverage> getResults(ProgressListener listener) {
		if( listener == null ) listener = new NullProgressListener();
		listener.started();
		try {
			synchronized (this.results) {
				return new ArrayList<GridCoverage>(this.results);
			}
		}
		finally {
			listener.complete();
		}
	}
	
	public void addResults(final Collection<? extends GridCoverage> results) {
		synchronized (this.results) {
			this.results.add(results);
		}
	}	
	
	public void addResult(GridCoverage grid ) {
		synchronized (this.results) {
			this.results.add(grid);
		}
	}		


	    /**
	     * Get the status of this coverage response. It should always be checked
	     * before assuming any data is available.
	     * 
	     * @return the {@linkplain Status status} of this coverage response.
	     */
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(final Status status){
		this.status= status;
	}

}

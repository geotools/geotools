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
package org.geotools.coverage.io.driver;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.io.CoverageAccess;
import org.geotools.data.Parameter;
import org.geotools.factory.Hints;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.logging.Logging;
import org.opengis.util.ProgressListener;

/**
 * Base class extending {@link BaseDriver} leveraging on URLs.
 */
public abstract class BaseFileDriver extends BaseDriver implements FileDriver {
	
	private final static Logger LOGGER= Logging.getLogger(BaseFileDriver.class);
	
	/**
	 * Parameter "url" used to indicate to a local file or remote resource being
	 * accessed as a coverage.
	 */
	public final static Parameter<URL> URL = new Parameter<URL>("url",
			java.net.URL.class, new SimpleInternationalString("URL"),
			new SimpleInternationalString(
					"Url to a local file or remote location"));

	/**
	 * Utility method to convert a URL to a file; or return null
	 * if not possible.
	 * @param url
	 * @return File or null if not available
	 */
	public static File toFile( URL url ){
		if( url == null ) return null;
		if (url.getProtocol().equalsIgnoreCase("file")) {
			try {
				return new File(URLDecoder.decode(url.getFile(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}
		return null;				
	}
	
	/**
	 * Utility method to help convert a URL to a file if possible.
	 * 
	 * @param url
	 * @return File
	 */
	public static File urlToFile(URL url) {
		URI uri;
		try {
			// this is the step that can fail, and so
			// it should be this step that should be fixed
			uri = url.toURI();
		} catch (URISyntaxException e) {
			// OK if we are here, then obviously the URL did
			// not comply with RFC 2396. This can only
			// happen if we have illegal unescaped characters.
			// If we have one unescaped character, then
			// the only automated fix we can apply, is to assume
			// all characters are unescaped.
			// If we want to construct a URI from unescaped
			// characters, then we have to use the component
			// constructors:
			try {
				uri = new URI(url.getProtocol(), url.getUserInfo(), url
						.getHost(), url.getPort(), url.getPath(), url
						.getQuery(), url.getRef());
			} catch (URISyntaxException e1) {
				// The URL is broken beyond automatic repair
				throw new IllegalArgumentException("broken URL: " + url);
			}
		}
		return new File(uri);
	}

	private List<String> fileExtensions;

	protected BaseFileDriver(final String name, final String description,
			final String title, final Hints implementationHints,
			final List<String> fileExtensions) {
		super(name, description, title, implementationHints);
		this.fileExtensions = new ArrayList<String>(fileExtensions);
	}

	public List<String> getFileExtensions() {
		return new ArrayList<String>(fileExtensions);
	}

	@Override
	protected boolean canConnect(Map<String, Serializable> params) {
		
		//check for URL
		if(!params.containsKey(URL.key))
		{
			if(LOGGER.isLoggable(Level.INFO))
				LOGGER.log(Level.INFO,"Unable to find parameter URL in parameters "+params.toString());
			return false;
		}
		
		//get the URL
		final URL url = (URL) params.get(URL.key);
		return canConnect(url,params);
	}
	protected abstract boolean canConnect(URL url,Map<String, Serializable> params);

	@Override
	protected boolean canCreate(Map<String, Serializable> params) {
		//check for URL
		if(!params.containsKey(URL.key))
		{
			if(LOGGER.isLoggable(Level.INFO))
				LOGGER.log(Level.INFO,"Unable to find parameter URL in parameters "+params.toString());
			return false;
		}
		//get the URL
		final URL url = (URL) params.get(URL.key);
		return canCreate(url, params);
	}
	protected abstract boolean canCreate(URL url,Map<String, Serializable> params);
	

	@Override
	protected boolean canDelete(Map<String, Serializable> params) {
		//check for URL
		if(!params.containsKey(URL.key))
		{
			if(LOGGER.isLoggable(Level.INFO))
				LOGGER.log(Level.INFO,"Unable to find parameter URL in parameters "+params.toString());
			return false;
		}
		
		//get the URL
		final URL url = (URL) params.get(URL.key);
		return canDelete(url, params);
	}
	protected abstract boolean canDelete(URL url,Map<String, Serializable> params);

	@Override
	protected CoverageAccess connect(Map<String, Serializable> params, Hints hints,
			ProgressListener listener) throws IOException {
		//check for URL
        if (params == null)
            throw new IllegalArgumentException("Invalid or no input provided.");
		if(!params.containsKey(URL.key))
			throw new IllegalArgumentException("Unable to find parameter URL in parameters "+params.toString());
		
		//get the URL
		final URL url = (URL) params.get(URL.key);
		return connect(url,params, hints, listener);
	}
	protected abstract CoverageAccess  connect(URL url,Map<String, Serializable> params, Hints hints,
			ProgressListener listener) throws IOException;

	@Override
	protected CoverageAccess create(Map<String, Serializable> params, Hints hints,
			ProgressListener listener) throws IOException {
		//check for URL
        if (params == null)
            throw new IllegalArgumentException("Invalid or no input provided.");
		if(!params.containsKey(URL.key))
			throw new IllegalArgumentException("Unable to find parameter URL in parameters "+params.toString());
		
		//get the URL
		final URL url = (URL) params.get(URL.key);
		return create(url,params, hints, listener);
	}
	protected abstract CoverageAccess  create(URL url,Map<String, Serializable> params, Hints hints,
			ProgressListener listener) throws IOException;

	@Override
	protected CoverageAccess delete(Map<String, Serializable> params, Hints hints,
			ProgressListener listener)throws IOException {
		//check for URL
        if (params == null)
            throw new IllegalArgumentException("Invalid or no input provided.");
		if(!params.containsKey(URL.key))
			throw new IllegalArgumentException("Unable to find parameter URL in parameters "+params.toString());
		
		//get the URL
		final URL url = (URL) params.get(URL.key);
		return delete(url,params, hints, listener);
	}
	protected abstract CoverageAccess  delete(URL url,Map<String, Serializable> params, Hints hints,
			ProgressListener listener) throws IOException;

	
	public boolean canProcess(
			DriverOperation operation, 
			URL url,
			Map<String, Serializable> params) {
		
    	if(!getDriverCapabilities().contains(operation))
    		throw new UnsupportedOperationException("Operation "+operation+" is not supported by this driver");
    	
		//check input URL
		if(url==null){
			//check for URL
			if(!params.containsKey(URL.key))
				throw new IllegalArgumentException("Unable to find parameter URL in parameters "+params.toString());
			
			//get the URL
			url=(URL) params.get(URL.key);
		}
		
		//check the operation
		switch (operation) {
		case CONNECT:
			return canConnect(url, params);
		case DELETE:
			return canDelete(url, params);
		case CREATE:
			return canCreate(url, params);
		default:
			throw new IllegalArgumentException("Unrecognized operation "+operation);
		}
	}

	public CoverageAccess process(DriverOperation operation, URL url,
			Map<String, Serializable> params, Hints hints,
			ProgressListener listener) throws IOException {
		
    	if(!getDriverCapabilities().contains(operation))
    		throw new UnsupportedOperationException("Operation "+operation+" is not supported by this driver");
    	
		//check input URL
		if(url==null){
			//check for URL
			if(!params.containsKey(URL.key))
				throw new IllegalArgumentException("Unable to find parameter URL in parameters "+params.toString());
			
			//get the URL
			url=(URL) params.get(URL.key);
		}
		
		//check the operation
		switch (operation) {
		case CONNECT:
			return connect(url, params, hints, listener);
		case DELETE:
			return delete(url, params, hints, listener);
		case CREATE:
			return create(url, params, hints, listener);
		default:
			throw new IllegalArgumentException("Unrecognized operation "+operation);
		}
	}

}




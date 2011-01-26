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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.coverage.io.service.RasterService;
import org.geotools.coverage.io.service.RasterServiceAction;
import org.geotools.data.Parameter;
import org.geotools.factory.Hints;
import org.geotools.util.SimpleInternationalString;

/**
 * Base class extending {@link BaseRasterService} leveraging on URLs.
 */
public abstract class BaseFileRasterService extends BaseRasterService implements RasterService {
	/**
	 * Parameter "url" used to indicate to a local file or remote resource being
	 * accessed as a coverage.
	 */
	public final static Parameter<URL> URL = new Parameter<URL>("url",
			java.net.URL.class, new SimpleInternationalString("URL"),
			new SimpleInternationalString(
					"Url to a local file or remote location"));

	/**
	 * Parameter "file" used to indicate to indicate a local file.
	 */
	public final static Parameter<File> FILE = new Parameter<File>("file",
			File.class, new SimpleInternationalString("File"),
			new SimpleInternationalString( "Local file"));

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

	protected BaseFileRasterService(
			final String name,
			final String description,
			final String title, 
			final Hints implementationHints,
			final List<String> fileExtensions,
            final EnumSet<RasterServiceAction> capabilities) {
		super(name, description, title, implementationHints,capabilities);
		this.fileExtensions = new ArrayList<String>(fileExtensions);
	}

	public List<String> getFileExtensions() {
		return new ArrayList<String>(fileExtensions);
	}

	/**
	 * Subclass can override to define required parameters.
	 * <p>
	 * Default implementation expects a single URL indicating the
	 * location.
	 * 
	 * @return
	 */
	protected Map<String, Parameter<?>> defineDefaultReaderParameterInfo(){
		HashMap<String, Parameter<?>> info = new HashMap<String, Parameter<?>>();
		info.put(URL.key, URL);
		return info;
	}
	
	/**
	 * Subclass can override to define required parameters.
	 * <p>
	 * Default implementation expects a single URL indicating the
	 * location.
	 * 
	 * @return
	 */
	protected Map<String, Parameter<?>> defineDefaultWriterParameterInfo(){
		HashMap<String, Parameter<?>> info = new HashMap<String, Parameter<?>>();
		info.put(URL.key, URL);
		return info;
	}
}

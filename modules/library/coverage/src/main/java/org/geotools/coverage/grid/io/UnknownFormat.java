/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import java.util.HashMap;

import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.ParameterValueGroup;

/**
 * This class can be used when a proper {@link Format} cannot be found for some
 * input sources.
 * 
 * <p>
 * It implements the abstract method of {@link AbstractGridFormat} but it always
 * returns null to indicate that the format it represents is unknown.
 * 
 * @author Jesse Eichar
 * @author Simone Giannecchini (simboss)
 * @source $URL:
 *         http://svn.geotools.org/geotools/branches/2.3.x/module/main/src/org/geotools/data/coverage/grid/UnknownFormat.java $
 * @version $Revision: 1.9 $
 */
public class UnknownFormat extends AbstractGridFormat implements Format {
	/**
	 * Creates a new UnknownFormat object.
	 */
	public UnknownFormat() {
		mInfo = new HashMap<String,String>();
		mInfo.put("name", "Unkown Format");
		mInfo.put("description", "This format describes all unknown formats");
		mInfo.put("vendor", null);
		mInfo.put("docURL", null);
		mInfo.put("version", null);
		readParameters = null;
		writeParameters = null;

	}

	/**
	 * @see AbstractGridFormat#getReader(Object)
	 */
	@Override
	public AbstractGridCoverage2DReader getReader(java.lang.Object source) {
		throw new UnsupportedOperationException(
			"Trying to get a reader from an unknown format.");
	}

	/**
	 * @see AbstractGridFormat#getWriter(Object)
	 */
	@Override
	public GridCoverageWriter getWriter(Object destination) {
		throw new UnsupportedOperationException(
				"Trying to get a writer from an unknown format.");
	}




	/**
	 * @see AbstractGridFormat#getReader(Object, Hints)
	 */
	@Override
	public AbstractGridCoverage2DReader getReader(Object source, Hints hints) {
		throw new UnsupportedOperationException(
				"Trying to get a reader from an unknown format.");
	}

	/**
	 * @see AbstractGridFormat#getDefaultImageIOWriteParameters()
	 */
	@Override
	public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
		throw new UnsupportedOperationException(
				"Trying to get a writing parameters from an unknown format.");
	}
	/**
	 * @see AbstractGridFormat#accepts(Object)
	 */
	@Override
	public  boolean accepts(Object input,Hints hints) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.geotools.coverage.grid.io.AbstractGridFormat#getReadParameters()
	 */
	@Override
	public ParameterValueGroup getReadParameters() {
		throw new UnsupportedOperationException(
		"Trying to get a reading parameters from an unknown format.");
	}

	/*
	 * (non-Javadoc)
	 * @see org.geotools.coverage.grid.io.AbstractGridFormat#getWriteParameters()
	 */
	@Override
	public ParameterValueGroup getWriteParameters() {
		throw new UnsupportedOperationException(
		"Trying to get a writing parameters from an unknown format.");
	}

	@Override
	public GridCoverageWriter getWriter(Object destination, Hints hints) {
		throw new UnsupportedOperationException(
		"Trying to get a writer from an unknown format.");
	}

}

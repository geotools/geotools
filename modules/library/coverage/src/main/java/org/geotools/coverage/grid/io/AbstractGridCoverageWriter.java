/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.stream.ImageOutputStream;

import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.GridCoverageWriter;

/**
 * An {@link AbstractGridCoverageWriter} is the base class for all
 * {@link GridCoverageWriter} implementations in GeoTools toolkit.
 *
 *
 * <p>
 * We expect it to become the place where to move functionalities common to all
 * {@link GridCoverageWriter}.
 *
 * @author Simone Giannecchini
 * @since 2.3.x
 *
 *
 * @source $URL$
 */
public abstract class AbstractGridCoverageWriter implements GridCoverageWriter {

	/** The {@link Logger} for this {@link AbstractGridCoverageWriter}. */
	private final static Logger LOGGER = Logging.getLogger(AbstractGridCoverageWriter.class.toString());

	/** the destination object where we will do the writing */
	protected Object destination;

	/** Hints to be used for the writing process. */
	protected Hints hints = GeoTools.getDefaultHints();

	/** The destination {@link ImageOutputStream}. */
	protected ImageOutputStream outStream = null;

	/**
	 * Default constructor for an {@link AbstractGridCoverageWriter}.
	 */

	public AbstractGridCoverageWriter() {

	}

	/**
	 * Releases resources held by this {@link AbstractGridCoverageWriter}.
	 */
	public void dispose() {
		if(LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("Disposing writer:"+destination);
		
		if (outStream != null) {
			try {
				outStream.flush();			

			} catch (IOException e) {

			}
			finally{
				try {
					outStream.close();
				} catch (Throwable e) {

				}
			}
			
			
		}

	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.opengis.coverage.grid.GridCoverageWriter#getDestination()
	 */
	public Object getDestination() {
		return destination;
	}

	/**
	 * Implementation of getMetadataNames. Currently unimplemented because it
	 * has not been specified where to retrieve the metadata
	 *
	 * @return null
	 *
	 * @see org.opengis.coverage.grid.GridCoverageWriter#getMetadataNames()
	 */
	public String[] getMetadataNames() {
		throw new UnsupportedOperationException("Unsupported method");
	}

	/**
	 * @see org.opengis.coverage.grid.GridCoverageWriter#setCurrentSubname(java.lang.String)
	 */
	public void setCurrentSubname(String name) throws IOException {
		throw new UnsupportedOperationException("Unsupported method");
	}

	/**
	 * @see org.opengis.coverage.grid.GridCoverageWriter#setMetadataValue(java.lang.String,
	 *      java.lang.String)
	 */
	public void setMetadataValue(String name, String value) throws IOException {
		throw new UnsupportedOperationException("Unsupported method");
	}


	/**
	 * Forcing the disposal of this {@link AbstractGridCoverageWriter} which may
	 * keep a reference to an open {@link ImageOutputStream}
	 */
	@Override
	protected void finalize() throws Throwable {
		dispose();
		super.finalize();
	}


}

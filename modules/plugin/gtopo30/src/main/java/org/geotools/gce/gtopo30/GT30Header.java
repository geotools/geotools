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
package org.geotools.gce.gtopo30;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;

/**
 * Class used to parse a GTOPO30 header (.HDR) file
 * 
 * @author Simone Giannecchini
 * @author aaime
 * @author mkraemer
 * @source $URL$
 */
final class GT30Header {
	/** Mnemonic constant for line labels in the header file */
	public static final String BYTEORDER = "BYTEORDER";

	/** Mnemonic constant for line labels in the header file */
	public static final String LAYOUT = "LAYOUT";

	/** Mnemonic constant for line labels in the header file */
	public static final String NROWS = "NROWS";

	/** Mnemonic constant for line labels in the header file */
	public static final String NCOLS = "NCOLS";

	/** Mnemonic constant for line labels in the header file */
	public static final String NBANDS = "NBANDS";

	/** Mnemonic constant for line labels in the header file */
	public static final String NBITS = "NBITS";

	/** Mnemonic constant for line labels in the header file */
	public static final String BANDROWBYTES = "BANDROWBYTES";

	/** Mnemonic constant for line labels in the header file */
	public static final String TOTALROWBYTES = "TOTALROWBYTES";

	/** Mnemonic constant for line labels in the header file */
	public static final String BANDGAPBYTES = "BANDGAPBYTES";

	/** Mnemonic constant for line labels in the header file */
	public static final String NODATA = "NODATA";

	/** Mnemonic constant for line labels in the header file */
	public static final String ULXMAP = "ULXMAP";

	/** Mnemonic constant for line labels in the header file */
	public static final String ULYMAP = "ULYMAP";

	/** Mnemonic constant for line labels in the header file */
	public static final String XDIM = "XDIM";

	/** Mnemonic constant for line labels in the header file */
	public static final String YDIM = "YDIM";

	/** The standard cell size of GTOPO30 files */
	private static final double STD_CELL_SIZE = 0.00833333333333;

	/**
	 * A map for fast and convenient retrieval of the properties contained in
	 * the header file
	 * 
	 */
	private Map<String, Object> propertyMap = new HashMap<String, Object>();

	/**
	 * Creates a new instance of GTOPO30Header
	 * 
	 * @param headerURL
	 *            URL of a GTOPO30 header (.HDR) file
	 * 
	 * @throws IOException
	 *             if some problem is encountered reading the file
	 * @throws DataSourceException
	 *             for problems related to the file content
	 */
	public GT30Header(final URL headerURL) throws IOException {
		final File header = DataUtilities.urlToFile(headerURL);

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(header));
			initMap();
			parseHeaderFile(reader);
			if (!fullPropertySet(this.propertyMap)) {
				throw new DataSourceException(
						"Needed properties missing in GTOPO30 header file");
			}
		} finally {
			if (reader != null)
				try {
					// freeing
					reader.close();
				} catch (Exception e1) {
				    //TODO log me
				}
		}
	}

	/**
	 * Returns a property value
	 * 
	 * @param property
	 *            use mnemonic constants
	 * 
	 * @return the property value or null if the passed property is not
	 *         recognized
	 */
	public Object getProperty(final String property) {
		return this.propertyMap.get(property);
	}

	/**
	 * Returns a string representing the byte order of the data file
	 * 
	 * @return a string representing the byte order of the data file
	 */
	public String getByteOrder() {
		return (String) this.propertyMap.get(BYTEORDER);
	}

	/**
	 * Layout of the binary file (see gtopo30 file format description)
	 * 
	 * @return a String describing the binary layour
	 */
	public String getLayout() {
		return (String) this.propertyMap.get(LAYOUT);
	}

	/**
	 * Returns the number of rows in the file
	 * 
	 * @return the number of rows in the file
	 */
	public int getNRows() {
		return (Integer) this.propertyMap.get(NROWS);
	}

	/**
	 * Returns the number of columns in the file
	 * 
	 * @return the number of columns in the file
	 */
	public int getNCols() {
		return (Integer) this.propertyMap.get(NCOLS);
	}

	/**
	 * Return the number of bands. Warning: official GTOPO30 files just have one
	 * band
	 * 
	 * @return the number of bands
	 */
	public int getNBands() {
		return ((Integer) this.propertyMap.get(NBANDS)).intValue();
	}

	/**
	 * Returns the number of bits used to encode a cell
	 * 
	 * @return the number of bits per cell
	 */
	public int getNBits() {
		return (Integer) this.propertyMap.get(NBITS);
	}

	/**
	 * Returns the number of bytes per row in a band
	 * 
	 * @return the number of bytes per row in a band
	 */
	public int getBandRowBytes() {
		return (Integer) this.propertyMap.get(BANDROWBYTES);
	}

	/**
	 * Returns the number of bytes per row
	 * 
	 * @return the number of bytes per row
	 */
	public int getRowBytes() {
		return (Integer) this.propertyMap.get(TOTALROWBYTES);
	}

	/**
	 * Returns the number of gap bytes used to separate bands, if any
	 * 
	 * @return the number of gap bytes used to separate bands
	 */
	public int getBandGapBytes() {
		return (Integer) this.propertyMap.get(BANDGAPBYTES);
	}

	/**
	 * Returns the value used to represent lack of data (usually -9999)
	 * 
	 * @return the value used to represent lack of data
	 */
	public int getNoData() {
		return (Integer) this.propertyMap.get(NODATA);
	}

	/**
	 * Returns the x coordinate (latitude) of the tile center
	 * 
	 * @return the x coordinate of the tile center
	 */
	public double getULXMap() {
		return (Double) this.propertyMap.get(ULXMAP);
	}

	/**
	 * Returns the y coordinate (longitude) of the tile center
	 * 
	 * @return the y coordinate of the tile center
	 */
	public double getULYMap() {
		return (Double) this.propertyMap.get(ULYMAP);
	}

	/**
	 * Returns the width of the tile in degrees
	 * 
	 * @return the width of the tile in degrees
	 */
	public double getXDim() {
		return (Double) this.propertyMap.get(XDIM);
	}

	/**
	 * Returns the height of the tile in degrees
	 * 
	 * @return the height of the tile in degrees
	 */
	public double getYDim() {
		return (Double) this.propertyMap.get(YDIM);
	}

	/**
	 * Initializes the map with the known properties, makes it easier to parse
	 * the file
	 * 
	 * @return the initialized map
	 */
	private void initMap() {
	        propertyMap.put(BYTEORDER, "M");
	        propertyMap.put(LAYOUT, "BIL");
	        propertyMap.put(NROWS, null);
	        propertyMap.put(NCOLS, null);
	        propertyMap.put(NBANDS, null);
	        propertyMap.put(NBITS, null);
	        propertyMap.put(BANDROWBYTES, null);
	        propertyMap.put(TOTALROWBYTES, null);
	        propertyMap.put(BANDGAPBYTES, new Integer(0));
	        propertyMap.put(NODATA, new Integer(0));
	        propertyMap.put(ULXMAP, null);
	        propertyMap.put(ULYMAP, null);
	        propertyMap.put(XDIM, new Double(STD_CELL_SIZE));
	        propertyMap.put(YDIM, new Double(STD_CELL_SIZE));

	}

	/**
	 * Parses the reader for the known properties
	 * 
	 * @param properties
	 *            the map to be filled in
	 * @param reader
	 *            the source data
	 * 
	 * @throws IOException
	 *             for reading errors
	 * @throws DataSourceException
	 *             for unrecoverable data format violations
	 */
	@SuppressWarnings("unchecked")
        private void parseHeaderFile(final BufferedReader reader) throws IOException {
		String currLine = reader.readLine();
		while (currLine != null) {
			// remove uneeded spaces
			currLine = currLine.trim();

			// get key and value
			int firstSpaceIndex = currLine.indexOf(' ');

			if (firstSpaceIndex == -1) {
				throw new IOException("Illegal line in GTOPO30 header file");
			}

			final String key = currLine.substring(0, firstSpaceIndex).toUpperCase();
			final String value = currLine.substring(firstSpaceIndex).trim();

			// be tolerant about unknown keys, all we need is a subset of the
			// knows keys, the others will be discarded
			if (propertyMap.containsKey(key)) {
				final Class propClass = getPropertyClass(key);

				try {
					if (propClass == String.class) {
					    propertyMap.put(key, value);
					} else if (propClass == Integer.class) {
					    propertyMap.put(key, Integer.valueOf(value));
					} else if (propClass == Double.class) {
					    propertyMap.put(key, Double.valueOf(value));
					}
				} catch (NumberFormatException nfe) {
					final IOException ex = new IOException();
					ex.initCause(nfe);
					throw ex;
				}
			}

			// read next line
			currLine = reader.readLine();
		}

		// closing the reader
		reader.close();
	}

	/**
	 * Checks wheter all of the properties in the map have been assigned
	 * 
	 * @param properties
	 *            the property map to be checked
	 * 
	 * @return true if the map is filled in with values, false if at least one
	 *         value is null
	 */
	private static boolean fullPropertySet(final Map<String,Object> properties) {
		boolean full = true;
		final Collection<Object> values = properties.values();
		for (final Iterator<Object>  it = values.iterator(); it.hasNext();) {
			if (it.next() == null) {
				full = false;

				break;
			}
		}

		return full;
	}

	/**
	 * Returns the class of the value associated with a key
	 * 
	 * @param key
	 *            The key used to insert the class into the map
	 * 
	 * @return the class of the value associated to the passed key
	 */
	private static Class<?> getPropertyClass(final String key) {
		Class<?> propClass = null;

		if (key.equals(BYTEORDER) || key.equals(LAYOUT)) {
			propClass = String.class;
		} else if (key.equals(ULXMAP) || key.equals(ULYMAP) || key.equals(XDIM)
				|| key.equals(YDIM)) {
			propClass = Double.class;
		} else {
			propClass = Integer.class;
		}

		return propClass;
	}
}

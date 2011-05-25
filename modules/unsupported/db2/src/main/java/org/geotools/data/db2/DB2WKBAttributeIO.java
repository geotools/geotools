/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2008. All rights reserved.
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
package org.geotools.data.db2;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.geotools.data.DataSourceException;
import org.geotools.data.jdbc.attributeio.AttributeIO;

import com.vividsolutions.jts.io.WKBReader;

/**
 * Support DB2 geometries using WKB
 * 
 * @author David Adler - IBM Corporation
 *
 *
 * @source $URL$
 */
public class DB2WKBAttributeIO implements AttributeIO {
	private WKBReader rdr = null;

	private WKBReader getReader() {
		if (rdr == null) {
			rdr = new WKBReader();
		}
		return rdr;
	}

	public Object read(ResultSet rs, int position) throws IOException {
		try {

			byte bytes[] = rs.getBytes(position);

			if (bytes == null) // Not sure this is really the best thing to do
								// here
				return null;

			try {
				return getReader().read(bytes);
			} catch (Exception e) {
				throw new DataSourceException("Exception processing WKB", e);
			}

		} catch (SQLException e) {
			throw new DataSourceException(
					"SQL exception occurred while reading the geometry.", e);
		}
	}

	public void write(PreparedStatement ps, int position, Object value)
			throws IOException {
		throw new UnsupportedOperationException("Write WKB not supported");

	}

	public void write(ResultSet rs, int position, Object value)
			throws IOException {
		throw new UnsupportedOperationException("Write WKB not supported");
	}

}

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

package org.geotools.gce.imagemosaic.jdbc;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

public class AbstractCmd {

	protected static String CONFIGPARAM = "-config";

	protected static String SPATIALTNPREFIXPARAM = "-spatialTNPrefix";

	protected static String TILETNPREFIXPARAM = "-tileTNPrefix";

	protected static URL getURLFromString(String source) {
		if (source == null) {
			return null;
		}

		URL sourceURL = null;
		File f = new File(source);
		try {
			if (f.exists()) {
				return f.toURI().toURL();
			}
			sourceURL = new URL(URLDecoder.decode((String) source, "UTF8"));

		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		return sourceURL;
	}

	static String getTableName(String prefix, int level) {
		return prefix + "_" + level;
	}

}

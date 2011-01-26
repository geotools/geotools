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

import java.net.URL;

import org.geotools.gce.imagemosaic.jdbc.Import.ImportTyp;

public class ImportParam {

	private String spatialTableName;

	private String tileTableName;

	private URL sourceURL;

	private String sourceParam;

	private ImportTyp typ;

	public ImportParam(String spatialTableName, String tileTableName,
			URL sourceURL, String sourceParam, ImportTyp typ) {
		super();
		this.spatialTableName = spatialTableName;
		this.tileTableName = tileTableName;
		this.sourceURL = sourceURL;
		this.sourceParam = sourceParam;
		this.typ = typ;
	}

	public String getSourceParam() {
		return sourceParam;
	}

	public URL getSourceURL() {
		return sourceURL;
	}

	public String getSpatialTableName() {
		return spatialTableName;
	}

	public String getTileTableName() {
		return tileTableName;
	}

	public ImportTyp getTyp() {
		return typ;
	}
}

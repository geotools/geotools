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

import java.util.Locale;

import javax.imageio.ImageWriteParam;

import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;

/**
 * @author Simone Giannecchini, GeoSolutions.
 * @since 2.3.x
 * 
 *
 *
 * @source $URL$
 */
public final class GTopo30WriteParams extends GeoToolsWriteParams {



	public int getCompressionMode() {
		return compressionMode;
	}

	public String getCompressionType() {
		return compressionType;
	}

	public boolean hasController() {
		return false;
	}

	public void setCompressionMode(int compressionMode) {
		this.compressionMode = compressionMode;
	}


	public void setCompressionType(String ct) {
		compressionType= new String(ct);
	}

	/**
	 * Default constructor.
	 */
	public GTopo30WriteParams() {
		super(new ImageWriteParam(Locale.getDefault()));
		//allowed compression types
		compressionTypes= new String[]{"NONE","ZIP"};
		//default compression type
		compressionType="NONE";
		canWriteCompressed=true;
		canWriteProgressive=false;
		canWriteTiles=false;
		canOffsetTiles=false;
		controller=null;
		

	}

	public String[] getCompressionTypes() {
		return compressionTypes;
	}



}

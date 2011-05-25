/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image;

import javax.media.jai.ImageLayout;

/**
 * Working around a bug in ImageLayout that blows up when doing hashing with NOT all the fields initialized.
 * 
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/coverage/src/main/java/org/geotools/image/ImageLayout2.java $
 */
public class ImageLayout2 extends ImageLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7921590012423277029L;

	@Override
	public int hashCode() {
		int code = 0, i = 1;
		
        // This implementation is quite arbitrary.
        // hashCode's NEED not be uniqe for two "different" objects
		if(isValid(ImageLayout2.WIDTH_MASK))
			code += (getWidth(null) * i++);
		
		if(isValid(ImageLayout2.HEIGHT_MASK))
			code += (getHeight(null) * i++);
        
		if(isValid(ImageLayout2.MIN_X_MASK))
			code += (getMinX(null) * i++);
        
		if(isValid(ImageLayout2.MIN_Y_MASK))
			code += (getMinY(null) * i++);
        
		if(isValid(ImageLayout2.TILE_HEIGHT_MASK))
			code += (getTileHeight(null) * i++);
        
		if(isValid(ImageLayout2.TILE_WIDTH_MASK))
			code += (getTileWidth(null) * i++);
        
		if(isValid(ImageLayout2.TILE_GRID_X_OFFSET_MASK))
			code += (getTileGridXOffset(null) * i++);
        
		if(isValid(ImageLayout2.TILE_GRID_Y_OFFSET_MASK))
			code += (getTileGridYOffset(null) * i++);

		if(isValid(ImageLayout2.SAMPLE_MODEL_MASK))
			code ^= getSampleModel(null).hashCode();
        
        code ^= validMask;
        
        if(isValid(ImageLayout2.COLOR_MODEL_MASK))
			code ^= getColorModel(null).hashCode();

        return code;
	}
	
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer3d.provider.texture.impl;

import org.geotools.renderer3d.utils.BoundingRectangle;

import java.awt.image.BufferedImage;

/**
 * A listener that is called when a TextureProvider has crated the texture for a requestd area.
 *
 * @author Hans Häggström
 */
public interface TextureListener
{
    /**
     * @param area    the area that the texture is for
     * @param texture the texture as a buffered image.
     *                The texture image should not be modified after calling this method, as it will be moved to the
     *                3D card in the OpenGL thread.
     *                <p/>
     *                NOTE: This makes it harder to reuse memory while rendering pictures, it would be nice if the
     *                TextureProvider could reuse the same image for the next request.
     *                Maybe another call-callback when the image is no longer needed?  Or maybe it isn't a big issue.
     */
    void onTextureReady( BoundingRectangle area, BufferedImage texture );

}

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
package org.geotools.renderer3d.terrainblock;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

import java.awt.image.BufferedImage;

/**
 * A square area of ground, containing the data required for the 3D view, at some resolution.
 *
 * @author Hans Häggström
 */
public interface TerrainBlock
{
    /**
     * @return the 3D node containing this terrain block.
     */
    Spatial getSpatial();

    /**
     * @return the center of the block at local ground level.
     */
    Vector3f getCenter();

    /**
     * Called from the constructor, and when a TerrainBlock is being re-used.
     */
    void updateDerivedData();

    BufferedImage getTextureImage();

    boolean hasCalculatedTextureImage();

    void clearPicture();

    /**
     * @return the texture currently used by this block, or null if it is using a placeholder texture.
     */
    Texture getTexture();
}

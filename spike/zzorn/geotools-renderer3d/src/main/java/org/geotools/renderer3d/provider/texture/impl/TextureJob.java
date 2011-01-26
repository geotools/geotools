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
 * Contains the information for one rendering task.
 *
 * @author Hans Häggström
 */
final class TextureJob
{

    //======================================================================
    // Private Fields

    final private BoundingRectangle area;
    final private BufferedImage buffer;
    final private TextureListener textureListener;

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    public TextureJob( final BoundingRectangle area,
                       final BufferedImage buffer,
                       final TextureListener textureListener )
    {
        this.area = area;
        this.buffer = buffer;
        this.textureListener = textureListener;
    }

    //----------------------------------------------------------------------
    // Other Public Methods

    public BoundingRectangle getArea()
    {
        return area;
    }


    public BufferedImage getBuffer()
    {
        return buffer;
    }


    public TextureListener getTextureListener()
    {
        return textureListener;
    }

    //----------------------------------------------------------------------
    // Caononical Methods

    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        final TextureJob paintJob = (TextureJob) o;

        if ( area != null ? !area.equals( paintJob.area ) : paintJob.area != null )
        {
            return false;
        }
        if ( buffer != null ? !buffer.equals( paintJob.buffer ) : paintJob.buffer != null )
        {
            return false;
        }
        if ( textureListener != null ? !textureListener.equals( paintJob.textureListener ) : paintJob.textureListener != null )
        {
            return false;
        }

        return true;
    }


    public int hashCode()
    {
        int result;
        result = ( area != null ? area.hashCode() : 0 );
        result = 31 * result + ( buffer != null ? buffer.hashCode() : 0 );
        result = 31 * result + ( textureListener != null ? textureListener.hashCode() : 0 );
        return result;
    }


    public String toString()
    {
        return "PaintJob{" +
               "area=" + area +
               ", buffer=" + buffer +
               ", textureListener=" + textureListener +
               '}';
    }

}

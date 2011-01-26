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
package org.geotools.renderer3d.provider.texture;

import org.geotools.renderer3d.utils.BoundingRectangle;
import org.geotools.renderer3d.utils.MathUtils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * A TextureRenderer implementation for testing purposes.
 * Outputs a texture with the the center coordinates of the requested area, and gradient colored.
 *
 * @author Hans Häggström
 */
public final class DummyTextureRenderer
        implements TextureRenderer
{

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // TextureRenderer Implementation

    public void renderArea( final BoundingRectangle area, final BufferedImage target )
    {
        final Graphics2D graphics = (Graphics2D) target.getGraphics();

        final int width = target.getWidth();
        final int height = target.getHeight();

        graphics.setColor( Color.BLUE );
        graphics.fillRect( 0, 0, width, height );

        // Fill with a gradient that depends on x coordinate.
        for ( int x = 0; x < width; x++ )
        {
            final Color color = makeCoordinateColor( MathUtils.interpolate( x,
                                                                            0,
                                                                            width,
                                                                            area.getX1(),
                                                                            area.getX2() ) );
            graphics.setColor( color );
            graphics.drawLine( x, 0, x, height );
        }

        // Write tile coordinate and draw a border
        graphics.setColor( Color.WHITE );
        graphics.drawRect( 0, 0, width - 1, height - 1 );
        graphics.drawString( "x:" + area.getCenterX() + ", y:" + area.getCenterY(),
                             width / 8,
                             height / 2 );

        // Simulate calculation / loading delay:
        pauseRandomTime( 50, 20 );
    }

    private void pauseRandomTime( final int maxMsToPause, final int addition )
    {
        try
        {
            Thread.sleep( (long) ( maxMsToPause * Math.random() + addition ) );
        }
        catch ( InterruptedException e )
        {
            // Ignore
        }
    }

    //======================================================================
    // Private Methods

    private Color makeCoordinateColor( double x )
    {
        x = Math.abs( x );
        int i = (int) x;
        return new Color( i % 255, ( i / 10 ) % 255, ( i / 100 ) % 255 );
    }

}

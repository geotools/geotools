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

import org.geotools.renderer3d.provider.texture.TextureRenderer;
import org.geotools.renderer3d.utils.BoundingRectangle;
import org.geotools.renderer3d.utils.ParameterChecker;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

/**
 * The default texture provider, will render the textures in a separate thread and call the texture listener on the
 * swing thread when a texture has been rendered.
 *
 * @author Hans Häggström
 */
public final class TextureProviderImpl
        implements TextureProvider
{

    //======================================================================
    // Private Fields

    private final TextureRenderer myTextureRenderer;
    private final List<TextureJob> myTextureJobs = new LinkedList<TextureJob>();
    private TextureJob myCurrentJob = null;
    private boolean myCurrentJobWasCanceled = false;
    private final int myTextureSize;
    private final BufferedImage myRenderBufer;
    private final Color myBackgroundColor;

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    /**
     * @param textureRenderer the renderer to use for rendering the textures.
     */
    public TextureProviderImpl( final TextureRenderer textureRenderer, int textureSize, Color backgroundColor )
    {
        ParameterChecker.checkNotNull( textureRenderer, "textureRenderer" );
        ParameterChecker.checkPositiveNonZeroInteger( textureSize, "textureSize" );
        ParameterChecker.checkNotNull( backgroundColor, "backgroundColor" );

        myTextureSize = textureSize;
        myTextureRenderer = textureRenderer;
        myBackgroundColor = backgroundColor;

        myRenderBufer = new BufferedImage( textureSize, textureSize, BufferedImage.TYPE_4BYTE_ABGR );

        // Start a thread that handles texture painting jobs
        final Thread renderThread = new Thread( new Runnable()
        {

            public void run()
            {
                while ( true )
                {
                    final TextureJob paintJob = getNextJob();

                    clearToColor( paintJob.getBuffer(), myBackgroundColor );
                    myTextureRenderer.renderArea( paintJob.getArea(), myRenderBufer );

                    synchronized ( myTextureJobs )
                    {
                        if ( !myCurrentJobWasCanceled )
                        {
                            // Copy image from the render buffer to the final desination
                            final Graphics graphics = paintJob.getBuffer().getGraphics();
                            graphics.drawImage( myRenderBufer, 0, 0, null );

                            notifyListener( paintJob );
                        }
                        myCurrentJobWasCanceled = false;
                    }
                }
            }

        } );
        renderThread.setDaemon( true );
        renderThread.start();
    }

    private void clearToColor( final BufferedImage image, final Color backgroundColor )
    {
        final Graphics graphics = image.getGraphics();
        graphics.setColor( backgroundColor );
        graphics.fillRect( 0, 0, myTextureSize, myTextureSize );
    }

    //----------------------------------------------------------------------
    // TextureProvider Implementation

    public void requestTexture( final BoundingRectangle area,
                                final BufferedImage buffer,
                                final TextureListener textureListener )
    {
        synchronized ( myTextureJobs )
        {
            myTextureJobs.add( new TextureJob( area, buffer, textureListener ) );
            myTextureJobs.notifyAll();
        }
    }

    public void cancelRequest( final TextureListener textureListener )
    {
        synchronized ( myTextureJobs )
        {
            TextureJob textureJobToRemove = null;
            for ( TextureJob textureJob : myTextureJobs )
            {
                if ( textureJob.getTextureListener() == textureListener )
                {
                    textureJobToRemove = textureJob;
                    break;
                }
            }

            if ( textureJobToRemove != null )
            {
                myTextureJobs.remove( textureJobToRemove );
            }
            else if ( myCurrentJob != null && myCurrentJob.getTextureListener() == textureListener )
            {
                myCurrentJobWasCanceled = true;

                // myTextureRenderer.cancelRendering(); // TODO: This could be implemented to speed up things a bit when
                // jobs are canceled, but there's some possibilities that it cancels the next job instead, so left out for now.
            }
        }
    }

    //======================================================================
    // Private Methods

    private void notifyListener( final TextureJob paintJob )
    {
        // Notify listener from swing thread
        // TODO: Maybe we could just call the listener directly from this thread?
        SwingUtilities.invokeLater( new Runnable()
        {

            public void run()
            {
                paintJob.getTextureListener().onTextureReady( paintJob.getArea(), paintJob.getBuffer() );
            }

        } );
    }


    /**
     * @return Returns the next paint job.  Blocks until one is available.
     */
    private TextureJob getNextJob()
    {
        TextureJob paintJob = null;

        while ( paintJob == null )
        {
            synchronized ( myTextureJobs )
            {
                myCurrentJob = null;

                while ( myTextureJobs.isEmpty() )
                {
                    try
                    {
                        myTextureJobs.wait();
                    }
                    catch ( InterruptedException e )
                    {
                        // Ignore
                    }
                }

                if ( !myTextureJobs.isEmpty() )
                {
                    paintJob = myTextureJobs.get( 0 );
                    myTextureJobs.remove( paintJob );
                }

                myCurrentJob = paintJob;
            }
        }

        return paintJob;
    }

}

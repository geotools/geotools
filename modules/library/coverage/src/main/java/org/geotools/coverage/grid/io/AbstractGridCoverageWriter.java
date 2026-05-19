/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import it.geosolutions.io.output.adapter.OutputStreamAdapter;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.stream.ImageOutputStream;
import org.geotools.api.coverage.grid.GridCoverageWriter;
import org.geotools.image.io.ImageIOExt;
import org.geotools.util.URLs;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * An {@link AbstractGridCoverageWriter} is the base class for all {@link GridCoverageWriter} implementations in
 * GeoTools toolkit.
 *
 * <p>We expect it to become the place where to move functionalities common to all {@link GridCoverageWriter}.
 *
 * @author Simone Giannecchini
 * @since 2.3.x
 */
public abstract class AbstractGridCoverageWriter implements GridCoverageWriter {

    /** The {@link Logger} for this {@link AbstractGridCoverageWriter}. */
    private static final Logger LOGGER = Logging.getLogger(AbstractGridCoverageWriter.class);

    /** the destination object where we will do the writing */
    protected Object destination;

    /** Hints to be used for the writing process. */
    protected Hints hints = GeoTools.getDefaultHints();

    /** The destination {@link ImageOutputStream}. */
    private ImageOutputStream outStream = null;

    /** Default constructor for an {@link AbstractGridCoverageWriter}. */
    public AbstractGridCoverageWriter() {}

    /**
     * Computes the {@link ImageOutputStream} to write the geotiff to, based on the type of the destination object. The
     * destination can be a {@link File}, a {@link URL}, an {@link OutputStream} or an {@link ImageOutputStream}.
     *
     * @param image The image that will be written
     * @return The output stream to write the GeoTIFF to.
     * @throws IOException Might be thrown during the construction of the output stream, for instance if the destination
     *     is a file and it cannot be created.
     */
    @SuppressWarnings("PMD.CloseResource")
    protected ImageOutputStream getImageOutputStream(RenderedImage image) throws IOException {
        if (this.outStream != null) return this.outStream;

        if (destination instanceof File) {
            this.outStream = ImageIOExt.createImageOutputStream(image, destination);
        } else if (destination instanceof URL) {
            URL dest = (URL) destination;
            if (dest.getProtocol().equalsIgnoreCase("file"))
                this.outStream = ImageIOExt.createImageOutputStream(image, URLs.urlToFile(dest));
            else this.outStream = ImageIOExt.createImageOutputStream(image, destination);
        } else if (destination instanceof OutputStream) {
            if (destination instanceof OutputStreamAdapter) {
                OutputStreamAdapter adapter = (OutputStreamAdapter) destination;
                this.outStream = adapter.getWrappedStream();
                this.destination = outStream;
            } else {
                this.outStream = ImageIOExt.createImageOutputStream(image, destination);
            }
        } else if (destination instanceof ImageOutputStream) {
            this.outStream = (ImageOutputStream) destination;
        } else throw new IllegalArgumentException("The provided destination cannot be used!");

        if (this.outStream == null)
            throw new IOException("Could not create an ImageOutputStream for destination: " + destination);
        return this.outStream;
    }

    /** Releases resources held by this {@link AbstractGridCoverageWriter}. */
    @Override
    @SuppressWarnings("PMD.UseTryWithResources")
    public void dispose() {
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("Disposing writer:" + destination);

        if (outStream != null) {
            try {
                outStream.flush();

            } catch (IOException e) {

            } finally {
                try {
                    outStream.close();
                } catch (Throwable e) {

                }
                outStream = null;
                destination = null;
            }
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see org.geotools.api.coverage.grid.GridCoverageWriter#getDestination()
     */
    @Override
    public Object getDestination() {
        return destination;
    }

    /**
     * Implementation of getMetadataNames. Currently unimplemented because it has not been specified where to retrieve
     * the metadata
     *
     * @return null
     * @see org.geotools.api.coverage.grid.GridCoverageWriter#getMetadataNames()
     */
    @Override
    public String[] getMetadataNames() {
        throw new UnsupportedOperationException("Unsupported method");
    }

    /** @see org.geotools.api.coverage.grid.GridCoverageWriter#setCurrentSubname(java.lang.String) */
    @Override
    public void setCurrentSubname(String name) throws IOException {
        throw new UnsupportedOperationException("Unsupported method");
    }

    /** @see org.geotools.api.coverage.grid.GridCoverageWriter#setMetadataValue(java.lang.String, java.lang.String) */
    @Override
    public void setMetadataValue(String name, String value) throws IOException {
        throw new UnsupportedOperationException("Unsupported method");
    }

    /**
     * Forcing the disposal of this {@link AbstractGridCoverageWriter} which may keep a reference to an open
     * {@link ImageOutputStream}
     */
    @Override
    @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }
}

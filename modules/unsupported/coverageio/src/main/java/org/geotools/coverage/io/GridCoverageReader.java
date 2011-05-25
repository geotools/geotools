/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io;

// J2SE dependencies
import java.io.IOException;

import org.geotools.coverage.GridSampleDimension;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;


/**
 * Interface for reading {@link GridCoverage} objects. Reading is a two steps process:
 * The input file must be set first, then the actual reading is performed with the
 * {@link #getGridCoverage}. Example:
 *
 * <blockquote><pre>
 * GridCoverageReader reader = ...
 * reader.{@linkplain #setInput setInput}(new File("MyCoverage.dat"), true);
 * GridCoverage coverage = reader.{@linkplain #getGridCoverage getGridCoverage}(0);
 * </pre></blockquote>
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public interface GridCoverageReader {
    /**
     * Sets the input source to the given object. The input is usually a
     * {@link java.io.File} or an {@link java.net.URL} object. But some
     * other types (e.g. {@link javax.imageio.stream.ImageInputStream})
     * may be accepted as well.
     *
     * @param  input The {@link java.io.File} or {@link java.net.URL} to be read.
     * @param  seekForwardOnly if {@code true}, grid coverages
     *         and metadata may only be read in ascending order from
     *         the input source.
     * @throws IOException if an I/O operation failed.
     * @throws IllegalArgumentException if input is not a valid instance.
     */
    void setInput(Object input, boolean seekForwardOnly) throws IOException;

    /**
     * Returns the number of images available from the current input source.
     * Note that some image formats do not specify how many images are present
     * in the stream. Thus determining the number of images will require the
     * entire stream to be scanned and may require memory for buffering.
     * The {@code allowSearch} parameter may be set to {@code false}
     * to indicate that an exhaustive search is not desired.
     *
     * @param  allowSearch If {@code true}, the true number of images will
     *         be returned even if a search is required. If {@code false},
     *         the reader may return -1 without performing the search.
     * @return The number of images, or -1 if {@code allowSearch} is
     *         {@code false} and a search would be required.
     * @throws IllegalStateException If the input source has not been set, or if
     *         the input has been specified with {@code seekForwardOnly} set to {@code true}.
     * @throws IOException If an error occurs reading the information from the input source.
     */
    int getNumImages(boolean allowSearch) throws IOException;

    /**
     * Gets the {@link GridCoverage} name at the specified index.
     *
     * @param  index The index of the image to be queried.
     * @return The name for the {@link GridCoverage} at the specified index.
     * @throws IllegalStateException if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IOException if an error occurs reading the information from the input source.
     */
    String getName(int index) throws IOException;

    /**
     * Returns the coordinate reference system for the {@link GridCoverage} to be read.
     *
     * @param  index The index of the image to be queried.
     * @return The coordinate reference system for the {@link GridCoverage} at the specified index.
     * @throws IllegalStateException if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IOException if an error occurs reading the width information from the input source.
     */
    CoordinateReferenceSystem getCoordinateReferenceSystem(int index) throws IOException;

    /**
     * Returns the envelope for the {@link GridCoverage} to be read.
     * The envelope must have the same number of dimensions than the
     * coordinate reference system.
     *
     * @param  index The index of the image to be queried.
     * @return The envelope for the {@link GridCoverage} at the specified index.
     * @throws IllegalStateException if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IOException if an error occurs reading the width information from the input source.
     */
    Envelope getEnvelope(int index) throws IOException;

    /**
     * Returns the grid range for the {@link GridCoverage} to be read.
     * The grid range must have the same number of dimensions than the
     * envelope.
     *
     * @param  index The index of the image to be queried.
     * @return The grid range for the {@link GridCoverage} at the specified index.
     * @throws IllegalStateException if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IOException if an error occurs reading the width information from the input source.
     */
    GridEnvelope getGridRange(int index) throws IOException;

    /**
     * Returns the transform from {@linkplain #getGridRange grid range} to
     * {@linkplain #getCoordinateReferenceSystem CRS} coordinates.
     */
    MathTransform getMathTransform(int index) throws IOException;

    /**
     * Returns the sample dimensions for each band of the {@link GridCoverage}
     * to be read. If sample dimensions are not known, then this method returns
     * {@code null}.
     *
     * @param  index The index of the image to be queried.
     * @return The category lists for the {@link GridCoverage} at the specified index.
     *         This array's length must be equals to the number of bands in {@link GridCoverage}.
     * @throws IllegalStateException if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IOException if an error occurs reading the width information from the input source.
     */
    GridSampleDimension[] getSampleDimensions(int index) throws IOException;

    /**
     * Reads the grid coverage.
     *
     * @param  index The index of the image to be queried.
     * @return The {@link GridCoverage} at the specified index.
     * @throws IllegalStateException if the input source has not been set.
     * @throws IndexOutOfBoundsException if the supplied index is out of bounds.
     * @throws IOException if an error occurs reading the width information from the input source.
     */
    GridCoverage getGridCoverage(int index) throws IOException;

    /**
     * Restores the {@code GridCoverageReader} to its initial state.
     *
     * @throws IOException if an error occurs while disposing resources.
     */
    void reset() throws IOException;
}

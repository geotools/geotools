/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.coverage.grid;

import java.io.IOException;
import java.io.FileNotFoundException;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.InvalidParameterNameException;
import org.opengis.parameter.InvalidParameterValueException;


/**
 * Support for reading grid coverages out of a persisten store. Instance of
 * {@code GridCoverageReader} are obtained through a call to
 * {@link GridCoverageExchange#getReader}. Grid coverages are usually
 * read from the input stream in a sequential order.
 *
 *
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 *
 * @see GridCoverageExchange#getReader
 * @see javax.imageio.ImageReader
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/coverage/grid/GridCoverageReader.java $
 */
public interface GridCoverageReader {
    /**
     * Returns the format handled by this {@code GridCoverageReader}.
     */
    Format getFormat();

    /**
     * Returns the input source. This is the object passed to the
     * {@link GridCoverageExchange#getReader} method. It can be a
     * {@link java.lang.String}, an {@link java.io.InputStream}, a
     * {@link java.nio.channels.FileChannel}, whatever.
     */
    Object getSource();

    /**
     * Returns the list of metadata keywords associated with the {@linkplain #getSource
     * input source} as a whole (not associated with any particular grid coverage).
     * If no metadata is available, the array will be empty.
     *
     * @return The list of metadata keywords for the input source.
     * @throws IOException if an error occurs during reading.
     *
     * @todo This javadoc may not apply thats well in the iterator scheme.
     */
    String[] getMetadataNames() throws IOException;

    /**
     * Retrieve the metadata value for a given metadata name.
     *
     * @param  name Metadata keyword for which to retrieve metadata.
     * @return The metadata value for the given metadata name. Should be one of
     *         the name returned by {@link #getMetadataNames}.
     * @throws IOException if an error occurs during reading.
     *
     * @todo This javadoc may not apply thats well in the iterator scheme.
     */
    String getMetadataValue(String name) throws IOException;

    /**
     * Retrieve the list of grid coverages contained within the {@linkplain #getSource
     * input source}. Each grid can have a different coordinate system, number of dimensions
     * and grid geometry. For example, a HDF-EOS file (GRID.HDF) contains 6 grid coverages
     * each having a different projection. An empty array will be returned if no sub names
     * exist.
     *
     * @return The list of grid coverages contained within the input source.
     * @throws IOException if an error occurs during reading.
     *
     * @todo The javadoc should also be more explicit about hierarchical format.
     *       Should the names be returned as paths?
     *       Explain what to return if the GridCoverage are accessible by index
     *       only. A proposal is to name them "grid1", "grid2", etc.
     */
    String[] listSubNames() throws IOException;

    /**
     * Returns the name for the next grid coverage to be {@linkplain #read read} from the
     * {@linkplain #getSource input source}.
     *
     * @throws IOException if an error occurs during reading.
     * @todo Do we need a special method for that, or should it be a metadata?
     */
    String getCurrentSubname() throws IOException;

    /**
     * Returns {@code true} if there is at least one more grid coverage
     * available on the stream.
     */
    boolean hasMoreGridCoverages() throws IOException;

    /**
     * Read the grid coverage from the current stream position, and move to the next grid
     * coverage. The method {@link #hasMoreGridCoverages} should be invoked first in order
     * to verify that a coverage is available.
     *
     * @param  parameters An optional set of parameters. Should be any or all of the
     *         parameters returned by {@link Format#getReadParameters}.
     * @return A new {@linkplain GridCoverage grid coverage} from the input source.
     * @throws InvalidParameterNameException if a parameter in {@code parameters}
     *         doesn't have a recognized name.
     * @throws InvalidParameterValueException if a parameter in {@code parameters}
     *         doesn't have a valid value.
     * @throws ParameterNotFoundException if a parameter was required for the operation but was
     *         not provided in the {@code parameters} list.
     * @throws CannotCreateGridCoverageException if the coverage can't be created for a logical
     *         reason (for example an unsupported format, or an inconsistency found in the data).
     * @throws IOException if a read operation failed for some other input/output reason, including
     *         {@link FileNotFoundException} if no file with the given {@code name} can be
     *         found, or {@link javax.imageio.IIOException} if an error was thrown by the
     *         underlying image library.
     */
    GridCoverage read(GeneralParameterValue[] parameters)
            throws IllegalArgumentException, IOException;

    /**
     * Skip the current grid coverage without reading it, and move the stream position to
     * the next grid coverage.
     *
     * @throws IOException if the operation failed.
     */
    void skip() throws IOException;

    /**
     * Allows any resources held by this object to be released. The result of calling any other
     * method subsequent to a call to this method is undefined. It is important for applications
     * to call this method when they know they will no longer be using this {@code GridCoverageReader}.
     * Otherwise, the reader may continue to hold on to resources indefinitely.
     *
     * @throws IOException if an error occured while disposing resources (for example while closing
     *         a file).
     */
    void dispose() throws IOException;
}

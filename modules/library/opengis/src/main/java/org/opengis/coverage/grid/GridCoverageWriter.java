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
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.InvalidParameterNameException;
import org.opengis.parameter.InvalidParameterValueException;


/**
 * Support for writing grid coverages into a persistent store. Instance
 * of {@code GridCoverageWriter} are obtained through a call to
 * {@link GridCoverageExchange#getWriter}. Grid coverages are usually
 * added to the output stream in a sequential order.
 *
 *
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 *
 * @see GridCoverageExchange#getWriter
 * @see javax.imageio.ImageWriter
 *
 */
public interface GridCoverageWriter {
    /**
     * Returns the format handled by this {@code GridCoverageWriter}.
     */
    Format getFormat();

    /**
     * Returns the output destination. This is the object passed to the
     * {@link GridCoverageExchange#getWriter} method. It can be a
     * {@link java.lang.String}, an {@link java.io.OutputStream},
     * a {@link java.nio.channels.FileChannel}, etc.
     */
    Object getDestination();

    /**
     * Returns the list of metadata keywords associated with the {@linkplain #getDestination
     * output destination} as a whole (not associated with any particular grid coverage).
     * If no metadata is allowed, the array will be empty.
     *
     * @return The list of metadata keywords for the output destination.
     * @throws IOException if an error occurs during reading or writing.
     *
     * @todo This javadoc may not apply thats well in the iterator scheme.
     */
    String[] getMetadataNames();

    /**
     * Sets the metadata value for a given metadata name.
     *
     * @param  name Metadata keyword for which to set the metadata.
     * @param  value The metadata value for the given metadata name.
     * @throws IOException if an error occurs during writing.
     *
     * @todo This javadoc may not apply thats well in the iterator scheme.
     */
    void setMetadataValue(String name, String value) throws IOException;

    /**
     * Set the name for the next grid coverage to {@linkplain #write write} within the
     * {@linkplain #getDestination output destination}. The subname can been fetch later
     * at reading time.
     *
     * @throws IOException if an error occurs during writing.
     * @todo Do we need a special method for that, or should it be a metadata?
     */
    void setCurrentSubname(String name) throws IOException;

    /**
     * Writes the specified grid coverage.
     *
     * @param  coverage The {@linkplain GridCoverage grid coverage} to write.
     * @param  parameters An optional set of parameters. Should be any or all of the
     *         parameters returned by {@link Format#getWriteParameters}.
     * @throws InvalidParameterNameException if a parameter in {@code parameters}
     *         doesn't have a recognized name.
     * @throws InvalidParameterValueException if a parameter in {@code parameters}
     *         doesn't have a valid value.
     * @throws ParameterNotFoundException if a parameter was required for the operation but was
     *         not provided in the {@code parameters} list.
     * @throws FileFormatNotCompatibleWithGridCoverageException if the grid coverage
     *         can't be exported in the {@linkplain #getFormat writer format}.
     * @throws IOException if the export failed for some other input/output reason, including
     *         {@link javax.imageio.IIOException} if an error was thrown by the underlying
     *         image library.
     */
    void write(GridCoverage coverage, GeneralParameterValue[] parameters)
            throws IllegalArgumentException, IOException;

    /**
     * Allows any resources held by this object to be released. The result of calling any other
     * method subsequent to a call to this method is undefined. It is important for applications
     * to call this method when they know they will no longer be using this {@code GridCoverageWriter}.
     * Otherwise, the writer may continue to hold on to resources indefinitely.
     *
     * @throws IOException if an error occured while disposing resources
     *         (for example while flushing data and closing a file).
     */
    void dispose() throws IOException;
}

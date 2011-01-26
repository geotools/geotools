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
package org.geotools.image.io.mosaic;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import org.geotools.util.Utilities;


/**
 * A pair of {@link ImageReader} with its input. Only used as keys in hash map.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class ReaderInputPair {
    /**
     * The image reader, or the provider if none.
     */
    private final Object reader;

    /**
     * The input to be given to the image reader.
     */
    private final Object input;

    /**
     * Hash code computed at construction time in order to avoid computing it twice.
     */
    private final int hash;

    /**
     * Creates a provider/input pair.
     */
    ReaderInputPair(final ImageReaderSpi provider, final Object input) {
        this.reader = provider;
        this.input  = input;
        this.hash   = hash();
    }

    /**
     * Creates a reader/input pair.
     */
    ReaderInputPair(final ImageReader reader, final Object input) {
        this.reader = reader;
        this.input  = input;
        this.hash   = hash();
    }
    /**
     * Returns a hash value for this reader/input pair.
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * Computes the hash value for this reader/input pair.
     */
    private int hash() {
        return reader.hashCode() + 37*Utilities.deepHashCode(input);
    }

    /**
     * Compares this reader/input pair with the specified object for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof ReaderInputPair) {
            final ReaderInputPair that = (ReaderInputPair) object;
            return Utilities.equals(this.reader, that.reader) &&
                   Utilities.deepEquals(this.input, that.input);
        }
        return false;
    }
}

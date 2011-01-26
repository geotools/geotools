/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.feature;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureReader;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * An iterator that wraps around a FeatureReader.
 * <p>
 * The Iterator's hasNext() will return false if the wrapped feature reader's hasNext method throws
 * an exception. If next() throws an exception a NoSuchElementException will be thrown.
 * </p>
 * <p>
 * {@link #close()} shall be called before disposing the iterator. Before propagating an exception
 * from {@link #next()} or eating an exception from the underlying feature reader at
 * {@link #hasNext()}, this iterator will auto-close.
 * </p>
 * 
 * @author jeichar
 * @author Jody Garnett
 * @author Gabriel Roldan
 * @source $URL$
 */
public class FeatureReaderIterator<F extends Feature> implements Iterator<F> {

    FeatureReader<? extends FeatureType, F> reader;

    public FeatureReaderIterator(FeatureReader<? extends FeatureType, F> reader) {
        this.reader = reader;
    }

    public boolean hasNext() {
        try {
            if (reader == null)
                return false;
            if (reader.hasNext()) {
                return true;
            } else {
                // auto close because we don't trust
                // client code to call closed :-)
                close();
                return false;
            }
        } catch (Exception e) {
            close();
            return false; // failure sounds like lack of next to me
        }
    }

    public F next() {
        if (reader == null) {
            throw new NoSuchElementException("Iterator has been closed");
        }
        try {
            return reader.next();
        } catch (IOException io) {
            close();
            NoSuchElementException problem = new NoSuchElementException(
                    "Could not obtain the next feature:" + io);
            problem.initCause(io);
            throw problem;
        } catch (org.opengis.feature.IllegalAttributeException create) {
            close();
            NoSuchElementException problem = new NoSuchElementException(
                    "Could not create the next feature:" + create);
            problem.initCause(create);
            throw problem;
        }
    }

    /** If this is a problem, a different iterator can be made based on FeatureWriter */
    public void remove() {
        throw new UnsupportedOperationException("Modification of contents is not supported");
    }

    /**
     * Close the reader please.
     */
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (Exception e) {
                // sorry but iterators die quitely in the night
            }
            reader = null;
        }
    }
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.aggregate;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureReader;
import org.geotools.data.simple.SimpleFeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Returns all the features stored in a {@link FeatureQueue}
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class QueueReader implements SimpleFeatureReader {

    private FeatureQueue queue;

    private SimpleFeatureType target;

    private SimpleFeature next;

    public QueueReader(FeatureQueue queue, SimpleFeatureType target) {
        this.queue = queue;
        this.target = target;
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return target;
    }

    @Override
    public boolean hasNext() throws IOException {
        if(next != null && next != FeatureQueue.END_MARKER) {
            return true;
        }
        
        checkException();

        // loop and see if we can grab a feature, or if we just get the end markers
        try {
            while(next == null || next == FeatureQueue.END_MARKER) {
                if (queue.isDone()) {
                    return false;
                }
                next = queue.take();
                checkException();
            }
        } catch (InterruptedException ie) {
            throw new IOException("Error while waiting for next feature", ie);
        }
        return true;
    }

    /**
     * Checks if the queue contains an exception, if so rethrows it
     * @throws IOException
     */
    void checkException() throws IOException {
        // did we get any exception? if so the queue is closing anyways
        Exception e = queue.getException();
        if (e != null) {
            throw new IOException("Data retrieval failed", e);
        }
    }

    @Override
    public SimpleFeature next() throws IOException, IllegalArgumentException,
            NoSuchElementException {
        if (next == null) {
            if (!hasNext()) {
                throw new NoSuchElementException("No more features to be read");
            }
        }

        checkException();
        SimpleFeature result = null;
        result = next;
        next = null;
        return result;
    }

    @Override
    public void close() throws IOException {
        queue.shutDown();
        // wait for all the workers to get out of dodge, they might be blocked
        // on a full queue for example
        while(!queue.isDone()) {
            queue.clear();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}

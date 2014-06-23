/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.store;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

import org.geotools.data.DataSourceException;
import org.geotools.data.Diff;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.factory.Hints;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A FeatureWriter that captures modifications against a FeatureReader.
 * 
 * <p>
 * You will eventually need to write out the differences, later.
 * </p>
 * 
 * <p>
 * The api has been implemented in terms of FeatureReader<SimpleFeatureType, SimpleFeature> to make
 * explicit that no Features are writen out by this Class.
 * </p>
 * 
 * @author Jody Garnett, Refractions Research
 * 
 * @see DiffContentState
 * 
 * @source $URL$
 */
public class DiffContentFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {
    protected FeatureReader<SimpleFeatureType, SimpleFeature> reader;

    ContentState state;

    protected Diff diff;

    SimpleFeature next; // next value aquired by hasNext()

    SimpleFeature live; // live value supplied by FeatureReader

    SimpleFeature current; // duplicate provided to user

    ContentFeatureStore store;
    
    SimpleFeatureBuilder builder;

    /**
     * DiffFeatureWriter construction.
     * 
     * @param reader
     * @param diff
     * @param filter
     */
    public DiffContentFeatureWriter(ContentFeatureStore store, Diff diff,
            FeatureReader<SimpleFeatureType, SimpleFeature> reader) {
        this(store,diff,reader,new SimpleFeatureBuilder(reader.getFeatureType()));
    }
    
    /**
     * DiffFeatureWriter construction.
     * 
     * @param reader
     * @param diff
     * @param filter
     */
    public DiffContentFeatureWriter(ContentFeatureStore store, Diff diff,
            FeatureReader<SimpleFeatureType, SimpleFeature> reader, SimpleFeatureBuilder builder) {
        this.store = store;
        this.reader = reader;
        this.state = store.getState();
        this.diff = diff;
        this.builder = builder;
    }

    /**
     * Supplys FeatureTypeFrom reader
     * 
     * @see org.geotools.data.FeatureWriter#getFeatureType()
     */
    public SimpleFeatureType getFeatureType() {
        return reader.getFeatureType();
    }

    /**
     * Next Feature from reader or new content.
     * 
     * @see org.geotools.data.FeatureWriter#next()
     */
    public SimpleFeature next() throws IOException {
        SimpleFeatureType type = getFeatureType();
        if (hasNext()) {
            // hasNext() will take care recording
            // any modifications to current
            live = next; // update live value
            next = null; // hasNext will need to search again
            current = SimpleFeatureBuilder.copy(live);

            return current;
        } else {
            // Create new content
            // created with an empty ID
            // (The real writer will supply a FID later)
            live = null;
            next = null;
            current = builder.buildFeature("new"+ diff.nextFID, new Object[type.getAttributeCount()]);
            diff.nextFID++;
            return current;
        }
    }

    /**
     * @see org.geotools.data.FeatureWriter#remove()
     */
    public void remove() throws IOException {
        if (live != null) {
            // mark live as removed
            diff.remove(live.getID());
            state.fireFeatureRemoved(store, live);

            live = null;
            current = null;
        } else if (current != null) {
            // cancel additional content
            current = null;
        }
    }

    /**
     * Writes out the current feature.
     * 
     * @throws IOException
     * 
     * @see org.geotools.data.FeatureWriter#write()
     */
    public void write() throws IOException {
        // DJB: I modified this so it doesnt throw an error if you
        // do an update and you didnt actually change anything.
        // (We do the work)
        if ((live != null)) {
            // We have a modification to record!
            diff.modify(live.getID(), current);

            ReferencedEnvelope bounds = ReferencedEnvelope.reference(current.getBounds());
            state.fireFeatureUpdated(store, live, bounds);
            live = null;
            current = null;
        } else if ((live == null) && (current != null)) {
            // We have new content to record
            //
            String fid = current.getID();
            if (Boolean.TRUE.equals(current.getUserData().get(Hints.USE_PROVIDED_FID))) {
                if (current.getUserData().containsKey(Hints.PROVIDED_FID)) {
                    fid = (String) current.getUserData().get(Hints.PROVIDED_FID);
                    Map<Object, Object> userData = current.getUserData();
                    current = SimpleFeatureBuilder.build(current.getFeatureType(),
                            current.getAttributes(), fid);
                    current.getUserData().putAll(userData);
                }
            }
            diff.add(fid, current);
            state.fireFeatureAdded(store, current);
            current = null;
        } else {
            throw new IOException("No feature available to write");
        }
    }

    /**
     * Query for more content.
     * 
     * @see org.geotools.data.FeatureWriter#hasNext()
     */
    public boolean hasNext() throws IOException {
        if (next != null) {
            // we found next already
            return true;
        }

        live = null;
        current = null;

        if (reader.hasNext()) {
            try {
                next = reader.next();
            } catch (NoSuchElementException e) {
                throw new DataSourceException("No more content", e);
            }

            return true;
        }

        return false;
    }

    /**
     * Clean up resources associated with this writer.
     * 
     * <p>
     * Diff is not clear()ed as it is assumed that it belongs to a Transaction.State object and may
     * yet be written out.
     * </p>
     * 
     * @see org.geotools.data.FeatureWriter#close()
     */
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
            reader = null;
        }

        current = null;
        live = null;
        next = null;
        diff = null;
    }

}
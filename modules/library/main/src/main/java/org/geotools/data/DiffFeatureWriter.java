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
package org.geotools.data;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.FeatureEvent;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.factory.Hints;

/**
 * A FeatureWriter that captures modifications against a FeatureReader.
 *
 * <p>You will eventually need to write out the differences, later.
 *
 * <p>The api has been implemented in terms of FeatureReader<SimpleFeatureType, SimpleFeature> to make explicit that no
 * Features are writen out by this Class.
 *
 * @author Jody Garnett, Refractions Research
 * @see TransactionStateDiff
 */
public abstract class DiffFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {
    protected FeatureReader<SimpleFeatureType, SimpleFeature> reader;
    protected Diff diff;
    SimpleFeature next; // next value aquired by hasNext()
    SimpleFeature live; // live value supplied by FeatureReader
    SimpleFeature current; // duplicate provided to user

    /** DiffFeatureWriter construction. */
    public DiffFeatureWriter(FeatureReader<SimpleFeatureType, SimpleFeature> reader, Diff diff) {
        this(reader, diff, Filter.INCLUDE);
    }

    /** DiffFeatureWriter construction. */
    public DiffFeatureWriter(FeatureReader<SimpleFeatureType, SimpleFeature> reader, Diff diff, Filter filter) {
        this.reader = new DiffFeatureReader<>(reader, diff, filter);
        this.diff = diff;
    }

    /**
     * Supplys FeatureTypeFrom reader
     *
     * @see FeatureWriter#getFeatureType()
     */
    @Override
    public SimpleFeatureType getFeatureType() {
        return reader.getFeatureType();
    }

    /**
     * Next Feature from reader or new content.
     *
     * @see FeatureWriter#next()
     */
    @Override
    public SimpleFeature next() throws IOException {
        SimpleFeatureType type = getFeatureType();
        if (hasNext()) {
            // hasNext() will take care recording
            // any modifications to current
            try {
                live = next; // update live value
                next = null; // hasNext will need to search again
                current = SimpleFeatureBuilder.copy(live);

                return current;
            } catch (IllegalAttributeException e) {
                throw (IOException) new IOException("Could not modify content").initCause(e);
            }
        } else {
            if (diff == null) {
                throw new IOException("FeatureWriter has been closed");
            }
            // Create new content
            // created with an empty ID
            // (The real writer will supply a FID later)
            try {
                live = null;
                next = null;
                current = SimpleFeatureBuilder.build(type, new Object[type.getAttributeCount()], "new" + diff.nextFID);
                diff.nextFID++;
                return current;
            } catch (IllegalAttributeException e) {
                throw new IOException("Could not create new content");
            }
        }
    }

    /** @see FeatureWriter#remove() */
    @Override
    public void remove() throws IOException {
        if (live != null) {
            // mark live as removed
            diff.remove(live.getID());
            fireNotification(FeatureEvent.Type.REMOVED.getType(), ReferencedEnvelope.reference(live.getBounds()));
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
     * @see FeatureWriter#write()
     */
    @Override
    public void write() throws IOException {
        // DJB: I modified this so it doesnt throw an error if you
        //     do an update and you didnt actually change anything.
        //     (We do the work)
        if (live != null) {
            // We have a modification to record!
            diff.modify(live.getID(), current);

            ReferencedEnvelope bounds = new ReferencedEnvelope((CoordinateReferenceSystem) null);
            bounds.include(live.getBounds());
            bounds.include(current.getBounds());
            fireNotification(FeatureEvent.Type.CHANGED.getType(), bounds);
            live = null;
            current = null;
        } else if (live == null && current != null) {
            // We have new content to record
            //
            String fid = current.getID();
            if (Boolean.TRUE.equals(current.getUserData().get(Hints.USE_PROVIDED_FID))) {
                if (current.getUserData().containsKey(Hints.PROVIDED_FID)) {
                    fid = (String) current.getUserData().get(Hints.PROVIDED_FID);
                    Map<Object, Object> userData = current.getUserData();
                    current = SimpleFeatureBuilder.build(current.getFeatureType(), current.getAttributes(), fid);
                    current.getUserData().putAll(userData);
                }
            }
            diff.add(fid, current);
            fireNotification(FeatureEvent.Type.ADDED.getType(), ReferencedEnvelope.reference(current.getBounds()));
            current = null;
        } else {
            throw new IOException("No feature available to write");
        }
    }

    /**
     * Query for more content.
     *
     * @see FeatureWriter#hasNext()
     */
    @Override
    public boolean hasNext() throws IOException {
        if (next != null) {
            // we found next already
            return true;
        }

        live = null;
        current = null;

        if (reader == null) {
            throw new IOException("FeatureWriter has been closed");
        }

        if (reader.hasNext()) {
            try {
                next = reader.next();
            } catch (NoSuchElementException | IllegalAttributeException e) {
                throw new DataSourceException("No more content", e);
            }

            return true;
        }

        return false;
    }

    /**
     * Clean up resources associated with this writer.
     *
     * <p>Diff is not clear()ed as it is assumed that it belongs to a Transaction.State object and may yet be written
     * out.
     *
     * @see FeatureWriter#close()
     */
    @Override
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

    /**
     * Subclass must provide the notification.
     *
     * <p>Notification requirements for modifications against a Transaction should only be issued to SimpleFeatureSource
     * instances that opperate against the same typeName and Transaction.
     *
     * <p>Other SimpleFeatureSource instances with the same typeName will be notified when the Transaction is committed.
     *
     * @param eventType One of FeatureType.FEATURES_ADDED, FeatureType.CHANGED, FeatureType.FEATURES_REMOVED
     */
    protected abstract void fireNotification(int eventType, ReferencedEnvelope bounds);
}

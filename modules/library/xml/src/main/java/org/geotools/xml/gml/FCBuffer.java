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
 */
package org.geotools.xml.gml;

import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.XMLHandlerHints;
import org.xml.sax.SAXException;

/**
 * Feature Buffer ... acts as a FeatureReader<SimpleFeatureType, SimpleFeature> by making itself as a seperate thread
 * prior starting execution with the SAX Parser.
 *
 * @author dzwiers
 */
public class FCBuffer extends Thread implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    /** Last feature is in the buffer */
    public static final int FINISH = -1;

    public static final int STOP = -2;

    protected static Logger logger = getLogger();

    // positive number is the number of feature to parse before yield

    protected int state = 0;

    private SimpleFeature[] features;

    private int end;
    private int size;
    private int head;
    private int timeout = 1000;
    private URI document; // for run
    protected SAXException exception = null;

    private FCBuffer() {
        // should not be called
        super("Feature Collection Buffer");
    }

    /** @param ft Nullable */
    protected FCBuffer(URI document, int capacity, int timeout, SimpleFeatureType ft) {
        super("Feature Collection Buffer");
        features = new SimpleFeature[capacity];
        this.timeout = timeout;
        this.document = document;
        end = size = head = 0;
        this.ft = ft;
    }

    /**
     * Returns the logger to be used for this class.
     *
     * @todo Logger.setLevel(...) should not be invoked, because it override any user setting in
     *     {@code jre/lib/logging.properties}. Users should edit their properties file instead. If Geotools is too
     *     verbose below the warning level, then some log messages should probably be changed from Level.INFO to
     *     Level.FINE.
     */
    private static final Logger getLogger() {
        Logger l = org.geotools.util.logging.Logging.getLogger(FCBuffer.class);
        l.setLevel(Level.WARNING);
        return l;
    }

    /** @return The buffer size */
    public int getSize() {
        return size;
    }

    /** @return The buffer capacity */
    public int getCapacity() {
        return features.length;
    }

    /** @return The buffer capacity */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Adds a feature to the buffer
     *
     * @param f Feature to add
     * @return false if unable to add the feature
     */
    protected boolean addFeature(SimpleFeature f) {
        if (ft == null) {
            ft = f.getFeatureType();
        }

        if (size >= features.length) {
            return false;
        }

        synchronized (this) {
            notify();

            features[end] = f;
            end++;

            if (end == features.length) {
                end = 0;
            }

            size++;
        }
        return true;
    }

    /**
     * The prefered method of using this class, this will return the Feature Reader for the document specified, using
     * the specified buffer capacity.
     *
     * @param document URL to parse
     */
    public static FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(URI document, int capacity)
            throws SAXException {
        return getFeatureReader(document, capacity, 1000, null);
    }

    public static FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(
            URI document, int capacity, SimpleFeatureType ft) throws SAXException {
        return getFeatureReader(document, capacity, 1000, ft);
    }

    public static FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(
            URI document, int capacity, int timeout) throws SAXException {

        return getFeatureReader(document, capacity, timeout, null);
    }

    public static FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(
            URI document, int capacity, int timeout, SimpleFeatureType ft) throws SAXException {
        FCBuffer fc = new FCBuffer(document, capacity, timeout, ft);
        fc.start(); // calls run

        if (fc.exception != null) {
            throw fc.exception;
        }

        // TODO set crs here.
        return fc;
    }

    protected SimpleFeatureType ft = null;

    private volatile Date lastUpdate;
    /** @see FeatureReader#getFeatureType() */
    @Override
    public SimpleFeatureType getFeatureType() {
        if (ft != null) return ft;
        Date d = new Date(Calendar.getInstance().getTimeInMillis() + timeout);

        while (ft == null && state != FINISH && state != STOP) {
            Thread.yield(); // let the parser run ... this is being called from

            if (d.before(Calendar.getInstance().getTime())) {
                exception = new SAXException("Timeout");
                state = STOP;
            }
        }

        // the original thread
        if (state == FINISH || state == STOP) {
            return ft;
        }

        return ft;
    }

    /** @see FeatureReader#next() */
    @Override
    public SimpleFeature next() throws IOException, NoSuchElementException {
        if (exception != null) {
            state = STOP;
            IOException e = new IOException(exception.toString());
            e.initCause(exception);
            throw e;
        }

        SimpleFeature f = null;
        synchronized (this) {
            size--;
            f = features[head++];
            notify();

            if (head == features.length) {
                head = 0;
            }
        }
        return f;
    }

    /** @see FeatureReader#next() */
    public SimpleFeature peek() throws IOException, NoSuchElementException {
        if (exception != null) {
            state = STOP;
            IOException e = new IOException(exception.toString());
            e.initCause(exception);
            throw e;
        }

        SimpleFeature f = features[head];
        return f;
    }

    /** @see FeatureReader#hasNext() */
    @Override
    public boolean hasNext() throws IOException {
        if (exception instanceof StopException) {
            return false;
        }

        if (exception != null) {
            IOException e = new IOException(exception.toString());
            e.initCause(exception);
            throw e;
        }

        logger.finest("hasNext " + size);

        resetTimer();

        while (size <= 1 && state != FINISH && state != STOP) { // && t>0) {

            if (exception != null) {
                state = STOP;
                IOException e = new IOException(exception.toString());
                e.initCause(exception);
                throw e;
            }

            logger.finest("waiting for parser");
            try {
                synchronized (this) {
                    wait(200);
                }
            } catch (InterruptedException e) {
                // just continue;
            }

            if (lastUpdate.before(new Date(Calendar.getInstance().getTimeInMillis() - timeout))) {
                exception = new SAXException("Timeout");
                state = STOP;
            }
        }

        if (state == STOP) {
            if (exception != null) {
                IOException e = new IOException(exception.toString());
                e.initCause(exception);
                throw e;
            }

            return false;
        }

        if (state == FINISH) {
            return !(size == 0);
        }

        if (size == 0) {
            state = STOP;

            if (exception != null) {
                throw new IOException(exception.toString());
            }

            throw new IOException("There was an error");
        }

        return true;
    }

    /** @see FeatureReader#close() */
    @Override
    public void close() {
        state = STOP; // note for the sax parser
        interrupt();
    }

    /** @see java.lang.Runnable#run() */
    @Override
    public void run() {
        XMLHandlerHints hints = new XMLHandlerHints();
        initHints(hints);

        try {
            DocumentFactory.getInstance(document, hints);

            // start parsing until buffer part full, then yield();
        } catch (SAXException e) {
            exception = e;
            state = STOP;
            Thread.yield();
        }
    }

    /** Called before parsing the FeatureCollection. Subclasses may override to set their custom hints. */
    protected void initHints(XMLHandlerHints hints) {
        hints.put(XMLHandlerHints.STREAM_HINT, this);
        hints.put(XMLHandlerHints.FLOW_HANDLER_HINT, new FCFlowHandler());
        if (this.ft != null) {
            hints.put("DEBUG_INFO_FEATURE_TYPE_NAME", ft.getTypeName());
        }
    }

    /**
     * @author $author$
     * @version $Revision: 1.9 $
     */
    public static class StopException extends SAXException {
        public StopException() {
            super("Stopping");
        }
    }

    public int getInternalState() {
        return state;
    }

    public void resetTimer() {
        this.lastUpdate = Calendar.getInstance().getTime();
    }
}

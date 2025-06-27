/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.gce.imagemosaic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.DefaultTransaction;
import org.geotools.util.Utilities;

/**
 * This class is responsible for walking through the elements of a mosaic.
 *
 * <p>Its role is basically to simplify the construction of the mosaic by implementing a visitor pattern for the mosaic
 * elements that we have to use for the index.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Carlo Cancellieri, GeoSolutions SAS
 */
abstract class ImageMosaicWalker<T> implements Runnable {

    /** Default Logger * */
    static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImageMosaicWalker.class);

    protected DefaultTransaction transaction;

    /**
     * Proper way to stop a thread is not by calling Thread.stop() but by using a shared variable that can be checked in
     * order to notify a terminating condition.
     */
    protected volatile boolean stop = false;

    protected final ImageMosaicConfigHandler configHandler;

    protected final ImageMosaicEventHandlers eventHandler;

    /** index of the element being processed */
    protected int elementIndex = 0;

    /** Number of elements to process. */
    protected int numElements = 1;

    public ImageMosaicConfigHandler getConfigHandler() {
        return configHandler;
    }

    public ImageMosaicEventHandlers getEventHandler() {
        return eventHandler;
    }

    public DefaultTransaction getTransaction() {
        return transaction;
    }

    /** @param configHandler configuration handler being used */
    public ImageMosaicWalker(ImageMosaicConfigHandler configHandler, ImageMosaicEventHandlers eventHandler) {
        Utilities.ensureNonNull("config handler", configHandler);
        Utilities.ensureNonNull("event handler", eventHandler);
        this.configHandler = configHandler;
        this.eventHandler = eventHandler;
    }

    public boolean getStop() {
        return stop;
    }

    public void stop() {
        stop = true;
    }

    /** Create a transaction for being used in this walker */
    public void startTransaction() {
        if (transaction != null) {
            throw new IllegalStateException("Transaction already open!");
        }
        this.transaction = new DefaultTransaction("MosaicCreationTransaction" + System.nanoTime());
    }

    public void rollbackTransaction() throws IOException {
        transaction.rollback();
    }

    public void commitTransaction() throws IOException {
        transaction.commit();
    }

    public void closeTransaction() {
        transaction.close();
    }

    protected boolean checkStop() {
        if (getStop()) {
            eventHandler.fireEvent(
                    Level.INFO,
                    "Stopping requested at file  " + elementIndex + " of " + numElements + " files",
                    elementIndex * 100.0 / numElements);
            return false;
        }
        return true;
    }

    /** @return the elementIndex */
    public int getElementIndex() {
        return elementIndex;
    }

    /** @return the numElements */
    public int getNumElements() {
        return numElements;
    }

    /** @param elementIndex the elementIndex to set */
    public void setElementIndex(int elementIndex) {
        this.elementIndex = elementIndex;
    }

    /** @param numElements the numElements to set */
    public void setNumElements(int numElements) {
        this.numElements = numElements;
    }

    /**
     * Warn this walker that we skip the provided path
     *
     * @param path the path to the file to skip
     */
    public void skip(String path) {
        LOGGER.log(Level.INFO, "Unable to use path: " + path + " - skipping it.");
        elementIndex++;
    }
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.util.logging;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.OperationRegistry;
import javax.media.jai.util.ImagingListener;

final class LoggingImagingListener implements ImagingListener {
    @Override
    public boolean errorOccurred(
            String message, Throwable thrown, Object where, boolean isRetryable)
            throws RuntimeException {
        Logger log = Logging.getLogger(LoggingImagingListener.class);
        if (message.contains("Continuing in pure Java mode")) {
            log.log(Level.FINER, message, thrown);
            return false; // we are not trying to recover
        } else {
            log.log(Level.INFO, message, thrown);
            if (thrown instanceof RuntimeException && !(where instanceof OperationRegistry)) {
                throw (RuntimeException) thrown;
            } else {
                return false;
            }
        }
    }

    @Override
    public String toString() {
        return "LoggingImagingListener";
    }
}

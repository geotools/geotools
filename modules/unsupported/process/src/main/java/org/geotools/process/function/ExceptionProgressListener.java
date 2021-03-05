/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.function;

import java.util.ArrayList;
import java.util.List;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * A listener that will just keep track of exceptions
 *
 * @author Andrea Aime - GeoSolutions
 */
class ExceptionProgressListener implements ProgressListener {

    List<Throwable> exceptions = new ArrayList<>();

    @Override
    public void exceptionOccurred(Throwable exception) {
        exceptions.add(exception);
    }

    public List<Throwable> getExceptions() {
        return exceptions;
    }

    @Override
    public boolean isCanceled() {
        return !exceptions.isEmpty();
    }

    // all other methods we don't care about

    @Override
    public void complete() {
        // nothing to do here
    }

    @Override
    public void dispose() {
        // nothing to do here
    }

    public String getDescription() {
        return null;
    }

    @Override
    public float getProgress() {
        return 0;
    }

    @Override
    public InternationalString getTask() {
        return null;
    }

    @Override
    public void progress(float percent) {
        // nothing to do here
    }

    @Override
    public void setCanceled(boolean cancel) {
        // nothing to do here
    }

    public void setDescription(String description) {
        // nothing to do here
    }

    @Override
    public void setTask(InternationalString task) {
        // nothing to do here
    }

    @Override
    public void started() {
        // nothing to do here
    }

    @Override
    public void warningOccurred(String source, String location, String warning) {
        // nothing to do here
    }
}

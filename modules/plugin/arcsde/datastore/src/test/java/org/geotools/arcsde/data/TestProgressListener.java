/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.arcsde.data;

import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * An implementation of ProgressListener to use when testing.
 * <p>
 * This implementation is good about throwing illegal state exceptions and so forth.
 * 
 * @author Jody
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/test/java/org
 *         /geotools/arcsde/data/TestProgressListener.java $
 */
public class TestProgressListener implements ProgressListener {
    int progressCount;

    float progress;

    int taskCount;

    InternationalString task;

    boolean isCanceled;

    int exceptionCount;

    Throwable exception;

    boolean completed;

    private boolean started;

    @SuppressWarnings("unused")
    private String[] warning;

    public void reset() {
        progressCount = 0;
        progress = 0f;
        taskCount = 0;
        task = null;
        isCanceled = false;
        exceptionCount = 0;
        exception = null;
        completed = false;
        started = false;
        warning = null;
    }

    public void complete() {
        if (completed)
            throw new IllegalStateException("Cannot complete twice");
        progress = 100f;
        completed = true;
    }

    public void dispose() {
        reset();
    }

    public void exceptionOccurred(Throwable exception) {
        this.exception = exception;
    }

    public String getDescription() {
        return task == null ? null : task.toString();
    }

    public float getProgress() {
        return progress;
    }

    public InternationalString getTask() {
        return task;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void progress(float percent) {
        if (!started)
            throw new IllegalStateException("Cannot record progress unless started");
        if (completed)
            throw new IllegalStateException("Cannot record progress when completed");
        progress = percent;
    }

    public void setCanceled(boolean cancel) {
        if (!started)
            throw new IllegalStateException("Cannot canel unless started");
        if (completed)
            throw new IllegalStateException("Cannot cancel when completed");
        isCanceled = cancel;
    }

    public void setDescription(String description) {
        task = new SimpleInternationalString(description);
    }

    public void setTask(InternationalString task) {
        this.task = task;
    }

    public void started() {
        if (started)
            throw new IllegalStateException("Cannot start twice");
        this.started = true;
    }

    public void warningOccurred(String source, String location, String warning) {
        this.warning = new String[] { source, location, warning };
    }

}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.util;

import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * A default progress listener implementation suitable for subclassing.
 *
 * <p>This implementation supports cancelation and getting/setting the description. The default
 * implementations of the other methods do nothing.
 *
 * @since 2.2
 * @version $Id$
 */
public class NullProgressListener implements ProgressListener {
    /** Description of the undergoing action. */
    private String description;

    /** {@code true} if the action is canceled. */
    private boolean canceled = false;

    /** Creates a null progress listener with no description. */
    public NullProgressListener() {}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void started() {
        // do nothing
    }

    @Override
    public void progress(float percent) {
        // do nothing
    }

    @Override
    public float getProgress() {
        return 0;
    }

    @Override
    public void complete() {
        // do nothing
    }

    @Override
    public void dispose() {
        // do nothing
    }

    @Override
    public void setCanceled(boolean cancel) {
        this.canceled = cancel;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void warningOccurred(String source, String location, String warning) {
        // do nothing
    }

    @Override
    public void exceptionOccurred(Throwable exception) {
        // do nothing
    }

    @Override
    public InternationalString getTask() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTask(InternationalString task) {
        // do nothing
    }
}

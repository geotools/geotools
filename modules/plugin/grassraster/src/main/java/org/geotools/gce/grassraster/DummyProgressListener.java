/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster;

import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * A dummy progress listener.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class DummyProgressListener implements ProgressListener {

    @Override
    public void complete() {}

    @Override
    public void dispose() {}

    @Override
    public void exceptionOccurred(Throwable exception) {}

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
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void progress(float percent) {}

    @Override
    public void setCanceled(boolean cancel) {}

    public void setDescription(String description) {}

    @Override
    public void setTask(InternationalString task) {}

    @Override
    public void started() {}

    @Override
    public void warningOccurred(String source, String location, String warning) {}
}

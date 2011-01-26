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
 */
package org.geotools.util;

import org.opengis.util.InternationalString;

public class DelegateProgressListener implements org.opengis.util.ProgressListener {
    protected org.opengis.util.ProgressListener delegate;
    private InternationalString task;
    private boolean isCanceled;

    public DelegateProgressListener( org.opengis.util.ProgressListener progress ){
        if( progress == null ) progress = new NullProgressListener();
        this.delegate = progress;
    }

    public void started() {
        delegate.started();        
    }
    public void complete() {
        delegate.complete();
    }

    public void dispose() {
        delegate.dispose();
        delegate = null;       
    }

    public void exceptionOccurred( Throwable exception ) {
        delegate.exceptionOccurred( exception );
    }

    public String getDescription() {
        return delegate.getDescription();
    }

    public InternationalString getTask() {
        return delegate.getTask();
    }

    public boolean isCanceled() {
        return delegate.isCanceled();
    }

    public void progress( float progress) {
        delegate.progress( progress );
    }
    
    public float getProgress() {
        return delegate.getProgress();
    }
    
    public void setCanceled( boolean cancel) {
        delegate.setCanceled( cancel );
        
    }

    public void setDescription( String description ) {
        delegate.setDescription( description );
    }

    public void setTask( InternationalString task ) {
        delegate.setTask( task );
    }

    public void warningOccurred( String source, String location, String warning ){
        delegate.warningOccurred( source, location, warning );
    }
    
}

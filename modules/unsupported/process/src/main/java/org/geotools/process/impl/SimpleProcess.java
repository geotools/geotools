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
package org.geotools.process.impl;

import java.util.HashMap;
import java.util.Map;

import org.geotools.process.ProcessFactory;
import org.geotools.util.NullProgressListener;
import org.opengis.util.ProgressListener;

/**
 * Provide an implementation for a simple process (ie so quick and easy it
 * is not going to need to report progress as it goes).
 * 
 * @author gdavis
 *
 *
 * @source $URL$
 */
public abstract class SimpleProcess extends AbstractProcess {
    /** Can only run once... should not need to check this but we are being careful */
    private boolean started = false; 
    protected Map<String,Object> input;
    protected Map<String,Object> result;
    
    protected SimpleProcess( ProcessFactory factory ){
        super( factory );
    }
        
    final public Map<String,Object> execute( Map<String,Object> input, ProgressListener monitor ) {
        if (started) throw new IllegalStateException("Process can only be run once");
        started = true;
        
        if( monitor == null ) monitor = new NullProgressListener();
        try {
            if( monitor.isCanceled() ) return null; // respect isCanceled
            this.input = input;
            result = new HashMap<String,Object>();
            
            process(); 
            
            return result;            
        }
        catch( Throwable eek){
            monitor.exceptionOccurred( eek );
            return null;
        }
        finally {
            monitor.complete();            
        }
    }
    
    /**
     * Implement your own process here.
     * 
     * @throws Exception
     */
    public abstract void process() throws Exception;

    protected Object get(String key ){
        return input.get( key );
    }
}

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
package org.geotools.feature.collection;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * A feature iterator that completely delegates to a normal
 * Iterator, simply allowing Java 1.4 code to escape the caste (sic)
 * system.
 * <p>
 * This implementation checks the iterator to see if it implements
 * {@link Closeable} in order to allow for collections that
 * make use of system resources.
 * </p>
 * @author Jody Garnett, Refractions Research, Inc.
 *
 *
 * @source $URL$
 */
public class DelegateFeatureIterator<F extends Feature> implements FeatureIterator<F> {
	Iterator<F> delegate;
	/**
	 * Wrap the provided iterator up as a FeatureIterator.
	 * 
	 * @param iterator Iterator to be used as a delegate.
	 */
	public DelegateFeatureIterator( Iterator<F> iterator ){
		delegate = iterator;
	}
	
	/**
	 * Wrap the provided iterator up as a FeatureIterator.
	 * 
	 * @param iterator Iterator to be used as a delegate.
	 * @deprecated collection no longer used
	 */
	public DelegateFeatureIterator( FeatureCollection<? extends FeatureType, F> collection, Iterator<F> iterator ){
		delegate = iterator;
	}
	public boolean hasNext() {
		return delegate != null && delegate.hasNext();
	}
	public F next() throws NoSuchElementException {
		if( delegate == null ) throw new NoSuchElementException();
		return  delegate.next();
	}
	public void close() {
	    if( delegate instanceof Closeable){
	        try {
                    ((Closeable)delegate).close();
                } catch (IOException e) {
                    Logger log = Logger.getLogger(delegate.getClass().getPackage().toString() );
                    log.log(Level.FINER, e.getMessage(), e);
                }
	    }
	    delegate = null;		
	}
}

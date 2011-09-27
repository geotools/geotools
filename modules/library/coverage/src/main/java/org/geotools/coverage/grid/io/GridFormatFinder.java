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
package org.geotools.coverage.grid.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryRegistry;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;

/**
 * Enable programs to find all available grid format implementations.
 *
 * <p>
 * In order to be located by this finder datasources must provide an
 * implementation of the {@link GridFormatFactorySpi} interface.
 * </p>
 *
 * <p>
 * In addition to implementing this interface datasouces should have a services
 * file:<br/><code>META-INF/services/org.geotools.data.GridFormatFactorySpi</code>
 * </p>
 *
 * <p>
 * The file should contain a single line which gives the full name of the
 * implementing class.
 * </p>
 *
 * <p>
 * Example:<br/><code>org.geotools.data.mytype.MyTypeDataStoreFacotry</code>
 * </p>
 * @author Simone Giannecchini, GeoSolutions
 *
 * @source $URL$
 */
public final class GridFormatFinder {
	/**
	 * The service registry for this manager. Will be initialized only when
	 * first needed.
	 */
	private static FactoryRegistry registry;

	/**
	 * Do not allows any instantiation of this class.
	 */
	private GridFormatFinder() {
		// singleton
	}

	/**
	 * Finds all avalaible implementations of {@link GridFormatFactorySpi} which
	 * have registered using the services mechanism, and that have the
	 * appropriate libraries on the classpath.
	 *
	 * @return An unmodifiable {@link Set} of all discovered datastores which
	 *         have registered factories, and whose available method returns
	 *         true.
	 */
	public static synchronized Set<GridFormatFactorySpi> getAvailableFormats() {
		// get all GridFormatFactorySpi implementations
		scanForPlugins();
		final Iterator<GridFormatFactorySpi> it = getServiceRegistry().
			getServiceProviders(GridFormatFactorySpi.class, true);
		final Set<GridFormatFactorySpi> formats= new HashSet<GridFormatFactorySpi>();
		while (it.hasNext()) {
			final GridFormatFactorySpi spi = (GridFormatFactorySpi) it.next();
			if (spi.isAvailable())
				formats.add(spi);

		}
		return Collections.unmodifiableSet(formats);
	}

	/**
	 * Returns the service registry. The registry will be created the first time
	 * this method is invoked.
	 */
	private static FactoryRegistry getServiceRegistry() {
		assert Thread.holdsLock(GridFormatFinder.class);
		if (registry == null) {
			registry = new FactoryCreator(Arrays
					.asList(new Class<?>[] { GridFormatFactorySpi.class }));
		}
		return registry;
	}

	/**
	 * Scans for factory plug-ins on the application class path. This method is
	 * needed because the application class path can theoretically change, or
	 * additional plug-ins may become available. Rather than re-scanning the
	 * classpath on every invocation of the API, the class path is scanned
	 * automatically only on the first invocation. Clients can call this method
	 * to prompt a re-scan. Thus this method need only be invoked by
	 * sophisticated applications which dynamically make new plug-ins available
	 * at runtime.
	 */
	public static synchronized void scanForPlugins() {
		getServiceRegistry().scanForPlugins();

	}

	/**
	 * Returns an array with all available {@link GridFormatFactorySpi}
	 * implementations.
	 *
	 * <p>
	 * It can be used toget basic information about all the available
	 * {@link GridCoverage} plugins. Note that this method finds all the
	 * implemented plugins but returns only the availaible one.
	 *
	 * <p>
	 * A plugin could be implemented but not availaible due to missing
	 * dependencies.
	 *
	 *
	 * @return an array with all available {@link GridFormatFactorySpi}
	 *         implementations.
	 */
	public static Format[] getFormatArray() {
		final Set<GridFormatFactorySpi> formats = GridFormatFinder.getAvailableFormats();
		final List<Format> formatSet = new ArrayList<Format>(formats.size());
		for (Iterator<GridFormatFactorySpi> iter = formats.iterator(); iter.hasNext();) {
			final GridFormatFactorySpi element = (GridFormatFactorySpi) iter.next();
			formatSet.add(element.createFormat());
		}
		return (Format[]) formatSet.toArray(new Format[formatSet.size()]);
	}

	/**
	 * Returns all the {@link Format}s that can read the supplied {@link Object}
	 * o.
	 * 
	 * @param o
	 *            is the object to search a {@link Format} that is able to read
	 * @return an unmodifiable {@link Set} comprising all the {@link Format}
	 *         that can read the {@link Object} o.
	 */
     public static synchronized Set<AbstractGridFormat> findFormats(Object o) {
		return findFormats(o, GeoTools.getDefaultHints());
	}
     
 	/**
 	 * Returns all the {@link Format}s that can read the supplied
 	 * {@link Object} o.
 	 *
 	 * @param o
 	 *            is the object to search a {@link Format} that is able to read
	 * @param hints
	 *            the {@link Hints} to control the format search.            
 	 * @return an unmodifiable {@link Set} comprising all the {@link Format}
 	 *         that can read the {@link Object} o.
 	 */
      public static synchronized Set<AbstractGridFormat> findFormats(Object o, Hints hints) {
 		final Set<GridFormatFactorySpi> availaibleFormats = getAvailableFormats();
 		final Set<AbstractGridFormat> formats=new HashSet<AbstractGridFormat>();
 		final Iterator<GridFormatFactorySpi> it = availaibleFormats.iterator();
 		while (it.hasNext()) {
 			// get the factory
 			final GridFormatFactorySpi spi = (GridFormatFactorySpi) it.next();
 			// create a format for it
 			final AbstractGridFormat retVal = spi.createFormat();
 			// check if we can accept it
 			if (retVal instanceof AbstractGridFormat) {
 				if (((AbstractGridFormat) retVal).accepts(o,hints))
 					formats.add(retVal);
 			}

 		}

 		return Collections.unmodifiableSet(formats);
 	}     

	/**
	 * Returns a {@link Format} that is able to read a certain object. If no
	 * {@link Format} is able to read such an {@link Object} we return an
	 * {@link UnknownFormat} object.
	 *
	 * <p>
	 * It is worth to point out that this method will try to convert each format
	 * implementation to {@link AbstractGridFormat} because the original
	 * {@link Format} interface did not allow for an accept method hence we had
	 * to subclass the interface to add such method and we did so by the
	 * {@link AbstractGridFormat} abstract class.
	 *
	 * @param o
	 *            the object to check for acceptance.
	 * @return an {@link AbstractGridFormat} that has stated to accept this
	 *         {@link Object} o or <code>null</code> in no plugins was able to
	 *         accept it.
	 */
      public static synchronized AbstractGridFormat findFormat(Object o) {
		return findFormat(o, GeoTools.getDefaultHints());

	}
        
    	/**
    	 * Returns a {@link Format} that is able to read a certain object. If no
    	 * {@link Format} is able to read such an {@link Object} we return an
    	 * {@link UnknownFormat} object.
    	 *
    	 * <p>
    	 * It is worth to point out that this method will try to convert each format
    	 * implementation to {@link AbstractGridFormat} because the original
    	 * {@link Format} interface did not allow for an accept method hence we had
    	 * to subclass the interface to add such method and we did so by the
    	 * {@link AbstractGridFormat} abstract class.
    	 *
    	 * @param o
    	 *            the object to check for acceptance.
    	 * @param hints 
    	 * 			   the {@link Hints} to control the format search.
    	 * @return an {@link AbstractGridFormat} that has stated to accept this
    	 *         {@link Object} o or <code>null</code> in no plugins was able to
    	 *         accept it.
    	 */
         public static synchronized AbstractGridFormat findFormat(Object o, Hints hints) {
    		final Set<AbstractGridFormat> formats = findFormats(o,hints);
    		final Iterator<AbstractGridFormat> it = formats.iterator();
    		if (it.hasNext()){
    			return (AbstractGridFormat) it.next();
    		}
    		return new UnknownFormat();

    	}        
}

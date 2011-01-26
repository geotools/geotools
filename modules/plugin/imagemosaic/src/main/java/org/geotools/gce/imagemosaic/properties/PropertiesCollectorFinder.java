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
package org.geotools.gce.imagemosaic.properties;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryRegistry;

/**
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public final class PropertiesCollectorFinder {
	/**
	 * The service registry for this manager. Will be initialized only when
	 * first needed.
	 */
	private static FactoryRegistry registry;

	/**
	 * Do not allows any instantiation of this class.
	 */
	private PropertiesCollectorFinder() {
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
	public static synchronized Set<PropertiesCollectorSPI> getPropertiesCollectorSPI() {
		// get all GridFormatFactorySpi implementations
		scanForPlugins();
		final Iterator<PropertiesCollectorSPI> it = getServiceRegistry().getServiceProviders(PropertiesCollectorSPI.class, true);
		final Set<PropertiesCollectorSPI> collectors= new HashSet<PropertiesCollectorSPI>();
		while (it.hasNext()) {
			final PropertiesCollectorSPI spi = (PropertiesCollectorSPI) it.next();
			if (spi.isAvailable())
				collectors.add(spi);

		}
		return Collections.unmodifiableSet(collectors);
	}

	/**
	 * Returns the service registry. The registry will be created the first time
	 * this method is invoked.
	 */
	private static FactoryRegistry getServiceRegistry() {
		assert Thread.holdsLock(PropertiesCollectorFinder.class);
		if (registry == null) {
			registry = new FactoryCreator(Arrays.asList(new Class<?>[] { PropertiesCollectorSPI.class }));
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

//	/**
//	 * Returns an array with all available {@link GridFormatFactorySpi}
//	 * implementations.
//	 *
//	 * <p>
//	 * It can be used toget basic information about all the available
//	 * {@link GridCoverage} plugins. Note that this method finds all the
//	 * implemented plugins but returns only the availaible one.
//	 *
//	 * <p>
//	 * A plugin could be implemented but not availaible due to missing
//	 * dependencies.
//	 *
//	 *
//	 * @return an array with all available {@link GridFormatFactorySpi}
//	 *         implementations.
//	 */
//	public static Format[] getFormatArray() {
//		final Set<GridFormatFactorySpi> formats = PropertiesCollectorFinder.getAvailableFormats();
//		final List<Format> formatSet = new ArrayList<Format>(formats.size());
//		for (Iterator<GridFormatFactorySpi> iter = formats.iterator(); iter.hasNext();) {
//			final GridFormatFactorySpi element = (GridFormatFactorySpi) iter.next();
//			formatSet.add(element.createFormat());
//		}
//		return (Format[]) formatSet.toArray(new Format[formatSet.size()]);
//	}
//
//	/**
//	 * Returns all the {@link Format}s that can read the supplied
//	 * {@link Object} o.
//	 *
//	 * @param o
//	 *            is the object to search a {@link Format} that is able to read
//	 * @return an unmodifiable {@link Set} comprising all the {@link Format}
//	 *         that can read the {@link Object} o.
//	 */
//        public static synchronized Set<PropertiesCollector> findPropertiesCollectors(final URL url) {
//		final Set<PropertiesCollectorSPI> availaibleCollectors =getPropertiesCollectorSPI();
//		final Set<PropertiesCollector> collectors=new HashSet<PropertiesCollector>();
//		final Iterator<PropertiesCollectorSPI> it = availaibleCollectors.iterator();
//		while (it.hasNext()) {
//			// get the factory
//			final PropertiesCollectorSPI spi = (PropertiesCollectorSPI) it.next();
//			// create a format for it
//			final PropertiesCollector retVal = spi.createFormat();
//			// check if we can accept it
//			if (retVal instanceof AbstractGridFormat) {
//				if (((AbstractGridFormat) retVal).accepts(o))
//					collectors.add(retVal);
//			}
//
//		}
//
//		return Collections.unmodifiableSet(collectors);
//	}
//
//	/**
//	 * Returns a {@link Format} that is able to read a certain object. If no
//	 * {@link Format} is able to read such an {@link Object} we return an
//	 * {@link UnknownFormat} object.
//	 *
//	 * <p>
//	 * It is worth to point out that this method will try to convert each format
//	 * implementation to {@link AbstractGridFormat} because the original
//	 * {@link Format} interface did not allow for an accept method hence we had
//	 * to subclass the interface to add such method and we did so by the
//	 * {@link AbstractGridFormat} abstract class.
//	 *
//	 * @param o
//	 *            the object to check for acceptance.
//	 * @return an {@link AbstractGridFormat} that has stated to accept this
//	 *         {@link Object} o or <code>null</code> in no plugins was able to
//	 *         accept it.
//	 */
//        public static synchronized AbstractGridFormat findFormat(Object o) {
//		final Set<AbstractGridFormat> formats = findPropertiesCollectors(o);
//		final Iterator<AbstractGridFormat> it = formats.iterator();
//		if (it.hasNext()){
//			return (AbstractGridFormat) it.next();
//		}
//		return new UnknownFormat();
//
//	}
}

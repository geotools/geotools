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
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/imagemosaic/src/main/java/org/geotools/gce/imagemosaic/properties/PropertiesCollectorFinder.java $
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
}

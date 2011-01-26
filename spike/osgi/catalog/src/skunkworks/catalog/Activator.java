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
package skunkworks.catalog;

import java.util.Properties;

import org.geotools.gtcatalog.Catalog;
import org.geotools.gtcatalog.ServiceFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		//register the catalog and service factory
		Properties props = new Properties();
		props.put("Language", "English");
		
		Catalog catalog = new SkunkCatalog();
		ServiceFactory factory = new SkunkServiceFactory(catalog);
		
		context.registerService(Catalog.class.getName(),catalog,props);
		context.registerService(ServiceFactory.class.getName(),factory,props);
	}

	public void stop(BundleContext context) throws Exception {
		//do nothing
	}
	
}

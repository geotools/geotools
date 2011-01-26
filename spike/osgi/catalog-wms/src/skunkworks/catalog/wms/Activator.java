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
package skunkworks.catalog.wms;

import org.geotools.gtcatalog.ServiceFactory;
import org.geotools.gtcatalog.wms.WMSServiceExtension;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import skunkworks.catalog.SkunkServiceFactory;

public class Activator implements BundleActivator {

	WMSServiceExtension ext;

	public void start(BundleContext context) throws Exception {
		
		//get the catalog service and register the service extension
		ext = new WMSServiceExtension();
		
		ServiceReference ref = 
			context.getServiceReference(ServiceFactory.class.getName());
		SkunkServiceFactory factory = (SkunkServiceFactory) context.getService(ref);
		factory.addServiceExtension(ext);
	}

	public void stop(BundleContext context) throws Exception {
		//unregister the service extension
		ServiceReference ref = 
			context.getServiceReference(ServiceFactory.class.getName());
		SkunkServiceFactory factory = (SkunkServiceFactory) context.getService(ref);
		factory.removeServiceExtension(ext);
	}

}

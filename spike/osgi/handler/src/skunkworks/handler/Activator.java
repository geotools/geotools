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
package skunkworks.handler;

import java.util.Properties;

import org.geotools.gtcatalog.Catalog;
import org.geotools.gtcatalog.ServiceFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		//get a reference to the service factory + catalog
		Catalog catalog = (Catalog) context
			.getService(context.getServiceReference(Catalog.class.getName()));
		ServiceFactory factory = (ServiceFactory)context
			.getService(context.getServiceReference(ServiceFactory.class.getName()));
		
		
		HttpService service = (HttpService)context
			.getService(context.getServiceReference(HttpService.class.getName()));
	
		service.registerServlet("/Add",new AddServiceHandler(catalog,factory),new Properties(),null);
		service.registerServlet("/List",new ListServicesHandler(catalog),new Properties(),null);
	}

	public void stop(BundleContext context) throws Exception {
		HttpService service = (HttpService)context
			.getService(context.getServiceReference(HttpService.class.getName()));

		service.unregister("/Add");
		service.unregister("/List");
	}

}

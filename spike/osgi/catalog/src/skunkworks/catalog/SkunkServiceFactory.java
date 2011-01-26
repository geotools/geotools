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


import java.util.ArrayList;
import java.util.List;

import org.geotools.gtcatalog.Catalog;
import org.geotools.gtcatalog.ServiceExtension;
import org.geotools.gtcatalog.ServiceFactory;
import org.geotools.gtcatalog.defaults.DefaultServiceFactory;

public class SkunkServiceFactory extends DefaultServiceFactory 
	implements ServiceFactory {

	List extensions = new ArrayList();
	
	public SkunkServiceFactory(Catalog catalog) {
		super(catalog);
	}
	
	protected List getServiceExtensions() {
		return extensions;
	}

	public void addServiceExtension(ServiceExtension ext) {
		extensions.add(ext);
	}
	
	public void removeServiceExtension(ServiceExtension ext) {
		extensions.remove(ext);
	}
	
}

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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geotools.gtcatalog.Catalog;
import org.geotools.gtcatalog.CatalogInfo;
import org.geotools.gtcatalog.ResolveChangeListener;
import org.geotools.gtcatalog.Service;
import org.geotools.util.ProgressListener;

import com.vividsolutions.jts.geom.Envelope;

public class SkunkCatalog extends Catalog {

	List services = new ArrayList();
	CatalogInfo info;
	
	public void add(Service service) throws UnsupportedOperationException {
		services.add(service);
		
	}

	public void remove(Service service) throws UnsupportedOperationException {
		services.remove(service);
		
	}

	public void replace(URI id, Service service) throws UnsupportedOperationException {
		if (id == null) 
			return;
		
		for (int i = 0; i < services.size(); i++) {
			Service replacee = (Service)services.get(i);
			if (id.equals(replacee.getIdentifier())) {
				services.set(i,service);
			}
		}
	}

	public Object resolve(Class adaptee, ProgressListener monitor) throws IOException {
		if (adaptee == null)
			return null;
		
		if (adaptee.isAssignableFrom(CatalogInfo.class)) {
			if (info == null) {
				synchronized (this) {
					if (info == null) {
						try {
							info = new CatalogInfo(
								"skunk","Skunkworks",
								new URI("http://www.skunkworks.com"),new String[]{}
							);
						} 
						catch (URISyntaxException e) {}
					}
				}
			}
			return info;
		}
		if (adaptee.isAssignableFrom(List.class)) {
			return services;
		}
		
		return null;
	}

	public List find(URI id, ProgressListener monitor) {
		return findService(id,monitor);
	}

	public List findService(URI query, ProgressListener monitor) {
		if (query == null) 
			return Collections.EMPTY_LIST;
		
		ArrayList matched = new ArrayList();
		for (int i = 0; i < services.size(); i++) {
			Service service = (Service)services.get(i);
			if (query.equals(service.getIdentifier())) {
				matched.add(service);
			}
		}
		
		return matched;
	}

	public List search(String pattern, Envelope bbox, ProgressListener monitor) throws IOException {
		return Collections.EMPTY_LIST;
	}

	public void addCatalogListener(ResolveChangeListener listener) {
	}

	public void removeCatalogListener(ResolveChangeListener listener) {
	}

	public boolean canResolve(Class adaptee) {
		if (adaptee == null) 
			return false;
		
		return adaptee.isAssignableFrom(List.class) || 
			adaptee.isAssignableFrom(CatalogInfo.class);
	}

	public List members(ProgressListener monitor) throws IOException {
		return services;
	}

	public Status getStatus() {
		return Status.CONNECTED;
	}

	public Throwable getMessage() {
		return null;
	}

	public URI getIdentifier() {
		try {
			return new URI("http://www.skunkworks.com");
		} 
		catch (URISyntaxException e) {
			return null;
		}
	}
	
	
}

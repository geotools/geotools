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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geotools.gtcatalog.Catalog;
import org.geotools.gtcatalog.Service;
import org.geotools.gtcatalog.ServiceFactory;

public class AddServiceHandler extends HttpServlet {

	Catalog catalog;
	ServiceFactory factory;

	public AddServiceHandler(Catalog catalog, ServiceFactory factory) {
		this.catalog = catalog;
		this.factory = factory;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		
		doAdd(request,response);
	}

	protected void doAdd(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		
		//pull out the url of the request
		String query = request.getQueryString();
		
		List services = null;
		try {
			services = factory.aquire(new URI(query));
		} 
		catch (URISyntaxException e) {}
		
		ServletOutputStream out = response.getOutputStream();
		
		if (services != null && !services.isEmpty()) {
			for (Iterator itr = services.iterator(); itr.hasNext();) {
				Service service = (Service) itr.next();
				
				//force conection to service to make sure its a valid handle
				try {
					service.members(null);
			
					catalog.add(service);
					out.println("Added " + service.getIdentifier());
				}
				catch(Throwable t) {
					//ok, continue
				}
			}
		}
		else {
			out.print("Service could not be added");
		}
		
	}
}

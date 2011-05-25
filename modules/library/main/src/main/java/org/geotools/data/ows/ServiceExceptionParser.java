/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ows;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.geotools.ows.ServiceException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Utility class that will parse ServiceExceptions out of an inputStream.
 * 
 * @author rgould
 *
 *
 *
 * @source $URL$
 */
public class ServiceExceptionParser {

	/**
	 * Tries to read a ServiceExceptionReport from the input stream, and 
	 * construct a chain of ServiceExceptions.
	 * 
	 * ServiceExceptions beyond the first can be accessed using 
	 * ServiceException.next();
	 * 
	 * @param inputStream
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static ServiceException parse(InputStream inputStream) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(inputStream);
		
		Element root = document.getRootElement();
		List serviceExceptions = root.getChildren("ServiceException");
				
		/*
		 * ServiceExceptions with codes get bumped to the top of the list.
		 */
		List codes = new ArrayList();
		List noCodes = new ArrayList();
		for (int i = 0; i < serviceExceptions.size(); i++) {
			Element element = (Element) serviceExceptions.get(i);
			ServiceException exception = parseSE(element);
			if (exception.getCode() != null && exception.getCode().length() != 0 ) {
				codes.add(exception);
			} else {
				noCodes.add(exception);
			}
		}
		
		/*
		 * Now chain them.
		 */
		ServiceException firstException = null;
		ServiceException recentException = null;
		for (int i = 0; i < codes.size(); i++) {
			ServiceException exception = (ServiceException) codes.get(i);
			if (firstException == null) {
				firstException = exception;
				recentException = exception;
			} else {
				recentException.setNext(exception);
				recentException = exception;
			}
		}
		codes = null;
		for (int i = 0; i < noCodes.size(); i++) {
			ServiceException exception = (ServiceException) noCodes.get(i);
			if (firstException == null) {
				firstException = exception;
				recentException = exception;
			} else {
				recentException.setNext(exception);
				recentException = exception;
			}
		}
		noCodes = null;
		
		return firstException;		
	}

	private static ServiceException parseSE(Element element) {
		String errorMessage = element.getText();
		String code = element.getAttributeValue("code");
		
		return new ServiceException(errorMessage, code);
	}

}

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
 */
package org.geotools.graph.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ParseErrorHandler 
  extends DefaultHandler implements Serializable {
 
  ArrayList m_parseErrors = null;
  
  public ParseErrorHandler() {
    super();       
    m_parseErrors = new ArrayList();
  }
  
  public void error(SAXParseException e) throws SAXException {
    super.error(e);
    m_parseErrors.add(e);
  }
  
  public void fatalError(SAXParseException e) throws SAXException {
	  super.fatalError(e);
    m_parseErrors.add(e);
  }
  
  public void reset() {
    m_parseErrors.clear();    
  }

  public boolean noErrors() {
    return(m_parseErrors.size() == 0);    
  }
  
  public void printErrors() {
    for(Iterator itr = m_parseErrors.iterator(); itr.hasNext();) {
      SAXParseException e = (SAXParseException)itr.next();
      System.out.println(e.getMessage());           
    }
  }
  
  public String toString() {
    StringBuffer out = new StringBuffer();
    for (Iterator itr = m_parseErrors.iterator(); itr.hasNext();) {
      SAXParseException e = (SAXParseException)itr.next();
      out.append(e.getMessage());               
    }
    
    return(out.toString());
  }
}

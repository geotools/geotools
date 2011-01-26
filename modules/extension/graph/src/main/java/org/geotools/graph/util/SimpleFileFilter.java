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

import java.io.File;
import java.io.Serializable;

import javax.swing.filechooser.FileFilter;

public class SimpleFileFilter extends FileFilter implements Serializable {

  private String m_ext = null;
  private String m_desc = null;
  
  public SimpleFileFilter() {}
  
  public SimpleFileFilter(String ext, String desc) {
    this.m_ext = ext;
    this.m_desc = desc;  
  }
  
  public boolean accept(File f) {
    if (f.isDirectory()) return(true);
    String path = f.getAbsolutePath();
    if (path.length() < m_ext.length() + 1) return(false);
    return(path.substring(path.length()-4)).equals("." + m_ext);
  }

  public String getExtension() {
    return(m_ext);  
  }
  
  public String getDescription() {
    return(m_desc);
  }
  
  public boolean equals(Object o) {
    if (o == null) return(false);
    if (o instanceof SimpleFileFilter) {
      SimpleFileFilter other = (SimpleFileFilter)o;
      return(m_ext.equals(other.m_ext));  
    }
    return(false);    
  }
}

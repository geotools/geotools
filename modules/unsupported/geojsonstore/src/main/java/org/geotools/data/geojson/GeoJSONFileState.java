/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geojson;

import java.io.File;
import java.net.URL;
import org.geotools.util.URLs;

/** @author ian */
public class GeoJSONFileState {
  private File file;
  private URL url;
  /**
   * @param url
   */
  public GeoJSONFileState(URL url) {
    this.url = url;
  }
  /** */
  public GeoJSONFileState(File f) {
    file = f;
  }
  /** @return the file */
  public File getFile() {
    if(file!=null) {
      return file;
    }
    return URLs.urlToFile(url);
  }
  /** @param file the file to set */
  public void setFile(File file) {
    this.file = file;
  }
  /** @return the url */
  public URL getUrl() {
    if (url != null) {
      return url;
    } else {
      return URLs.fileToUrl(file);
    }
  }
  /** @param url the url to set */
  public void setUrl(URL url) {
    this.url = url;
  }
}

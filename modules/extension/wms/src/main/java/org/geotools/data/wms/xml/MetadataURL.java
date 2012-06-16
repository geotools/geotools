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
package org.geotools.data.wms.xml;

import java.net.URL;

/**
 * A Map Server may use zero or more MetadataURL elements to offer detailed,
 *   standardized metadata about the data underneath a particular layer. The type
 *   attribute indicates the standard to which the metadata complies.  Two types
 *  are defined at present: 'TC211' = ISO TC211 19115; 'FGDC' = FGDC CSDGM.  The
 *   format element indicates how the metadata is structured. -->
 *   <!ELEMENT MetadataURL (Format, OnlineResource) >
 *   <!ATTLIST MetadataURL
 *           type ( TC211 | FGDC ) #REQUIRED>
 * @author Meine Toonen meinetoonen@b3partners.nl
 */
public class MetadataURL {
    
    protected URL url;
    protected String type;
    protected String format;

    public MetadataURL(URL url, String type, String format) {
        this.url = url;
        this.type = type;
        this.format = format;
    }
    
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "MetadataURL{" + "url=" + url + ", type=" + type + '}';
    }
}

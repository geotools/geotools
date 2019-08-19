/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wms.xml;

import java.net.URL;

/**
 * This class is used for representing the Attribution element inside a Layer when parsing a
 * GetCapabilities response.
 *
 * @author Nicola Lagomarsini GeoSolutions S.A.S.
 */
public class Attribution {

    /** Attribution Title */
    private String title;

    /** Attribution Link */
    private URL onlineResource;

    /** Attribution Logo */
    private LogoURL logoURL;

    public Attribution(String title, URL onlineResource, LogoURL logoURL) {
        super();
        this.title = title;
        this.onlineResource = onlineResource;
        this.logoURL = logoURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getOnlineResource() {
        return onlineResource;
    }

    public void setOnlineResource(URL onlineResource) {
        this.onlineResource = onlineResource;
    }

    public LogoURL getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(LogoURL logoURL) {
        this.logoURL = logoURL;
    }

    @Override
    public String toString() {
        return "Attribution [title="
                + title
                + ", onlineResource="
                + onlineResource
                + ", logoURL="
                + logoURL
                + "]";
    }
}

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
 * This class is used for representing the LogoURL element inside the Attribution element of a Layer
 * when parsing a GetCapabilities response.
 *
 * @author Nicola Lagomarsini GeoSolutions S.A.S.
 */
public class LogoURL {

    /** Format of the Logo */
    private String format;

    /** Logo URL */
    private URL onlineResource;

    /** Logo width */
    private int width;

    /** Logo height */
    private int height;

    public LogoURL(String format, URL onlineResource, int width, int height) {
        this.format = format;
        this.onlineResource = onlineResource;
        this.width = width;
        this.height = height;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public URL getOnlineResource() {
        return onlineResource;
    }

    public void setOnlineResource(URL onlineResource) {
        this.onlineResource = onlineResource;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "LogoURL [format="
                + format
                + ", onlineResource="
                + onlineResource
                + ", width="
                + width
                + ", height="
                + height
                + "]";
    }
}

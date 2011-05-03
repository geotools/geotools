/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagecollection;

import javax.imageio.spi.ImageReaderSpi;

/**
 * A simple property container, which store basic image properties such as
 * width, height, absolutePath of the image, ...
 * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 * 
 */
public class ImageProperty {
    public int width;

    public int height;

    public int numOverviews;

    public String path;

    /** 
     * In case the file has been modified, we need to update the main parameters. This flag
     * takes note of the last Modified time value of the underlying file.
     */
    public long lastModifiedTime;

    /** 
     * the last time at which the file has been checked.
     */
    public long lastCheckTime;

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

    public int getNumOverviews() {
        return numOverviews;
    }

    public void setNumOverviews(int numOverviews) {
        this.numOverviews = numOverviews;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLastModified() {
        return lastModifiedTime;
    }

    public void setLastModified(long lastModified) {
        this.lastModifiedTime = lastModified;
    }

    public long getLastCheck() {
        return lastCheckTime;
    }

    public void setLastCheck(long lastCheck) {
        this.lastCheckTime = lastCheck;
    }

    public ImageReaderSpi getSpi() {
        return spi;
    }

    public void setSpi(ImageReaderSpi spi) {
        this.spi = spi;
    }

    public ImageReaderSpi spi;

    public ImageProperty() {
    }

    /**
     * 
     * @param path
     * @param width
     * @param height
     * @param numOverviews
     * @param spi
     * @param lastModifiedTime
     */
    public ImageProperty(final String path, final int width, final int height, 
            final int numOverviews, final ImageReaderSpi spi, final long lastModifiedTime) {
        this.width = width;
        this.height = height;
        this.numOverviews = numOverviews;
        this.path = path;
        this.spi = spi;
        this.lastModifiedTime = lastModifiedTime;
        this.lastCheckTime = System.currentTimeMillis();
    }
}
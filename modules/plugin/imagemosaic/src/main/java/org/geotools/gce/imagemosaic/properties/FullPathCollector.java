/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.gce.imagemosaic.properties;

/** Marker interface for collectors that can be made to inspect the full file path */
public interface FullPathCollector {
    boolean isFullPath();

    void setFullPath(boolean fullPath);
}

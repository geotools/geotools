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
 *
 */

package org.geotools.tpk;

import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A TPK file contains one or more zoom levels This interface is an abstraction of a zoom level
 * providing the coverage values of the zoom level (minColumn, minRow, maxColumn, maxRow) and the
 * ability to retrieve tiles for a particular coverage in that zoom level.
 *
 * <p>A zoom level can consist of multiple bundle/bundle index files; the zoomLevel interface
 * abstracts this away from the caller.
 */
public interface TPKZoomLevel {

    // on every subsequent use (after creation) the ZipFile and ZipEntry map must
    // be injected into each TPKZoomLevel concrete instance, this allows us to close
    // the zip file and release its resources between renders
    void setTPKandEntryMap(ZipFile theTPK, Map<String, ZipEntry> zipEntryMap);

    // this is where the rubber meets the road
    List<TPKTile> getTiles(long top, long bottom, long left, long right, String format);

    // for low memory footprint release resources after each render!
    void releaseResources();

    // simple getters
    long getZoomLevel(); // the zoom level

    long getMinRow(); // minimum row in coverage at this zoom level

    long getMaxRow(); // maximum row in coverage at this zoom level

    long getMinColumn(); // minimum column in coverage at this zoom level

    long getMaxColumn(); // maximum column in coverage at this zoom level
}

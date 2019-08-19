/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io.footprint;

/**
 * Constructs a live FootprintLoader
 *
 * <p>In addition to implementing this interface, this service file should be defined:
 *
 * <p><code>META-INF/services/org.geotools.coverage.grid.io.footprint.FootprintLoaderSpi</code>
 *
 * <p>The file should contain a single line which gives the full name of the implementing class.
 *
 * <p>example:<br>
 * <code>e.g.
 * org.geotools.coverage.grid.io.footprint.WKBLoaderSpi</code>
 *
 * <p>The factories are never called directly by users, instead the FootprintLoaderFinder class is
 * used.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public interface FootprintLoaderSpi {

    FootprintLoader createLoader();
}

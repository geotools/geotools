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
package org.geotools.gce.imagemosaic;

/**
 * Policy for excess granule removal
 *
 * @author Andrea Aime
 */
public enum ExcessGranulePolicy {

    /** No excess granule removal is performed. This is the default */
    NONE,
    /** Excess granule removal based on image ROI, performed in raster space */
    ROI
    // TODO: we could add a policy based on vector footprint in model space, if there is one that's
    // accurate enough
}

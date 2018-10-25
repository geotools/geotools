/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import org.opengis.filter.Filter;

/**
 * Policy to be used while running a removal on {@link GranuleStore#removeGranules(Filter,
 * org.geotools.util.factory.Hints)}. Needs to be specified as part of the {@link
 * org.geotools.util.factory.Hints}
 */
public enum GranuleRemovalPolicy {

    /**
     * Do not remove granule data nor metadata, simply un-register granules from the {@link
     * GranuleStore}
     */
    NONE,

    /** Remove metadata, but not data */
    METADATA,

    /** Remove both data and metadata */
    ALL
}

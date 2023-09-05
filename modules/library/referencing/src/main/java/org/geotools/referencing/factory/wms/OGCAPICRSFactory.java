/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.wms;

import org.geotools.api.metadata.citation.Citation;
import org.geotools.metadata.iso.citation.Citations;

/**
 * Similar to the OGC CRS factory, but with a different authority name, designed to support URNs
 * coming from the OGC API - Features - Part 1: Core specification (e.g.,
 * "http://www.opengis.net/def/crs/OGC/1.3/CRS84").
 */
public class OGCAPICRSFactory extends WebCRSFactory {

    @Override
    public Citation getAuthority() {
        return Citations.OGC;
    }
}

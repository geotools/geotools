/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.netcdf.index;

import ucar.ma2.Array;

/**
 *
 * @author Yancy Matherne - Geocent
 */
public class NearestNeighborBinarySearch implements IndexingStrategy {

    // TODO: Implement using Jon Blower's NCWMS code for reference.
    // http://www.resc.rdg.ac.uk/trac/ncWMS/browser/trunk/src/java/uk/ac/rdg/resc/edal/coverage/grid/impl/ReferenceableAxisImpl.java?rev=836
    public int getCoordinateIndex(Array coordArray, double desiredCoordinate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

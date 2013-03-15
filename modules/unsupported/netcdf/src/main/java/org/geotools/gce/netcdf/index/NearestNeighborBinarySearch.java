/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
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

package org.geotools.gml.stream;

import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public interface AxisOrderInversionChecker {
    boolean invertAxisNeeded(CRS.AxisOrder axisOrder, CoordinateReferenceSystem crs);
}

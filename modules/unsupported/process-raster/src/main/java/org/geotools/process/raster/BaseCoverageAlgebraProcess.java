/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2012, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2007 TOPP - www.openplans.org.
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
package org.geotools.process.raster;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import org.geotools.api.coverage.grid.GridEnvelope;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.process.ProcessException;
import org.geotools.referencing.CRS;

/** @author Daniele Romagnoli, GeoSolutions */
public class BaseCoverageAlgebraProcess {

    static final String MISMATCHING_ENVELOPE_MESSAGE = "coverageA and coverageB should share the same Envelope";

    static final String MISMATCHING_GRID_MESSAGE = "coverageA and coverageB should have the same gridRange";

    static final String MISMATCHING_CRS_MESSAGE =
            "coverageA and coverageB should share the same CoordinateReferenceSystem";

    private BaseCoverageAlgebraProcess() {}

    public static void checkCompatibleCoverages(GridCoverage2D coverageA, GridCoverage2D coverageB)
            throws ProcessException {
        if (coverageA == null || coverageB == null) {
            String coveragesNull =
                    coverageA == null ? coverageB == null ? "coverageA and coverageB" : "coverageA" : "coverageB";
            throw new ProcessException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, coveragesNull));
        }

        //
        // checking same CRS
        //
        CoordinateReferenceSystem crsA = coverageA.getCoordinateReferenceSystem();
        CoordinateReferenceSystem crsB = coverageB.getCoordinateReferenceSystem();
        if (!CRS.equalsIgnoreMetadata(crsA, crsB)) {
            MathTransform mathTransform = null;
            try {
                mathTransform = CRS.findMathTransform(crsA, crsB);
            } catch (FactoryException e) {
                throw new ProcessException(
                        "Exceptions occurred while looking for a mathTransform between the 2 coverage's CRSs", e);
            }
            if (mathTransform != null && !mathTransform.isIdentity()) {
                throw new ProcessException(MISMATCHING_CRS_MESSAGE);
            }
        }

        //
        // checking same Envelope and grid range
        //
        Bounds envA = coverageA.getEnvelope();
        Bounds envB = coverageB.getEnvelope();
        if (!envA.equals(envB)) {
            throw new ProcessException(MISMATCHING_ENVELOPE_MESSAGE);
        }

        GridEnvelope gridRangeA = coverageA.getGridGeometry().getGridRange();
        GridEnvelope gridRangeB = coverageA.getGridGeometry().getGridRange();
        if (gridRangeA.getSpan(0) != gridRangeB.getSpan(0) || gridRangeA.getSpan(1) != gridRangeB.getSpan(1)) {
            throw new ProcessException(MISMATCHING_GRID_MESSAGE);
        }
    }

    /** Utility method for ensuring that all the Input Coverages have the same CRS */
    public static void checkCompatibleCoveragesForMerge(Collection<GridCoverage2D> coverages) throws ProcessException {
        if (coverages == null || coverages.isEmpty()) {
            throw new ProcessException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, "Input coverage List"));
        }

        //
        // checking same CRS
        //

        // CRS which must be equal for all
        CoordinateReferenceSystem crs = null;

        // Iterator on all the coverages
        Iterator<GridCoverage2D> it = coverages.iterator();

        while (it.hasNext()) {
            // Selection of the coverage
            GridCoverage2D coverage = it.next();
            // Get the CRS associated
            if (crs == null) {
                crs = coverage.getCoordinateReferenceSystem();
            } else {
                CoordinateReferenceSystem crs1 = coverage.getCoordinateReferenceSystem();
                // Check that the CRS are the same
                checkCompatibleCRS(crs, crs1);
            }
        }
    }

    /** Utility method for checking if two CRS are equals */
    public static void checkCompatibleCRS(CoordinateReferenceSystem crsA, CoordinateReferenceSystem crsB) {
        // check if they are equal
        if (!CRS.equalsIgnoreMetadata(crsA, crsB)) {
            MathTransform mathTransform = null;
            try {
                mathTransform = CRS.findMathTransform(crsA, crsB);
            } catch (FactoryException e) {
                throw new ProcessException(
                        "Exceptions occurred while looking for a mathTransform between the coverage's CRSs", e);
            }
            // Check if their transformation is an identity
            if (mathTransform != null && !mathTransform.isIdentity()) {
                throw new ProcessException(MISMATCHING_CRS_MESSAGE);
            }
        }
    }
}

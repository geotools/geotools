/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.projection;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.util.Utilities;
import org.opengis.parameter.*;
import org.opengis.referencing.operation.ConicProjection;
import org.opengis.referencing.operation.MathTransform;
import si.uom.NonSI;

/**
 * A specialized version
 *
 * @author Martin Desruisseaux
 * @author Rueben Schulz
 * @version $Id$
 * @see org.geotools.referencing.operation.DefaultMathTransformFactory
 * @since 2.2
 */
public class LambertConformalEsriProvider extends MapProjection.AbstractProvider {

    /**
     * Override of the std parallel 1 as we downgrade from 2sp to 1sp when the two std parallels are
     * equal
     */
    public static final ParameterDescriptor STANDARD_PARALLEL_1 =
            createOptionalDescriptor(
                    new NamedIdentifier[] {
                        new NamedIdentifier(Citations.OGC, "standard_parallel_1"),
                        new NamedIdentifier(Citations.EPSG, "Latitude of 1st standard parallel"),
                        new NamedIdentifier(Citations.ESRI, "Standard_Parallel_1"),
                        new NamedIdentifier(Citations.ESRI, "standard_parallel_1"),
                        new NamedIdentifier(Citations.GEOTIFF, "StdParallel1")
                    },
                    -90,
                    90,
                    NonSI.DEGREE_ANGLE);

    /** The parameters group. */
    static final ParameterDescriptorGroup PARAMETERS =
            createDescriptorGroup(
                    new NamedIdentifier[] {
                        new NamedIdentifier(Citations.ESRI, "Lambert_Conformal_Conic"),
                        new NamedIdentifier(Citations.ESRI, "Lambert_Conformal_Conic_2SP")
                    },
                    new ParameterDescriptor[] {
                        SEMI_MAJOR, SEMI_MINOR,
                        CENTRAL_MERIDIAN, LATITUDE_OF_ORIGIN,
                        STANDARD_PARALLEL_1, STANDARD_PARALLEL_2,
                        FALSE_EASTING, FALSE_NORTHING,
                        SCALE_FACTOR // This last parameter is for ESRI compatibility
                    });

    /** Constructs a new provider. */
    public LambertConformalEsriProvider() {
        super(PARAMETERS);
    }

    /** Returns the operation type for this map projection. */
    @Override
    public Class<ConicProjection> getOperationType() {
        return ConicProjection.class;
    }

    /**
     * Creates a transform from the specified group of parameter values.
     *
     * @param parameters The group of parameter values.
     * @return The created math transform.
     * @throws ParameterNotFoundException if a required parameter was not found.
     */
    protected MathTransform createMathTransform(final ParameterValueGroup parameters)
            throws ParameterNotFoundException {
        boolean hasStdParallel1 = getParameter(STANDARD_PARALLEL_1, parameters) != null;
        double stdParallel1 = doubleValue(STANDARD_PARALLEL_1, parameters);
        boolean hasStdParallel2 = getParameter(STANDARD_PARALLEL_2, parameters) != null;
        double stdParallel2 = doubleValue(STANDARD_PARALLEL_2, parameters);
        boolean hasLatitudeOfOrigin = getParameter(LATITUDE_OF_ORIGIN, parameters) != null;
        double latitudeOfOrigin = doubleValue(LATITUDE_OF_ORIGIN, parameters);

        if (!hasStdParallel1 && !hasStdParallel2 && hasLatitudeOfOrigin) {
            // handle the ESRI 1SP case
            return new LambertConformal1SP(parameters);
        } else if (hasStdParallel1
                && hasStdParallel2
                && hasLatitudeOfOrigin
                && Utilities.equals(stdParallel1, stdParallel2)
                && Utilities.equals(stdParallel1, latitudeOfOrigin)) {
            // handle the ESRI 1SP case
            return new LambertConformal1SP(parameters);
        } else if (!hasStdParallel2
                && hasStdParallel1
                && Utilities.equals(stdParallel1, latitudeOfOrigin)) {
            // handle the ESRI 1SP case
            return new LambertConformal1SP(parameters);
        } else {
            // switch sp1 and sp2 so that we get a consistent ordering, this allows to recognize
            // two Lambert conformal with the same standard parallels declared in opposite order
            ParameterValue<Double> sp1 = getParameter(STANDARD_PARALLEL_1, parameters);
            ParameterValue<Double> sp2 = getParameter(STANDARD_PARALLEL_2, parameters);
            if (sp1 != null && sp2 != null) {
                if (sp1.doubleValue() < sp2.doubleValue()) {
                    final double temp = sp1.doubleValue();
                    sp1.setValue(sp2.doubleValue());
                    sp2.setValue(temp);
                }
            }

            return new LambertConformal2SP(parameters);
        }
    }
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

// J2SE dependencies
import java.util.Collections;

// OpenGIS dependencies
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.crs.ProjectedCRS;

// Geotools dependencies
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.DefiningConversion;


/**
 * Mini Plug-In API for {@linkplain ProjectedCRS projected CRS} from the {@code AUTO} authority.
 *
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett
 * @author Rueben Schulz
 * @author Martin Desruisseaux
 */
abstract class Factlet {
    /**
     * Returns the {@code AUTO} code for this plugin.
     */
    public abstract int code();

    /**
     * Returns the name for the CRS to be created by this plugin.
     */
    public abstract String getName();

    /**
     * Returns the classification of projection method.
     * For example {@code "Transverse_Mercator"}.
     */
    public abstract String getClassification();

    /**
     * Creates a coordinate reference system from the specified code. The default
     * implementation creates a {@linkplain ParameterValueGroup parameter group}
     * for the {@linkplain #getClassification projection classification}, and then
     * invokes {@link #setProjectionParameters} in order to fill the parameter values.
     */
    public final ProjectedCRS create(final Code code, final ReferencingFactoryContainer factories)
            throws FactoryException
    {
        final String classification = getClassification();
        final ParameterValueGroup parameters;
        parameters = factories.getMathTransformFactory().getDefaultParameters(classification);
        setProjectionParameters(parameters, code);
        final String name = getName();
        final DefiningConversion conversion = new DefiningConversion(name, parameters);
        return factories.getCRSFactory().createProjectedCRS(
                Collections.singletonMap(IdentifiedObject.NAME_KEY, name),
                DefaultGeographicCRS.WGS84, conversion, DefaultCartesianCS.PROJECTED);
    }

    /**
     * Fills the parameter values for the specified code.
     */
    protected abstract void setProjectionParameters(ParameterValueGroup parameters, Code code);
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */

package org.geotools.api.coverage.processing;

import static org.geotools.api.annotation.Obligation.MANDATORY;
import static org.geotools.api.annotation.Specification.OGC_01004;

import org.geotools.api.annotation.UML;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.parameter.ParameterValueGroup;

/**
 * This interface provides descriptive information for a grid coverage processing operation. The descriptive information
 * includes such information as the name of the operation, operation description, number of source grid coverages
 * required for the operation etc.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 * @todo This interface should be renamed {@code CoverageOperation}.
 */
public interface Operation {
    /**
     * Name of the processing operation. This name is passed as a parameter to the
     * {@link GridCoverageProcessor#doOperation doOperation} method to instantiate a new grid coverage on which the
     * processing operation is performed.
     *
     * @return The name of the processing operation.
     * @todo The return type will be changed from {@link String} to {@link Identifier}.
     */
    String getName();

    /**
     * Description of the processing operation. If no description is available, the value will be {@code null}.
     *
     * @return The description of the processing operation, or {@code null}.
     */
    String getDescription();

    /**
     * Vendor of the processing operation implementation. If no vendor name is available, the value will be
     * {@code null}.
     *
     * @return The implementation vendor name, or {@code null}.
     */
    String getVendor();

    /**
     * URL for documentation on the processing operation. If no online documentation is available the string will be
     * {@code null}.
     *
     * @return The URL for documentation on the processing operation, or {@code null}.
     */
    String getDocURL();

    /**
     * Version number for the implementation.
     *
     * @return The version number for the implementation, or {@code null}.
     */
    String getVersion();

    /**
     * Number of source grid coverages required for the operation.
     *
     * @return The number of source grid coverages required for the operation.
     */
    int getNumSources();

    /**
     * Retrieve the parameters information.
     *
     * @return The parameter informations.
     */
    @UML(identifier = "getParameterInfo, numParameters", obligation = MANDATORY, specification = OGC_01004)
    ParameterValueGroup getParameters();
}

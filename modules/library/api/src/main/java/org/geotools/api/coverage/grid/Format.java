/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.coverage.grid;

import static org.geotools.api.annotation.Obligation.MANDATORY;
import static org.geotools.api.annotation.Specification.OGC_01004;

import org.geotools.api.annotation.UML;
import org.geotools.api.parameter.ParameterValueGroup;

/**
 * A discovery mechanism to determine the formats supported by a {@link GridCoverageReader} or
 * {@link GridCoverageWriter} implementation. A {@code GridCoverageReader/GridCoverageWriter} implementation can support
 * a number of file format or resources.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface Format {
    /** Name of the file format. */
    String getName();

    /** Description of the file format. If no description, the value will be {@code null}. */
    String getDescription();

    /** Vendor or agency for the format. */
    String getVendor();

    /** Documentation URL for the format. */
    String getDocURL();

    /** Version number of the format. */
    String getVersion();

    /** Retrieve the parameter information for a {@link GridCoverageReader#read read} operation. */
    @UML(identifier = "getParameterInfo, numParameters", obligation = MANDATORY, specification = OGC_01004)
    ParameterValueGroup getReadParameters();

    /** Retrieve the parameter information for a {@link GridCoverageWriter#write write} operation. */
    @UML(identifier = "getParameterInfo, numParameters", obligation = MANDATORY, specification = OGC_01004)
    ParameterValueGroup getWriteParameters();
}

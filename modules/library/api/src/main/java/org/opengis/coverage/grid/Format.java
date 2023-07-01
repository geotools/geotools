/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.coverage.grid;

import static org.opengis.annotation.Obligation.MANDATORY;
import static org.opengis.annotation.Obligation.OPTIONAL;
import static org.opengis.annotation.Specification.OGC_01004;

import org.opengis.annotation.UML;
import org.opengis.parameter.ParameterValueGroup;

/**
 * A discovery mechanism to determine the formats supported by a {@link GridCoverageExchange}
 * implementation. A {@code GridCoverageExchange} implementation can support a number of file format
 * or resources.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier = "CV_Format", specification = OGC_01004)
public interface Format {
    /** Name of the file format. */
    @UML(identifier = "name", obligation = MANDATORY, specification = OGC_01004)
    String getName();

    /** Description of the file format. If no description, the value will be {@code null}. */
    @UML(identifier = "description", obligation = OPTIONAL, specification = OGC_01004)
    String getDescription();

    /** Vendor or agency for the format. */
    @UML(identifier = "vendor", obligation = OPTIONAL, specification = OGC_01004)
    String getVendor();

    /** Documentation URL for the format. */
    @UML(identifier = "docURL", obligation = OPTIONAL, specification = OGC_01004)
    String getDocURL();

    /** Version number of the format. */
    @UML(identifier = "version", obligation = OPTIONAL, specification = OGC_01004)
    String getVersion();

    /** Retrieve the parameter information for a {@link GridCoverageReader#read read} operation. */
    @UML(
            identifier = "getParameterInfo, numParameters",
            obligation = MANDATORY,
            specification = OGC_01004)
    ParameterValueGroup getReadParameters();

    /**
     * Retrieve the parameter information for a {@link GridCoverageWriter#write write} operation.
     */
    @UML(
            identifier = "getParameterInfo, numParameters",
            obligation = MANDATORY,
            specification = OGC_01004)
    ParameterValueGroup getWriteParameters();
}

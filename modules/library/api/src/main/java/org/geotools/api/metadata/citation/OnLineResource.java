/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.citation;

import java.net.URI;
import org.geotools.api.util.InternationalString;

/**
 * Information about on-line sources from which the dataset, specification, or community profile name and extended
 * metadata elements can be obtained.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @author Cory Horner (Refractions Research)
 * @since GeoAPI 1.0
 */
public interface OnLineResource {
    /**
     * Location (address) for on-line access using a Uniform Resource Locator address or similar addressing scheme such
     * as http://www.statkart.no/isotc211.
     *
     * @return Location for on-line access using a Uniform Resource Locator address or similar scheme.
     */
    URI getLinkage();

    /**
     * Connection protocol to be used. Returns {@code null} if none.
     *
     * @return Connection protocol to be used, or {@code null}.
     */
    String getProtocol();

    /**
     * Name of an application profile that can be used with the online resource. Returns {@code null} if none.
     *
     * @return Application profile that can be used with the online resource, or {@code null}.
     */
    String getApplicationProfile();

    /**
     * Name of the online resource. Returns {@code null} if none.
     *
     * @return Name of the online resource, or {@code null}.
     * @since GeoAPI 2.1
     */
    String getName();

    /**
     * Detailed text description of what the online resource is/does. Returns {@code null} if none.
     *
     * @return Text description of what the online resource is/does, or {@code null}.
     */
    InternationalString getDescription();

    /**
     * Code for function performed by the online resource. Returns {@code null} if unspecified.
     *
     * @return Function performed by the online resource, or {@code null}.
     */
    OnLineFunction getFunction();
}

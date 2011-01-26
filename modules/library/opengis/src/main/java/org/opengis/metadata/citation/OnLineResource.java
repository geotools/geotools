/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.citation;

import java.net.URI;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;
import org.opengis.annotation.Profile;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.ComplianceLevel.*;
import static org.opengis.annotation.Specification.*;


/**
 * Information about on-line sources from which the dataset, specification, or
 * community profile name and extended metadata elements can be obtained.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @author  Cory Horner (Refractions Research)
 * @since   GeoAPI 1.0
 */
@Profile (level=CORE)
@UML(identifier="CI_OnlineResource", specification=ISO_19115)
public interface OnLineResource {
    /**
     * Location (address) for on-line access using a Uniform Resource Locator address or
     * similar addressing scheme such as http://www.statkart.no/isotc211.
     *
     * @return Location for on-line access using a Uniform Resource Locator address or similar scheme.
     */
    @UML(identifier="linkage", obligation=MANDATORY, specification=ISO_19115)
    URI getLinkage();

    /**
     * Connection protocol to be used. Returns {@code null} if none.
     *
     * @return Connection protocol to be used, or {@code null}.
     */
    @UML(identifier="protocol", obligation=OPTIONAL, specification=ISO_19115)
    String getProtocol();

    /**
     * Name of an application profile that can be used with the online resource.
     * Returns {@code null} if none.
     *
     * @return Application profile that can be used with the online resource, or {@code null}.
     */
    @UML(identifier="applicationProfile", obligation=OPTIONAL, specification=ISO_19115)
    String getApplicationProfile();

    /**
     * Name of the online resource. Returns {@code null} if none.
     *
     * @return Name of the online resource, or {@code null}.
     *
     * @since GeoAPI 2.1
     */
    @UML(identifier="name", obligation=OPTIONAL, specification=ISO_19115)
    String getName();

    /**
     * Detailed text description of what the online resource is/does.
     * Returns {@code null} if none.
     *
     * @return Text description of what the online resource is/does, or {@code null}.
     */
    @UML(identifier="description", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getDescription();

    /**
     * Code for function performed by the online resource.
     * Returns {@code null} if unspecified.
     *
     * @return Function performed by the online resource, or {@code null}.
     */
    @UML(identifier="function", obligation=OPTIONAL, specification=ISO_19115)
    OnLineFunction getFunction();
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.identification;

import static org.opengis.annotation.Specification.*;

import org.opengis.annotation.UML;

/**
 * Identification of capabilities which a service provider makes available to a service user through
 * a set of interfaces that define a behaviour - See ISO 19119 for further information.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier = "SV_ServiceIdentification", specification = ISO_19115)
public interface ServiceIdentification extends Identification {}

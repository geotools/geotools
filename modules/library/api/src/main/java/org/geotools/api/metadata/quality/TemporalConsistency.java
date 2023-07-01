/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.quality;

import static org.geotools.api.annotation.Specification.ISO_19115;

import org.geotools.api.annotation.UML;

/**
 * Correctness of ordered events or sequences, if reported.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier = "DQ_TemporalConsistency", specification = ISO_19115)
public interface TemporalConsistency extends TemporalAccuracy {}

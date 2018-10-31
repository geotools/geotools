/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.crs;

import static org.opengis.annotation.Specification.*;

import org.opengis.annotation.UML;

/**
 * A coordinate reference system that is defined by its coordinate conversion from another
 * coordinate reference system but is not a projected coordinate reference system. This category
 * includes coordinate reference systems derived from a {@linkplain ProjectedCRS projected
 * coordinate reference system}.
 *
 * @departure ISO 19111 defines a {@code DerivedCRSType} code list. The later is omitted in GeoAPI
 *     since Java expressions like {@code (baseCRS instanceof FooCRS)} provides the same capability
 *     with more flexibility.
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract
 *     specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier = "SC_DerivedCRS", specification = ISO_19111)
public interface DerivedCRS extends GeneralDerivedCRS {}

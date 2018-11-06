/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.operation;

import org.opengis.annotation.Extension;

/**
 * Base interface for cylindrical map projections.
 *
 * <p>&nbsp;
 *
 * <p align="center"><img src="../doc-files/CylindricalProjection.png">
 *
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 * @see org.opengis.referencing.crs.ProjectedCRS
 * @see <A HREF="http://mathworld.wolfram.com/CylindricalProjection.html">Cylindrical projection on
 *     MathWorld</A>
 */
@Extension
public interface CylindricalProjection extends Projection {}

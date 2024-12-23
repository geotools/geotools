/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.referencing.datum;

/**
 * Defines the origin of an engineering coordinate reference system. An engineering datum is used in a region around
 * that origin. This origin can be fixed with respect to the earth (such as a defined point at a construction site), or
 * be a defined point on a moving vehicle (such as on a ship or satellite).
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface EngineeringDatum extends Datum {}

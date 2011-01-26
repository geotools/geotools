/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing;

import org.opengis.annotation.UML;
import static org.opengis.annotation.Specification.*;


/**
 * Spatial reference system using geographic identifier, a reference to a feature with a known
 * spatial location. Spatial reference systems using geographic identifiers are not based on
 * coordinates.
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@UML(identifier="RS_SpatialReferenceSystemUsingGeographicIdentifier", specification=ISO_19111)
public interface SpatialReferenceSystemUsingGeographicIdentifier extends ReferenceSystem {
}

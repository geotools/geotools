/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.temporal;

import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * The Julian day numbering system is a temporal coordinate system that has its origin at noon
 * on 1 January 4713 BC in the Julian proleptic calendar. The Julian day number is an integer
 * value; the Julian date is a decimal value that allows greater resolution.
 *
 * @author Stephane Fellah (Image Matters)
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/temporal/JulianDate.java $
 */
@UML(identifier="JulianDate", specification=ISO_19108)
public interface JulianDate extends TemporalCoordinate {
}

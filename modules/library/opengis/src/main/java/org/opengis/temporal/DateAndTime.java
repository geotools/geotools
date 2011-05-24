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
 * Provides a single data type for identifying a temporal position with a resolution
 * of less than a day.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/temporal/DateAndTime.java $
 */
@UML(identifier="TM_DateAndTime", specification=ISO_19108)
public interface DateAndTime extends ClockTime, CalendarDate {
}

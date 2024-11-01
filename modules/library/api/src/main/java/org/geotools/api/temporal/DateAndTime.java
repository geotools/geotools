/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.temporal;

/**
 * Provides a single data type for identifying a temporal position with a resolution of less than a day.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 */
public interface DateAndTime extends ClockTime, CalendarDate {}

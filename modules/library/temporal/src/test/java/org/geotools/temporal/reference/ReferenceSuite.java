/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.temporal.reference;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({org.geotools.temporal.reference.DefaultTemporalReferenceSystemTest.class, org.geotools.temporal.reference.DefaultOrdinalEraTest.class, org.geotools.temporal.reference.DefaultTemporalCoordinateSystemTest.class, org.geotools.temporal.reference.DefaultClockTest.class, org.geotools.temporal.reference.DefaultOrdinalReferenceSystemTest.class, org.geotools.temporal.reference.DefaultCalendarEraTest.class, org.geotools.temporal.reference.DefaultCalendarTest.class})
public class ReferenceSuite {
}

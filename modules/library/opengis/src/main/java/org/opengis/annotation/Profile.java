/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.*;


/**
 * An annotation mapping an interface, methods or fields to a profile.
 *
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/annotation/Profile.java $
 */
@Documented
@Target({TYPE, FIELD, METHOD})
public @interface Profile {
    /**
     * The level for the annoted element. {@link ComplianceLevel#CORE CORE} means
     * that all profiles should provides this element.
     *
     * @return The compliance level.
     */
    ComplianceLevel level();
}

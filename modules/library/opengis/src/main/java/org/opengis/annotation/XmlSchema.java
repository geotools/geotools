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
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;


/**
 * An annotation mapping a package to the XML schema from which it was derived.
 *
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/annotation/XmlSchema.java $
 */
@Documented
@Target(PACKAGE)
@Retention(RUNTIME)
public @interface XmlSchema {
    /**
     * The URL to the schema.
     *
     * @return The URL to the schema.
     */
    String URL();

    /**
     * The specification where this XML schema come from.
     *
     * @return The originating specification.
     */
    Specification specification();
}

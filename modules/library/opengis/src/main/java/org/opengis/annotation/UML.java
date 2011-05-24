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
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;


/**
 * An annotation mapping each interface, methods or fields to
 * the UML identifier where they come from.
 *
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/annotation/UML.java $
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, FIELD, METHOD})
public @interface UML {
    /**
     * The UML identifier for the annotated interface, method or code list element.
     * Scripts can use this identifier in order to maps a GeoAPI method to the UML
     * entity where it come from.
     *
     * @return The UML identifier used in the standard.
     */
    String identifier();

    /**
     * The obligation declared in the UML. This metadata can be queried in order to
     * determine if a null value is allowed for the annotated method or not. If the
     * obligation is {@link Obligation#MANDATORY}, then null value are not allowed.
     *
     * @return The obligation declared in the standard.
     */
    Obligation obligation() default Obligation.MANDATORY;

    /**
     * The specification where this UML come from.
     *
     * @return The originating specification.
     */
    Specification specification();
}

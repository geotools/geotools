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
 * An annotation for classes or method that are <A HREF="http://geoapi.sourceforge.net">GeoAPI</A>
 * extension. This annotation is mutually exclusive with {@link UML}.
 *
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/annotation/Extension.java $
 */
@Documented
@Retention(SOURCE)
@Target({TYPE, FIELD, METHOD})
public @interface Extension {
}

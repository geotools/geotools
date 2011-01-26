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
 * Maps a method to the XML parameter. This annotation can be used with reflection for
 * finding the corresponding XML parameter in the {@linkplain XmlSchema schema}.
 *
 * @author  Johann Sorel (Geomatys)
 * @since   GeoAPI 2.2
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface XmlParameter {
    /**
     * The name of the element in the XML schema.
     *
     * @return The XML parameter name.
     */
    String value();
}

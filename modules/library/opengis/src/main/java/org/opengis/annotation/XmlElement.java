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
 * Maps an interface or a method to the XML type, element or attribute.
 * Interfaces usually map to XML types, while methods map to XML element
 * or attribute. It is not the purpose of this annotation to differentiate
 * types from attributes, since this distinction can already be inferred from
 * Java reflection. This annotation, completed with reflection if needed, should
 * only provides enough information for finding the corresponding XML element in
 * the {@linkplain XmlSchema schema}.
 *
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@Documented
@Target({TYPE,METHOD,FIELD})
@Retention(RUNTIME)
public @interface XmlElement {
    /**
     * The name of the element in the XML schema.
     *
     * @return The XML element name.
     */
    String value();
}

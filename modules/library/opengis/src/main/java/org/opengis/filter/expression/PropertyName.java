/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.expression;

// Annotations
import org.opengis.annotation.XmlElement;
import org.xml.sax.helpers.NamespaceSupport;


/**
 * Expression class whose value is computed by retrieving the value indicated
 * by the provided name.
 * <p>
 * The most common applicatoin of this is to retrive
 * a {@linkplain org.opengis.feature.Feature feature}'s property using
 * an xpath expression.
 * </p>
 * <p>
 * Please note that the Filter specification allows a limited subset of
 * XPath to be used for the PropertyName. We encourage implementations to
 * match this functionality.
 * </p>
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@XmlElement("PropertyName")
public interface PropertyName extends Expression {
    /**
     * Returns the name of the property whose value will be returned by the
     * {@link #evaluate evaluate} method.
     */
    String getPropertyName();
    
    /**
     * Returns namespace context information, or null if unavailable/inapplicable
     * 
     * @return namespace context information, or null if unavailable/inapplicable
     */
    NamespaceSupport getNamespaceContext();

}

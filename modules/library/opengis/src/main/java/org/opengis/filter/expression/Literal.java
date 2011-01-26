/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.expression;

// Annotations
import org.opengis.annotation.XmlElement;


/**
 * Instances of this interface provide a constant, literal value that can be
 * used in expressions.
 * <p>
 * The {@link #evaluate evaluate} method of this class must return the same
 * value as {@link #getValue()}.
 * </p>
 * <p>
 * It should be noted that content of getValue() may be persisted with with
 * XML based technologies. As an example a geoapi Geometry may be written out
 * uding GML3, while a JTS Geometry may be written out using GML2. You should
 * not assume that the same instance will be made available to all callers,
 * please limit your self to pure data objects and don't use Literal to pass
 * state or operations between systems.
 * <p>
 *
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@XmlElement("Literal")
public interface Literal extends Expression {
    /**
     * Returns the constant value held by this object.
     */
    Object getValue();

}

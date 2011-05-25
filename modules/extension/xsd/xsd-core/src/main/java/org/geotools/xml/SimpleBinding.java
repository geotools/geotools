/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;


/**
 *        A strategy for parsing components in an instance document which are of
 *        simple type.
 *
 * <p>
 * Simple types can be manifested in elements and in attributes. Simple type
 * strategies must be capable of parsing simple values regardless of the form.
 * </p>
 *
 * <p>
 * Strategy objects must declare how they relate to other strategy objects in
 * the type hierarchy of the type they parse. To allow strategy objects which
 * relate through a type hiearchy to communicate, a value is passed along to
 * strategies as they are executed. As an example, consider the strategies for
 * <b>integer</b> and <b>decimal</b>.
 * </p>
 *
 * <pre>
 * <code>
 * class DecimalStrategy implements Strategy {
 *                 ...
 *
 *                 int getExecutionMode() {
 *                         return OVERRIDE;
 *                 }
 *
 *                 Object parse(InstanceComponent instance, Object value)
 *                         throws Exception {
 *
 *                         BigDecimal decimal = new BigDecimal(instance.getText());
 *                         return decimal;
 *                 }
 *                 ...
 *         }
 *
 * class IntegerStrategy implements Strategy {
 *         ...
 *         int getExecutionMode() {
 *                 return AFTER;
 *         }
 *
 *         Object parse(InstanceComponent instance, Object value)
 *                 throws Exception {
 *
 *                 BigDecimal decimal = (BigDecimal)value;
 *                 return decimal.toBigInteger();
 *         }
 *         ...
 * }
 * </code>
 * </pre>
 *
 * <p>
 * In the above example, the decimal strategy is at the top of the hierarchy as
 * it declares its execution mode as {@link org.geotools.xml.Binding#OVERRIDE}.
 * Therefore it must process the raw text of the instance being parsed, and transform
 * it into the specific object, in this case an object of type BigDecimal.
 * </p>
 * <p>
 * The integer strategy extends the decimal strategy as it declares its
 * execution mode as {@link org.geotools.xml.Binding#AFTER}. Therefore
 * the integer strategy has access to the result of the decimal strategy,
 * and can simply transform the result of the decimal strategy into its
 * specific type. In this case an object of type BigInteger.
 * </p>
 *
 * @author Justin Deoliveira,Refractions Research Inc.,jdeolive@refractions.net
 *
 *
 *
 * @source $URL$
 */
public interface SimpleBinding extends Binding {
    /**
     * Parses an instance component (element or attribute) into an object
     * representation.
     *
     * @param instance The component being parsed.
     * @param value The result of the parse from another strategy in the type
     * hierarchy. Could be null if this is the first strategy being executed.
     *
     * @return The parsed object, or null if the component could not be parsed.
     *
     * @throws Delegate objects should not attempt to handle any exceptions.
     */
    Object parse(InstanceComponent instance, Object value)
        throws Exception;

    /**
     * Performs the encoding of the object as a String.
     *
     * @param object The object being encoded, never null.
     * @param value The string returned from another binding in the type
     * hierachy, which could be null.
     *
     * @return A String representing the object.
     */
    String encode(Object object, String value) throws Exception;
}

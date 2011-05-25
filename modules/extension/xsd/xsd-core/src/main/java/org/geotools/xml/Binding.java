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

import java.awt.List;
import javax.xml.namespace.QName;

/**
 * A specialized handler for a specific type in an xml schema.
 * <p>
 * Bindings have the following responsibilities.
 *   <ul>
 *       <li>Parsing components from an instance document (elements and attributes)
 * into model objects</li>
 *       <li>Encoding model objects as xml components</li>
 *       <li>Sorting themselves in case multiple bindings target the same type</li>
 *  </ul>
 * </p>
 *
 * <p>
 * <h3>Type Binding</h3>
 *
 * Binding objects correspond to xml schema types. A binding declares
 * it target type by advertising the qualified name of the type it binds to.
 * For instance, the following strategy binds itself to type <b>xsd:string</b>.
 * </p>
 *
 * <pre>
 * <code>
 * class XSDStringStrategy {
 *
 *         QName getTarget() {
 *                 return new QName("http://www.w3.org/2001/XMLSchema","string");
 *         }
 *
 *  ...
 * </code>
 * </pre>
 *
 * <p>
 * The upshot is that whenever an element or attribute is encountered in an
 * instance document that is of type xsd:string, this binding will be used to
 * turn the string into an object representation.
 * </p>
 *
 * <p>
 * And on the other side of coin, when an instanceof String is encountered when
 * serializing an object model, the binding will be used to encode the string as
 * xml.
 * </p>
 *
 * <p>
 * <h3>Inheritance</h3>
 *
 * XML Schema supports Inheritance. As a concrete example, consider the simple
 * xml schema simple types <b>decimal</b> and <b>integer</b>.
 * </p>
 *
 * <pre>
 * <code>
 *  &lt;xs:simpleType name="decimal" id="decimal"&gt;
 *   &lt;xs:annotation&gt;
 *     &lt;xs:appinfo&gt;
 *      &lt;hfp:hasFacet name="totalDigits"/&gt;
 *       &lt;hfp:hasFacet name="fractionDigits"/&gt;
 *       &lt;hfp:hasFacet name="pattern"/&gt;
 *       &lt;hfp:hasFacet name="whiteSpace"/&gt;
 *       &lt;hfp:hasFacet name="enumeration"/&gt;
 *       &lt;hfp:hasFacet name="maxInclusive"/&gt;
 *       &lt;hfp:hasFacet name="maxExclusive"/&gt;
 *       &lt;hfp:hasFacet name="minInclusive"/&gt;
 *       &lt;hfp:hasFacet name="minExclusive"/&gt;
 *       &lt;hfp:hasProperty name="ordered" value="total"/&gt;
 *       &lt;hfp:hasProperty name="bounded" value="false"/&gt;
 *       &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
 *       &lt;hfp:hasProperty name="numeric" value="true"/&gt;
 *     &lt;/xs:appinfo&gt;
 *     &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#decimal"/&gt;
 *   &lt;/xs:annotation&gt;
 *   &lt;xs:restriction base="xs:anySimpleType"&gt;
 *     &lt;xs:whiteSpace fixed="true" value="collapse" id="decimal.whiteSpace"/&gt;
 *   &lt;/xs:restriction&gt;
 * &lt;/xs:simpleType&gt;
 *
 * &lt;xs:simpleType name="integer" id="integer"&gt;
 *   &lt;xs:annotation&gt;
 *     &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#integer"/&gt;
 *   &lt;/xs:annotation&gt;
 *   &lt;xs:restriction base="xs:decimal"&gt;
 *     &lt;xs:fractionDigits fixed="true" value="0" id="integer.fractionDigits"/&gt;
 *     &lt;xs:pattern value="[\-+]?[0-9]+"/&gt;
 *   &lt;/xs:restriction&gt;
 * &lt;/xs:simpleType&gt;
 * </code>
 * </pre>
 *
 * <p>
 * The above types define an inheiretance hierarcy. To model this relationship
 * among the corresponding binding objects, an <B>execution mode</B> must be
 * declared by each binding. The execution mode specifies wether a binding
 * should be executed before, after, or totally override the binding for a
 * direct parent.
 * </p>
 *
 * <pre>
 *         <code>
 *         class DecimalBinding implements Binding {
 *                 ...
 *
 *                 int getExecutionMode() {
 *                         return OVERRIDE;
 *                 }
 *                 ...
 *         }
 *
 *         class IntegerBinding implemnts Binding {
 *                 ...
 *                 int getExecutionMode() {
 *                         return AFTER;
 *                 }
 *                 ...
 *         }
 *         </code>
 * </pre>
 *
 * <p>
 *         In the above example, the decimal bidning declares its execution mode to
 * be override. This means that no bindings for any of the base types of
 * decimal will be executed. The integer binding declares its execution mode
 * as AFTER. This means that the integer binding will be executed after the
 * decimal strategy.
 * </p>
 *
 * <p>
 *         <h3>Context</h3>
 *
 * Bindings are executed within a particular context or container. A binding
 * can use its context to obtain objects that it depends on to perform a parse
 * or encoding. A binding can declare a dependency by simply adding it to its
 * constructor.
 *
 * This is known as
 * <a href=http://www.martinfowler.com/articles/injection.html#ConstructorInjectionWithPicocontainer>
 * Constructor Injection</a>.
 *
 * When the binding is created it is injected with all dependencies. The context is
 * responsible for satisfying the dependencies of the binding. As an example
 * consider the following complex type defintion.
 *
 *  <pre>
 *          <code>
 *          &lt;xsd:complexType name="collection"&gt;
 *             &lt;xsd:sequence&gt;
 *              &lt;xsd:element name="item" type="xsd:any" minOccurs="0" maxOccurs="unbounded"/&gt;
 *            &lt;/xsd:sequence&gt;
 *           &lt;/xsd:complexType&gt;
 *          </code>
 *  </pre>
 *
 *  The associated binding must turn instances of this type into objects of
 *  type {@link java.util.Collection}. However, the question remains what
 *  concrete subclass of Collection to use. And perhaps we need this type to
 *  vary in different situations. The solution is to take the decision out of
 *  the hands of this binding, and into the hands of someone else. To acheive
 *  this the collection binding adds a dependency of type Collection.
 *
 *  <pre>
 *          <code>
 *class CollectionStrategy implements ComplexBinding {
 *
 *          Collection collection;
 *          <b>
 *          CollectionStrategy(Collection collection) {
 *                    this.collection = collection;
 *          }
 *          </b>
 *          QName getTarget() {
 *                    return new QName("http://org/geotools/","collection");
 *          }
 *
 *          int getExecutionMode() {
 *                    return OVERRIDE;
 *          }
 *
 *                 <b>
 *          Object parse(Element instance, Node[] children, Node[] atts, Object value)
 *                    throws Exception {
 *
 *                  for (int i = 0; i < children.length; i++) {
 *                          collection.add(children[i].getValue());
 *                  }
 *
 *                  return collection;
 *          }
 *          </b>
 *}
 *          </code>
 *  </pre>
 *  </p>
 *
 *  <h3>Conflict resolution</h3>
 *  In some cases multiple bindings are targetting the same java class. This happens, for example,
 *  in GML3 where {@link MultiPolygon} is associated to two different elements, gml:MultiPolygon
 *  and gml:MultiSurface (the former being deprecated and kept for backwards compatibility).
 *
 *  <p>In such occasions, binding implementations must implement the {@link Comparable} interface,
 *  in case of doubt the bindings associated to a specific class will be sorted and the first
 *  element in the resulting {@link List} will be used.
 *
 *
 * @author Justin Deoliveira,Refractions Research Inc.,jdeolive@refractions.net
 *
 *
 *
 * @source $URL$
 */
public interface Binding {
    /**
     * Specifies that a binding should be executed after its direct parent
     */
    static final int AFTER = 0;

    /**
     * Specifes that a binding should be executed before its direct parent.d
     */
    static final int BEFORE = 1;

    /**
     * Specifies that a binding should totally override the execution of its
     * direct parent.
     */
    static final int OVERRIDE = 2;

    /**
     * @return The qualified name of the target for the binding.
     */
    QName getTarget();

    /**
     * @return The java type this binding maps to.
     */
    Class getType();

    /**
     * @return The execution mode of the binding, one of the constants AFTER,
     * BEFORE, or OVERRIDE.
     *
     * @see Binding#AFTER
     * @see Binding#BEFORE
     * @see Binding#OVERRIDE
     */
    int getExecutionMode();
}

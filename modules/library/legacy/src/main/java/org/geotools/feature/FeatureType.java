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
package org.geotools.feature;

import java.net.URI;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * A metadata template for a Feature of arbitrary complexity.
 * <p>
 * Notes:
 * <ul>
 * <li>that this documentation should be read in conjunction with the {@link Feature}API.
 * <li>the attributes described by this FeatureType <b>and its ancestors </b> define the complete
 * schema for a Feature
 * </p>
 * <p>
 * This interface answers the question: How do we represent features within GeoTools? Of course, the
 * most general answer would be: features can be any Java object. However, this is also the least
 * useful solution because it means that users of features have essentially no way to find out about
 * the meaning of features other than using Java introspection/reflection. This is too cumbersome
 * and is insufficient for the goal of creating a simple framework for manipulating and accessing
 * generic geographic data. The opposite approach might be to define a very constrained set of
 * possible attributes (that, for example, mirrored Java primitives and OGC simple geometries) and
 * only allow features of this type.
 * </p>
 * <p>
 * This interface takes a different approach: it defines a minimal ontology for representing a
 * feature and serves as a consistent framework for defining more constrained (and, therefore, often
 * more meaningful) feature types. A <code>FeatureType</code> represents features as an object
 * that contains zero or more attribute objects, one of which will generally be a geometry, but no
 * geometry and multiple geometries are allowed, according to implementation. Note that instances of
 * implementations of this class are henceforth referred to as schemas.
 * </p>
 * <p>
 * With oneexceptions, the type of an attribute is considered to be its cannonical definition by the
 * FeatureType. For example, an attribute type might be a <code>javax.sound.midi.Sequence</code>
 * object, which contains a <code>float</code> public field called PPQ. The fact that this
 * attribute exists is not known by the <code>FeatureType</code> itself. If a caller asks this
 * <code>FeatureType</code> for all of its attributes, the <code>
 * FeatureType</code> will tell
 * the caller that it has an attribute of type <code>javax.sound.midi.Sequence</code>, but not
 * that this attribute has a sub-attribute (field) called PPQ. It is the responsibility of the
 * callers to understand the objects it is asking for and manipulate them appropriately.
 * </p>
 * <p>
 * The exceptions is for:
 * <ul>
 * <li>if the type stored in the <code>FeatureType</code> is a
 * <code>org.geotools.datasource.Feature</code> type. <br>
 * In this case, all information about sub-attributes are stored and passed to calling classes upon
 * request. The style of reference (XPath) is defined in and mediated by <code>FeatureType</code>
 * implementations.
 * </ul>
 * Question: how does one determine the schema for the attribute defined as a FeatureType? I suspect
 * that FeatureType may be a valid AttributeType? (One needs this schema information before xpath
 * can be used to query the Feature value.
 * </p>
 * <p>
 * It is the responsibility of the implementing class to ensure that the <code>FeatureType</code>
 * is always in a valid state. This means that each attribute tuple must be fully initialized and
 * valid. The minimum valid <code>FeatureType</code> is one with nulls for namespace, type, and
 * attributes; this is clearly a trivial case, since it is so constrained that it would not allow
 * for any feature construction. There are a few conventions of which implementers of this interface
 * must be aware in order to successfully manage a <code>FeatureType</code>:
 * </p>
 * <ol>
 * <li><b>Immutability </b> <br>
 * <i>FeatureTypes must be implemented as immutable objects! </i> All setting methods have been
 * removed from this interface, that functionality is now available in the mutable {@link
 * FeatureTypeFactory}</li>
 * <li><b>Default Geometries </b> <br>
 * Note that the FeatureType contains a special methods for handling geometries. The primary
 * geometry retrieval methods are in <code>Feature</code> because they may change over the life of
 * the feature, while the schema may not. In cases where there are more than one geometries it is up
 * to the implementor to determine which is the default geometry. <code>getDefaultGeometry</code>
 * may return <code>null</code> if there are no geometries in the FeatureType, but if there is one
 * or more geometry then the method must return one of them, <code>null</code> is never an
 * acceptable return value.</li>
 * <li><b>XPath </b> <br>
 * XPath is the standard used to access all attributes (flat, nested, and multiple), via a single,
 * unified string. Using XPath to access attributes has the convenient side-benefit of making them
 * appear to be non-nested and non-multiple to callers with no awareness of XPath. This greatly
 * simplifies accessing and manipulating data. However, it does put extra burden on the implementers
 * of <code>FeatureType</code> to understand and correctly implement XPath pointers. Note that the
 * <code>Feature</code> object does not understand XPath at all and relies on implementors of this
 * interface to interpret XPath references. Fortunately, XPath is quite simple and has a clearly
 * written <a href="http://www.w3.org/TR/xpath">specification </a>.</li>
 * <li><b>Feature Creation </b> <br>
 * FeatureType also must provide methods for the creation of Features, as specified in
 * FeatureFactory. The creating FeatureType should check to see if the passed in objects validate
 * against its AttributeTypes, and if it does should return a new Feature.</li>
 * </ol>
 * <h2>Redesign Notes (feature-exp2)</h2>
 * The main design goal of this is to have FeatureType extend AttributeType. This allows us to
 * nesting of features much more nicely. We already are going in this direction, with the
 * FeatureAttributeType buried in DefaultAttribute. This is just making it explicit, so it works
 * with the complex objects GML can return a lot more sensible. So much of the work in this class is
 * figuring out what concepts are the same. Some stuff may need to be rethought a bit, as there are
 * a few subtle assumptions that we are working with flat files. We will revisit this when we
 * implement choice and multiplicity.
 * <ul>
 * <li>got rid of deprecated getNamespace() method that returned a string, replaced it with a URI
 * return. This has been deprecated for a bit, and was done out of a desire to keep backwards
 * compatibility with 2.0, but that mission failed, so we're just moving on. This change will break
 * a few things, but is a good one, people just need to update their client code a bit.
 * <li>Deprecated getTypeName() to be getName(). They are the same thing, would be nice to get rid
 * of getTypeName, but it's used super extensively. Though perhaps we could consider keeping it as a
 * convenience, a bit more explicit, but it seems like overkill.
 * <li>Updated comments of the AttributeType operations that are inhierited to say what they mean
 * in the context of a FeatureType.
 * </ul>
 * <h2>Redesign Notes (factory-hints)</h2>
 * The factory-hints design is finally coming through on the promiss of the great geotools factory
 * design. This design is actualy placing the factory system under application (rather than
 * 'default') control, as such it is really showing every last place where we did not follow our
 * architecture.
 * <ul>
 * Use Cases:
 * <li>Custom Feature: <br>
 * Application spedcifies the use of a custom feature implementation. This is used so an application
 * interface is supported by each and every feature created.
 * <li>optiomized coordinate storage <br>
 * LiteRenderer2 wants Shape2d specific CoordinateSequenceFactory used for all Geometry creation.
 * The point is to allow only the xy information to be retrieved, and in a format suitable for rapid
 * reprojection and coversion to a Java2D Shape. Any OpenGL (or Java3D) based renderer would also
 * run into this need.
 * </ul>
 * The second use case is interesting in that LiteRenderer2 will be using the Datastore at the same
 * time as other threads that want the normal coordinate sequence. So this is a per Query hint.
 * </ul>
 * <p>
 * Consequence: Since FeatureType is immutable, and CoordianteSequence is specified by the
 * GeometryFactory of the DefaultGeometryAttribute this implys that we have a per Query SchemaType.
 * </p>
 * <p>
 * It strikes me that this is a bad separation of concerns the "schema" should be exactly the same,
 * it is just the GeometryFactory that controls construction that is in the wrong spot. It should be
 * a hint, not attached to GeomtryAttributeType.
 * </p>
 *
 * @author Rob Hranac, VFNY
 * @author Chris Holmes, TOPP
 * @author David Zwiers, Refractions
 * @author Jody Garnett, Refractions
 * @source $URL$
 * @version $Id$
 * @see org.geotools.feature.Feature
 * @see org.geotools.feature.FeatureTypeFactory
 * @see org.geotools.feature.type.NestedAttributeType
 * @see org.geotools.feature.DefaultFeatureType
 *
 * @deprecated use {@link SimpleFeatureType}.
 */
public interface FeatureType extends SimpleFeatureType {
    //
    // XML Mapping Information
    //
    // Used for mapping Feature information to Geographical Markup Language,
    // 
    /**
     * Gets the global schema namespace.
     * <p>
     * This is often used to record the schema prefix (not the URI) required when writing out this
     * Feature using GML. The supporting application will need to be able to start the GMLWriter off
     * with the correct URI information so everything makes sense.
     * </p>
     * <p>
     * The Registry interface provided by the data module provides an example of how to store
     * associate FeatureType and namespace information. Please note that you may not have duplicate
     * typeNames in the same Namespace.
     * </p>
     *
     * @return Namespace of schema
     */
    URI getNamespace();

    /**
     * Gets the type name for this schema.
     * <p>
     * In GML this must be the element name of the Feature.
     * </p>
     *
     * @return The name of this feature type.
     */
    String getTypeName();

    /**
     * Test to determine whether this FeatureType is descended from the given FeatureType. Think of
     * this relationship likes the "extends" relationship in java.
     *
     * @param nsURI The namespace URI to use.
     * @param typeName The typeName.
     * @return true if descendant, false otherwise.
     */
    boolean isDescendedFrom(URI nsURI, String typeName);

    /**
     * A convenience method for calling <br>
     * <code><pre>
     * FeatureType f1;
     * FeatureType f2;
     * f1.isDescendedFrom(f2.getNamespace(), f2.getName());
     * </pre></code>
     * <p>
     * Question: this method duplicates the information provided by getAncestors().
     * </p>
     *
     * @param type The type to compare to.
     * @return true if descendant, false otherwise.
     */
    boolean isDescendedFrom(FeatureType type);

    /**
     * Is this FeatureType an abstract type?
     * <p>
     * When true is returned instances of this Feature cannot be created, instead a child
     * FeatureType must use this FeatureType as its ancestor.
     * </p>
     *
     * @return true if abstract, false otherwise.
     */
    boolean isAbstract();

    /**
     * Obtain an array of this FeatureType's direct ancestors.
     * Implementors should return a non-null array (may be of length 0).
     * <p>
     * <ul>
     * <li>length 0 - a root FeatureType
     * <li>length 1 - singe inhieratance
     * <li>length 1+ - multiple inheiratance, order is not significant
     * </ul>
     * </p>
     * GetAncestors() indicates *just* the direct parents of this FeatureType.
     * It capures the inheirtance relationship from the OGC overview document.
     * </p>
     * <p>
     * Example Code:<pre><code>
     * public FeatureType getAncestors()}
     *     return new FeatureType[]{ getSuper(), };
     * }
     * </code></pre>
     * </p>
     *
     * @return An array of ancestors.
     */
    FeatureType[] getAncestors();

    /**
     * Return the direct supertype of this FeatureType.
     * <p>
     * This is a conviance method for FeatureType where only single
     * inheirtance is supported (as is the case with GML).
     * </p>
     * <p>
     * This method will return null for a root FeatureType.
     * </p>
     * @return Get the super type, or null for root.
     */

    //FeatureType getSuper();

    /**
     * Gets the default geometry AttributeType.
     * <p>
     * If the FeatureType has more one geometry it is up to the implementor to determine which
     * geometry is the default. If working with multiple geometries it is best to get the
     * attributeTypes and iterate through them, checking for instances of GeometryAttribtueType.
     * </p>
     * <p>
     * This should just be used a convenience method when it is known that the features do not have
     * multiple geometries.
     * </p>
     *
     * @return The attribute type of the default geometry, which will contain the position.
     * @deprecated use {@link #getPrimaryGeometry()}.
     */

    //GeometryAttributeType getPrimaryGeometry();

    /**
     * Gets the primary or default geometry AttributeType.
     * <p>
     * If the FeatureType has more one geometry it is up to the implementor to determine which
     * geometry is the default. If working with multiple geometries it is best to get the
     * attributeTypes and iterate through them, checking for instances of GeometryAttribtueType.
     * </p>
     * <p>
     * This should just be used a convenience method when it is known that the features do not have
     * multiple geometries.
     * </p>
     * <p>
     * This method is a replacement for {@link #getGeometryDescriptor()} in order
     * to resolve a naming conflict with the geoapi feature model.
     * </p>
     *
     * @return The attribute type of the default geometry, which will contain the position.
     * @since 2.4
     */
    GeometryAttributeType getGeometryDescriptor();

    /**
     * The number of attribues defined by this schema.
     * <p>
     * This method to allows access to the complete schema as defined by this
     * FeatureType and its ancestors.
     * </p>
     * <p>
     * <ul>
     * Notes:
     * <li>
     * <li>for FeatureType with no super type this count value is the the
     *     same as getAttributeTypes().length.
     * </p>
     *
     * @return number of distinct attributeTypes available, taking ancestors and taking overrides
     *         into account.
     */
    int getAttributeCount();

    /**
     * Gets the attributeType at this xPath, if the specified attributeType does
     * not exist then <code>null</code> is returned.
     * <p>
     * Question: it is unclear how this interacts with the complete schema defined by this
     * FeatureType and its ancestors (in which a given xpath may refer to several AttributeTypes as
     * restrictions are applied.
     * </p>
     * <p>
     * Perhaps this method should be restricted to a FlatFeatureType? Or should have the option of
     * returning an array of matching AttributeType in order of inheiritence?
     * </p>
     *
     * @param xPath XPath pointer to attribute type.
     * @return AttributeType, or null if unavaialble
     */
    AttributeType getAttributeType(String xPath);

    /**
     * Find the position of a given AttributeType.
     * <p>
     * Match is based on attribute type name, the resulting index
     * may be used with getAttributeType( index ).
     * </p>
     * @param type The type to search for.
     * @return -1 if not found, a zero-based index if found.
     */
    int find(AttributeType type);

    /**
     * Find the position of an AttributeType which matches the given String.
     * <p>
     * This index may be used with getAttributeType( index ), the search space is the entire schema
     * defined by this FeatureType and its ancestors.
     * </p>
     *
     * @param attName the name to look for
     * @return -1 if not found, zero-based index otherwise
     */
    int find(String attName);

    /**
     * Gets the schema attributeType at the specified index.
     * <p>
     * The index is specified with respect to the entire Schema (as defined by this FeatureType and
     * it's ancestors).
     * <p>
     * <ul>
     * The index value should not be used with either:
     * <li>FeatureType.getAttributeTypes()[index] - as it defines only attributes contributed by
     * this FeatureType
     * <li>Feature.getAttribute( index ) - as attributes order may or may not be in sequence
     * </ul>
     * </p>
     *
     * @param position a integer index into the complete schema represented by
     *                 this FeatureType and its ancestors
     *
     * @return the attribute type at the specified position
     */
    AttributeType getAttributeType(int position);

    /**
     * AttributeTypes for this FeatureType, all attributes defined by this FeatureType
     * and its super types define schema for feature instances.
     * <p>
     * The provided array of AttributeTypes should be considered as adding to (or overriding) the
     * the AttribtueTypes defined by this FeatureTypes ancestors.
     * </p>
     * <p>
     * Note Well: Client code should not consider the index provided by the find( attName ) method
     * as a valid index into the returned array.
     * </p>
     *
     * @return Array of AttributeType describing this schema, array may be length 0, but should
     *         not be null
     */
    AttributeType[] getAttributeTypes();

    //
    // Depreicated AttributeType handling code
    //
    // This is origional Geotools 2.0 api that is depricated and will
    // be removed as part of Geotools 2.2.
    //
    /**
     * This is only used twice in the whole geotools code base, and one of those is for a test, so
     * we're removing it from the interface. If getAttributeType does not have the AttributeType it
     * will just return null. Gets the number of occurrences of this attribute.
     * <p>
     * Question: the comment says we are removing this, but it is not depricated? And how the heck
     * can the number of occurances out of a boolean.
     * </p>
     *
     * @deprecated It seems this method is ill concieved, use getAttributeType( xpath ) != null as a
     *             replacement
     * @param xPath XPath pointer to attribute type.
     * @return Number of occurrences.
     */
    boolean hasAttributeType(String xPath);

    //
    // This is code that was available in Geotools 2.0 as part of the
    // FeatureFactory API, In geotools 2.1 we are providing a clear
    // separation between schema information and construction.
    //
    /**
     * Create a duplicate of the provided feature, must delegate to an appropriate FeatureFactory
     * create method.
     * <p>
     * The implementation is assumed to make use of AttributeType duplicate as required for a deep
     * copy.
     * </p>
     *
     * //@deprecated This method will be removed in 2.2, please use FeatureFactory obtained from FactoryFinder
     * @param feature
     * @return a deep copy of feature
     * @throws IllegalAttributeException
     */
    Feature duplicate(Feature feature) throws IllegalAttributeException;

    /**
     * Creates a new feature, with a generated unique featureID.
     * <p>
     * Generating a FeatureID is less than ideal, as a FeatureID should be something
     * special about the "real world object" being modeled as a feature.
     * <p>
     * As an example: the "Effiel Tower" is a great feature id (there is only one) and we can
     * use that Feature ID in serveral systems:
     * <ul>
     * <li>In a tour guide FeatureTypes one which records (NAME, LOCATION, COST); and
     * <li>In a historical reference (NAME, LOCATION, HIEGHT, DATE, HISTORY)
     * </ul>
     * <p>
     * Explicitly a FeatureID should be persistant over time, and between systems.
     * <p>
     * The FeatureIDs "generated" by this method are generally created by a datasource.
     * This method is more for testing that doesn't need featureID.
     * </p>
     * <p>
     * Warning: This class does not follow the geotools guidelines of sepearting out
     * Interface from Factory, in geotools 2.3 there will be a formal FeatureFactory
     * allowing you to provide application specific FeatureImplementations as an orthogonal
     * concern to definition of type.
     * </p>
     *
     * @param attributes the array of attribute values
     * @return The created feature
     * @throws IllegalAttributeException if the FeatureType does not validate the attributes.
     */
    Feature create(Object[] attributes) throws IllegalAttributeException;

    /**
     * Scheduled for removal in Geotools 2.2, please use FeatureFactory.
     * <p>
     * Creates a new feature, with the indicated featureID.
     * </p>
     *
     * @param attributes the array of attribute values.
     * @param featureID the feature ID.
     * @return the created feature.
     * @throws IllegalAttributeException if the FeatureType does not validate the attributes.
     */
    Feature create(Object[] attributes, String featureID)
        throws IllegalAttributeException;

    /**
     * FeatureType check based on namespace, typeName, attributes and ancestors.
     * <p>
     * Conceptually FeatureType equality is supped to indicate an exact match based AttributeType
     * and Ancestors. The geotools models includes additional information of namespace
     * and typename.
     * </p>
     * <p>
     * As far as the geotools data model is concerned any FeatureType implementation should be able
     * to be subsituted for another. (An actually application may provided an implementation that
     * provdes tracking of metadata such as creation history, or support for additional interfaces).
     * </p>
     * <p>
     * Required implementation:<pre><code>
     * </code></pre>
     * A sample implementation is provided in FeatureTypes.equals( FeatureType, FeatureType ).
     * </p>
     * <p>
     * Questions:
     * <br>Q: You may wonder why namespace and typeName are not enought?
     * <br>A: Geotools often returns a "limitied" schema in response to a query where only a subset
     * of the attributes are requested.
     * <br>Q: Doesn't that still indicate the same schema?
     * <br>A: Yes it kind of should, a GML application (such as GeoServer) has to actually follow the
     * application schema and include attributes required by the schema (but not request by the query).
     * This library is more lax in its treatment, and expects you to uses isDecendedFrom as required.
     * </p>
     */
    public boolean equals(Object arg0);

    /**
     * Hascode based on namespace, typeName, attributes and ancestors.
     * <p>
     * Hascode is always a matched set with an equals implementation, please see the javadocs
     * for equals for a detailed discussion.
     * </p>
     * @see java.lang.Object#hashCode()
     */
    public int hashCode();
}

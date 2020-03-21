/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.schema;

import java.net.URI;
import org.geotools.util.factory.Factory;

/**
 * This Interface is intended to represent the public portion of an XML Schema. By public portion, I
 * mean the portion of the Schema which can be included in an instance document, or imported into
 * another Schema.
 *
 * <p>The distinction between the public portion of a XML Schema and the entire XML Schema is or
 * particular important when comparing, or printing two XML Schemas. This interface does is intended
 * to provide enough information to re-create the original Schema (note the lack or annotations as
 * an example). This interface is however intended to provide functional semantic equivalence. By
 * this is mean that two XML Schemas represented using this interface should have the same SET of
 * declarations. There is no guarantee that the Schema represented matches the original document
 * with respect to orderwithin the sets, except where order is explicitly defined (Sequence,
 * Choice).
 *
 * <p>This method must be inplemented within extensions: public static Schema getInstance();. It
 * will be used by the Schema factory to load the required extensions into memory.
 *
 * @author dzwiers www.refractions.net
 */
public interface Schema extends Factory {
    /**
     * Used to denote byte masks representing either XML block attributes or XML final attributes.
     */
    public static final int NONE = 0;

    /**
     * Used to denote byte masks representing either XML block attributes or XML final attributes.
     */
    public static final int EXTENSION = 1;

    /**
     * Used to denote byte masks representing either XML block attributes or XML final attributes.
     */
    public static final int RESTRICTION = 2;

    /**
     * Used to denote byte masks representing either XML block attributes or XML final attributes.
     */
    public static final int ALL = 4;

    /**
     * This method is intended to provide a list of public AttributeGroups defined by this Schema.
     * The definition of 'public AttributeGroups' should be interpreted as the set of
     * AttributeGroups available when creating an instance document, extending the schema, or
     * importing the schema.
     *
     * @return AttributeGroup[]
     * @see AttributeGroup
     */
    public AttributeGroup[] getAttributeGroups();

    /**
     * This method is intended to provide a list of public Attributes defined by this Schema. The
     * definition of 'public Attributes' should be interpreted as the set of Attributes available
     * when creating an instance document, extending the schema, or importing the schema.
     *
     * @see Attribute
     */
    public Attribute[] getAttributes();

    /**
     * This method returns the default block value associated with this schema as a mask. The keys
     * for the mask are represented as constants at the head of this file. As defined in the XML
     * Schema specification, element and type blocks should only be extended to include this block
     * if one is not specified.
     *
     * @return Block Mask
     */
    public int getBlockDefault();

    /**
     * This method is intended to provide a list of public ComplexTypes defined by this Schema. The
     * definition of 'public ComplexTypes' should be interpreted as the set of ComplexTypes
     * available when creating an instance document, extending the schema, or importing the schema.
     *
     * @see ComplexType
     */
    public ComplexType[] getComplexTypes();

    /**
     * This method is intended to provide a list of public Elements defined by this Schema. The
     * definition of 'public Elements' should be interpreted as the set of Elements available when
     * creating an instance document, extending the schema, or importing the schema.
     *
     * @see Element
     */
    public Element[] getElements();

    /**
     * This method returns the default final value associated with this schema as a mask. The keys
     * for the mask are represented as constants at the head of this file. As defined in the XML
     * Schema specification, element and type final values should only be extended to include this
     * final value if one is not specified.
     *
     * @return Final Mask
     */
    public int getFinalDefault();

    /**
     * This method is intended to provide a list of public Groups defined by this Schema. The
     * definition of 'public Groups' should be interpreted as the set of Groups available when
     * creating an instance document, extending the schema, or importing the schema.
     *
     * @see Group
     */
    public Group[] getGroups();

    /** This method is intended to provide the ID of this Schema. */
    public String getId();

    /**
     * This method is intended to provide a list of public Imports defined by this Schema. The
     * definition of 'public Imports' should be interpreted as the set of Imports available when
     * creating an instance document, extending the schema, or importing the schema.
     *
     * @see Schema
     */
    public Schema[] getImports();

    /** Gets the recommended prefix for this schema. */
    public String getPrefix();

    /**
     * This method is intended to provide a list of public SimpleTypes defined by this Schema. The
     * definition of 'public SimpleTypes' should be interpreted as the set of SimpleTypes available
     * when creating an instance document, extending the schema, or importing the schema.
     *
     * @see SimpleType
     */
    public SimpleType[] getSimpleTypes();

    /**
     * This returns the intended use name of the Schema (kinda like an ID, for a better definition
     * see the XML Schema Specification).
     */
    public URI getTargetNamespace();

    // may be different than targNS
    public URI getURI();

    /** This returns the Schema version ... */

    // TODO Use the Version in the merge + parsing portion for comparisons
    public String getVersion();

    /**
     * This looks to see if the URI passed in is represented by this Schema. Often this method uses
     * some heuritics on the list of included URIs. This allows one Schema to represent one
     * targetNamespace, but be potentially represented in more than one file.
     *
     * <p>Used to determine if the uri should provided should be included in an instance document.
     *
     * @see #getUris()
     */
    public boolean includesURI(URI uri);

    /** Returns true when the Default Attribute Form is qualified, false otherwise. */
    public boolean isAttributeFormDefault();

    /** Returns true when the Default Element Form is qualified, false otherwise. */
    public boolean isElementFormDefault();
}

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

import org.geotools.factory.Factory;


/**
 * <p>
 * This Interface is intended to represent the public portion of an XML Schema.
 * By public portion, I mean the portion of the Schema which can be included
 * in an instance document, or imported into another Schema.
 * </p>
 * 
 * <p>
 * The distinction between the public portion of a XML Schema and the entire
 * XML Schema is or particular important when comparing, or printing two XML
 * Schemas. This interface does is intended to provide enough information to
 * re-create the original Schema (note the lack or annotations as an example).
 * This interface is however intended to provide functional semantic
 * equivalence. By this is mean that two XML Schemas represented using this
 * interface should have the same SET of declarations. There is no guarantee
 * that the Schema represented matches the original document with respect to
 * orderwithin the sets, except where order is explicitly defined (Sequence,
 * Choice).
 * </p>
 * 
 * <p>
 * This method must be inplemented within extensions:  public static Schema
 * getInstance();. It will be used by the Schema factory to  load the required
 * extensions into memory.
 * </p>
 *
 * @author dzwiers www.refractions.net
 * @source $URL$
 */
public interface Schema extends Factory {
    /**
     * Used to denote byte masks representing either XML block attributes or
     * XML final attributes.
     */
    public static final int NONE = 0;

    /**
     * Used to denote byte masks representing either XML block attributes or
     * XML final attributes.
     */
    public static final int EXTENSION = 1;

    /**
     * Used to denote byte masks representing either XML block attributes or
     * XML final attributes.
     */
    public static final int RESTRICTION = 2;

    /**
     * Used to denote byte masks representing either XML block attributes or
     * XML final attributes.
     */
    public static final int ALL = 4;

    /**
     * <p>
     * This method is intended to provide a list of public AttributeGroups
     * defined by this Schema. The definition of 'public AttributeGroups'
     * should be interpreted as the set of AttributeGroups availiable when
     * creating an instance document, extending the schema, or importing the
     * schema.
     * </p>
     *
     * @return AttributeGroup[]
     *
     * @see AttributeGroup
     */
    public AttributeGroup[] getAttributeGroups();

    /**
     * <p>
     * This method is intended to provide a list of public Attributes defined
     * by this Schema. The definition of 'public Attributes' should be
     * interpreted as the set of Attributes availiable when creating an
     * instance document, extending the schema, or importing the schema.
     * </p>
     *
     *
     * @see Attribute
     */
    public Attribute[] getAttributes();

    /**
     * <p>
     * This method returns the default block value associated with this schema
     * as a mask. The keys for the mask are represented as constants at the
     * head of this file. As defined in the XML Schema specification, element
     * and type blocks should only be extended to include this block if one is
     * not specified.
     * </p>
     *
     * @return Block Mask
     */
    public int getBlockDefault();

    /**
     * <p>
     * This method is intended to provide a list of public ComplexTypes defined
     * by this Schema. The definition of 'public ComplexTypes' should be
     * interpreted as the set of ComplexTypes availiable when creating an
     * instance document, extending the schema, or importing the schema.
     * </p>
     *
     *
     * @see ComplexType
     */
    public ComplexType[] getComplexTypes();

    /**
     * <p>
     * This method is intended to provide a list of public Elements defined by
     * this Schema. The definition of 'public Elements' should be interpreted
     * as the set of Elements availiable when creating an instance document,
     * extending the schema, or importing the schema.
     * </p>
     *
     *
     * @see Element
     */
    public Element[] getElements();

    /**
     * <p>
     * This method returns the default final value associated with this schema
     * as a mask. The keys for the mask are represented as constants at the
     * head of this file. As defined in the XML Schema specification, element
     * and type final values should only be extended to include this final
     * value if one is not specified.
     * </p>
     *
     * @return Final Mask
     */
    public int getFinalDefault();

    /**
     * <p>
     * This method is intended to provide a list of public Groups defined by
     * this Schema. The definition of 'public Groups' should be interpreted as
     * the set of Groups availiable when creating an instance document,
     * extending the schema, or importing the schema.
     * </p>
     *
     *
     * @see Group
     */
    public Group[] getGroups();

    /**
     * <p>
     * This method is intended to provide the ID of this Schema.
     * </p>
     *
     */
    public String getId();

    /**
     * <p>
     * This method is intended to provide a list of public Imports defined by
     * this Schema. The definition of 'public Imports' should be interpreted
     * as the set of Imports availiable when creating an instance document,
     * extending the schema, or importing the schema.
     * </p>
     *
     *
     * @see Schema
     */
    public Schema[] getImports();

    /**
     * <p>
     * Gets the recommended prefix for this schema.
     * </p>
     *
     */
    public String getPrefix();

    /**
     * <p>
     * This method is intended to provide a list of public SimpleTypes defined
     * by this Schema. The definition of 'public SimpleTypes' should be
     * interpreted as the set of SimpleTypes availiable when creating an
     * instance document, extending the schema, or importing the schema.
     * </p>
     *
     *
     * @see SimpleType
     */
    public SimpleType[] getSimpleTypes();

    /**
     * <p>
     * This returns the intended use name of the Schema (kinda like an ID, for
     * a better definition see the XML Schema Specification).
     * </p>
     *
     */
    public URI getTargetNamespace();

    // may be different than targNS
    public URI getURI();

    /**
     * <p>
     * This returns the Schema version ...
     * </p>
     *
     */

    //TODO Use the Version in the merge + parsing portion for comparisons
    public String getVersion();

    /**
     * <p>
     * This looks to see if the URI passed in is represented by this Schema.
     * Often this method uses some heuritics on the list of included URIs.
     * This allows one Schema to represent one targetNamespace, but be
     * potentially represented in more than one file.
     * </p>
     * 
     * <p>
     * Used to determine if the uri should provided should be included in an
     * instance document.
     * </p>
     *
     * @param uri
     *
     *
     * @see #getUris()
     */
    public boolean includesURI(URI uri);

    /**
     * <p>
     * Returns true when the Default Attribute Form is qualified, false
     * otherwise.
     * </p>
     *
     */
    public boolean isAttributeFormDefault();

    /**
     * <p>
     * Returns true when the Default Element Form is qualified, false
     * otherwise.
     * </p>
     *
     */
    public boolean isElementFormDefault();
}

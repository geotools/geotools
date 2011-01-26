/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.opengis.util.MemberName;
import org.opengis.util.Record;
import org.opengis.util.RecordSchema;
import org.opengis.util.RecordType;
import org.opengis.util.TypeName;


/**
 * The type definition of a {@linkplain Record record}.
 * Used to provide a record of data type (in a manner similar to a strongly typed Map).
 * <p>
 * Please note that a record is <em>strongly</em> typed (and may be better thought
 * of as a mathematical tuple). The "keys" are strictly controlled "{@link MemberName}s"
 * and are usually defined in the context of a schema.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Jody Garnet
 * @author Martin Desruisseaux
 */
public class RecordTypeImpl implements RecordType {
    /**
     * The name that identifies this record type.
     */
    private TypeName typeName;

    /**
     * The members and their values.
     */
    private Map<MemberName,TypeName> attributeTypes;

    /**
     * The schema that contains this record type.
     */
    private RecordSchema parent;

    /**
     * Empty constructor only used by JAXB.
     */
    private RecordTypeImpl() {
    }

    /**
     * Creates a record with all attribute types specified.
     *
     * @param parent   The schema that contains this record type.
     * @param typeName The name that identifies this record type.
     * @param members  The name of the members to be included in this record type.
     */
    public RecordTypeImpl(final RecordSchema parent, final TypeName typeName,
                          final Collection<MemberName> members)
    {
        this.parent = parent;
        this.typeName = typeName;
        final Map<MemberName,TypeName> attributeTypes = new HashMap<MemberName,TypeName>();
        for (final MemberName member : members) {
            attributeTypes.put(member, member.getAttributeType());
        }
        this.attributeTypes = Collections.unmodifiableMap(attributeTypes);
    }

    /**
     * Creates a record with all attribute types specified.
     *
     * @param parent   The schema that contains this record type.
     * @param typeName The name that identifies this record type.
     * @param attributeTypes
     *
     * @todo Should we really provide this method? There is no garantee that the user-provided
     *       values are consistent with {@link MemberName#getAttributeType}.
     */
    public RecordTypeImpl(final RecordSchema parent, final TypeName typeName,
                          final Map<MemberName,TypeName> attributeTypes)
    {
        this.parent = parent;
        this.typeName = typeName;
        this.attributeTypes = Collections.unmodifiableMap(attributeTypes);
    }

    /**
     * Returns the name that identifies this record type.
     * If this {@code RecordType} is contained in a {@linkplain RecordSchema record schema},
     * then the record type name should be a valid in the {@linkplain NameSpace name space}
     * of the record schema:
     *
     * <blockquote><code>
     * {@linkplain #getContainer()}.{@linkplain RecordSchema#getSchemaName
     * getSchemaName()}.{@linkplain org.opengis.util.LocalName#scope scope()}
     * </code></blockquote>
     */
    public TypeName getTypeName() {
        return typeName;
    }

    /**
     * Returns the schema that contains this record type.
     */
    public RecordSchema getContainer() {
        return parent;
    }

    /**
     * Returns the unmodifiable dictionary of all (<var>name</var>, <var>type</var>)
     * pairs in this record type.
     */
    public Map<MemberName,TypeName> getAttributeTypes() {
        return attributeTypes;
    }

    /**
     * Returns the set of attribute names defined in this {@code RecordType}'s dictionary.
     * If there are no attributes, this method returns the empty set. This method is functionally
     * equivalent to <code>{@linkplain #getAttributeTypes()}.{@linkplain Map#keySet() keySet()}</code>.
     */
    public Set<MemberName> getMembers() {
        return getAttributeTypes().keySet();
    }

    /**
     * Looks up the provided attribute name and returns the associated type name. If the attribute
     * name is not defined in this record type, then this method returns {@code null}. This method
     * is functionnaly equivalent to
     * <code>{@linkplain #getAttributeTypes()}.{@linkplain Map#get get}(name)</code>.
     *
     * @todo Does it make sense given that {@link MemberName#getAttributeType} already provides
     *       this information?
     */
    public TypeName locate(final MemberName memberName) {
        return getAttributeTypes().get(memberName);
    }

    /**
     * Determines if the specified record is compatible with this record type. This method returns
     * {@code true} if the specified {@code record} argument is non-null and the following condition
     * holds:
     * <p>
     * <ul>
     *    <li><code>{@linkplain #getMembers()}.{@linkplain Set#containsAll containsAll}(record.{@linkplain
     *        Record#getAttributes() getAttributes()}.{@linkplain Map#keySet keySet()})</code></li>
     *    <li>Any other implementation-specific conditions.
     * </ul>
     *
     * @todo Replace {@code equals} by {@code containsAll}.
     */
    public boolean isInstance(final Record record) {
        return getMembers().equals(record.getAttributes().keySet());
    }
}

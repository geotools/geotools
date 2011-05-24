/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.util;

import java.util.Map;
import java.util.Set;
import org.opengis.annotation.UML;
import org.opengis.annotation.Extension;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * The type definition of a {@linkplain Record record}.  A {@code RecordType} defines dynamically
 * constructed data type.  This interface has methods for data access, but no methods to dynamically
 * add members.  This approach ensures that once a {@code RecordType} is constructed, it is immutable.
 * <p>
 * A {@code RecordType} is {@linkplain #getTypeName identified} by a {@link TypeName}. It contains
 * an arbitrary amount of {@linkplain #getAttributeTypes attribute types} which are also identified
 * by {@link TypeName}. A {@code RecordType} may therefore contain another {@code RecordType} as a
 * member.  This is a limited association because a named member may be defined to be a single
 * instance of some externally defined {@code RecordType}.  This does not permit aggregation of any
 * kind.
 * <p>
 * This class can be think as the equivalent of the Java {@link Class} class.
 *
 * @author Bryce Nordgren (USDA)
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.1
 *
 * @see Record
 * @see RecordSchema
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/util/RecordType.java $
 */
@UML(identifier="RecordType", specification=ISO_19103)
public interface RecordType {
    /**
     * Returns the name that identifies this record type.
     * If this {@code RecordType} is contained in a {@linkplain RecordSchema record schema},
     * then the record type name should be a valid in the {@linkplain NameSpace name space}
     * of the record schema:
     *
     * <blockquote><code>
     * {@linkplain #getContainer()}.{@linkplain RecordSchema#getSchemaName
     * getSchemaName()}.{@linkplain LocalName#scope scope()}
     * </code></blockquote>
     *
     * This method can be think as the equivalent of the Java {@link Class#getName()} method.
     */
    @UML(identifier="typeName", obligation=MANDATORY, specification=ISO_19103)
    TypeName getTypeName();

    /**
     * Returns the schema that contains this record type.
     */
    @UML(identifier="container", obligation=OPTIONAL, specification=ISO_19103)
    RecordSchema getContainer();

    /**
     * Returns the dictionary of all (<var>name</var>, <var>type</var>) pairs in this record type.
     * The dictionary shall be {@linkplain java.util.Collections#unmodifiableMap unmodifiable}.
     *
     * @see Record#getAttributes
     */
    @UML(identifier="attributeTypes", obligation=MANDATORY, specification=ISO_19103)
    Map<MemberName, TypeName> getAttributeTypes();

    /**
     * Returns the set of attribute names defined in this {@code RecordType}'s dictionary.
     * If there are no attributes, this method returns the empty set. This method is functionally
     * equivalent to <code>{@linkplain #getAttributeTypes()}.{@linkplain Map#keySet() keySet()}</code>.
     * <p>
     * The {@linkplain NameSpace name space} associated with a {@code RecordType} contains only
     * members of this {@code RecordType}. There is no potential for conflict with subpackages.
     * <p>
     * This method can be think as the equivalent of the Java {@link Class#getFields()} method.
     */
    @Extension
    Set<MemberName> getMembers();

    /**
     * Looks up the provided attribute name and returns the associated type name. If the attribute name is
     * not defined in this record type, then this method returns {@code null}. This method is functionnaly
     * equivalent to <code>{@linkplain #getAttributeTypes()}.{@linkplain Map#get get}(name)</code>.
     * <p>
     * This method can be think as the equivalent of the Java {@link Class#getField(String)} method.
     *
     * @see Record#locate
     *
     * @todo Does it make sense given that {@link MemberName#getAttributeType} already provides
     *       this information?
     */
    @UML(identifier="locate", obligation=MANDATORY, specification=ISO_19103)
    TypeName locate(MemberName name);

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
     * <p>
     * This method can be think as the equivalent of the Java {@link Class#isInstance(Object)} method.
     */
    @Extension
    boolean isInstance(Record record);
}

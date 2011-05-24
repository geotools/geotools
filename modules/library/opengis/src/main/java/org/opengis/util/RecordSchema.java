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
import java.util.Collection;
import org.opengis.annotation.UML;
import org.opengis.annotation.Extension;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A collection of {@linkplain RecordType record types}. All schemas possess an associated
 * {@linkplain NameSpace name space} within which the {@linkplain RecordType record type}
 * names are defined. A schema is a flat data structure, similar to a Java package.
 * <p>
 * Record schemas do not provide a hierarchical framework within which data types may be organized.
 * {@linkplain NameSpace Name spaces}, however, do define a hierarchical framework for arbitrary
 * named items. Record schemas can participate in this framework by virtue of the fact that they
 * are all identified by {@linkplain LocalName local name} or some subclass.  A schema's location
 * in the hierarchy can be communicated by
 *
 * <code>{@linkplain #getSchemaName()}.{@linkplain LocalName#scope scope()}.{@linkplain NameSpace#name name()}</code>.
 *
 * @author Bryce Nordgren (USDA)
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.1
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/util/RecordSchema.java $
 */
@UML(identifier="RecordSchema", specification=ISO_19103)
public interface RecordSchema {
    /**
     * Returns the schema name. The {@linkplain LocalName#scope scope} of the schema name is
     * associated with a {@linkplain NameSpace name space} which fixes this schema to a specific
     * location in the type hierarchy.
     */
    @UML(identifier="schemaName", obligation=MANDATORY, specification=ISO_19103)
    LocalName getSchemaName();

    /**
     * Returns the dictionary of all (<var>name</var>, <var>record type</var>) pairs
     * in this schema.
     */
    @UML(identifier="description", obligation=MANDATORY, specification=ISO_19103)
    Map<TypeName, RecordType> getDescription();

    /**
     * Returns all record types declared in this schema. This is functionnaly equivalent to
     * <code>{@linkplain #getDescription()}.{@linkplain Map#values values()}</code>.
     */
    @UML(identifier="element", obligation=OPTIONAL, specification=ISO_19103)
    Collection<RecordType> getElements();

    /**
     * Looks up the provided type name and returns the associated record type. If the type name is not
     * defined within this schema, then this method returns {@code null}. This is functionnaly equivalent
     * to <code>{@linkplain #getDescription()}.{@linkplain Map#get get}(name)</code>.
     */
    @UML(identifier="locate", obligation=MANDATORY, specification=ISO_19103)
    RecordType locate(TypeName name);
}

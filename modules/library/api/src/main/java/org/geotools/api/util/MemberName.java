/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.util;

/**
 * The name to identify a member of a {@linkplain Record record}. This name bears an association with a
 * {@linkplain TypeName type name}. There may be alternate implementations of this: for instance, one implementation may
 * apply to the in-memory model. Another may apply to a shapefile data store, etc.
 *
 * @author Bryce Nordgren (USDA)
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.1
 */
public interface MemberName extends LocalName {
    /**
     * Returns the type of the data associated with the record member.
     *
     * @todo Check in the specification if this association is really navigable that way. This association seems
     *     redundant with {@link RecordType#locate}.
     */
    TypeName getAttributeType();
}

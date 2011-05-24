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

import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * The name to identify a member of a {@linkplain Record record}. This name bears an association
 * with a {@linkplain TypeName type name}. There may be alternate implementations of this: for
 * instance, one implementation may apply to the in-memory model.  Another may apply to a shapefile
 * data store, etc.
 *
 * @author Bryce Nordgren (USDA)
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.1
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/util/MemberName.java $
 */
@UML(identifier="MemberName", specification=ISO_19103)
public interface MemberName extends LocalName {
    /**
     * Returns the type of the data associated with the record member.
     *
     * @todo Check in the specification if this association is really navigable that way.
     *       This association seems redundant with {@link RecordType#locate}.
     */
    @UML(identifier="attributeType", obligation=MANDATORY, specification=ISO_19103)
    TypeName getAttributeType();
}

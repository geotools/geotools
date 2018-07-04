/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.aggregate;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;

import java.util.Set;
import org.opengis.annotation.UML;
import org.opengis.geometry.primitive.Primitive;

/**
 * Specialization of the {@linkplain Aggregate} interface that restricts the elements to only being
 * of type {@linkplain Primitive}.
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @since GeoAPI 1.0
 */
@UML(identifier = "GM_MultiPrimitive", specification = ISO_19107)
public interface MultiPrimitive extends Aggregate {
    /**
     * Returns the set containing the primitives that compose this aggregate. The set may be
     * modified if this geometry {@linkplain #isMutable is mutable}.
     */
    @UML(identifier = "element", obligation = MANDATORY, specification = ISO_19107)
    Set<? extends Primitive> getElements();
}

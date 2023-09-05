/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.temporal;

import java.util.Collection;

/**
 * An aggregation of connected {@linkplain TemporalTopologicalPrimitive temporal topological
 * primitives}. This is the only subclass of {@linkplain TemporalComplex temporal complex}.
 *
 * @author Alexander Petkov
 */
public interface TemporalTopologicalComplex extends TemporalComplex {
    /**
     * The aggregation of connected {@linkplain TemporalTopologicalPrimitive temporal topological
     * primitives}.
     *
     * @todo Missing UML annotation.
     */
    Collection<TemporalTopologicalPrimitive> getTemporalTopologicalPrimitives();
}

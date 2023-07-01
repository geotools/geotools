/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.geometry.complex;

import static org.geotools.api.annotation.Obligation.MANDATORY;
import static org.geotools.api.annotation.Specification.ISO_19107;

import java.util.List;
import org.geotools.api.annotation.UML;
import org.geotools.api.geometry.primitive.Point;

/**
 * A separate class for composite point, included for completeness. It is a {@linkplain Complex
 * complex} containing one and only one {@linkplain Point point}.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Sanjay Jena
 * @author Jackson Roehrig
 * @since GeoAPI 2.1
 */
@UML(identifier = "GM_CompositePoint", specification = ISO_19107)
public interface CompositePoint extends Composite {
    /**
     * Returns the single point in this composite.
     *
     * @return The single point in this composite.
     */
    @Override
    @UML(identifier = "generator", obligation = MANDATORY, specification = ISO_19107)
    List<Point> getGenerators();
}

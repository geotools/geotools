/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.complex;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;

import java.util.List;
import org.opengis.annotation.Association;
import org.opengis.annotation.UML;
import org.opengis.geometry.primitive.Point;

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
    @Association("Composition")
    @UML(identifier = "generator", obligation = MANDATORY, specification = ISO_19107)
    List<Point> getGenerators();
}

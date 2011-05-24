/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.primitive;

import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * The boundary of {@linkplain Curve curves}.
 * A {@code CurveBoundary} contains two {@linkplain Point point} references
 * ({@linkplain #getStartPoint start point} and {@linkplain #getEndPoint end point}).
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/primitive/CurveBoundary.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier="GM_CurveBoundary", specification=ISO_19107)
public interface CurveBoundary extends PrimitiveBoundary {
    /**
     * Returns the start point.
     *
     * @see #getEndPoint
     */
    @UML(identifier="startPoint", obligation=MANDATORY, specification=ISO_19107)
    Point getStartPoint();

    /**
     * Returns the end point.
     *
     * @see #getStartPoint
     */
    @UML(identifier="endPoint", obligation=MANDATORY, specification=ISO_19107)
    Point getEndPoint();
}

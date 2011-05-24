/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.coordinate;

import java.util.List;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A {@linkplain GriddedSurface gridded surface} that uses cubic polynomial splines as the
 * horizontal and vertical curves. The initial tangents for the splines are often replaced
 * by an extra pair of rows (and columns) of control points.
 * <p>
 * The horizontal and vertical curves require initial and final tangent vectors for a complete
 * definition. These values are supplied by the four methods defined in this interface.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/coordinate/BicubicGrid.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier="GM_BicubicGrid", specification=ISO_19107)
public interface BicubicGrid extends GriddedSurface {
    /**
     * Returns the initial tangent vectors.
     */
    @UML(identifier="horiVectorAtStart", obligation=MANDATORY, specification=ISO_19107)
    List<double[]> getHorizontalVectorAtStart();

    /**
     * Returns the initial tangent vectors.
     */
    @UML(identifier="horiVectorAtEnd", obligation=MANDATORY, specification=ISO_19107)
    List<double[]> getHorizontalVectorAtEnd();

    /**
     * Returns the initial tangent vectors.
     */
    @UML(identifier="vertVectorAtStart", obligation=MANDATORY, specification=ISO_19107)
    List<double[]> getVerticalVectorAtStart();

    /**
     * Returns the initial tangent vectors.
     */
    @UML(identifier="vertVectorAtEnd", obligation=MANDATORY, specification=ISO_19107)
    List<double[]> getVerticalVectorAtEnd();
}

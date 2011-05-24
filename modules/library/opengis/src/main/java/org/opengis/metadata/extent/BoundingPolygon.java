/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.extent;

import java.util.Collection;
import org.opengis.geometry.Geometry;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Boundary enclosing the dataset, expressed as the closed set of
 * (<var>x</var>,<var>y</var>) coordinates of the polygon. The last
 * point replicates first point.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/extent/BoundingPolygon.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@UML(identifier="EX_BoundingPolygon", specification=ISO_19115)
public interface BoundingPolygon extends GeographicExtent {
    /**
     * Returns the sets of points defining the bounding polygon.
     *
     * @return The sets of points defining the bounding polygon.
     */
    @UML(identifier="polygon", obligation=MANDATORY, specification=ISO_19115)
    Collection<? extends Geometry> getPolygons();
}

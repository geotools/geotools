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
 * A polyhedral surface that is composed only of {@linkplain Triangle triangles}.
 * There is no restriction on how the triangulation is derived.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier="GM_TriangulatedSurface", specification=ISO_19107)
public interface TriangulatedSurface extends PolyhedralSurface {
    /**
     * Associates this surface with its individual triangles.
     */
    @UML(identifier="patch", obligation=MANDATORY, specification=ISO_19107)
    List<Triangle> getPatches();
}

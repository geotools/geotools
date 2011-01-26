/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.spatial;

import java.util.Collection;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Information about the vector spatial objects in the dataset.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_VectorSpatialRepresentation", specification=ISO_19115)
public interface VectorSpatialRepresentation extends SpatialRepresentation {
    /**
     * Code which identifies the degree of complexity of the spatial relationships.
     *
     * @return The degree of complexity of the spatial relationships, or {@code null}.
     */
    @UML(identifier="topologyLevel", obligation=OPTIONAL, specification=ISO_19115)
    TopologyLevel getTopologyLevel();

    /**
     * Information about the geometric objects used in the dataset.
     *
     * @return Information about the geometric objects used in the dataset, or {@code null}.
     */
    @UML(identifier="geometricObjects", obligation=OPTIONAL, specification=ISO_19115)
    Collection<? extends GeometricObjects> getGeometricObjects();
}

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

import org.opengis.annotation.UML;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Number of objects, listed by geometric object type, used in the dataset.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/spatial/GeometricObjects.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_GeometricObjects", specification=ISO_19115)
public interface GeometricObjects {
    /**
     * Name of point and vector spatial objects used to locate zero-, one-, and twodimensional
     * spatial locations in the dataset.
     *
     * @return Name of spatial objects used to locate spatial locations in the dataset.
     */
    @UML(identifier="geometricObjectType", obligation=MANDATORY, specification=ISO_19115)
    GeometricObjectType getGeometricObjectType();

    /**
     * Total number of the point or vector object type occurring in the dataset.
     *
     * @return Total number of the point or vector object type, or {@code null}.
     */
    @UML(identifier="geometricObjectCount", obligation=OPTIONAL, specification=ISO_19115)
    Integer getGeometricObjectCount();
}
